package org.cujau.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ThreadLocalDecimalFormat extends ThreadLocal<DecimalFormat> {

    private final Locale locale;
    private final String pattern;

    public ThreadLocalDecimalFormat() {
        locale = Locale.getDefault();
        pattern = null;
    }

    public ThreadLocalDecimalFormat( Locale locale ) {
        this.locale = locale;
        pattern = null;
    }

    public ThreadLocalDecimalFormat( String pattern ) {
        this.locale = Locale.getDefault();
        this.pattern = pattern;
    }

    public ThreadLocalDecimalFormat( Locale locale, String pattern ) {
        this.locale = locale;
        this.pattern = pattern;
    }

    protected DecimalFormat initialValue() {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance( locale );
        if ( pattern != null ) {
            df.applyPattern( pattern );
        }
        return df;
    }

}
