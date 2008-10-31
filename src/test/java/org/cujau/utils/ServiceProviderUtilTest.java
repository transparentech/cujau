package org.cujau.utils;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceProviderUtilTest {

    private static final Logger LOG = LoggerFactory.getLogger( ServiceProviderUtilTest.class );
    
    @Test
    public void getServiceProvidersTest() {
        List<String> providers = ServiceProviderUtil.getServiceProviderNames( ServiceProviderUtilTest.class );
        for ( String p : providers ) {
            LOG.debug( p );
        }
        assertTrue( "expected 2 providers by got " + providers.size(), providers.size() == 2 );
        assertTrue( providers.get( 0 ).equals( "org.cujau.util.impl.MyTestServiceProviderTestClass" ) );
        assertTrue( providers.get( 1 ).equals( "org.cujau.util.impl.AnotherServiceProviderTestClass" ) );
    }

    @Test
    public void parseLineTest() {
        String line = "org.me.you.MyClass #hello world";
        assertTrue( ServiceProviderUtil.parseLine( line ).equals( "org.me.you.MyClass" ) );
        line = "#asdf  asd asdf ds";
        assertNull( ServiceProviderUtil.parseLine( line ) );
        line = "# asdf  asd asdf ds #asdf asdf asdf";
        assertNull( ServiceProviderUtil.parseLine( line ) );
        line = "####asdf  asd asdf ds # asdf asdf asdf";
        assertNull( ServiceProviderUtil.parseLine( line ) );
        line = "#";
        assertNull( ServiceProviderUtil.parseLine( line ) );
        line = "                    ";
        assertNull( ServiceProviderUtil.parseLine( line ) );
        line = "org.me.you.AllIsGood";
        assertTrue( ServiceProviderUtil.parseLine( line ).equals( "org.me.you.AllIsGood" ) );
        line = "   org.me.you.AllIsGood   ";
        assertTrue( ServiceProviderUtil.parseLine( line ).equals( "org.me.you.AllIsGood" ) );
        line = "org.asdf+asdf.NotGood  ";
        assertTrue( ServiceProviderUtil.parseLine( line ).equals( "org.asdf+asdf.NotGood" ) );
    }

    @Test
    public void nonExistantSPITest() {
        List<String> providers = ServiceProviderUtil.getServiceProviderNames( FileUtil.class );
        assertTrue( providers.size() == 0 );
    }
}
