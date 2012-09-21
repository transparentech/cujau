package org.cujau.utils.converters;

public class StringObjectConverter implements StringConverter<Object> {

    @Override
    public Object convert( String val )
            throws IllegalArgumentException {
        return val;
    }

    @Override
    public String convert( Object val )
            throws IllegalArgumentException {
        if ( val != null ) {
            return val.toString();
        }
        return "";
    }

    @Override
    public Class<Object> getConvertedClass() {
        return Object.class;
    }

}
