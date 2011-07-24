package org.cujau.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class ThreadLocalDateFormat extends ThreadLocal<DateFormat> {

    protected final int style;
    protected final Locale locale;
    protected final String simpleFormat;
    protected TimeZone zone = null;
    
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

    public void setTimeZone( TimeZone timeZone ) {
        zone = timeZone;
    }
    
    @Override
    protected DateFormat initialValue() {
        DateFormat ret;
        if ( simpleFormat != null ) {
            ret = new SimpleDateFormat( simpleFormat );
        } else if ( style == -1 ) {
            ret = DateFormat.getDateInstance();
        } else {
            ret = DateFormat.getDateInstance( style, locale );
        }
        if ( zone != null ) {
            ret.setTimeZone( zone );
        }
        return ret;
    }
}
