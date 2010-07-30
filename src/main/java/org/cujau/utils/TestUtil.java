package org.cujau.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestUtil {

    private static final SimpleDateFormat SDF = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
    private static final SimpleDateFormat SDFTS = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS" );
    private static final SimpleDateFormat SDFSIMPLE = new SimpleDateFormat( "yyyy-MM-dd" );

    public static void assertTimestamp( Date orig, String exp ) {
        String sorig = SDFTS.format( orig );
        assertTrue( "Expecting '" + exp + "' but got '" + sorig + "'", sorig.equals( exp ) );
    }

    public static void assertDatetime( Date orig, String exp ) {
        String sorig = SDF.format( orig );
        assertTrue( "Expecting '" + exp + "' but got '" + sorig + "'", sorig.equals( exp ) );
    }

    public static void assertDate( Date orig, String exp ) {
        String sorig = SDFSIMPLE.format( orig );
        assertTrue( "Expecting '" + exp + "' but got '" + sorig + "'", sorig.equals( exp ) );
    }

    static public void assertTrue( String message, boolean condition ) {
        if ( !condition ) {
            fail( message );
        }
    }

    static public void assertTrue( boolean condition ) {
        if ( !condition ) {
            fail( null );
        }
    }

    static public void fail( String message ) {
        throw new AssertionError( message == null ? "" : message );
    }

}
