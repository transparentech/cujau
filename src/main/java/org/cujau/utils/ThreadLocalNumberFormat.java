package org.cujau.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class ThreadLocalNumberFormat extends ThreadLocal<NumberFormat> {

    private final Locale locale;

    public ThreadLocalNumberFormat() {
        locale = Locale.getDefault();
    }

    public ThreadLocalNumberFormat( Locale locale ) {
        this.locale = locale;
    }

    @Override
    protected NumberFormat initialValue() {
        return NumberFormat.getInstance( locale );
    }

}
