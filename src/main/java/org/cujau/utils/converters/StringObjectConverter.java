package org.cujau.utils.converters;

public class StringObjectConverter implements StringConverter<Object> {

    public Object convert( String val )
            throws IllegalArgumentException {
        return val;
    }

    public String convert( Object val )
            throws IllegalArgumentException {
        return val.toString();
    }

    public Class<Object> getConvertedClass() {
        return Object.class;
    }

}
