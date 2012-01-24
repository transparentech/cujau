package org.cujau.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for working with the SPI pattern defined in the "Service Provider" section of the
 * Java "JAR File Specification".
 * <p>
 * NOTE: This is Java 5 compatible. If you are running Java 6, you may prefer to use the
 * <tt>java.util.ServiceLoader&lt;S&gt;</tt> class.
 */
public final class ServiceProviderUtil {

    private static final Logger LOG = LoggerFactory.getLogger( ServiceProviderUtil.class );

    /**
     * Get a list of service provider instances.
     * <p>
     * This implementation is based on "Service Provider" section of the Java "JAR File
     * Specification".
     * <p>
     * If an exception is thrown while trying to instantiate any of the implementing service
     * classes, the problematic class will be discarded and processing will continue with the next
     * class. Thus the returned list will contain only instances of the implementing service classes
     * that could be instantiated without an exception being thrown. If more flexibility is required
     * during the instantiation of the implementing service class, the
     * {@link #getServiceProviderNames} method should be used.
     * 
     * @param <E>
     *            The type of the service class.
     * @param serviceClass
     *            The Class object of the service class.
     * @return A list of service class instances.
     */
    public static <E> List<E> getServiceProviders( Class<E> serviceClass ) {
        List<String> names = getServiceProviderNames( serviceClass );
        ArrayList<E> providers = new ArrayList<E>();
        for ( String name : names ) {
            try {
                E info = ReflectionUtil.instantiateClass( name, serviceClass );
                providers.add( info );
                LOG.debug( "Service provider: {}", info.getClass().getName() );
            } catch ( ReflectionException e ) {
                LOG.error( "Exception while instantiating service class:" + name, e );
                throw new Error( "Exception while instantiating service class:" + name, e );
            }
        }
        return providers;
    }

    /**
     * Get a list of fully qualified classes that are providers (i.e. implementors) of the given
     * service class.
     * <p>
     * This implementation is based on the "Service Provider" section of the Java "JAR File
     * Specification".
     * 
     * @param serviceClass
     *            The service class.
     * @return A list of Strings containing the fully qualified class names of providers.
     */
    public static List<String> getServiceProviderNames( Class<?> serviceClass ) {
        LOG.info( "Loading services for {}", serviceClass.getName() );

        // Get the list of all the resources on the classpath that match the fullResourceName.
        ArrayList<String> availableClasses = new ArrayList<String>();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if ( cl == null ) {
            LOG.warn( "Thread class loader was null. Using system class loader to load service provider files." );
            cl = ClassLoader.getSystemClassLoader();
        }
        Enumeration<URL> resources = null;
        String fullResourceName = "META-INF/services/" + serviceClass.getName();
        try {
            resources = cl.getResources( fullResourceName );
        } catch ( IOException e ) {
            LOG.warn( "Exception getting resources for {}", fullResourceName );
            LOG.warn( e.getMessage() );
            LOG.warn( "Returning no services." );
            return availableClasses;
        }

        // Load each resource (URL) and parse it, taking each line to be the fully qualified
        // classname of the class implementing the serviceName.
        while ( resources.hasMoreElements() ) {
            URL url = resources.nextElement();
            LOG.debug( "Getting service classes defined in {}", url.toString() );
            try {
                BufferedReader reader =
                    new BufferedReader( new InputStreamReader( url.openStream(), "UTF-8" ) );
                try {
                    String line = null;
                    while ( ( line = reader.readLine() ) != null ) {
                        line = parseLine( line );
                        if ( line != null ) {
                            availableClasses.add( line );
//                            LOG.debug( "  {}", line );
                        }
                    }
                } finally {
                    reader.close();
                }
            } catch ( IOException e ) {
                LOG.warn( "Exception reading URL, {}", url.toString() );
            }
        }

        return availableClasses;
    }

    static String parseLine( String line ) {
        line = line.trim();
        int ind = line.indexOf( '#' );
        if ( ind != -1 ) {
            line = line.substring( 0, ind ).trim();
        }
        if ( line.equals( "" ) ) {
            return null;
        }
        return line;
    }

}
