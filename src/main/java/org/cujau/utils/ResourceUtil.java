package org.cujau.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
     * </p>
     *
     * @param resourceName
     *            The resource on the classpath.
     * @return A String containing the requested resource or <tt>null</tt> if no resource with the
     *         give name is available.
     * @throws IOException
     */
    public static String getResourceAsString( String resourceName )
            throws IOException {
        return getResourceAsString(ResourceUtil.class, resourceName);
    }

    /**
     * Return the requested resource as a String.
     * <p>
     * This method assumes that the resource is a text file encoded in UTF-8.
     * </p>
     *
     * @param klass
     *         The class whose classloader should load the resource.
     * @param resourceName
     *         The resource on the classpath.
     * @return A String containing the requested resource or <tt>null</tt> if no resource with the
     * give name is available.
     * @throws IOException
     */
    public static String getResourceAsString( Class<?> klass, String resourceName )
            throws IOException {
        InputStream is = klass.getResourceAsStream( resourceName );
        if ( is == null ) {
            LOG.info( "Can't find the requested resource: {}", resourceName );
            return null;
        }
        return StreamUtil.getStreamAsString( is );
    }

    /**
     * Return the requested resource as an array of bytes.
     * 
     * @param resourceName
     *            The resource on the classpath.
     * @return An arra of bytes containing the requested resource or <tt>null</tt> if no resource
     *         with the given name is available.
     * @throws IOException
     */
    public static byte[] getResourceBytes( String resourceName )
            throws IOException {
        InputStream inputStream = ResourceUtil.class.getResourceAsStream( resourceName );
        if ( inputStream == null ) {
            LOG.info( "Can't find the requested resource: {}", resourceName );
            return null;
        }
        return StreamUtil.getStreamBytes( inputStream );
    }

    /**
     * Return a File representing the location on disk of the given class. For a class contained in
     * a JAR file, the returned File will be the containing JAR file. For a class in a directory,
     * the returned File will be the directory on the classpath that contains (likely via a
     * sub-directory) the class.
     * 
     * @param klass
     *            The class to be located.
     * @return A File representing the containing location (either a JAR file or class directory).
     */
    public static File getLocationOfClass( Class<?> klass ) {
        File jarFile;
        try {
            jarFile = new File( klass.getProtectionDomain().getCodeSource().getLocation().toURI() );
        } catch ( Exception e ) {
            return null;
        }
        return jarFile;
    }
}
