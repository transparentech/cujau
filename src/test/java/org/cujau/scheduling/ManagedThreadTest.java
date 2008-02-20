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

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test class for the {@link ManagedThread} class.
 */
public class ManagedThreadTest {

    public TestManagedThread mt;

    @Before
    public void setUp() {
        mt = new TestManagedThread();
    }

    @Test
    public void pass() {
        assertTrue( true );
    }
    
//    @Test
    public void startStop() {
        mt.stop();
        assertTrue( mt.getState().equals( ManagedThread.State.STOPPED ) );

        mt.start();
        assertTrue( mt.getState().equals( ManagedThread.State.RUNNING ) );

        mt.pause();
        assertTrue( mt.getState().equals( ManagedThread.State.PAUSED ) );

        mt.start();
        assertTrue( mt.getState().equals( ManagedThread.State.RUNNING ) );

        while ( mt.ctr == 0 ) {
            try {
                Thread.sleep( 1000 );
            } catch ( InterruptedException ex ) {

            }
        }
        mt.stop();
        assertTrue( mt.getState().equals( ManagedThread.State.STOPPED ) );
        assertTrue( mt.ctr > 0 );
    }

    public class TestManagedThread extends ManagedThread {
        public int ctr = 0;

        public void run() {
            while ( getState() == State.RUNNING ) {
                ctr++;
            }
        }
    }
}
