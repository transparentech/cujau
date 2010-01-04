package org.cujau.utils.converters;

public class StringStringConverter implements StringConverter<String> {

    public String convert( String val ) {
        return (String) val;
    }

    public String convert( Object val )
            throws IllegalArgumentException {
        return val.toString();
    }

    public Class<String> getConvertedClass() {
        return String.class;
    }

}
