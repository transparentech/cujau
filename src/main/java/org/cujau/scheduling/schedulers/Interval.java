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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.cujau.scheduling.EventScheduler;

/**
 * Class that executes
 * {@link org.cujau.scheduling.SchedulableEvent SchedulableEvent}s at periodic
 * intervals.
 */
public class Interval extends EventScheduler {

    /** The logging object for this class. */
    private Logger _log = LoggerFactory.getLogger( Interval.class );

    /** The time interval between executions in milliseconds. */
    private long timeInterval;

    /** Whether to catch up to the minute when starting. */
    private boolean catchUp;

    /** Public constructor. */
    public Interval( String name ) {
        super( name );
        timeInterval = 10000;
        catchUp = true;
    }

    /** Default public constructor. */
    public Interval() {
        this( "Interval" );
    }

    /** Return the time interval between executions in milliseconds. */
    public long getTimeInterval() {
        return timeInterval;
    }

    /** Set the time interval between executions in minutes. */
    public void setTimeIntervalMinutes( int t ) {
        timeInterval = t * 60 * 1000;
    }

    /** Set the time interval between executions in minutes. */
    public void setTimeIntervalMinutes( String t ) {
        int i = 1;
        try {
            i = Integer.parseInt( t );
        } catch ( NumberFormatException nfe ) {
            _log.warn( "Can't convert '{}' mintues to an int!", t );
        }
        setTimeIntervalMinutes( i );
    }

    /** Set the time interval between executions in seconds. */
    public void setTimeIntervalSeconds( int t ) {
        timeInterval = t * 1000;
    }

    /** Set the time interval between executions in seconds. */
    public void setTimeIntervalSeconds( String t ) {
        int i = 1;
        try {
            i = Integer.parseInt( t );
        } catch ( NumberFormatException nfe ) {
            _log.warn( "Can't convert '{}' seconds to an int!", t );
        }
        setTimeIntervalSeconds( i );
    }

    /** Get whether to catchup to the minute when starting. */
    public boolean getCatchUp() {
        return catchUp;
    }

    /** Set whether to catchup to the minute when starting. */
    public void setCatchUp( boolean c ) {
        catchUp = c;
    }

    /** Set whether to catchup to the minute when starting. */
    public void setCatchUp( String c ) {
        catchUp = Boolean.valueOf( c ).booleanValue();
    }

    /**
     * Run the {@link org.cujau.scheduling.SchedulableEvent}s at periodic
     * intervals.
     * 
     * <p>
     * Before running the events for the first time, this method will (if
     * {@link #getCatchUp() catchUp} is true [the default]) wait to "catchup" to
     * the start of the minute. This makes the periodically executed events
     * first run start on the minute rather than at an undetermined point during
     * the minute.
     * 
     * <p>
     * Then while this thread is not in the
     * {@link org.cujau.scheduling.ManagedThread.State#STOPPED} state, the
     * following actions are performed in this order:
     * 
     * <ol>
     * <li>if the state of this thread is
     * {@link org.cujau.scheduling.ManagedThread.State#RUNNING}, invoke the
     * {@link org.cujau.scheduling.EventScheduler#runRunners()} method.</li>
     * <li>If the thread is not in the
     * {@link org.cujau.scheduling.ManagedThread.State#STOPPED} state the
     * executing thread will sleep for the configured amount of time.</li>
     * <ol>
     * 
     * When this instance moves into the
     * {@link org.cujau.scheduling.ManagedThread.State#STOPPED} state, this
     * method will complete and the thread executing this method will die. The
     * {@link org.cujau.scheduling.ManagedThread#start()} method must then be
     * called to restart this instance.
     * <p>
     * If the invocation of this instance's runRunners() method throws an
     * Exception, the state of this instance will become
     * {@link org.cujau.scheduling.ManagedThread.State#STOPPED} STOPPED and this
     * method will finish.
     * <p>
     * This method is generally not called directly, but invoked as part of the
     * {@link org.cujau.scheduling.ManagedThread#start()} of the executing
     * thread.
     */
    public void run() {
        _log.info( "Interval[{}] - Running...", execution.getName() );

        Calendar now = null;
        // Catch up to the start of the minute.
        if ( getCatchUp() ) {
            try {
                // Catch up to be exactly on the start of the minute.
                // 
                // If, by some freak of the electons, we are here at exactly
                // the start of the minute (SECONDS=0 and MILLISECONDS=0) we
                // wait until the next minute to execute.
                //
                now = Calendar.getInstance();
                Thread.sleep( 60000 - ( now.get( Calendar.SECOND ) * 1000 ) -
                              ( now.get( Calendar.MILLISECOND ) ) );
            } catch ( InterruptedException ex ) {
            }
        }

        while ( getState() != State.STOPPED ) {

            if ( getState() == State.RUNNING ) {
                _log.info( "Interval[{}] - doEvent...", execution.getName() );
                runRunners();
            }

            if ( getState() != State.STOPPED ) {
                try {
                    // Sleep for the interval time.
                    Thread.sleep( timeInterval );
                } catch ( InterruptedException ex ) {
                }
            }
        }

        _log.info( "Interval[{}] - Stopped.", execution.getName() );

    }

}
