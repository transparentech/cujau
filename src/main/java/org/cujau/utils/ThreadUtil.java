package org.cujau.utils;

public class ThreadUtil {

    public static void sleep( long millis ) {
        try {
            Thread.sleep( millis );
        } catch ( InterruptedException e ) {
            // Ignore.
        }
    }
    
    public static void sleepSeconds( int seconds ) {
        try {
            Thread.sleep( seconds * CalendarUtil.MILLIS_IN_SECOND );
        } catch ( InterruptedException e ) {
            // Ignore.
        }
    }
    
}
