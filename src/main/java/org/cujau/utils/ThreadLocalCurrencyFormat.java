package org.cujau.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class ThreadLocalCurrencyFormat extends ThreadLocal<NumberFormat> {

    private final Locale locale;

    public ThreadLocalCurrencyFormat() {
        locale = Locale.getDefault();
    }

    public ThreadLocalCurrencyFormat( Locale locale ) {
        this.locale = locale;
    }

    @Override
    protected NumberFormat initialValue() {
        return NumberFormat.getCurrencyInstance( locale );
    }

}
