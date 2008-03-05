package org.cujau.utils.converters;

import java.util.HashMap;
import java.util.Map;

public class StringConverterFactory {

    private static Map<Class<?>, StringConverter<?>> map = new HashMap<Class<?>, StringConverter<?>>();
    static {
        map.put( String.class, new StringToString() );
        map.put( Integer.class, new StringToInteger() );
        map.put( Float.class, new StringToFloat() );
    }
    
    public static StringConverter<?> getInstance( Class<?> klass ) {
        return map.get( klass );
    }
}
