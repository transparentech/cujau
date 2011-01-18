package org.cujau.utils.converters;

public class StringObjectConverter implements StringConverter<Object> {

    public Object convert( String val )
            throws IllegalArgumentException {
        return val;
    }

    public String convert( Object val )
            throws IllegalArgumentException {
        if ( val != null ) {
            return val.toString();
        }
        return "";
    }

    public Class<Object> getConvertedClass() {
        return Object.class;
    }

}
