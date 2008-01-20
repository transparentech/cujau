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
 * SimpleXMLReader}</tt> as an error handler.  This interface extends
 * the <tt>org.xml.sax.ErrorHandler</tt> class, adding methods
 * specific to errors encountered when used with a
 * <tt>SimpleXMLReader</tt>.
 */
public interface ErrorHandler extends org.xml.sax.ErrorHandler {

    /**
     * Method to be called when no {@link ElementHandler} object has
     * been registered for the given element.
     *
     * @param qName The qualified XML name of the element for which
     * there is no registered {@link ElementHandler}.
     */
    public void missingElementHandler( String qName );

}