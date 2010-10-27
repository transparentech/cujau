package org.cujau;

/**
 * Interface that can be implemented in 'enum's that use special toString implementations and where
 * converting back to an enum object from the toString() value is desired.
 * 
 * @param <T>
 *            Any class, but typically an 'enum'.
 */
public interface ExtendedEnum<T> {

    T valueOfString( String val );

}
