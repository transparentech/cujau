package org.cujau.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ThreadLocalDateTimeFormat extends ThreadLocalDateFormat {

    public ThreadLocalDateTimeFormat( int style, Locale locale ) {
        super( style, locale );
    }

    public ThreadLocalDateTimeFormat( int style ) {
        super( style );
    }

    public ThreadLocalDateTimeFormat( String simpleFormat ) {
        super( simpleFormat, Locale.getDefault() );
    }

    public ThreadLocalDateTimeFormat( String simpleFormat, Locale locale ) {
        super( simpleFormat, locale );
    }

    public ThreadLocalDateTimeFormat() {
        this( -1, Locale.getDefault() );
    }

    @Override
    protected DateFormat initialValue() {
        if ( simpleFormat != null ) {
            return new SimpleDateFormat( simpleFormat );
        } else if ( style == -1 ) {
            return DateFormat.getDateTimeInstance();
        } else {
            return DateFormat.getDateTimeInstance( style, style, locale );
        }
    }
}
