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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.cujau.scheduling.SchedulableEvent;


/**
 * Experimental class for retrieving the contents of a URL at periodic intervals.
 */
public class GetUrl implements SchedulableEvent {

    /** The logger object for this class. */
    private static final Logger LOG = LoggerFactory.getLogger( GetUrl.class );
    
    private String surl;

    private String sparams;

    private StringBuffer result;

    public GetUrl() {
        surl = null;
        sparams = null;
        result = null;
    }

    public void setUrl( String u ) {
        surl = u;
    }

    public String getUrl() {
        return surl;
    }

    public void setParams( String p ) {
        sparams = p;
    }

    public String getParams() {
        return sparams;
    }

    public StringBuffer getResult() {
        return result;
    }

    public void init() {}

    public void destroy() {}

    public void execute() {
        result = null;

        try {
            // Instantiate the URL.
            URL url = new URL( surl + "?" + sparams );
            // Get the return page.
            BufferedReader in = new BufferedReader( new InputStreamReader( url.openStream() ) );
            String buf = in.readLine();
            result = new StringBuffer();
            while ( buf != null ) {
                result.append( buf );
                buf = in.readLine();
            }
            in.close();
        } catch ( MalformedURLException me ) {
            LOG.warn( "Bad URL!", me );
        } catch ( IOException ie ) {
            LOG.warn(  "Problem reading data from URL.", ie );
        }
    }
}
