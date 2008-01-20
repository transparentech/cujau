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

/**
 * An interface for classes wishing to be registered with a <tt>{@link
 * SimpleXMLReader}</tt> as a document handler.  
 * <p>
 * A document handler receives events concerning the start and end of
 * the parsing of an XML document.  It will also receive events
 * concerning elements for which there is no individual {@link
 * ElementHandler} instance registered.
 */
public interface DocumentHandler extends ElementHandler {

    /**
     * Method to be called when parsing of the document is started.
     */
    public void startDocument();
    /**
     * Method to be called when parsing of the document is finished.
     */
    public void endDocument();

}
