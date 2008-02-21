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

/**
 * Class that runs a {@link SchedulableEvent} in a separate thread.
 * <p>
 * This class is declared package-private.
 */
class SchedulableEventRunner extends ManagedThread {

    /** The Commons-Logging log object for this class. */
    private static final Logger LOG = LoggerFactory.getLogger( SchedulableEventRunner.class );

    /** The SchedulableEvent object that will be run in this thread. */
    private SchedulableEvent event;

    /**
     * The owning {@link EventScheduler} object that is using this
     * SchedulableEventRunner to run one if its managed SchedulableEvents in a
     * separate thread.
     */
    private EventScheduler scheduler;

    /** Public constructor. */
    public SchedulableEventRunner( String name, SchedulableEvent evt, EventScheduler sched ) {
        super( name );
        event = evt;
        scheduler = sched;
    }

    /** Return the SchedulableEvent managed by this SchedulableEventRunner. */
    public SchedulableEvent getHandler() {
        return event;
    }

    /**
     * Initialize this SchedulableEventRunner. This method calls the managed
     * SchedulableEvent's <code>init()</code> method.
     */
    public void init() {
        event.init();
    }

    /**
     * Destroy this SchedulableEventRunner. This method calls the managed
     * SchedulableEvent's <code>destroy()</code> method.
     */
    public void destroy() {
        event.destroy();
    }

    /**
     * Method that will be executed when this thread is started. Executes the
     * {@link SchedulableEvent}'s
     * <code>{@link SchedulableEvent#execute execute}()</code> method and
     * returns. If the SchedulableEvent's <code>execute()</code> method throws
     * an exception and the parent EventScheduler has the
     * <code>removeHandlerOnException</code> option set to <code>true</code>
     * this SchedulableEventRunner (and consequently, the SchedulableEvent
     * itself) will be removed from the list of SchedulableEvents managed by the
     * parent EventScheduler.
     */
    public void run() {
        // Do the event.
        try {
            event.execute();
        } catch ( Exception e ) {
            StringBuffer buf = new StringBuffer();
            buf.append( "SchedulableEventRunner[" );
            buf.append( getName() ).append( "]" );
            buf.append( " - Exception thrown by SchedulableEvent - " + e.getClass() );
            if ( e.getMessage() == null ) {
                buf.append( "\n  " ).append( e.getClass().getName() );
            } else {
                buf.append( "\n  " ).append( e.getMessage() );
            }

            // Show the stack trace.
            buf.append( "\n" );
            // buf.append( Backtrace.backtraceFull( e.getStackTrace(), 0 ) );

            // Remove the SchedulableEvent from the timer.
            if ( scheduler != null && scheduler.getRemoveOnException() ) {
                scheduler.removeSchedulableEvent( getName() );

                buf.append( "\nRemoving SchedulableEventRunner[" );
                buf.append( getName() ).append( "]" );
                buf.append( " from SchedulableEvents managed by [" );
                buf.append( scheduler.getName() ).append( "]." );

            }
            LOG.warn( buf.toString() );
        }
        setState( State.STOPPED );
    }
}
