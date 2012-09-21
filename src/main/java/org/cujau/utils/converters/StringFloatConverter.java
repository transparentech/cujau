package org.cujau.utils.converters;

public class StringFloatConverter implements StringConverter<Float> {

    protected String formatStr = "%f";
    
    public void setFormatString( String fmt ) {
        formatStr = fmt;
    }
    
    @Override
    public Float convert( String val ) {
        return StringConverterHelper.floatValueOf( val );
    }

    @Override
    public String convert( Object val )
            throws IllegalArgumentException {
        return String.format( formatStr, val );
    }

    @Override
    public Class<Float> getConvertedClass() {
        return Float.class;
    }
}
