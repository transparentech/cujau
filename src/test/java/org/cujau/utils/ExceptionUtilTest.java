package org.cujau.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionUtilTest {

    private static final Logger LOG = LoggerFactory.getLogger( ExceptionUtilTest.class );

    @Test
    public void test1() {
        try {
            ( (String) null ).length();
        } catch ( Throwable t ) {
            String trace = ExceptionUtil.getSimpletrace( t );
            LOG.info( trace );
            assertEquals( "NullPointerException", trace );
            return;
        }
        fail( "Should not be here." );
    }

    @Test
    public void test2() {
        try {
            throw new RuntimeException( "Got an exception.",
                                        new InvocationTargetException( new NullPointerException(),
                                                                       "Problem Alert!" ) );
        } catch ( Throwable t ) {
            String trace = ExceptionUtil.getSimpletrace( t );
            LOG.info( trace );
            assertEquals( "RuntimeException : Got an exception.\n  InvocationTargetException : Problem Alert!\n    NullPointerException",
                          trace );
            return;
        }
    }
}
