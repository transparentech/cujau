package org.cujau.utils.converters;

public class StringIntegerConverter implements StringConverter<Integer> {

    protected static String FORMAT_STR = "%d";
    
    public static void setFormatString( String fmt ) {
        FORMAT_STR = fmt;
    }
    
    public Integer convert( String val ) {
        return StringConverterHelper.intValueOf( val );
    }

    public String convert( Object val )
            throws IllegalArgumentException {
        return String.format( FORMAT_STR, val );
    }
    
    public Class<Integer> getConvertedClass() {
        return Integer.class;
    }

}
