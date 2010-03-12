package org.cujau.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
        return getStreamAsString( is );
    }

    /**
     * Return the given InputStream contents as a String.
     * <p>
     * This method assumes that the InputStream contents is a text encoded in UTF-8.
     * 
     * @param is
     *            The InputStream whose contents is converted to a String.
     * @return The String.
     * @throws IOException
     */
    public static String getStreamAsString( InputStream is )
            throws IOException {
        InputStreamReader reader = new InputStreamReader( is, "UTF-8" );
        StringBuilder b = new StringBuilder();
        char[] buffer = new char[1024];
        int read;
        while ( ( read = reader.read( buffer ) ) != -1 ) {
            b.append( buffer, 0, read );
        }
        return b.toString();
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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( 1024 );
        byte[] bytes = new byte[512];

        // Read bytes from the input stream in bytes.length-sized chunks and write
        // them into the output stream
        int readBytes;
        while ( ( readBytes = inputStream.read( bytes ) ) > 0 ) {
            outputStream.write( bytes, 0, readBytes );
        }

        // Convert the contents of the output stream into a byte array
        byte[] byteData = outputStream.toByteArray();

        // Close the streams
        inputStream.close();
        outputStream.close();

        return byteData;
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
