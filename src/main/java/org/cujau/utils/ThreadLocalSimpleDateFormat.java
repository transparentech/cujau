package org.cujau.utils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Utility for using <tt>java.text.SimpleDateFormat</tt> in a thread-safe way.
 * <p>
 * Typically, in a multi-threaded environment such as Java servlets, where you need to use a
 * SimpleDateFormat instance, you would declare this class as a private static field:
 * 
 * <pre>
 * private static final ThreadLocalSimpleDateFormat TL_SDF = new ThreadLocalSimpleDateFormat();
 * </pre>
 * 
 * Then, within the your code, you can get a SimpleDateFormat instance like this:
 * 
 * <pre>
 * SimpleDateFormat sdf = TL_SDF.get();
 * </pre>
 * 
 * @see java.lang.ThreadLocal
 * @see java.text.SimpleDateFormat
 */
public class ThreadLocalSimpleDateFormat extends ThreadLocal<SimpleDateFormat> {

    private final String format;
    private final TimeZone zone;
    
    /**
     * Constructs SimpleDateFormat instances using the default date format.
     */
    public ThreadLocalSimpleDateFormat() {
        this.format = null;
        this.zone = null;
    }

    /**
     * Constructs SimpleDateFormat instances using the specified date format.
     * 
     * @param format
     *            The format to use when instantiating the SimpleDateFormat instances.
     * @see java.text.SimpleDateFormat
     */
    public ThreadLocalSimpleDateFormat( String format ) {
        this.format = format;
        this.zone = null;
    }
    
    public ThreadLocalSimpleDateFormat( String format, TimeZone zone ) {
        this.format = format;
        this.zone = zone;
    }

    @Override
    protected SimpleDateFormat initialValue() {
        SimpleDateFormat sdf = new SimpleDateFormat( format );
        if ( zone != null ) {
            sdf.setTimeZone( zone );
        }
        return sdf;
    }

}
