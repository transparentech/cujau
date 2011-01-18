package org.cujau.utils.converters;

import java.util.HashMap;
import java.util.Map;

public class StringConverterFactoryImpl implements StringConverterFactory {

    private static Map<Class<?>, StringConverter<?>> map = new HashMap<Class<?>, StringConverter<?>>();
    static {
        map.put( String.class, new StringStringConverter() );
        map.put( Integer.class, new StringIntegerConverter() );
        map.put( Float.class, new StringFloatConverter() );
        map.put( Boolean.class, new StringBooleanConverter() );
        map.put( Object.class, new StringObjectConverter() );
    }
    
    public StringConverter<?> getConverter( Class<?> klass ) {
        return map.get( klass );
    }
    
    public String toString( Object val ) {
        StringConverter<?> cvtr = null;
        if ( val != null ) {
            cvtr = map.get( val.getClass() );
        }
        if ( cvtr == null ) {
            cvtr = map.get( Object.class );
        }
        return (String) cvtr.convert( val );
    }
}
