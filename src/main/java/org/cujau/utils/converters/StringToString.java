package org.cujau.utils.converters;

public class StringToString implements StringConverter<String> {

    public String convert( String val ) {
        return (String) val;
    }

    public Class<String> getConvertedClass() {
        return String.class;
    }

}
