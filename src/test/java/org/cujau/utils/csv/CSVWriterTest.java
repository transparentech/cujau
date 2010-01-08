package org.cujau.utils.csv;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CSVWriterTest {

    CSVWriter writer;
    
    @Before
    public void before() {
        writer = new CSVWriter( null );
    }
    
    @Test
    public void testEscapeDoubleQuotes() {
        String s = "asdf";
        assertTrue( "asdf".equals( writer.escapeDoubleQuotes( s ) ) );
        s = "as\"df";
        assertTrue( "as\"\"df".equals( writer.escapeDoubleQuotes( s ) ) );
        s = "as\n\rdf";
        assertTrue( "as\n\rdf".equals( writer.escapeDoubleQuotes( s ) ) );
        s = "asdf \"as\ndf\" fdsa";
        assertTrue( "asdf \"\"as\ndf\"\" fdsa".equals( writer.escapeDoubleQuotes( s ) ) );
    }
}
