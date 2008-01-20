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

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * JUnit test class for the {@link SimpleXMLReader} class.
 * Java 1.5 or greater and JUnit 4.1 or greater are required.
 */
public class SimpleXMLReaderTest {

    private XMLDocument doc;
    private SimpleXMLReader reader;

    @Before
    public void setUp() 
        throws SAXException
    {
        doc = new XMLDocument();
        reader = new SimpleXMLReader();
        reader.setDocumentHandler( new MyHandler() );
        reader.setErrorHandler( new MyErrorHandler() );
    }
   
	@Test
    public void xmlA() 
        throws IOException,
               SAXException
    {
        reader.parse( new InputSource( new StringReader( XML_A ) ) );

        checkStdDocument();
        checkElement( "test", "Here is sit,\nso broken hearted.\n" );
    }

    @Test
    public void xmlA_A() 
        throws IOException,
               SAXException
    {
        reader.setDocumentHandler( null );
        reader.addElementHandler( "test", new MyHandler( "T" ) );
        reader.parse( new InputSource( new StringReader( XML_A ) ) );

        checkErrDocument();
        checkElement( "Ttest", "Here is sit,\nso broken hearted.\n" );
    }

    /*
     * Test with no handlers registered.
     */
    @Test
    public void xmlA_B()
        throws IOException,
               SAXException
    {
        reader.setDocumentHandler( null );
        reader.setErrorHandler( null );
        reader.parse( new InputSource( new StringReader( XML_A ) ) );
    }

    @Test
    public void xmlB()
        throws IOException,
               SAXException
    {
        reader.parse( new InputSource( new StringReader( XML_B ) ) );

        checkStdDocument();
        checkElement( "test", null );
        checkElement( "a", "AAA" );
        checkElement( "b", "BBB" );
    }

    @Test
    public void xmlB_A()
        throws IOException,
               SAXException
    {
        reader.setDocumentHandler( null );
        reader.addElementHandler( "test", new MyHandler( "T" ) );
        reader.addElementHandler( "a", new MyHandler( "A" ) );
        reader.addElementHandler( "b", new MyHandler( "B" ) );
        reader.parse( new InputSource( new StringReader( XML_B ) ) );

        checkErrDocument();
        checkElement( "Ttest", null );
        checkElement( "Aa", "AAA" );
        checkElement( "Bb", "BBB" );
    }

    @Test
    public void xmlC()
        throws IOException,
               SAXException
    {
        reader.parse( new InputSource( new StringReader( XML_C ) ) );

        checkStdDocument();
        checkElement( "md:test", null );
        checkElement( "md:a", "AAA" );
        checkElement( "md:b", "BBB" );
    }

    @Test
    public void xmlC_A()
        throws IOException,
               SAXException
    {
        reader.setDocumentHandler( null );
        reader.addElementHandler( "md:test", new MyHandler( "T" ) );
        reader.addElementHandler( "md:a", new MyHandler( "A" ) );
        reader.addElementHandler( "md:b", new MyHandler( "B" ) );
        reader.parse( new InputSource( new StringReader( XML_C ) ) );

        checkErrDocument();
        checkElement( "Tmd:test", null );
        checkElement( "Amd:a", "AAA" );
        checkElement( "Bmd:b", "BBB" );
    }

    @Test
    public void xmlC_B()
        throws IOException,
               SAXException
    {
        reader.setDocumentHandler( null );
        reader.addElementHandler( "md:test", new MyHandler( "T" ) );
        reader.addElementHandler( "md:a", new MyHandler( "A" ) );
        reader.parse( new InputSource( new StringReader( XML_C ) ) );

        checkErrDocument();
        checkElement( "Tmd:test", null );
        checkElement( "Amd:a", "AAA" );
        checkMissingElement( "md:b" );
    }

    @Test
    public void xmlD()
        throws IOException,
               SAXException
    {
        reader.parse( new InputSource( new StringReader( XML_D ) ) );

        checkStdDocument();
        checkElement( "test", null );
        checkElementAttribute( "test", "one", "111" );
        checkElementAttribute( "test", "two", "222" );
        checkElementAttribute( "test", "three", null );
        checkElement( "a", "AAA" );
        checkElementAttribute( "a", "aaa", "321" );
        checkElementAttribute( "a", "bbb", null );
        checkElement( "b", "BBB" );
        checkElementAttribute( "b", "bbb", "654" );
        checkElementAttribute( "b", "aaa", null );
    }

    @Test
    public void xmlD_A()
        throws IOException,
               SAXException
    {
        reader.setDocumentHandler( null );
        reader.addElementHandler( "test", new MyHandler( "T" ) );
        reader.addElementHandler( "a", new MyHandler( "A" ) );
        reader.addElementHandler( "b", new MyHandler( "B" ) );
        reader.parse( new InputSource( new StringReader( XML_D ) ) );

        checkErrDocument();
        checkElement( "Ttest", null );
        checkElementAttribute( "Ttest", "one", "111" );
        checkElementAttribute( "Ttest", "two", "222" );
        checkElementAttribute( "Ttest", "three", null );
        checkElement( "Aa", "AAA" );
        checkElementAttribute( "Aa", "aaa", "321" );
        checkElementAttribute( "Aa", "bbb", null );
        checkElement( "Bb", "BBB" );
        checkElementAttribute( "Bb", "bbb", "654" );
        checkElementAttribute( "Bb", "aaa", null );
    }

    @Test
    public void xmlD_B()
        throws IOException,
               SAXException
    {
        reader.setDocumentHandler( null );
        reader.addElementHandler( "test", new MyHandler( "T" ) );
        reader.addElementHandler( "b", new MyHandler( "B" ) );
        reader.parse( new InputSource( new StringReader( XML_D ) ) );

        checkErrDocument();
        checkElement( "Ttest", null );
        checkElementAttribute( "Ttest", "one", "111" );
        checkElementAttribute( "Ttest", "two", "222" );
        checkElementAttribute( "Ttest", "three", null );
        checkMissingElement( "a" );
        checkElement( "Bb", "BBB" );
        checkElementAttribute( "Bb", "bbb", "654" );
        checkElementAttribute( "Bb", "aaa", null );
    }

    @Test
    public void xmlE()
        throws IOException,
               SAXException
    {
        reader.parse( new InputSource( new StringReader( XML_E ) ) );

        checkStdDocument();
        checkElement( "test", 
                      "\n  Some text for testing.\n  " +
                      "~\n  More text for testing.\n" );
        checkElementAttribute( "test", "one", "111" );
        checkElementAttribute( "test", "two", "222" );
        checkElementAttribute( "test", "three", null );
        checkElement( "a", "AAA" );
        checkElementAttribute( "a", "aaa", "321" );
        checkElementAttribute( "a", "bbb", null );
        checkElement( "b", "BBB" );
        checkElementAttribute( "b", "bbb", "654" );
        checkElementAttribute( "b", "aaa", null );
    }

    @Test
    public void xmlE_A()
        throws IOException,
               SAXException
    {
        reader.setDocumentHandler( null );
        reader.addElementHandler( "test", new MyHandler( "T" ) );
        reader.addElementHandler( "a", new MyHandler( "A" ) );
        reader.addElementHandler( "b", new MyHandler( "B" ) );
        reader.parse( new InputSource( new StringReader( XML_E ) ) );

        checkErrDocument();
        checkElement( "Ttest", 
                      "\n  Some text for testing.\n  " +
                      "~\n  More text for testing.\n" );
        checkElementAttribute( "Ttest", "one", "111" );
        checkElementAttribute( "Ttest", "two", "222" );
        checkElementAttribute( "Ttest", "three", null );
        checkElement( "Aa", "AAA" );
        checkElementAttribute( "Aa", "aaa", "321" );
        checkElementAttribute( "Aa", "bbb", null );
        checkElement( "Bb", "BBB" );
        checkElementAttribute( "Bb", "bbb", "654" );
        checkElementAttribute( "Bb", "aaa", null );
    }

    @Test
    public void xmlE_B()
        throws IOException,
               SAXException
    {
        reader.setDocumentHandler( null );
        reader.addElementHandler( "a", new MyHandler( "A" ) );
        reader.addElementHandler( "b", new MyHandler( "B" ) );
        reader.parse( new InputSource( new StringReader( XML_E ) ) );

        checkErrDocument();
        checkMissingElement( "test" );
        checkElement( "Aa", "AAA" );
        checkElementAttribute( "Aa", "aaa", "321" );
        checkElementAttribute( "Aa", "bbb", null );
        checkElement( "Bb", "BBB" );
        checkElementAttribute( "Bb", "bbb", "654" );
        checkElementAttribute( "Bb", "aaa", null );
    }


    @Test
    public void xmlF_warning()
        throws IOException,
               SAXException
    {
        try {
            reader.parse( new InputSource( new StringReader( XML_WARNING ) ) );
        } catch( Exception e ) {
            //System.out.print( "\nwarning - " + e.getClass().getName() );
            //System.out.println( " - " + e.getMessage() );
            assertTrue( e instanceof SAXParseException );
            assertTrue( e.getMessage().equals( "The version is required " +
                                               "in the XML declaration." ) );
        }

        // From what I've read, this particular problem should be
        // categorized as a warning, not a fatalError.  The Sun xerces
        // parser only seems to generate fatalErrors.
        assertFalse( doc.doc.containsKey( "warning" ) );
        assertFalse( doc.doc.containsKey( "error" ) );
        assertTrue( doc.doc.containsKey( "fatalError" ) );
    }

    @Test
    public void xmlF_error()
        throws IOException,
               SAXException
    {
        try {
            reader.parse( new InputSource( new StringReader( XML_ERROR ) ) );
        } catch ( Exception e ) {
            //System.out.print( "\nerror - " + e.getClass().getName() );
            //System.out.println( " - " + e.getMessage() );
            assertTrue( e instanceof SAXParseException );
            assertTrue( e.getMessage().equals( "The entity \"bugger\" was " +
                                               "referenced, " +
                                               "but not declared." ) );
        }

        // From what I've read, this particular problem should be
        // categorized as an error, not a fatalError.  The Sun xerces
        // parser only seems to generate fatalErrors.
        assertFalse( doc.doc.containsKey( "warning" ) );
        assertFalse( doc.doc.containsKey( "error" ) );
        assertTrue( doc.doc.containsKey( "fatalError" ) );

        assertTrue( doc.doc.containsKey( "test~start" ) );
        assertFalse( doc.doc.containsKey( "test~cdata" ) );
        assertFalse( doc.doc.containsKey( "test~end" ) );

        assertTrue( doc.doc.containsKey( "a~start" ) );
        assertFalse( doc.doc.containsKey( "a~cdata" ) );
        assertFalse( doc.doc.containsKey( "a~end" ) );
    }

    @Test
    public void xmlF_fatalError()
        throws IOException,
               SAXException
    {
        try {
            reader.parse( new InputSource( new StringReader( XML_FATAL ) ) );
        } catch ( Exception e ) {
            //System.out.print( "\nfatal - " + e.getClass().getName() );
            //System.out.println( " - " + e.getMessage() );
            assertTrue( e instanceof SAXParseException );
            assertTrue( e.getMessage().equals( "The end-tag for element " +
                                               "type \"a\" must end with a " +
                                               "'>' delimiter." ) );
        }

        assertFalse( doc.doc.containsKey( "warning" ) );
        assertFalse( doc.doc.containsKey( "error" ) );
        assertTrue( doc.doc.containsKey( "fatalError" ) );

        assertTrue( doc.doc.containsKey( "test~start" ) );
        assertFalse( doc.doc.containsKey( "test~cdata" ) );
        assertFalse( doc.doc.containsKey( "test~end" ) );

        assertTrue( doc.doc.containsKey( "a~start" ) );
        assertFalse( doc.doc.containsKey( "a~cdata" ) );
        assertFalse( doc.doc.containsKey( "a~end" ) );
    }

    protected void checkErrDocument() {
        assertFalse( doc.doc.containsKey( "warning" ) );
        assertFalse( doc.doc.containsKey( "error" ) );
        assertFalse( doc.doc.containsKey( "fatalError" ) );
    }
    protected void checkStdDocument() {
        assertTrue( doc.doc.containsKey( "startDocument" ) );
        assertTrue( doc.doc.containsKey( "endDocument" ) );
        checkErrDocument();
    }

    protected void checkElementAttribute( String elem, String attrName, 
                                          String attrVal ) {
        String elemAttrName = elem + ":" + attrName;
        if ( attrVal != null ) {
            assertTrue( doc.doc.containsKey( elemAttrName ) );
            assertNotNull( doc.get( elemAttrName ) );
            assertTrue( doc.get( elemAttrName ).equals( attrVal ) );
        } else {
            assertFalse( doc.doc.containsKey( elemAttrName ) );
        }
    }

    protected void checkElement( String elem, String data ) {
        assertTrue( doc.doc.containsKey( elem + "~start" ) );
        if ( data != null ) {
            assertTrue( doc.doc.containsKey( elem + "~cdata" ) );
            String val = doc.get( elem + "~cdata" );
            assertNotNull( val );
            assertTrue( val.equals( data ) );
        } else {
            assertFalse( doc.doc.containsKey( elem + "~cdata" ) );
        }
        assertTrue( doc.doc.containsKey( elem + "~end" ) );
    }

    protected void checkMissingElement( String elem ) {
        assertTrue( doc.doc.containsKey( "missing~" + elem ) );
    }

    protected class XMLDocument {
        public HashMap<String,String> doc;
        public XMLDocument() {
            doc = new HashMap<String,String>();
        }
        public void put( String name, String value ) {
            doc.put( name, value );
        }
        public String get( String name ) {
            return doc.get( name );
        }
    }

    protected class MyHandler implements DocumentHandler {
        String prefix;
        public MyHandler( String prefix ) {
            this.prefix = prefix;
        }
        public MyHandler() {
            this.prefix = "";
        }
        public void startElement( String qName, Attributes atts ) {
            doc.put( prefix + qName + "~start", "yes" );
            for ( int i = 0; i < atts.getLength(); i++ ) {
                doc.put( prefix + qName + ":" + atts.getQName( i ), 
                         atts.getValue( i ) );
            }
        }
        public void characterData( String qName, String cdata ) {
            String name = prefix + qName + "~cdata";
            if ( doc.doc.containsKey( name ) ) {
                doc.put( name, (String)doc.get( name ) + "~" + cdata );
            } else {
                doc.put( name, cdata );
            }
        }
        public void endElement( String qName ) {
            doc.put( prefix + qName + "~end", "yes" );
        }
        public void startDocument() {
            doc.put( "startDocument", "yes" );
        }
        public void endDocument() {
            doc.put( "endDocument", "yes" );
        }
    }

    protected class MyErrorHandler implements ErrorHandler {
        public void warning( SAXParseException exception ) 
            throws SAXException
        {
            doc.put( "warning", exception.getMessage() );
        }
        public void error( SAXParseException exception ) 
            throws SAXException
        {
            doc.put( "error", exception.getMessage() );
        }
        public void fatalError( SAXParseException exception ) 
            throws SAXException
        {
            doc.put( "fatalError", exception.getMessage() );
        }
        public void missingElementHandler( String qName ) {
            doc.put( "missing~" + qName, qName );
        }
    }

    private static final String XML_A = 
        "<test>Here is sit,\n" +
        "so broken hearted.\n" +
        "</test>";
    private static final String XML_B = 
        "<test>\n" +
        "  <a>AAA</a>\n" +
        "  <b>BBB</b>\n" +
        "</test>";
    private static final String XML_C = 
        "<md:test xmlns:md=\"http://www.mdude.com\">\n" +
        "  <md:a>AAA</md:a>\n" +
        "  <md:b>BBB</md:b>\n" +
        "</md:test>";
    private static final String XML_D = 
        "<test one=\"111\" two=\"222\">\n" +
        "  <a aaa=\"321\">AAA</a>\n" +
        "  <b bbb=\"654\">BBB</b>\n" +
        "</test>";
    private static final String XML_E = 
        "<test one=\"111\" two=\"222\">\n" +
        "  Some text for testing.\n" +
        "  <a aaa=\"321\">AAA</a>\n" +
        "  <b bbb=\"654\">BBB</b>\n" +
        "  More text for testing.\n" +
        "</test>";

    private static final String XML_WARNING = 
        "<?xml encoding=\"UTF-8\" ?>" +
        "<test>\n" +
        "  <a>AAA</a>\n" +
        "  <b>BBB</b>\n" +
        "</test>";
    private static final String XML_ERROR = 
        "<test>\n" +
        "  <a>&bugger; AAA AAA</a>\n" +
        "</test>";
    private static final String XML_FATAL = 
        "<test>\n" +
        "  <a>AAA</a\n" +
        "</test>";
}
