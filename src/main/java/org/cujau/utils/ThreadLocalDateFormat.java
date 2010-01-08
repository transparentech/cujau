package org.cujau.utils;

import java.text.DateFormat;
import java.util.Locale;

public class ThreadLocalDateFormat extends ThreadLocal<DateFormat> {

    private final int style;
    private final Locale locale;

    public ThreadLocalDateFormat( int style, Locale locale ) {
        this.style = style;
        this.locale = locale;
    }

    public ThreadLocalDateFormat( int style ) {
        this( style, Locale.getDefault() );
    }

    public ThreadLocalDateFormat() {
        this( -1, Locale.getDefault() );
    }

    protected DateFormat initialValue() {
        if ( style == -1 ) {
            return DateFormat.getDateInstance();
        } else {
            return DateFormat.getDateInstance( style, locale );
        }
    }
}
