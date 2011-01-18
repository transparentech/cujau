package org.cujau.utils.converters;

public class StringBooleanConverter implements StringConverter<Boolean> {

    public Boolean convert( String val ) {
        return StringConverterHelper.booleanValueOf( val );
    }

    public String convert( Object val )
            throws IllegalArgumentException {
        if ( val == null ) {
            return "";
        }
        if ( val instanceof Boolean ) {
            return Boolean.toString( (Boolean) val );
        }
        return Boolean.toString( false );
    }
    
    public Class<Boolean> getConvertedClass() {
        return Boolean.class;
    }

}
