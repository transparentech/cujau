/*
 *                           CUJAU
 *         Common Utilities for Java Application Use
 *
 *        http://www.transparentech.com/projects/cujau
 *
 * Copyright (c) 2006 Nicholas Rahn <nick at transparentech.com>
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Base class for event scheduling classes. Instances of this class maintain a
 * set of {@link SchedulableEvent}s which are
 * {@link SchedulableEvent#execute executed} according to the rules implemented
 * by extending classes.
 */
public abstract class EventScheduler extends ManagedThread {

    /** The logger object for this instance. */
    private static final Logger LOG = LoggerFactory.getLogger( EventScheduler.class );

    /** A description of this EventScheduler. */
    private String description;

    /**
     * Option to remove a <code>SchedulableEvent</code> if it throws an
     * exception during execution. Defaults to false.
     */
    private boolean removeOnException;

    /**
     * The <tt>SchedulableEvent</tt>s which will be executed according to the
     * scheduling rules.
     */
    protected ArrayList<SchedulableEventRunner> runners;

    /** Public constructor. */
    public EventScheduler( String name ) {
        super( name );
        runners = new ArrayList<SchedulableEventRunner>();
        description = name;
        removeOnException = false;
    }

    /** Default public constructor. */
    public EventScheduler() {
        this( "EventScheduler" );
    }

    /** Set this EventScheduler's {@link #description}. */
    public void setDescription( String desc ) {
        description = desc;
    }

    /** Get this EventScheduler's {@link #description}. */
    public String getDescription() {
        return description;
    }

    /**
     * Set the option to remove a SchedulableEvent if it throws an excption
     * during its execution.
     */
    public void setRemoveOnException( boolean val ) {
        removeOnException = val;
    }

    /**
     * Set the option to remove a SchedulableEvent if it throws an excption
     * during its execution.
     */
    public void setRemoveOnException( String val ) {
        removeOnException = Boolean.valueOf( val ).booleanValue();
    }

    /**
     * Get the option to remove a SchedulableEvent if it throws an excption
     * during its execution.
     */
    public boolean getRemoveOnException() {
        return removeOnException;
    }

    /**
     * Add this {@link SchedulableEvent} to the list of {@link SchedulableEvent}s
     * managed by this EventScheduler.
     */
    public void addSchedulableEvent( String name, SchedulableEvent hand ) {
        runners.add( new SchedulableEventRunner( name, hand, this ) );
    }

    /**
     * Remove the named {@link SchedulableEvent} from the list of
     * SchedulableEvents managed by this EventScheduler. After removing the
     * SchedulableEvent, if there are no SchedulableEvents managed by this
     * EventScheduler, the EventScheduler is stopped.
     */
    public void removeSchedulableEvent( String name ) {
        // Find the named SchedulableEvent and remove it.
        SchedulableEventRunner ehr = null;
        for ( int i = 0; i < runners.size(); i++ ) {
            ehr = runners.get( i );
            if ( ehr.getName().equals( name ) ) {
                runners.remove( i );
            }
        }
        // If there are no managed SchedulableEvents, stop the timer.
        if ( runners.isEmpty() ) {
            LOG.info( "No SchedulableEvents remain in this EventScheduler. Stopping..." );
            stop();
        }
    }

    /**
     * Initialize this instance. This EventScheduler is initialized by calling
     * the <code>init()</code> method of all managed <tt>SchedulableEvent</tt>s.
     */
    public void init() {
        for ( int i = 0; i < runners.size(); i++ ) {
            ( runners.get( i ) ).init();
        }
    }

    /**
     * Destroy this instance. This EventScheduler is initialized by calling the
     * <code>destroy()</code> method of all managed <tt>SchedulableEvent</tt>s.
     */
    public void destroy() {
        for ( int i = 0; i < runners.size(); i++ ) {
            ( runners.get( i ) ).destroy();
        }
    }

    /**
     * Run this instance. Extending classes will implement their event
     * scheduling algorithm here.
     * <p>
     * This method is generally not called directly, but invoked as part of the
     * {@link ManagedThread#start start()} of the executing thread.
     */
    public abstract void run();

    /**
     * Execute the {@link SchedulableEvent}s. Each SchedulableEvent object is
     * wrapped in a separate thread. These threads invoke the
     * {@link SchedulableEvent#execute()} and exit. This method starts these
     * wrapping threads.
     */
    protected void runRunners() {
        for ( int i = 0; i < runners.size(); i++ ) {
            ( runners.get( i ) ).start();
        }
    }
}
