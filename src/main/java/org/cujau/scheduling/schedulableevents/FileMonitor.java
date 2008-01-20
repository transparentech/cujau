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
package org.cujau.scheduling.schedulableevents;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.cujau.scheduling.DefaultSchedulableEvent;
import org.cujau.scheduling.SchedulableEvent;

/**
 * Experimental class for monitoring file modifications.
 */
public class FileMonitor implements SchedulableEvent {

    /** The logger object for this class. */
    private Logger _log = LoggerFactory.getLogger( FileMonitor.class );

    public Date lastExecution;

    public File file;

    public SchedulableEvent handler;

    public FileMonitor( String filename ) {
        lastExecution = new Date();
        file = new File( filename );
        handler = new DefaultSchedulableEvent();
    }

    public String getAbsoluteFileName() {
        return file.getAbsolutePath();
    }

    public SchedulableEvent getEventHandler() {
        return handler;
    }

    public void setSchedulableEvent( SchedulableEvent h ) {
        handler = h;
    }

    public void init() {}

    public void destroy() {}

    public void execute() {
        if ( lastExecution.getTime() < file.lastModified() ) {
            _log.debug( "File was modified!" );
            // handler.doEvent();
        } else {
            _log.debug( "File not modified." );
        }
        /*
         * if ( file.exists() ) { if ( lastExecution.getTime() <
         * file.lastModified() ) { Jakal.dbg.debug( "File was modified!" ); }
         * else { Jakal.dbg.debug( "File not modified." ); } } else {
         * Jakal.dbg.debug( "File does not exist." ); }
         */
        lastExecution = new Date();
    }
}
