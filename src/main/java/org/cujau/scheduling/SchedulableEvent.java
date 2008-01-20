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
 * Interface for classes wishing to be scheduled for execution by an
 * {@link EventScheduler}.
 */
public interface SchedulableEvent {

    /**
     * Initialize this SchedulableEvent.
     */
    public void init();

    /**
     * Destroy this SchedulableEvent. This should clean up any resources used by
     * this SchedulableEvent such as JDBC connections or open file handles.
     */
    public void destroy();

    /**
     * Execute the required event task.
     */
    public void execute();

}
