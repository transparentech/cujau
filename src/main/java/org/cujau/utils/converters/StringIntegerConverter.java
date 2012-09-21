package org.cujau.utils.converters;

public class StringIntegerConverter implements StringConverter<Integer> {

    protected static String FORMAT_STR = "%d";
    
    public static void setFormatString( String fmt ) {
        FORMAT_STR = fmt;
    }
    
    @Override
    public Integer convert( String val ) {
        return StringConverterHelper.intValueOf( val );
    }

    @Override
    public String convert( Object val )
            throws IllegalArgumentException {
        return String.format( FORMAT_STR, val );
    }
    
    @Override
    public Class<Integer> getConvertedClass() {
        return Integer.class;
    }

}
