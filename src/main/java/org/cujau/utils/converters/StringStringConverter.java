package org.cujau.utils.converters;

public class StringStringConverter implements StringConverter<String> {

    @Override
    public String convert( String val ) {
        return (String) val;
    }

    @Override
    public String convert( Object val )
            throws IllegalArgumentException {
        return val.toString();
    }

    @Override
    public Class<String> getConvertedClass() {
        return String.class;
    }

}
