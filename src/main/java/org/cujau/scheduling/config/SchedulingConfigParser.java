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
package org.cujau.scheduling.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.cujau.scheduling.EventScheduler;
import org.cujau.scheduling.SchedulableEvent;
import org.cujau.scheduling.SchedulingManager;
import org.cujau.xml.DefaultElementHandler;
import org.cujau.xml.DocumentHandler;
import org.cujau.xml.ElementHandler;
import org.cujau.xml.ReflectionHelpers;
import org.cujau.xml.SimpleXMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * A class that can parse an XML file containing event scheduling definitions
 * and create a {@link SchedulingManager} instance to manage them.
 */
public class SchedulingConfigParser extends SimpleXMLReader {

    protected Hashtable<String, SchedulableEvent> events = null;

    protected Hashtable<String, EventScheduler> schedulers = null;

    protected SchedulingManager manager = null;

    /**
     * The current object on which we are building. Will be either a
     * SchedulableEvent or an EventScheduler instance. We use a single object
     * rather than separate, type specific objects in order to simplify the
     * setting of property values in BasicElement.
     */
    protected Object curObj = null;

    /**
     * Public constructor.
     */
    public SchedulingConfigParser( String filename ) throws SAXException {

        // Add the parsing ElementHandlers.
        addElementHandler( "CUJAUScheduling", new DefaultElementHandler() );
        addElementHandler( "schedulableEvent", new SchedulableEventParser() );
        addElementHandler( "eventScheduler", new EventSchedulerParser() );
        addElementHandler( "addSchedulableEvent", new AddSchedulableEvent() );
        // // Catch any other elements.
        setDocumentHandler( new BasicPropertyElement() );
    }

    /**
     * Get an Hashtable containing the SchedulableEvents created when parsing
     * the file. Will return <tt>null</tt> if called before
     * {@link #loadConfig}.
     */
    public Hashtable<String, SchedulableEvent> getEvents() {
        return events;
    }

    /**
     * Get an Hashtable containing the EventSchedulers created when parsing the
     * file. Will return <tt>null</tt> if called before {@link #loadConfig}.
     */
    public Hashtable<String, EventScheduler> getSchedulers() {
        return schedulers;
    }

    /**
     * Get the SchedulingManager instance containing the EventSchedulers created
     * when parsing this file. Will return <tt>null</tt> if called before
     * {@link #loadConfig}.
     */
    public SchedulingManager getSchedulingManager() {
        SchedulingManager tim = new SchedulingManager();
        for ( Enumeration<EventScheduler> e = schedulers.elements(); e.hasMoreElements(); ) {
            EventScheduler t = e.nextElement();
            tim.addEventScheduler( t.getName(), t );
        }
        return tim;
    }

    /**
     * Load the events from an XML file. After this method has returned, the
     * {@link #getEvents()}, {@link #getSchedulers()} and
     * {@link #getSchedulingManager()} methods will return structures that
     * contain the objects created during the parsing of the XML config file.
     */
    public void loadConfig( String xmlfile ) throws IOException, SAXException, FileNotFoundException {
        loadConfig( new InputSource( new FileReader( xmlfile ) ) );
    }

    /**
     * Load the events from an XML InputSource. After this method has returned,
     * the {@link #getEvents()}, {@link #getSchedulers()} and
     * {@link #getSchedulingManager()} methods will return structures that
     * contain the objects created during the parsing of the XML config file.
     */
    public void loadConfig( InputSource xmlin ) throws IOException, SAXException {
        // Create the holder hashtables.
        events = new Hashtable<String, SchedulableEvent>();
        schedulers = new Hashtable<String, EventScheduler>();
        manager = new SchedulingManager();

        this.parse( xmlin );
    }

    /**
     * Class for parsing the "addSchedulableEvent" element.
     */
    protected class AddSchedulableEvent implements ElementHandler {
        public void startElement( String qName, Attributes attrs ) throws SAXParseException {
            if ( !getMarker( "eventScheduler" ) || curObj instanceof EventScheduler ) {
                throw new SAXParseException( "Can't use 'addSchedulableEvent' here!", null );
            }

            SchedulableEvent evt = (SchedulableEvent)events.get( attrs.getValue( "id" ) );

            if ( evt != null ) {
                ( (EventScheduler)curObj ).addSchedulableEvent( attrs.getValue( "id" ), evt );
            } else {
                throw new SAXParseException( "Can't find SchedulableEvent named, " + attrs.getValue( "id" ) +
                                             ", to add to EventScheduler, " +
                                             ( (EventScheduler)curObj ).getName(), null );
            }
        }

        public void endElement( String qName ) {}

        public void characterData( String qName, String cdata ) {}
    }

    /**
     * Class for parsing the "schedulableEvent" element.
     */
    protected class SchedulableEventParser implements ElementHandler {
        public void startElement( String qName, Attributes attrs ) throws SAXParseException {
            if ( getMarker( qName ) ) {
                throw new SAXParseException( "Nested schedulableEvent elements are not allowed.", null );
            }
            if ( getMarker( "eventScheduler" ) ) {
                throw new SAXParseException( "Can't use " + qName + "within an eventScheduler element.", null );
            }

            curObj = ReflectionHelpers.objectFromClassName( attrs.getValue( "class" ) );

            if ( curObj == null ) {
                throw new SAXParseException( "Can't instantiate " + attrs.getValue( "class" ), null );
            }
            if ( !( curObj instanceof SchedulableEvent ) ) {
                throw new SAXParseException( attrs.getValue( "class" ) +
                                             " does not implement the SchedulableEvent interface.", null );
            }

            setMarker( qName, true );
            events.put( attrs.getValue( "id" ), (SchedulableEvent)curObj );
        }

        public void endElement( String qName ) throws SAXParseException {
            if ( curObj == null || !( curObj instanceof SchedulableEvent ) ) {
                throw new SAXParseException( "Can't initialize null or non-SchedulableEvent object here.",
                                             null );
            }

            ( (SchedulableEvent)curObj ).init();
            setMarker( qName, false );
        }

        public void characterData( String qName, String cdata ) {}
    }

    /**
     * Class for parsing the "eventScheduler" element.
     */
    protected class EventSchedulerParser implements ElementHandler {
        public void startElement( String qName, Attributes attrs ) throws SAXParseException {
            if ( getMarker( qName ) ) {
                throw new SAXParseException( "Nested eventScheduler elements are not allowed.", null );
            }
            if ( getMarker( "schedulableEvent" ) ) {
                throw new SAXParseException( "Can't use " + qName + " within an schedulableEvent element.",
                                             null );
            }

            curObj = ReflectionHelpers.objectFromClassName( attrs.getValue( "class" ) );

            if ( curObj == null ) {
                throw new SAXParseException( "Can't instantiate " + attrs.getValue( "class" ), null );
            }
            if ( !( curObj instanceof EventScheduler ) ) {
                throw new SAXParseException( attrs.getValue( "class" ) +
                                             " does not implement the EventScheduler interface.", null );
            }

            setMarker( qName, true );
            // Add this EventScheduler to the hash of schedulers.
            schedulers.put( attrs.getValue( "id" ), (EventScheduler)curObj );
            // And to the SchedulingManager.
            manager.addEventScheduler( attrs.getValue( "id" ), (EventScheduler)curObj );
            // Set the name of the EventScheduler with the id attribute.
            ( (EventScheduler)curObj ).setName( attrs.getValue( "id" ) );
        }

        public void endElement( String qName ) throws SAXParseException {
            if ( curObj == null || !( curObj instanceof EventScheduler ) ) {
                throw new SAXParseException( "Can't initialize null or non-EventScheduler object here.", null );
            }

            ( (EventScheduler)curObj ).init();
            setMarker( qName, false );
        }

        public void characterData( String qName, String cdata ) {}
    }

    /**
     * ElementHandler class for parsing basic property elements.
     */
    protected class BasicPropertyElement implements DocumentHandler {
        private String data = null;

        public void startDocument() {}

        public void endDocument() {}

        public void startElement( String qName, Attributes attributes ) {}

        public void endElement( String qName ) throws SAXParseException {
            if ( !getMarker( "schedulableEvent" ) || !getMarker( "eventScheduler" ) ) {
                throw new SAXParseException( "Unknown element (" + qName +
                                             ") outside of schedulableEvent or eventScheduler elements.",
                                             null );
            }
            
            if ( curObj != null ||
                 ( !( curObj instanceof SchedulableEvent ) && !( curObj instanceof EventScheduler ) ) ) {
                ReflectionHelpers.invokeSetStringProperty( curObj, qName, data );
            } else {
                throw new SAXParseException( "No valid object available for property: " + qName, null );
            }
        }

        public void characterData( String qName, String cdata ) {
            data = cdata.trim();
        }
    }
}
