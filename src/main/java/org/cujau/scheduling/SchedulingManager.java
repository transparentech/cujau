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

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * A class that manages a set of {@link EventScheduler}s. The
 * {@link EventScheduler}s can either be controled individually or as a group.
 */
public class SchedulingManager {

    /** Hashtable containing the EventScheduler instances. */
    private Hashtable<String, EventScheduler> schedulers;

    /** Default public constructor. */
    public SchedulingManager() {
        schedulers = new Hashtable<String, EventScheduler>();
    }

    /** Start all of the EventSchedulers. */
    public void startEventSchedulers() {
        for ( Enumeration<EventScheduler> e = schedulers.elements(); e.hasMoreElements(); ) {
            EventScheduler et = e.nextElement();
            et.start();
        }
    }

    /** Start the EventScheduler named by the parameter. */
    public boolean startEventScheduler( String name ) {
        EventScheduler et = getEventScheduler( name );
        if ( et != null ) {
            et.start();
            return true;
        } else {
            return false;
        }
    }

    /** Stop all of the EventSchedulers. */
    public void stopEventSchedulers() {
        for ( Enumeration<EventScheduler> e = schedulers.elements(); e.hasMoreElements(); ) {
            EventScheduler et = e.nextElement();
            et.stop();
        }
    }

    /** Stop the EventScheduler named by the parameter. */
    public boolean stopEventSchedulers( String name ) {
        EventScheduler et = getEventScheduler( name );
        if ( et != null ) {
            et.stop();
            return true;
        } else {
            return false;
        }
    }

    /** Pause all of the EventSchedulers. */
    public void pauseEventSchedulers() {
        for ( Enumeration<EventScheduler> e = schedulers.elements(); e.hasMoreElements(); ) {
            EventScheduler et = e.nextElement();
            et.pause();
        }
    }

    /** Pause the EventScheduler named by the parameter. */
    public boolean pauseEventSchedulers( String name ) {
        EventScheduler et = getEventScheduler( name );
        if ( et != null ) {
            et.pause();
            return true;
        } else {
            return false;
        }
    }

    /** Initialize all of the EventSchedulers. */
    public void initEventSchedulers() {
        for ( Enumeration<EventScheduler> e = schedulers.elements(); e.hasMoreElements(); ) {
            EventScheduler et = e.nextElement();
            et.init();
        }
    }

    /** Destroy all of the EventSchedulers. */
    public void destroyEventSchedulers() {
        for ( Enumeration<EventScheduler> e = schedulers.elements(); e.hasMoreElements(); ) {
            EventScheduler et = e.nextElement();
            et.destroy();
        }
    }

    /** Add an EventScheduler to the set of managed EventSchedulers. */
    public void addEventScheduler( String name, EventScheduler scheduler ) {
        schedulers.put( name, scheduler );
    }

    /** Remove an EventScheduler from the set of managed EventSchedulers. */
    public EventScheduler removeEventScheduler( String name ) {
        return (EventScheduler)schedulers.remove( name );
    }

    /** Get the named EventScheduler. */
    public EventScheduler getEventScheduler( String name ) {
        return (EventScheduler)schedulers.get( name );
    }

    /** Get an Enumeration of all managed EventSchedulers. */
    public Enumeration<EventScheduler> getEventScheduler() {
        return schedulers.elements();
    }

    /** Return the number of EventSchedulers managed by this SchedulingManager. */
    public int size() {
        return schedulers.size();
    }
}
