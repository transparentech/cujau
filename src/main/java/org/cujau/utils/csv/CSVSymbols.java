package org.cujau.utils.csv;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CSVSymbols implements CSVSettableDateFormat {

    private static final char DEFAULT_SEPARATOR = ',';
    private static final char ALTERNATE_SEPARATOR = ';';

    private char separator;
    private String lineSeparator;
    private String dateFormat;
    
    public CSVSymbols() {
        this( Locale.getDefault( Locale.Category.FORMAT ) );
    }

    public CSVSymbols( Locale locale ) {
        buildDefaults( locale );
    }

    public void setRecordSeparator( char separator ) {
        this.separator = separator;
    }

    public char getRecordSeparator() {
        return separator;
    }

    public void setLineSeparator( String lineSeparator ) {
        this.lineSeparator = lineSeparator;
    }

    public String getLineSeparator() {
        return lineSeparator;
    }

    @Override
    public void setDateFormat( String df ) {
        this.dateFormat = df;
    }
    
    @Override
    public String getDateFormat() {
        return dateFormat;
    }
    
    private void buildDefaults( Locale locale ) {
        separator = DEFAULT_SEPARATOR;
        lineSeparator = System.getProperty( "line.separator" );

        //
        // Try to determine what the record separator should be.
        //
        DecimalFormatSymbols dfs = new DecimalFormatSymbols( locale );
        // The first part of this statement is to detect locales like 'fr' and 'de' where
        // the decimal separator is not the 'en' standard '.'.
        //
        // The second part of this statement is to detect locales like 'de_CH' where the
        // grouping separator is not the 'en' standard ','.
        //
        // If either of these are detected, we will use the alternate separator (;) rather
        // than the standard (,) separator.
        //
        if ( dfs.getDecimalSeparator() == DEFAULT_SEPARATOR
             || dfs.getGroupingSeparator() != DEFAULT_SEPARATOR ) {
            // Use the alternate separator as that is probably what Excel is expecting.
            separator = ALTERNATE_SEPARATOR;
        }
    }
    
    @Override
    public String toString() {
        return "CSVSymbols: sep='"+separator+"' lineSep='"+lineSeparator+"'";
    }
}
