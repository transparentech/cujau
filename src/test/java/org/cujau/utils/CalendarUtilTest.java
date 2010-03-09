package org.cujau.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class CalendarUtilTest {

    private static final SimpleDateFormat SDF = new SimpleDateFormat( "yyyy-MM-dd" );

    @Test
    public void testIsSameOrOlder()
            throws ParseException {
        Calendar baseCal = mkCal( "2005-09-09" );
        Calendar cal = mkCal( "2005-09-09" );
        assertTrue( CalendarUtil.isSameOrOlder( cal, baseCal ) );
        assertTrue( CalendarUtil.isSameOrOlder( mkCal( "2004-09-09" ), baseCal ) );
        assertFalse( CalendarUtil.isSameOrOlder( mkCal( "2006-09-09" ), baseCal ) );
        assertTrue( CalendarUtil.isSameOrOlder( mkCal( "2005-08-09" ), baseCal ) );
        assertFalse( CalendarUtil.isSameOrOlder( mkCal( "2005-10-09" ), baseCal ) );
        assertTrue( CalendarUtil.isSameOrOlder( mkCal( "2005-09-08" ), baseCal ) );
        assertFalse( CalendarUtil.isSameOrOlder( mkCal( "2005-09-10" ), baseCal ) );

        baseCal = mkCal( "2005-01-01" );
        assertTrue( CalendarUtil.isSameOrOlder( mkCal( "2004-12-31" ), baseCal ) );
        assertFalse( CalendarUtil.isSameOrOlder( mkCal( "2005-01-04" ), baseCal ) );
    }

    @Test
    public void testIsSameOrYounger()
            throws ParseException {
        Calendar baseCal = mkCal( "2005-09-09" );
        Calendar cal = mkCal( "2005-09-09" );
        assertTrue( CalendarUtil.isSameOrYounger( cal, baseCal ) );
        assertFalse( CalendarUtil.isSameOrYounger( mkCal( "2004-09-09" ), baseCal ) );
        assertTrue( CalendarUtil.isSameOrYounger( mkCal( "2006-09-09" ), baseCal ) );
        assertFalse( CalendarUtil.isSameOrYounger( mkCal( "2005-08-09" ), baseCal ) );
        assertTrue( CalendarUtil.isSameOrYounger( mkCal( "2005-10-09" ), baseCal ) );
        assertFalse( CalendarUtil.isSameOrYounger( mkCal( "2005-09-08" ), baseCal ) );
        assertTrue( CalendarUtil.isSameOrYounger( mkCal( "2005-09-10" ), baseCal ) );

        baseCal = mkCal( "2005-01-01" );
        assertFalse( CalendarUtil.isSameOrYounger( mkCal( "2004-12-31" ), baseCal ) );
        assertTrue( CalendarUtil.isSameOrYounger( mkCal( "2005-01-04" ), baseCal ) );
    }

    private Calendar mkCal( String dateStr )
            throws ParseException {
        Date date = SDF.parse( dateStr );
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        return cal;
    }
}
