package org.cujau.utils.converters;

public class StringToInteger implements StringConverter<Integer> {

    public Integer convert( String val ) {
        return Integer.valueOf( (String) val );
    }

    public Class<Integer> getConvertedClass() {
        return Integer.class;
    }

}
