package org.cujau.utils.converters;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Helper methods for converting Strings to integers or floats.
 */
public class StringConverterHelper {

    private final static Pattern NUM_STRIPPER_FLOAT_RE = Pattern.compile( "[^0-9\\.\\-\\'\\,]" );
    private final static Pattern NUM_STRIPPER_ALL_FLOAT_RE = Pattern.compile( "[^0-9\\.\\-\\'\\,]" );
    private final static Pattern NUM_STRIPPER_INT_RE = Pattern.compile( "[^0-9\\-\\'\\,]" );
    private final static Pattern NUM_STRIPPER_ALL_INT_RE = Pattern.compile( "[^0-9\\-]" );

    private static String stripNonIntlFloatJunk( String str ) {
        return NUM_STRIPPER_FLOAT_RE.matcher( str ).replaceAll( "" );
    }

    private static String stripNonIntlIntJunk( String str ) {
        return NUM_STRIPPER_INT_RE.matcher( str ).replaceAll( "" );
    }

    public static BigDecimal simpleBigDecimalValueOf( String str, Locale loc )
            throws ParseException {
        str = NUM_STRIPPER_ALL_FLOAT_RE.matcher( str ).replaceAll( "" );
        BigDecimal ret;
        try {
            DecimalFormat fmt = (DecimalFormat) NumberFormat.getInstance( loc );
            fmt.setParseBigDecimal( true );
            ret = (BigDecimal) fmt.parse( str );
        } catch ( ParseException e ) {
            throw e;
        }
        return ret;
    }

    public static BigDecimal bigDecimalValueOf( String str )
            throws ParseException {
        return bigDecimalValueOf( str, Locale.getDefault() );
    }

    public static BigDecimal bigDecimalValueOf( String str, Locale loc )
            throws ParseException {
        // Always use the simple version, which removes non-number characters before parsing.
        // This may be slightly slower than the parse-if-error-strip-parse way that was used 
        // before, but it can handle a broader range of number formats (notably fr_FR with the
        // space grouping separator).
        return simpleBigDecimalValueOf( str, loc );
//        BigDecimal ret;
//        try {
//            DecimalFormat fmt = (DecimalFormat) NumberFormat.getInstance( loc );
//            fmt.setParseBigDecimal( true );
//            ret = (BigDecimal) fmt.parse( str );
//        } catch ( ParseException e ) {
//            return simpleBigDecimalValueOf( str, loc );
//        }
//        return ret;
    }

    public static float simpleFloatValueOf( String str )
            throws NumberFormatException {
        str = NUM_STRIPPER_ALL_FLOAT_RE.matcher( str ).replaceAll( "" );
        float ret;
        try {
            ret = Float.valueOf( str );
        } catch ( NumberFormatException e ) {
            throw e;
        }
        return ret;
    }

    public static float floatValueOf( String str ) {
        NumberFormat nf = NumberFormat.getInstance();
        Number nbr;
        try {
            nbr = nf.parse( str );
            return nbr.floatValue();
        } catch ( ParseException e1 ) {
            try {
                nbr = nf.parse( stripNonIntlFloatJunk( str ) );
                return nbr.floatValue();
            } catch ( ParseException e2 ) {
                return simpleFloatValueOf( str );
            }
        }
    }

    public static double simpleDoubleValueOf( String str )
            throws NumberFormatException {
        str = NUM_STRIPPER_ALL_FLOAT_RE.matcher( str ).replaceAll( "" );
        double ret;
        try {
            ret = Double.valueOf( str );
        } catch ( NumberFormatException e ) {
            throw e;
        }
        return ret;
    }

    public static double doubleValueOf( String str ) {
        NumberFormat nf = NumberFormat.getInstance();
        Number nbr;
        try {
            nbr = nf.parse( str );
            return nbr.doubleValue();
        } catch ( ParseException e1 ) {
            try {
                nbr = nf.parse( stripNonIntlFloatJunk( str ) );
                return nbr.doubleValue();
            } catch ( ParseException e2 ) {
                return simpleDoubleValueOf( str );
            }
        }
    }

    public static int simpleIntValueOf( String str )
            throws NumberFormatException {
        str = NUM_STRIPPER_ALL_INT_RE.matcher( str ).replaceAll( "" );
        int ret;
        try {
            ret = Integer.valueOf( str );
        } catch ( NumberFormatException e ) {
            throw e;
        }
        return ret;
    }

    public static int intValueOf( String str ) {
        NumberFormat nf = NumberFormat.getInstance();
        Number nbr;
        try {
            nbr = nf.parse( str );
            return nbr.intValue();
        } catch ( ParseException e ) {
            try {
                nbr = nf.parse( stripNonIntlIntJunk( str ) );
                return nbr.intValue();
            } catch ( ParseException e2 ) {
                return simpleIntValueOf( str );
            }
        }
    }

    public static boolean booleanValueOf( String str ) {
        if ( str != null && ( str.equals( "1" ) || str.toLowerCase().equals( "yes" ) ) ) {
            return true;
        }
        return Boolean.valueOf( str );
    }
}
