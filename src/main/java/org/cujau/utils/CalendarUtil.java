package org.cujau.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Utility for working with {@link java.util.Calendar}s.
 */
public class CalendarUtil {

    /**
     * Do the given {@link java.util.Calendar}s represent the same historical date?
     * 
     * @param cal1
     *            A Calendar to compare.
     * @param cal2
     *            A Calendar to compare.
     * @return <tt>true</tt> if the Calendars represent the same historical date (time is ignored).
     */
    public static boolean isSameDate( Calendar cal1, Calendar cal2 ) {
        if ( cal1.get( Calendar.YEAR ) == cal2.get( Calendar.YEAR )
             && cal1.get( Calendar.MONTH ) == cal2.get( Calendar.MONTH )
             && cal1.get( Calendar.DAY_OF_MONTH ) == cal2.get( Calendar.DAY_OF_MONTH ) ) {
            return true;
        }
        return false;
    }

    /**
     * Do the given dates represent the same historical date?
     * 
     * @param cal1
     *            A Calendar to compare.
     * @param date
     *            A Date to compare.
     * @return <tt>true</tt> if the Calendar and Date represent the same historical date (time is
     *         ignored).
     */
    public static boolean isSameDate( Calendar cal1, Date date ) {
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime( date );
        return isSameDate( cal1, cal2 );
    }

    /**
     * Do the given {@link java.util.Date}s represent the same historical date?
     * 
     * @param date1
     *            A Date to compare.
     * @param date2
     *            A Date to compare.
     * @return <tt>true</tt> if the Dates represent the same historical date (time is ignored).
     */
    public static boolean isSameDate( Date date1, Date date2 ) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime( date1 );
        return isSameDate( cal1, date2 );
    }
}
