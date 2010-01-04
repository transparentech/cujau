package org.cujau.utils.converters;

public interface StringConverterFactory {

    StringConverter<?> getConverter( Class<?> klass );

    String toString( Object val );

}
