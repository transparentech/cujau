package org.cujau.utils.converters;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringConverterHelperTest {

    private static final float DELTA = 0.0001f;

    @Test
    public void testFloatValueOf() {
        assertEquals( 1234f, StringConverterHelper.floatValueOf( "1234" ), DELTA );
        assertEquals( 1234f, StringConverterHelper.floatValueOf( "1234." ), DELTA );
        assertEquals( 1234f, StringConverterHelper.floatValueOf( "1234.0" ), DELTA );
        assertEquals( 1234.56f, StringConverterHelper.floatValueOf( "1234.56" ), DELTA );
        assertEquals( 1234.5007f, StringConverterHelper.floatValueOf( "1234.5007" ), DELTA );
        assertEquals( 1234.5678f, StringConverterHelper.floatValueOf( "1234.5678" ), DELTA );
        assertEquals( 1234.5678f, StringConverterHelper.floatValueOf( "1,234.5678" ), DELTA );
        assertEquals( 1234567.89f, StringConverterHelper.floatValueOf( "1,234,567.89" ), DELTA );

        assertEquals( -1234f, StringConverterHelper.floatValueOf( "-1234" ), DELTA );
        assertEquals( -1234f, StringConverterHelper.floatValueOf( "-1234." ), DELTA );
        assertEquals( -1234f, StringConverterHelper.floatValueOf( "-1234.0" ), DELTA );
        assertEquals( -1234.56f, StringConverterHelper.floatValueOf( "-1234.56" ), DELTA );
        assertEquals( -1234.5007f, StringConverterHelper.floatValueOf( "-1234.5007" ), DELTA );
        assertEquals( -1234.5678f, StringConverterHelper.floatValueOf( "-1234.5678" ), DELTA );
        assertEquals( -1234.5678f, StringConverterHelper.floatValueOf( "-1,234.5678" ), DELTA );
        assertEquals( -1234567.89f, StringConverterHelper.floatValueOf( "-1,234,567.89" ), DELTA );

        assertEquals( 1234f, StringConverterHelper.floatValueOf( "$1234" ), DELTA );
        assertEquals( 1234f, StringConverterHelper.floatValueOf( "1234.\u20AC" ), DELTA ); // Euro
                                                                                           // sym
        assertEquals( 1234f, StringConverterHelper.floatValueOf( "1234.0 CHF" ), DELTA );
        assertEquals( 1234.56f, StringConverterHelper.floatValueOf( "C$1234.56" ), DELTA );
        assertEquals( 1234.5007f, StringConverterHelper.floatValueOf( "AU$1234.5007\u20AC" ), DELTA );
        assertEquals( 1234.5678f, StringConverterHelper.floatValueOf( "1234.5678\u20A4" ), DELTA ); // GBP
                                                                                                    // sym
        assertEquals( 1234.5678f, StringConverterHelper.floatValueOf( "$1,234.5678$" ), DELTA );
        assertEquals( 1234567.89f, StringConverterHelper.floatValueOf( "\u20A41,234,567.89\u20A4" ), DELTA );
        
        boolean exception = false;
        try {
            StringConverterHelper.floatValueOf( "1-23.45" );
        } catch ( NumberFormatException e ) {
            exception = true;
        }
        assertTrue( exception );
    }

    @Test
    public void testIntValueOf() {
        assertTrue( 1234 == StringConverterHelper.intValueOf( "1234" ) );
        assertTrue( 1234 == StringConverterHelper.intValueOf( "1,234" ) );
        assertTrue( 1234567 == StringConverterHelper.intValueOf( "1,234,567" ) );
        assertTrue( 1234 == StringConverterHelper.intValueOf( "$1,234$" ) );
        assertTrue( 1234567 == StringConverterHelper.intValueOf( "\u20A41,234,567\u20A4" ) );
    }
}
