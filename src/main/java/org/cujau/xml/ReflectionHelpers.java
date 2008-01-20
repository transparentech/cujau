/*
 *                           CUJAU
 *         Common Utilities for Java Application Use
 *
 *        http://www.transparentech.com/projects/cujau
 *
 * Copyright (c) 2007 Nicholas Rahn <nick at transparentech.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */
package org.cujau.xml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple set of methods for general reflective tasks.
 */
public class ReflectionHelpers {

    /** The logger object for this class. */
    protected static Logger _log = LoggerFactory.getLogger( ReflectionHelpers.class );

    /**
     * Instantiate the class, <code>klass_name</code>.
     * 
     * @param klass_name
     *            The fully qualified class name to instantiate.
     * @return The newly instantiated Object or <code>null</code> if there was
     *         an exception during instantiation.
     */
    public static Object objectFromClassName( String klass_name ) {
        Object obj = null;
        try {
            Class<?> objclass = Class.forName( klass_name );
            obj = objclass.newInstance();
        } catch ( ClassNotFoundException ex ) {
            _log.warn( "objectFromClassName : ClassNotFound : " + klass_name );
        } catch ( InstantiationException ex ) {
            _log.warn( "objectFromClassName : CanNotInstantiate : " + klass_name );
        } catch ( IllegalAccessException ex ) {
            _log.warn( "objectFromClassName : IllegalAccess : " + klass_name );
        }
        return obj;
    }

    /**
     * Invoke the <code>set&lt;property&gt;</code> method on the object
     * passing it "value" as a parameter.
     * 
     * @param obj
     *            The Object on which the method will be invoked.
     * @param property
     *            The name of the property whose "set" method will be invoked on
     *            the object. The complete name of the invoked method will be
     *            <code>set<em>Property</em></code>. This method <b>must</b>
     *            accept only 1 parameter of type String.
     * @param value
     *            The String value passed as the single parameter to the invoked
     *            method.
     * @return true if the method was invoked without error, false otherwise.
     */
    public static boolean invokeSetStringProperty( Object obj, String property, String value ) {
        String methodname = getSetMethodNameFromProperty( property );
        Class<?> params[] = new Class[] { String.class };
        Object args[] = new Object[] { value };
        Method mtd = null;
        try {
            mtd = obj.getClass().getMethod( methodname, params );
            mtd.invoke( obj, args );
        } catch ( IllegalArgumentException ex ) {
            _log.warn( "invokeSetStringProperty : IllegalArgument : {}.{}(String)", obj.getClass().getName(),
                       methodname );
            return false;
        } catch ( NoSuchMethodException ex ) {
            _log.warn( "invokeSetStringProperty : NoSuchMethod : {}.{}(String)", obj.getClass().getName(),
                       methodname );
            return false;
        } catch ( IllegalAccessException ex ) {
            _log.warn( "invokeSetStringProperty : IllegalAccess : {}.{}(String)", obj.getClass().getName(),
                       methodname );
            return false;
        } catch ( InvocationTargetException ex ) {
            _log.warn( "invokeSetStringProperty : InvocationTarget : {}.{}(String)",
                       obj.getClass().getName(), methodname );
            _log.warn( "Exception thrown during invocation of this method!", ex );
            return false;
        }
        return true;
    }

    /**
     * Invoke the <code>get&lt;Property&gt;</code> method on the object.
     * 
     * @param obj
     *            The Object on which the get method will be invoked.
     * @param property
     *            String containing the property name to use. The complete name
     *            of the invoked method will be
     *            <code>get<em>Property</em></code>. The get method
     *            <b>must</b> not take any parameters.
     * @return The value returned from the <code>get&lt;Property&gt;</code>
     *         method invocation. The returned Object can be downcasted to the
     *         expected return type. <code>null</code> if an exception was
     *         thrown during the invocation.
     */
    public static Object invokeGetProperty( Object obj, String property ) {
        String methodname = getGetMethodNameFromProperty( property );
        Class<?> params[] = null;
        Object args[] = null;
        Method mtd = null;
        Object ret = null;
        try {
            mtd = obj.getClass().getMethod( methodname, params );
            ret = mtd.invoke( obj, args );
        } catch ( IllegalArgumentException ex ) {
            _log.warn( "invokeGetProperty : IllegalArgument : {}.{}()", obj.getClass().getName(), methodname );
            return null;
        } catch ( NoSuchMethodException ex ) {
            _log.warn( "invokeGetProperty : NoSuchMethod : {}.{}()", obj.getClass().getName(), methodname );
            return null;
        } catch ( IllegalAccessException ex ) {
            _log.warn( "invokeGetProperty : IllegalAccess : {}.{}()", obj.getClass().getName(), methodname );
            return null;
        } catch ( InvocationTargetException ex ) {
            _log.warn( "invokeGetProperty : InvocationTarget : {}.{}()", obj.getClass().getName(), methodname );
            return null;
        }
        return ret;
    }

    /**
     * Return the property's "get" method name.  For example, if the property
     * is "foo", this method will return "getFoo".
     */
    public static String getGetMethodNameFromProperty( String property ) {
        return "get" + upcaseFirstLetter( property );
    }

    /**
     * Return the property's "set" method name.  For example, if the property
     * is "foo", this method will return "setFoo".
     */
    public static String getSetMethodNameFromProperty( String property ) {
        return "set" + upcaseFirstLetter( property );
    }

    /**
     * Return the given String with the first letter changed to upper case.
     */
    public static String upcaseFirstLetter( String val ) {
        return val.substring( 0, 1 ).toUpperCase() + val.substring( 1 );
    }

}
