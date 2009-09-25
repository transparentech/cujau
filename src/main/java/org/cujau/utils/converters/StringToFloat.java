package org.cujau.utils.converters;

public class StringToFloat implements StringConverter<Float> {

    public Float convert( String val ) {
        return StringConverterHelper.floatValueOf( val );
    }

    public Class<Float> getConvertedClass() {
        return Float.class;
    }
}
