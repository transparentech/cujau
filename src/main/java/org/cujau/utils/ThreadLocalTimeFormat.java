package org.cujau.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ThreadLocalTimeFormat extends ThreadLocalDateFormat {

    public ThreadLocalTimeFormat( int style, Locale locale ) {
        super( style, locale );
    }

    public ThreadLocalTimeFormat( int style ) {
        super( style );
    }

    public ThreadLocalTimeFormat( String simpleFormat ) {
        super( simpleFormat, Locale.getDefault() );
    }

    public ThreadLocalTimeFormat( String simpleFormat, Locale locale ) {
        super( simpleFormat, locale );
    }

    public ThreadLocalTimeFormat() {
        this( -1, Locale.getDefault() );
    }

    @Override
    protected DateFormat initialValue() {
        if ( simpleFormat != null ) {
            return new SimpleDateFormat( simpleFormat );
        } else if ( style == -1 ) {
            return DateFormat.getTimeInstance();
        } else {
            return DateFormat.getTimeInstance( style, locale );
        }
    }
}
