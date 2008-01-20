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
package org.cujau.xml;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Class for reading simple XML documents and propagating SAX (Simple API for
 * XML) events to handler objects.
 * 
 * <p>
 * Handler objects (instances of a class that implements one or more of the
 * <tt>{@link ElementHandler}</tt>, <tt>{@link
 * DocumentHandler}</tt> or
 * <tt>{@link ErrorHandler}</tt> interfaces) are registered with an instance
 * of the <tt>SimpleXMLReader</tt> class via one of the <tt>add*Handler</tt>
 * methods. An XML document can then be parsed by the <tt>SimpleXMLReader</tt>
 * class using the {@link SimpleXMLReader#parse} method. Parsing of an XML
 * document generates events (such as at the start of an XML element, the end of
 * the document or parsing errors) which are passed to any handler objects that
 * have been registered.
 * 
 * <p>
 * The <tt>SimpleXMLReader</tt> class allows you to register one
 * <tt>{@link ElementHandler}</tt> instance for each tag element. For example,
 * if you were parsing the following XML:
 * 
 * <pre>
 * &lt;test&gt;
 *   &lt;abc&gt;ABC123&lt;/abc&gt;
 * &lt;/test&gt;
 * </pre>
 * 
 * You could register an <tt>{@link ElementHandler}</tt> instance for the
 * <tt>test</tt> tag and another for the <tt>abc</tt> tag. You could also
 * register an <tt>{@link ElementHandler}</tt> instance for the <tt>xyz</tt>
 * tag, but since this tag is not present in the example XML document, no events
 * would ever be propogated to it.
 * 
 * <p>
 * By registering a <tt>{@link DocumentHandler}</tt> instance, you can catch
 * events for tag elements that do not have a specific
 * <tt>{@link ElementHandler}</tt> registered. For example, if you registered
 * an <tt>{@link ElementHandler}</tt> instance for the <tt>test</tt> tag and
 * a <tt>{@link DocumentHandler}</tt> instance, when the example XML document
 * above was parsed, the events for the <tt>abc</tt> tag would be handled by
 * the <tt>{@link
 * DocumentHandler}</tt> instance.
 * <tt>{@link DocumentHandler}s</tt> also allow you to catch events such as
 * the start or end of the document.
 * 
 * @see ElementHandler
 * @see DocumentHandler
 * @see ErrorHandler
 */
public class SimpleXMLReader {

    private XMLReader reader;

    private Hashtable<String, ElementHandler> handlers;

    private Hashtable<String, Boolean> markers;

    private DocumentHandler docHandler;

    private ErrorHandler errHandler;

    /**
     * Instantiate a new <tt>SimpleXMLReader</tt>.
     */
    public SimpleXMLReader() throws SAXException {
        handlers = new Hashtable<String, ElementHandler>();
        markers = new Hashtable<String, Boolean>();
        docHandler = null;
        errHandler = null;

        // Create an XML reader.
        reader = XMLReaderFactory.createXMLReader();
        // reader = XMLReaderFactory.createXMLReader( DEFAULT_READER );

        reader.setContentHandler( new InternContentHandler() );
        // Handle namespaces.
        reader.setFeature( "http://xml.org/sax/features/namespaces", true );
        // Require the qualified name (with prefix) since we only use
        // the qualfied name (and not the "uri" or "localName").
        reader.setFeature( "http://xml.org/sax/features/namespace-prefixes", true );
        // Turning off validation as it doesn't seem to make a difference.
        reader.setFeature( "http://xml.org/sax/features/validation", false );
    }

    /**
     * Parse the given XML document source, propogating SAX events to any
     * registered handler objects.
     * 
     * @param source
     *            An <tt>InputSource</tt> representing the XML document to be
     *            parsed.
     */
    public synchronized void parse( InputSource source ) throws IOException, SAXException {
        reader.parse( source );
    }

    /**
     * Register an object to handle events for the given XML element.
     * 
     * @param elementName
     *            The name of the XML element whose events should be propogated
     *            to the given <tt>handler</tt>.
     * @param handler
     *            The <tt>{@link ElementHandler}</tt> object whose methods
     *            will be called when events for the element,
     *            <tt>elementName</tt>, arise during parsing.
     */
    public void addElementHandler( String elementName, ElementHandler handler ) {
        handlers.put( elementName, handler );
    }

    /**
     * Register an object to handle XML document level events.
     * 
     * @param handler
     *            The <tt>{@link DocumentHandler}</tt> object whose methods
     *            will be called when document level events arise during
     *            parsing.
     */
    public void setDocumentHandler( DocumentHandler handler ) {
        docHandler = handler;
    }

    /**
     * Register an object to handle errors arising during parsing of the XML
     * document.
     * 
     * @param handler
     *            The <tt>{@link ErrorHandler}</tt> object whose methods will
     *            be called when errors arise durning parsing of the XML
     *            document.
     */
    public void setErrorHandler( ErrorHandler handler ) {
        errHandler = handler;
        reader.setErrorHandler( handler );
    }

    /**
     * Set a marker. Markers are typically used to denote informational points
     * during the parsing of an XML document.
     * 
     * @param markName
     *            The name of the marker to set.
     * @param mark
     *            <tt>true</tt> if setting a marker, <tt>false</tt> if
     *            unsetting a marker.
     */
    public void setMarker( String markName, boolean mark ) {
        markers.put( markName, mark );
    }

    /**
     * Get the named marker.
     * 
     * @param markName
     *            The name of the marker whose value is retrieved.
     * @return The value of the named marker. If the named marker does not
     *         exist, <tt>false<tt> is returned.
     */
    public boolean getMarker( String markName ) {
        Boolean ret = markers.get( markName );
        if ( ret == null ) {
            return false;
        } else {
            return ret;
        }
    }

    //
    // Private, internal methods and classes.
    //

    private class InternContentHandler implements ContentHandler {
        private static final String EMPTY_STR = "";

        private String cdata;

        private Stack<ElementHandler> elems;

        private Stack<String> elemNames;

        public InternContentHandler() {
            cdata = null;
            elems = new Stack<ElementHandler>();
            elemNames = new Stack<String>();
        }

        public void startDocument() {
            if ( docHandler != null ) {
                docHandler.startDocument();
            }
        }

        public void endDocument() {
            if ( docHandler != null ) {
                docHandler.endDocument();
            }
        }

        public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException {
            if ( cdata != null ) {
                ElementHandler h = elems.peek();
                String hName = elemNames.peek();
                h.characterData( hName, cdata );
            }
            cdata = null;

            ElementHandler handler = handlers.get( qName );

            if ( handler != null ) {
                handler.startElement( qName, atts );
                elems.push( handler );
                elemNames.push( qName );
            } else if ( docHandler != null ) {
                docHandler.startElement( qName, atts );
                elems.push( docHandler );
                elemNames.push( qName );
            } else if ( errHandler != null ) {
                errHandler.missingElementHandler( qName );
                elems.push( new DefaultElementHandler() );
                elemNames.push( qName );
            } else {
                // Do nothing since no handler was registered.
            }
        }

        public void characters( char[] ch, int start, int length ) {
            if ( cdata == null ) {
                cdata = EMPTY_STR;
            }
            cdata = cdata + new String( ch, start, length );
            if ( cdata.trim().equals( EMPTY_STR ) ) {
                cdata = null;
            }
        }

        public void endElement( String uri, String localName, String qName ) throws SAXException {
            ElementHandler handler = handlers.get( qName );

            if ( handler != null ) {
                if ( cdata != null ) {
                    handler.characterData( qName, cdata );
                }
                handler.endElement( qName );
                elems.pop();
                elemNames.pop();
            } else if ( docHandler != null ) {
                if ( cdata != null ) {
                    docHandler.characterData( qName, cdata );
                }
                docHandler.endElement( qName );
                elems.pop();
                elemNames.pop();
            } else if ( errHandler != null ) {
                elems.pop();
                elemNames.pop();
            } else {
                // Do nothing since no handler was registered.
            }
            cdata = null;
        }

        public void startPrefixMapping( String prefix, String uri ) {}

        public void endPrefixMapping( String prefix ) {}

        public void ignorableWhitespace( char[] ch, int start, int length ) {}

        public void processingInstruction( String target, String data ) {}

        public void setDocumentLocator( Locator locator ) {}

        public void skippedEntity( String name ) {}
    }
}
