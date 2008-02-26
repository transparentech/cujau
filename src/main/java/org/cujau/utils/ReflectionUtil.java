package org.cujau.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for common reflection operations.
 */
public final class ReflectionUtil {

    private static final Logger LOG = LoggerFactory.getLogger( ReflectionUtil.class );

    /**
     * Instantiate an instance of the given class.
     * 
     * @param <T>
     *            The type of the object to be returned. Can be an interface.
     * @param klass
     *            The class to be instantiated.
     * @return An instance of the type T.
     */
    public static <T> T instantiateClass( Class<? extends T> klass )
            throws ReflectionException {
        try {
            // Return a new instance.
            return klass.newInstance();
        } catch ( ClassCastException e ) {
            String msg = "Class does not implement the interface: '" + klass.getName() + "'";
            LOG.error( msg );
            throw new ReflectionException( msg, e );
        } catch ( InstantiationException e ) {
            LOG.error( e.getMessage() );
            throw new ReflectionException( e );
        } catch ( IllegalAccessException e ) {
            LOG.error( e.getMessage() );
            throw new ReflectionException( e );
        }
    }

    /**
     * Instantiate an instance of the given class.
     * 
     * @param <T>
     *            The type of the object to be returned. Can be an interface.
     * @param klass
     *            The fully qualified class name of the class to be instantiated. Must be a subclass
     *            of T.
     * @param baseKlass
     *            The Class object of the generic type T.
     * @return An instance of the type T.
     */
    public static <T> T instantiateClass( String klass, Class<T> baseKlass )
            throws ReflectionException {
        try {
            // Get the class to instantiate in a java5 safe way.
            Class<? extends T> c = Class.forName( klass ).asSubclass( baseKlass );
            // Return a new instance.
            return c.newInstance();
        } catch ( ClassNotFoundException e ) {
            String msg = "Can't find " + baseKlass + " implementation class: '" + klass + "'";
            LOG.error( msg );
            throw new ReflectionException( msg, e );
        } catch ( ClassCastException e ) {
            String msg = "Class does not implement the " + baseKlass + " interface: '" + klass + "'";
            LOG.error( msg );
            throw new ReflectionException( msg, e );
        } catch ( InstantiationException e ) {
            LOG.error( e.getMessage() );
            throw new ReflectionException( e );
        } catch ( IllegalAccessException e ) {
            LOG.error( e.getMessage() );
            throw new ReflectionException( e );
        }
    }

    /**
     * Invoke the getter method for the given <tt>propertyName</tt> on the given <tt>bean</tt>.
     * 
     * @param bean
     *            The object on which the method will be invoked.
     * @param propertyName
     *            The property to get.
     * @return The object returned by the getter method invocation.
     * @throws ReflectionException
     */
    public static Object invokeGetProperty( Object bean, String propertyName )
            throws ReflectionException {
        String getProperty = mkPropertyMethodName( "get", propertyName );

        Object ret = null;
        try {
            Method method = bean.getClass().getMethod( getProperty, (Class[]) null );
            ret = method.invoke( bean, (Object[]) null );
        } catch ( SecurityException e ) {
            LOG.error( e.getMessage(), e );
            throw new ReflectionException( e );
        } catch ( NoSuchMethodException e ) {
            LOG.error( e.getMessage(), e );
            throw new ReflectionException( e );
        } catch ( IllegalArgumentException e ) {
            LOG.error( e.getMessage(), e );
            throw new ReflectionException( e );
        } catch ( IllegalAccessException e ) {
            LOG.error( e.getMessage(), e );
            throw new ReflectionException( e );
        } catch ( InvocationTargetException e ) {
            LOG.error( e.getMessage(), e );
            throw new ReflectionException( e );
        }
        return ret;
    }

    /**
     * Invoke the setter method of the given <tt>propertyName</tt> on the given <tt>bean</tt>.
     * 
     * @param bean
     *            The object on which the method will be invoked.
     * @param propertyName
     *            The propety to set.
     * @param value
     *            The value to which the property will be set.
     * @throws ReflectionException
     */
    public static void invokeSetProperty( Object bean, String propertyName, Object value )
            throws ReflectionException {
        String setProperty = mkPropertyMethodName( "set", propertyName );

        Method method;
        try {
            method = bean.getClass().getMethod( setProperty, new Class[] { value.getClass() } );
            method.invoke( bean, new Object[] { value } );
        } catch ( SecurityException e ) {
            LOG.error( e.getMessage(), e );
            throw new ReflectionException( e );
        } catch ( NoSuchMethodException e ) {
            LOG.error( e.getMessage(), e );
            throw new ReflectionException( e );
        } catch ( IllegalArgumentException e ) {
            LOG.error( e.getMessage(), e );
            throw new ReflectionException( e );
        } catch ( IllegalAccessException e ) {
            LOG.error( e.getMessage(), e );
            throw new ReflectionException( e );
        } catch ( InvocationTargetException e ) {
            LOG.error( e.getMessage(), e );
            throw new ReflectionException( e );
        }
    }

    /**
     * Invoke the setter method of the given <tt>propertyName</tt> on the given <tt>bean</tt>.
     * This method can be used when the wrong class type of the value parameter would be selected by
     * the compiler (for example, when using <tt>int</tt> rather than <tt>Integer</tt>).
     * 
     * @param bean
     *            The object on which the method will be invoked.
     * @param propertyName
     *            The propety to set.
     * @param value
     *            The value to which the property will be set.
     * @param valueKlass
     *            The Class object of the value parameter.
     * @throws ReflectionException
     */
    public static void invokeSetProperty( Object bean, String propertyName, Object value, Class<?> valueKlass )
            throws ReflectionException {
        String setProperty = mkPropertyMethodName( "set", propertyName );

        Method method;
        try {
            method = bean.getClass().getMethod( setProperty, new Class[] { valueKlass } );
            method.invoke( bean, new Object[] { value } );
        } catch ( SecurityException e ) {
            LOG.error( e.getMessage(), e );
            throw new ReflectionException( e );
        } catch ( NoSuchMethodException e ) {
            LOG.error( e.getMessage(), e );
            throw new ReflectionException( e );
        } catch ( IllegalArgumentException e ) {
            LOG.error( e.getMessage(), e );
            throw new ReflectionException( e );
        } catch ( IllegalAccessException e ) {
            LOG.error( e.getMessage(), e );
            throw new ReflectionException( e );
        } catch ( InvocationTargetException e ) {
            LOG.error( e.getMessage(), e );
            throw new ReflectionException( e );
        }
    }

    private static String mkPropertyMethodName( String prefix, String propertyName ) {
        String pn = Character.toUpperCase( propertyName.charAt( 0 ) ) + propertyName.substring( 1 );
        return prefix + pn;
    }
}
