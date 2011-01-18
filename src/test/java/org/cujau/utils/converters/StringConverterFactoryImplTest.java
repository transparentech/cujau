package org.cujau.utils.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class StringConverterFactoryImplTest {

    @Test
    public void testGetConverter() {
        StringConverterFactoryImpl impl = new StringConverterFactoryImpl();
        assertEquals( StringIntegerConverter.class, impl.getConverter( Integer.class ).getClass() );
        assertEquals( StringFloatConverter.class, impl.getConverter( Float.class ).getClass() );
        assertEquals( StringObjectConverter.class, impl.getConverter( Object.class ).getClass() );
        assertEquals( StringStringConverter.class, impl.getConverter( String.class ).getClass() );
        assertEquals( StringBooleanConverter.class, impl.getConverter( Boolean.class ).getClass() );

        assertNull( impl.getConverter( Date.class ) );
        assertNull( impl.getConverter( Comparable.class ) );
        assertNull( impl.getConverter( Calendar.class ) );
    }

    @Test
    public void testToString() {
        StringConverterFactoryImpl impl = new StringConverterFactoryImpl();
        assertEquals( "12", impl.toString( 12 ) );
        assertEquals( "true", impl.toString( true ) );
        assertEquals( "3.1415", impl.toString( 3.1415 ) );
        assertEquals( "Hi", impl.toString( "Hi" ) );
        Object obj = new String( "Bob" );
        assertEquals( "Bob", obj );
        assertEquals( "", impl.toString( null ) );
    }
}
