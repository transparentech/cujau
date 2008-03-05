package org.cujau.utils.converters;

public interface StringConverter<E> {

    E convert( String val )
            throws IllegalArgumentException;

    Class<E> getConvertedClass();
}
