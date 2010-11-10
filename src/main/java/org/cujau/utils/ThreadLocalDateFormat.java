package org.cujau.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ThreadLocalDateFormat extends ThreadLocal<DateFormat> {

    private final int style;
    private final Locale locale;
    private final String simpleFormat;
    
    public ThreadLocalDateFormat( int style, Locale locale ) {
        this.style = style;
        this.locale = locale;
        this.simpleFormat = null;
    }

    public ThreadLocalDateFormat( int style ) {
        this( style, Locale.getDefault() );
    }

    public ThreadLocalDateFormat( String simpleFormat ) {
        this( simpleFormat, Locale.getDefault() );
    }
    
    public ThreadLocalDateFormat( String simpleFormat, Locale locale ) {
        this.simpleFormat = simpleFormat;
        this.style = -1;
        this.locale = locale;
    }
    
    public ThreadLocalDateFormat() {
        this( -1, Locale.getDefault() );
    }

    @Override
    protected DateFormat initialValue() {
        if ( simpleFormat != null ) {
            return new SimpleDateFormat( simpleFormat );
        } else if ( style == -1 ) {
            return DateFormat.getDateInstance();
        } else {
            return DateFormat.getDateInstance( style, locale );
        }
    }
}
