package org.cujau.utils.converters;

public class StringToInteger implements StringConverter<Integer> {

    public Integer convert( String val ) {
        return StringConverterHelper.intValueOf( val );
    }

    public Class<Integer> getConvertedClass() {
        return Integer.class;
    }

}
