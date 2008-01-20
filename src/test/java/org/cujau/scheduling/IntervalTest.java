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
package org.cujau.scheduling;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.cujau.scheduling.schedulers.Interval;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JUnit test class for the {@link org.cujau.scheduling.schedulers.Interval}
 * class.
 */
public class IntervalTest {

    /** The logging object for this class. */
    private Logger _log = LoggerFactory.getLogger( IntervalTest.class );

    public static final int SIMPLE_INTERVAL = 2;

    public static final int SIMPLE_INTERVAL_IN_MILLIS = SIMPLE_INTERVAL * 1000;

    public static final int SIMPLE_NUM_EVENTS = 5;

    public static final int LONG_INTERVAL = 30;

    public static final int LONG_INTERVAL_IN_MILLIS = LONG_INTERVAL * 1000;

    public static final int LONG_NUM_EVENTS = 20;

    public static final int SKEW_IN_MILLIS = 20;

    public static final int LENIENT_SKEW_IN_MILLIS = 50;

    /*
     * public static final int INTERVAL = 2; public static final int
     * INTERVAL_IN_MILLIS = INTERVAL * 1000; public static final int NUM_EVENTS =
     * 5; public static final int SKEW_IN_MILLIS = 100;
     */

    protected MyEvent myEvent;

    @Before
    public void setUp() {
        myEvent = new MyEvent();
    }

    @Test
    public void simple() {
        Interval scheduler = getNewIntervalScheduler( "simple", SIMPLE_INTERVAL );
        runAndValidateScheduler( scheduler, SIMPLE_NUM_EVENTS );
        validateTimings( SIMPLE_INTERVAL_IN_MILLIS, SKEW_IN_MILLIS );
    }

    @Test
    public void simpleWithSkew() {
        Interval scheduler = getNewIntervalScheduler( "simpleWithSkew", SIMPLE_INTERVAL );
        runAndValidateScheduler( scheduler, SIMPLE_NUM_EVENTS );
        validateTimings( SIMPLE_INTERVAL_IN_MILLIS, LENIENT_SKEW_IN_MILLIS );
    }

    @Test
    public void simpleNoCatchUp() {
        Interval scheduler = getNewIntervalScheduler( "simpleNoCatchUp", SIMPLE_INTERVAL );
        scheduler.setCatchUp( false );
        runAndValidateScheduler( scheduler, SIMPLE_NUM_EVENTS );
        validateTimings( SIMPLE_INTERVAL_IN_MILLIS, SKEW_IN_MILLIS );
    }

    @Test
    public void simpleNoCatchUpWithSkew() {
        Interval scheduler = getNewIntervalScheduler( "simpleNoCatchUpWithSkew", SIMPLE_INTERVAL );
        scheduler.setCatchUp( false );
        runAndValidateScheduler( scheduler, SIMPLE_NUM_EVENTS );
        validateTimings( SIMPLE_INTERVAL_IN_MILLIS, LENIENT_SKEW_IN_MILLIS );
    }

    @Test
    public void longTest() {
        Interval scheduler = getNewIntervalScheduler( "longTest", LONG_INTERVAL );
        runAndValidateScheduler( scheduler, LONG_NUM_EVENTS );
        validateTimings( LONG_INTERVAL_IN_MILLIS, SKEW_IN_MILLIS );
    }

    @Test
    public void longTestWithSkew() {
        Interval scheduler = getNewIntervalScheduler( "longTestWithSkew", LONG_INTERVAL );
        runAndValidateScheduler( scheduler, LONG_NUM_EVENTS );
        validateTimings( LONG_INTERVAL_IN_MILLIS, LENIENT_SKEW_IN_MILLIS );
    }

    @Test
    public void longTestNoCatchUp() {
        Interval scheduler = getNewIntervalScheduler( "longTestNoCatchUp", LONG_INTERVAL );
        scheduler.setCatchUp( false );
        runAndValidateScheduler( scheduler, LONG_NUM_EVENTS );
        validateTimings( LONG_INTERVAL_IN_MILLIS, SKEW_IN_MILLIS );
    }

    @Test
    public void longTestNoCatchUpWithSkew() {
        Interval scheduler = getNewIntervalScheduler( "longTestNoCatchUpWithSkew", LONG_INTERVAL );
        scheduler.setCatchUp( false );
        runAndValidateScheduler( scheduler, LONG_NUM_EVENTS );
        validateTimings( LONG_INTERVAL_IN_MILLIS, LENIENT_SKEW_IN_MILLIS );
    }

    protected void validateTimings( int interval_in_millis, int skew_in_millis ) {
        int ctr = 0;
        Calendar cur = null;
        for ( Calendar c : myEvent.executedEvents ) {
            if ( ctr > 0 ) {
                long ct_c = c.getTimeInMillis();
                long ct_cur = cur.getTimeInMillis();
                long timediff = ct_c - ct_cur;
                if ( timediff > interval_in_millis + skew_in_millis ||
                     timediff < interval_in_millis - skew_in_millis ) {
                    SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSZ" );
                    _log.error( "{} - {}", sdf.format( new Date( c.getTimeInMillis() ) ).toString(),
                                sdf.format( new Date( cur.getTimeInMillis() ) ).toString() );
                    fail( ctr + " : timediff=" + timediff + " (" + ct_c + "-" + ct_cur + ")" );
                }
            }
            cur = c;
            ctr++;
        }
    }

    protected void runAndValidateScheduler( EventScheduler scheduler, int num_events ) {
        myEvent.reset();

        scheduler.start();

        while ( myEvent.executedEvents.size() < num_events ) {
            try {
                Thread.sleep( 1000 );
            } catch ( InterruptedException ex ) {
            }
        }

        scheduler.stop();

        assertTrue( scheduler.getState().equals( ManagedThread.State.STOPPED ) );
        assertTrue( myEvent.executedEvents.size() == num_events );
    }

    protected Interval getNewIntervalScheduler( String name, int interval ) {
        Interval scheduler = new Interval( name );
        scheduler.setTimeIntervalSeconds( interval );
        scheduler.init();
        scheduler.addSchedulableEvent( name, myEvent );
        return scheduler;
    }

    public class MyEvent extends DefaultSchedulableEvent {
        public ArrayList<Calendar> executedEvents = new ArrayList<Calendar>();

        public void execute() {
            executedEvents.add( Calendar.getInstance() );
        }

        public void reset() {
            executedEvents.clear();
        }
    }
}
