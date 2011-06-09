package org.cujau.utils;

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
}
