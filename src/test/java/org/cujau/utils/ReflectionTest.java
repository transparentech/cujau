package org.cujau.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ReflectionTest {

    class MyTestClass {
        protected String me = "Hi";
        protected String youThere = "yoYo";
        protected float boat = 3.14f;
        protected float truck = 5.643f;
        
        public String getMe() {
            return me;
        }

        public void setMe( String newMe ) {
            me = newMe;
        }
        
        public String getYouThere() {
            return youThere;
        }

        public void setYouThere( String newYou ) {
            youThere = newYou;
        }
        
        public Float getHMSBoat() {
            return boat;
        }
        
        public void setHMSBoat( Float newBoat ) {
            boat = newBoat;
        }
        
        public float getTruck() {
            return truck;
        }
        
        public void setTruck( float newTruck ) {
            truck = newTruck;
        }
    }

    @Test
    public void testGetProperty() {
        MyTestClass mtc = new MyTestClass();

        String ret;
        try {
            ret = (String) ReflectionUtils.invokeGetProperty( mtc, "me" );
            assertTrue( ret.equals( "Hi" ) );
        } catch ( ReflectionException e ) {
            fail( "problem getting property, 'me'" );
        }
        try {
            ret = (String) ReflectionUtils.invokeGetProperty( mtc, "youThere" );
            assertTrue( ret.equals( "yoYo" ) );
        } catch ( ReflectionException e ) {
            fail( "problem getting property, 'youThere'" );
        }

        try {
            Float f = (Float) ReflectionUtils.invokeGetProperty( mtc, "hMSBoat" );
            assertEquals( 3.14f, f, 0 );
        } catch ( ReflectionException e ) {
            fail( "problem getting property, 'hMSBoat'" );
        }
        
        try {
            float f = (Float) ReflectionUtils.invokeGetProperty( mtc, "truck" );
            assertEquals( 5.643f, f, 0.0001f );
        } catch( ReflectionException e ) {
            fail( "problem getting property, 'truck'" );
        }
    }
    
    @Test
    public void testSetProperty() {
        MyTestClass mtc = new MyTestClass();
        
        try {
            ReflectionUtils.invokeSetProperty( mtc, "me", "WakaWaka" );
            assertTrue( mtc.getMe().equals( "WakaWaka" ) );
        } catch ( ReflectionException e ) {
            fail( "problem setting property, 'me'" );
        }
        try {
            ReflectionUtils.invokeSetProperty( mtc, "youThere", "Hallo" );
            assertTrue( mtc.getYouThere().equals( "Hallo" ) );
        } catch ( ReflectionException e ) {
            fail( "problem setting property, 'youThere'" );
        }
        try {
            ReflectionUtils.invokeSetProperty( mtc, "hMSBoat", 6.789f );
            assertTrue( mtc.getHMSBoat() == 6.789f );
        } catch ( ReflectionException e ) {
            fail( "problem setting property, 'hMSBoat'" );
        }
        try {
            ReflectionUtils.invokeSetProperty( mtc, "truck", 6.789f, float.class );
            assertTrue( mtc.getTruck() == 6.789f );
        } catch ( ReflectionException e ) {
            fail( "problem setting property, 'truck'" );
        }
    }
}
