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

import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;

/**
 * An interface for classes wishing to be registered with a <tt>{@link
 * SimpleXMLReader}</tt> as element handlers.  It declares methods
 * that will be called when the start and end of the element are
 * reached as well as a method for handling the character data
 * contained within the element.
 */
public interface ElementHandler {

    /**
     * Method to be called when the start tag of an XML element is
     * encountered.
     *
     * @param qName The qualified XML name of the element for which
     * this method is called.
     * @param attributes The attributes defined in this XML
     * element. This may be an empty <tt>Attributes</tt> object if
     * there were no attributes defined for this element.
     */
    public void startElement( String qName, Attributes attributes ) throws SAXParseException;
    /**
     * Method to be called when the end tag of an XML element is
     * encountered.
     *
     * @param qName The qualified XML name of the element for which
     * this method is called.
     */
    public void endElement( String qName ) throws SAXParseException;
    /**
     * Method to be called with the character data found between the
     * starting element tag and the ending element tag.
     *
     * @param qName The qualified XML name of the element for which
     * this method is called.
     * @param cdata A <tt>String</tt> containing the character data
     * (i.e. text) found between the start and end element tags.  This
     * will include any newlines, control characters, etc, but does
     * not contain sub-elements.
     */
    public void characterData( String qName, String cdata ) throws SAXParseException;

}
