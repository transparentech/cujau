package org.cujau.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimingUtil {
    private static final Logger LOG = LoggerFactory.getLogger( TimingUtil.class );

    public static final Map<String, Long> actives = new HashMap<String, Long>();
    public static final Map<String, Long> laps = new HashMap<String, Long>();
    public static final Map<String, Long> lapCount = new HashMap<String, Long>();

    public static void startTiming( String name ) {
        if ( name == null ) {
            return;
        }
        actives.put( name, System.currentTimeMillis() );
    }

    public static long stopTiming( String name ) {
        return stopTiming( name, true );
    }

    private static long stopTiming( String name, boolean doLog ) {
        if ( name == null ) {
            return 0;
        }
        Long start = actives.get( name );
        if ( start == null ) {
            LOG.warn( "No timing named '{}'", name );
            return 0;
        }
        long total = System.currentTimeMillis() - start;
        if ( doLog ) {
            LOG.info( "Timing: {}ms for {}", total, name );
        }
        actives.remove( name );
        return total;
    }

    public static void startLap( String name ) {
        if ( name == null ) {
            return;
        }
        startTiming( "~lap~" + name );
    }

    public static long lap( String name ) {
        if ( name == null ) {
            return 0;
        }
        long lapTime = stopTiming( "~lap~" + name, false );
        Long lap = laps.get( name );
        Long lapCt = lapCount.get( name );
        if ( lap == null ) {
            lap = 0l;
            lapCt = 0l;
        }
        lap += lapTime;
        lapCt++;
        laps.put( name, lap );
        lapCount.put( name, lapCt );
        return lap;
    }

    public static long stopLap( String name ) {
        if ( name == null ) {
            return 0;
        }
        Long lap = laps.get( name );
        Long lapCt = lapCount.get( name );
        if ( lap == null ) {
            return 0;
        }
        LOG.info( "TimingLap: {}ms/{}laps = {}ms/lap for {}", new Object[] { lap, lapCt, lap / lapCt, name } );
        laps.remove( name );
        lapCount.remove( name );
        return lap;
    }

    /**
     * Get the formatted String representation of the given lap value.
     * 
     * @param lapTime
     *            The lap time in milliseconds.
     * @return A String representing the lap value formatted with {@link #formatTimeInMillis(long)}.
     */
    public String formatLap( long lapTime ) {
        return formatTimeInMillis( lapTime );
    }

    /**
     * Convert the given time in milliseconds to a String with the format:
     * 
     * <pre>
     * NNd, NNh, NNm, NNs, NNms
     * </pre>
     * 
     * Where "NN" is the value and
     * <ul>
     * <li>d = days</li>
     * <li>h = hours</li>
     * <li>m = minutes</li>
     * <li>s = seconds</li>
     * <li>ms = milliseconds</li>
     * </ul>
     * Values starting from the left of the formatted string will be omitted if they are O until the
     * first non-zero value appears.
     * 
     * @param time
     *            The time in milliseconds to format.
     * @return A String with the formatted time.
     */
    public static String formatTimeInMillis( long time ) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis( time );
        c.add( Calendar.HOUR_OF_DAY, -1 );
        String ret = "";
        ret = format( ret, "d", c, Calendar.DAY_OF_YEAR );
        ret = format( ret, "h", c, Calendar.HOUR_OF_DAY );
        ret = format( ret, "m", c, Calendar.MINUTE );
        ret = format( ret, "s", c, Calendar.SECOND );
        ret = format( ret, "ms", c, Calendar.MILLISECOND );
        return ret;
    }

    /**
     * Convert the input String into a millisecond value. The input must have the form:
     * <ul>
     * <li>X milliseconds = "Xms"</li>
     * <li>X seconds = "Xs"</li>
     * <li>X minutes = "Xm"</li>
     * <li>X hours = "Xh"</li>
     * <li>X days = "Xd"</li>
     * <li>X weeks = "Xw"</li>
     * </ul>
     * For example: "1w 2d 3h 4m 5s 678ms".
     * 
     * @param stringValue
     * @return Value in milliseconds
     */
    public static long stringToMillis( String stringValue ) {

        if ( stringValue == null ) {
            return 0;
        }

        stringValue = stringValue.trim();
        if ( stringValue.endsWith( "ms" ) ) {
            // Milliseconds
            return Long.parseLong( stringValue.trim().substring( 0, stringValue.length() - 2 ) );
        }

        // Setup in millis
        char unit = stringValue.trim().charAt( stringValue.length() - 1 );
        long wert = Long.parseLong( stringValue.trim().substring( 0, stringValue.length() - 1 ) );

        switch ( unit ) {
        case 's':
            // second = 1000 millis
            return wert * 1000;
        case 'm':
            // minute = 60 seconds * 1000 millis
            return wert * 60 * 1000;
        case 'h':
            // hour = 60 minutes * 60 seconds * 1000 millis
            return wert * 60 * 60 * 1000;
        case 'd':
            // day = 24 hours * 60 minutes * 60 seconds * 1000 millis
            return wert * 24 * 60 * 60 * 1000;
        case 'w':
            // week = 7 days * 24 hours * 60 minutes * 60 seconds * 1000
            // millis
            return wert * 7 * 24 * 60 * 60 * 1000;
        default:
            throw new IllegalArgumentException( "Invalid timeunit for interval (" + unit + ")" );
        }
    }

    /**
     * Format the {@link java.util.Calendar} field selected with the <tt>field</tt> parameter and
     * append it to the given <tt>ret</tt> String. Nothing will be added to the String if the value
     * is at the field minimum value.
     * 
     * @param ret
     *            The String to which the formatted value (if any) will be added.
     * @param name
     *            The suffix String to append if the field value is greater then the field minimum.
     * @param c
     *            The Calendar containing the values.
     * @param field
     *            The selected Calendar field.
     * @return The String with the formatted field value appended.
     */
    private static String format( String ret, String name, Calendar c, int field ) {
        int tmp = c.get( field );
        if ( tmp == c.getActualMinimum( field ) ) {
            return ret;
        }
        if ( ret.equals( "" ) ) {
            if ( field == Calendar.DAY_OF_YEAR ) {
                // DAY_OF_YEAR is special as the minimum value is 1. So if we
                // are here, the day is
                // not the minimum (1). However, this means that we need to
                // subtract 1 from the day
                // to have an accurate day count.
                return ( tmp - 1 ) + name;
            }
            return tmp + name;
        }
        return ret + ", " + tmp + name;
    }
}
