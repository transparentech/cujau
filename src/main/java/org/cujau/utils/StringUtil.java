package org.cujau.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.text.Format;
import java.util.Collection;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility functions for Strings.
 */
public final class StringUtil {

    private static final Pattern PROPERTY_NAME_PATTERN = Pattern.compile( "(\\$\\{([\\w\\.]+)\\})" );
    private static final String DEF_SEP = ",";
    public static final String EMPTY_STR = "";
    public static final String SPACE_STR = " ";
    public static final String UTF8_STR = "UTF-8";
    public static final Charset UTF8 = Charset.forName( UTF8_STR );

    /**
     * Get the bytes of the given String encoded in UTF-8.
     *
     * @param val
     *         The String whose UTF-8 encoded bytes are returned.
     * @return The UTF-8 encoded bytes.
     */
    public static byte[] toUtf8( String val ) {
        return val.getBytes( UTF8 );
    }

    public static String toString( double[] ary ) {
        return toString( ary, DEF_SEP );
    }

    public static String toString( double[] ary, String separator ) {
        StringBuilder buf = new StringBuilder();
        for ( double e : ary ) {
            buf.append( e );
            buf.append( separator );
        }
        if ( buf.length() > 0 ) {
            buf.deleteCharAt( buf.length() - 1 );
        }
        return buf.toString();
    }

    /**
     * Convert the given array of objects into it's String representation. The array elements will
     * be separated by a comma (',').
     *
     * @param <E>
     *            The type of the elements.
     * @param ary
     *            The array of the elements.
     * @return A String containg the String representation of each element in the array, separated
     *         by a comma.
     */
    public static <E> String toString( E[] ary ) {
        return toString( ary, DEF_SEP );
    }

    /**
     * Convert the given array of objects into it's String representation. The array elements will
     * be separated by the given <tt>separator</tt> string.
     *
     * @param <E>
     *            The type of the elements.
     * @param ary
     *            The array of the elements.
     * @param separator
     *            The character(s) to place between each element of the array.
     * @return A String containg the String representation of each element in the array, separated
     *         by the given separator character(s).
     */
    public static <E> String toString( E[] ary, String separator ) {
        StringBuilder buf = new StringBuilder();
        if ( ary == null ) {
            return buf.toString();
        }
        for ( E e : ary ) {
            buf.append( e );
            buf.append( separator );
        }
        if ( buf.length() > 0 ) {
            buf.deleteCharAt( buf.length() - 1 );
        }
        return buf.toString();
    }

    /**
     * Convert the given collection of objects into it's String representation. The String
     * representation of an element of the collection will be separated from the next element with a
     * comma (',').
     *
     * @param <E>
     *            The type of the elements.
     * @param col
     *            The collection of elements.
     * @return A String containing the String representation of each element in the Collection,
     *         separated by a comma.
     */
    public static <E> String toString( Collection<E> col ) {
        return toString( col, DEF_SEP );
    }

    /**
     * Convert the given collection of objects into it's String representation. The String
     * representation of an element of the collection will be separated from the next element with
     * the given separator.
     *
     * @param <E>
     *            The type of the elements.
     * @param col
     *            The collection of elements.
     * @param separator
     *            The string/character that will separate elements in the returned string.
     * @return A String containing the String representation of each element in the Collection,
     *         separated by the given separator.
     */
    public static <E> String toString( Collection<E> col, String separator ) {
        return toString( col, null, separator );
    }

    public static <E> String toString( Collection<E> col, Format formatter ) {
        return toString( col, formatter, DEF_SEP );
    }

    public static <E> String toString( Collection<E> col, Format formatter, String separator ) {
        StringBuilder buf = new StringBuilder();
        if ( col == null ) {
            return buf.toString();
        }
        for ( E e : col ) {
            if ( e != null ) {
                if ( formatter != null ) {
                    buf.append( formatter.format( e ) );
                } else {
                    buf.append( e.toString() );
                }
                buf.append( separator );
            }
        }
        if ( buf.length() > 0 ) {
            buf.delete( buf.length() - separator.length(), buf.length() );
        }
        return buf.toString();
    }

    /**
     * Convert the given Throwable object into a stacktrace.
     *
     * @param t
     *            The Throwable for which the stacktrace will be generated.
     * @return A String representation of the stacktrace.
     */
    public static String toString( Throwable t ) {
        if ( t == null ) {
            return EMPTY_STR;
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter( stringWriter );
        t.printStackTrace( printWriter );
        printWriter.flush();
        printWriter.close();
        return stringWriter.toString();
    }

    /**
     * Replace the delimited property keys in the given String with their values taken from the
     * System properties.
     * <p>
     * A delimited property key has the form
     * <tt>${<em>{@link #PROPERTY_NAME_PATTERN property.name}</em>}</tt>. Property names may only
     * contain alphanumeric characters plus the '_' and '.' characters.
     * </p>
     * <p>
     * Any delimited propery keys in the given String that do not have replacements in the System
     * properties object will remain in the String.
     * </p>
     *
     * @param orig
     *            The original String
     * @return The new String with the delimited properties replaced.
     */
    public static String replaceProperties( String orig ) {
        return replaceProperties( orig, System.getProperties() );
    }

    /**
     * Replace the delimited property keys in the given String with their values taken from the
     * given Properties object.
     * <p>
     * A delimited property key has the form
     * <tt>${<em>{@link #PROPERTY_NAME_PATTERN property.name}</em>}</tt>. Property names may only
     * contain alphanumeric characters plus the '_' and '.' characters.
     * </p>
     * <p>
     * Any delimited propery keys in the given String that do not have replacements in the given
     * Properties object will remain in the String.
     * </p>
     *
     * @param orig
     *            The original String
     * @param replacements
     *            A Properties object from which the property values will be taken.
     * @return The new String with the delimited properties replaced.
     */
    public static String replaceProperties( String orig, Properties replacements ) {
        return replaceProperties( orig, replacements, false );
    }

    /**
     * Replace the delimited property keys in the given String with their values taken from the
     * given Properties object.
     * <p>
     * A delimited property key has the form
     * <tt>${<em>{@link #PROPERTY_NAME_PATTERN property.name}</em>}</tt>. Property names may only
     * contain alphanumeric characters plus the '_' and '.' characters.
     * </p>
     * <p>
     * Any delimited propery keys in the given String that do not have replacements in the given
     * Properties object can be removed (replaced with the empty String) from the resulting String
     * by setting the <tt>removeUnmatchedKeys</tt> parameter to <tt>true</tt>.
     * </p>
     *
     * @param orig
     *            The original String
     * @param replacements
     *            A Properties object from which the property values will be taken.
     * @param removeUnmatchedKeys
     *            <tt>true</tt> if any delimited property keys in the given String should be removed
     *            from the resulting String if there is no matching property in the given Properties
     *            object.
     * @return The new String with the delimited properties replaced.
     */
    public static String replaceProperties( String orig, Properties replacements, boolean removeUnmatchedKeys ) {
        if ( orig == null ) {
            return null;
        }

        Matcher m = PROPERTY_NAME_PATTERN.matcher( orig );
        while ( m.find() ) {
            String propVal = null;
            if ( replacements != null ) {
                propVal = replacements.getProperty( m.group( 2 ) );
            }

            if ( propVal != null ) {
                orig = orig.replace( m.group( 1 ), propVal );
            } else if ( removeUnmatchedKeys ) {
                orig = orig.replace( m.group( 1 ), EMPTY_STR );
            }
        }
        return orig;
    }

    public static String padLeft( String str, int totalLength ) {
        return String.format( "%" + totalLength + "s", str );
    }

    public static String padRight( String str, int totalLength ) {
        if ( str == null ) {
            str = EMPTY_STR;
        }
        int padding = totalLength - str.length();
        String padFmt;
        if ( padding <= 0 ) {
            padFmt = EMPTY_STR;
        } else {
            padFmt = "%" + padding + "s";
        }
        return String.format( "%s" + padFmt, str, EMPTY_STR );
    }

    public static String secretize( String str ) {
        if ( str == null ) {
            return null;
        }
        if ( str.length() == 0 ) {
            return str;
        }
        String padFmt = "%0" + str.length() + "d";
        return String.format( padFmt, 0 ).replace( '0', '*' );
    }

    public static String secretizeRight( String str, int readableChars ) {
        if ( str == null ) {
            return null;
        }
        if ( readableChars >= str.length() ) {
            return str;
        }
        if ( readableChars < 0 ) {
            readableChars = 0;
        }
        return str.substring( 0, readableChars ) + secretize( str.substring( readableChars ) );
    }

    /**
     * Compare the two strings lexographically. A null String is considered to be less than a
     * non-null String.
     *
     * @param s1 First string
     * @param s2 Second string
     * @return -1, 0 or 1
     */
    public static int compareTo( String s1, String s2 ) {
        if ( s1 == null && s2 == null ) {
            return 0;
        }
        if ( s1 == null ) {
            return -1;
        }
        if ( s2 == null ) {
            return 1;
        }
        return s1.compareTo( s2 );
    }

    /**
     * Compare the two string lexographically ignoring case. A null String is considered to be less
     * than a non-null String.
     *
     * @param s1 First String
     * @param s2 Second String
     * @return -1, 0 or 1
     */
    public static int compareToIgnoreCase( String s1, String s2 ) {
        if ( s1 == null && s2 == null ) {
            return 0;
        }
        if ( s1 == null ) {
            return -1;
        }
        if ( s2 == null ) {
            return 1;
        }
        return s1.compareToIgnoreCase( s2 );
    }

    /**
     * Is the string empty or null?
     *
     * @param str The string to check.
     * @return <tt>true</tt> if the given String is <tt>null</tt> or, when trimmed, is equal to the
     *         empty String. <tt>false</tt> otherwise.
     */
    public static boolean isEmpty( String str ) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Truncate the string to the given length. If the string is already less than <tt>len</tt>
     * characters in length, return the string itself.
     *
     * @param val
     *            The string to be truncated.
     * @param len
     *            The length at which to truncate the string.
     * @return A new string of length <tt>len</tt> or the passed in <tt>val</tt> if it is already
     *         less than <tt>len</tt> in length.
     */
    public static String trunc( String val, int len ) {
        if ( val != null && val.length() > len ) {
            val = val.substring( 0, len );
        }
        return val;
    }
}
