package org.cujau.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ExceptionUtil {

    /**
     * Convert the given Throwable to a text-based stacktrace.
     * 
     * @param t
     *            The Exception to convert.
     * @return The Exception's Stacktrace as string
     */
    public static String getStacktrace( Throwable t ) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter( result );
        t.printStackTrace( printWriter );

        return result.toString();
    }

    public static String getSimpletrace( Throwable t ) {
        StringBuilder buf = new StringBuilder();
        int indent = 0;
        while ( t != null ) {
            if ( indent != 0 ) {
                buf.append( "\n" );
                buf.append( String.format( "%" + indent + "c", ' ' ) );
            }
            buf.append( t.getClass().getSimpleName() );
            if ( t.getMessage() != null ) {
                buf.append( " : " );
                buf.append( t.getMessage() );
            }
            indent += 2;
            t = t.getCause();
        }
        return buf.toString();
    }
}
