package org.cujau.utils.converters;

import java.util.regex.Pattern;

/**
 * Helper methods for converting Strings to integers or floats.
 */
public class StringConverterHelper {

    private final static Pattern NUM_STRIPPER_FLOAT_RE = Pattern.compile( "[^0-9\\.]" );
    private final static Pattern NUM_STRIPPER_INT_RE = Pattern.compile( "[^0-9]" );

    public static float floatValueOf( String str )
            throws NumberFormatException {
        str = NUM_STRIPPER_FLOAT_RE.matcher( str ).replaceAll( "" );
        float ret;
        try {
            ret = Float.valueOf( str );
        } catch ( NumberFormatException e ) {
            throw e;
        }
        return ret;
    }

    public static int intValueOf( String str )
            throws NumberFormatException {
        str = NUM_STRIPPER_INT_RE.matcher( str ).replaceAll( "" );
        int ret;
        try {
            ret = Integer.valueOf( str );
        } catch ( NumberFormatException e ) {
            throw e;
        }
        return ret;
    }
}
