package org.cujau.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ThreadLocalIntegerFormat extends ThreadLocal<DecimalFormat> {

    private final Locale locale;
    private final String pattern = "####";

    public ThreadLocalIntegerFormat() {
        locale = Locale.getDefault();
    }

    public ThreadLocalIntegerFormat( Locale locale ) {
        this.locale = locale;
    }

    protected DecimalFormat initialValue() {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance( locale );
        if ( pattern != null ) {
            df.applyPattern( pattern );
        }
        return df;
    }
}
