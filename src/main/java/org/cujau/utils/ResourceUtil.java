package org.cujau.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for retrieving class path resources.
 */
public class ResourceUtil {

    private static final Logger LOG = LoggerFactory.getLogger( ResourceUtil.class );

    /**
     * Return the requested resource as a String.
     * <p>
     * This method assumes that the resource is a text file encoded in UTF-8.
     * 
     * @param resourceName
     *            The resource on the classpath.
     * @return A String containing the requested resource or <tt>null</tt> if no resource with the
     *         give name is available.
     * @throws IOException
     */
    public static String getResourceAsString( String resourceName )
            throws IOException {
        InputStream is = ResourceUtil.class.getResourceAsStream( resourceName );
        if ( is == null ) {
            LOG.info( "Can't find the requested resource: {}", resourceName );
            return null;
        }

        InputStreamReader reader = new InputStreamReader( is, "UTF-8" );
        StringBuilder b = new StringBuilder();
        char[] buffer = new char[1024];
        int read;
        while ( ( read = reader.read( buffer ) ) != -1 ) {
            b.append( buffer, 0, read );
        }

        return b.toString();
    }

}
