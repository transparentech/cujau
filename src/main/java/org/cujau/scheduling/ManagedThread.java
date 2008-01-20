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

/**
 * Class that provides thread management capabilities including methods such as
 * starting, stopping and pausing.
 * <p>
 * Extending classes are expected to manage the thread according to the
 * guidelines described in the {@link #start()}, {@link #stop()} and
 * {@link #pause()} methods.
 */
public abstract class ManagedThread implements Runnable {

    /* The various states in which this ManagedThread might be. */
    public enum State {
        STOPPED,
        PAUSED,
        RUNNING,
        NOTSTARTED
    }

    /* The current state of the thread. */
    private State state;

    /** The name of this thread. */
    private String name;

    /** The internal managable thread. */
    protected Thread execution;

    /** Set the state of this ManagedThread. */
    protected void setState( State s ) {
        state = s;
    }

    /** Get the current state. */
    public State getState() {
        return state;
    }

    /** Get this ManagedThread's name. */
    public String getName() {
        return name;
    }

    /** Set this ManagedThread's name. */
    public void setName( String n ) {
        name = n;
    }

    /** Public constructor. */
    public ManagedThread( String name ) {
        state = State.NOTSTARTED;
        this.name = name;
    }

    /** Default public constructor. */
    public ManagedThread() {
        this( "ManagedThread" );
    }

    /**
     * Start the timing and execution of events. If this {@link ManagedThread}
     * is in the {@link State#NOTSTARTED} or {@link State#STOPPED} state, a new thread will
     * be created and started. If this {@link ManagedThread} is in the
     * {@link State#PAUSED} state, the executing thread will be interupted so that it
     * can take the new state into account in its run() method. If this
     * {@link ManagedThread} is already in the {@link State#RUNNING} state, nothing is
     * done. When this method completes, this {@link ManagedThread} is in the
     * {@link State#RUNNING} state.
     */
    public void start() {
        if ( state == State.NOTSTARTED || state == State.STOPPED ) {
            state = State.RUNNING;
            execution = new Thread( this, name );
            execution.start();
        } else if ( state == State.PAUSED ) {
            state = State.RUNNING;
            if ( execution != null ) {
                execution.interrupt();
            }
        } else {
            // In State.RUNNING state already so do nothing.
        }
    }

    /**
     * Set the state of this {@link ManagedThread} to {@link State#STOPPED}. The
     * change of state will take effect immediately as the execution thread will
     * be interupted. The implementation of the run() method should terminate,
     * making this thread unusable. When this method completes, this
     * {@link ManagedThread} is in the {@link State#STOPPED} state.
     */
    public void stop() {
        state = State.STOPPED;
        if ( execution != null ) {
            execution.interrupt();
        }
    }

    /**
     * Set the state of this {@link ManagedThread} to {@link State#PAUSED}. The
     * change of state will take effect immedately as the execution thread will
     * be interupted. The implementation of the {@link #run()} method should
     * continue to execute without executing any actions until the state of this
     * {@link ManagedThread} changes to {@link State#STOPPED} or {@link State#RUNNING}.
     * When this method completes, this {@link ManagedThread} is in the
     * {@link State#PAUSED} state.
     */
    public void pause() {
        state = State.PAUSED;
        if ( execution != null ) {
            execution.interrupt();
        }
    }

    /**
     * Run the {@link ManagedThread}. Subclasses must implement the specifics
     * of what happens when the thread is executed.
     */
    public abstract void run();

}
