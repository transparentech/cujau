/*
 *                           CUJAU
 *         Common Utilities for Java Application Use
 *
 *        http://www.transparentech.com/projects/cujau
 *
 * Copyright (c) 2007 Nicholas Rahn <nick at transparentech.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */
package org.cujau.scheduling.schedulers;

import java.util.Calendar;
import java.util.HashSet;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.cujau.scheduling.EventScheduler;

/**
 * Class that executes SchedulableEvents at times which are configurable with a
 * cron-like specicivity. See the <code>crontab(5)</code> for details.
 */
public class Cron extends EventScheduler {

    /** The logging object for this class. */
    private static final Logger LOG = LoggerFactory.getLogger( Cron.class );

    private HashSet<Integer> minutes;

    private HashSet<Integer> hours;

    private HashSet<Integer> doms;

    private HashSet<Integer> months;

    private HashSet<Integer> dows;

    /** Public constructor. */
    public Cron( String name ) {
        super( name );

        minutes = new HashSet<Integer>();
        hours = new HashSet<Integer>();
        doms = new HashSet<Integer>();
        months = new HashSet<Integer>();
        dows = new HashSet<Integer>();

        minutes.add( Integer.MAX_VALUE );
        hours.add( Integer.MAX_VALUE );
        doms.add( Integer.MAX_VALUE );
        months.add( Integer.MAX_VALUE );
        dows.add( Integer.MAX_VALUE );
    }

    /** Default public constructor. */
    public Cron() {
        this( "Cron" );
    }

    /** Set the minute in which to execute. */
    public void setMinute( String mins ) {
        setTime( mins, minutes, 0 );
    }

    /** Set the hour in which to execute. */
    public void setHour( String hrs ) {
        setTime( hrs, hours, 0 );
    }

    /** Set the day of the month in which to execute. */
    public void setDayOfMonth( String dom ) {
        setTime( dom, doms, 0 );
    }

    /**
     * Set the month in which to execute. Months are specified as 1 (January) to
     * 12 (December).
     */
    public void setMonth( String mons ) {
        setTime( mons, months, -1 );
    }

    /**
     * Set on which day of the week to execute. Days are specified as 0 (Sunday)
     * to 6 (Saturday).
     */
    public void setDayOfWeek( String dow ) {
        setTime( dow, dows, 1 );
    }

    /**
     * Add an Integer element to the HashSet for every comma separated number in
     * the given String. If the string is "*", it is added directly to signify
     * the default execution. A correction parameter is added in order to adapt
     * Java constants to crontab values. Any non-number values are ignored.
     */
    protected void setTime( String t, HashSet<Integer> set, int correction ) {

        set.clear();
        StringTokenizer toks = new StringTokenizer( t, "," );
        String tok = null;
        while ( toks.hasMoreTokens() ) {
            tok = toks.nextToken();
            if ( tok.equals( "*" ) ) {
                set.add( Integer.MAX_VALUE );
            } else {
                try {
                    set.add( new Integer( new Integer( tok.trim() ).intValue() + correction ) );
                } catch ( NumberFormatException ne ) {
                }
            }
        }
    }

    /**
     * Return true if the given value is contained in the given HashSet. This
     * method is used by validation() to see if there is a value configured for
     * the given time.
     */
    protected boolean contains( int value, HashSet<Integer> set ) {
        if ( set.contains( Integer.MAX_VALUE ) ) {
            return true;
        } else {
            return set.contains( new Integer( value ) );
        }
    }

    /**
     * Validate that the current time corresponds to one of the configured
     * execution times.
     * 
     * @param cal
     *            Calendar with the current time.
     * @return <code>true</code> if the current time corresponds to one of the
     *         configured execution times <code>false</code> otherwise.
     */
    protected boolean validation( Calendar cal ) {
        if ( contains( cal.get( Calendar.MINUTE ), minutes ) && contains( cal.get( Calendar.HOUR_OF_DAY ), hours )
             && contains( cal.get( Calendar.DAY_OF_MONTH ), doms ) && contains( cal.get( Calendar.MONTH ), months )
             && contains( cal.get( Calendar.DAY_OF_WEEK ), dows ) ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Run the cronjob thread, executing the configured SchedulableEvents via
     * the runRunners method according to the configuration.
     * <p>
     * The thread will initially sleep up to the start of the next minute
     * (seconds=0, milliseconds=0) and then it will begin its continuous loop.
     * <p>
     * The loop executes every minute. If the SchedulableEvents have been
     * configured to execute on that minute, the runRunners() method will be
     * called. If nothing has been configured for that minute, it will pass
     * directly to the end of the loop where the thread will sleep until the
     * start of the next minute.
     */
    public void run() {
        LOG.info( "Cron[{}] - Running...", execution.getName() );
        LOG.debug( "Cron[{}] set to wakeup for: min={} hour={} dayofmonth={} month={} dayofweek={}",
                    new Object[] { minutes, hours, doms, months, dows } );

        Calendar now = Calendar.getInstance();
        try {
            // Catch up to be exactly on the start of the minute.
            // 
            // If, by some freak of the electrons, we are here at exactly the
            // start of the minute (SECONDS=0 and MILLISECONDS=0) we wait
            // until the next minute to execute.
            //
            Thread.sleep( 60000 - now.get( Calendar.SECOND ) * 1000 - now.get( Calendar.MILLISECOND ) );
        } catch ( InterruptedException ex ) {
        }

        while ( getState() != State.STOPPED ) {

            Calendar execTime = Calendar.getInstance();

            if ( getState() == State.RUNNING ) {

                // Determine if we are at one of the execution times.
                if ( validation( execTime ) ) {

                    LOG.info( "Cron[{}] - Execute...", execution.getName() );

                    // Run all of the registered SchedulableEvents.
                    runRunners();
                }
            }

            if ( getState() != State.STOPPED ) {
                // Sleep up to the start of the next minute. Pay particular
                // attention to not continue if we wake up
                // before the end of the minute (except if we're stopped), to
                // not execute twice the same cron!
                now = Calendar.getInstance();
                while ( getState() != State.STOPPED && now.get( Calendar.MINUTE ) == execTime.get( Calendar.MINUTE ) ) {
                    try {
                        Thread.sleep( 60000 - now.get( Calendar.SECOND ) * 1000 - now.get( Calendar.MILLISECOND ) );
                    } catch ( InterruptedException ex ) {
                    }
                    now = Calendar.getInstance();
                }
            }
        }

        LOG.info( "Cron[{}] - Stopped.", execution.getName() );

    }
}
