package org.cujau.utils.csv;

/**
 * Interface for any class on which a date format configuration can be set.
 */
public interface CSVSettableDateFormat {

    void setDateFormat( String fmt );

    String getDateFormat();

}
