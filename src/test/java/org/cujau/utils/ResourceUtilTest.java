package org.cujau.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceUtilTest {

    private static final Logger LOG = LoggerFactory.getLogger( ResourceUtilTest.class );
    
    @Test
    public void resource1()
            throws IOException {
        String res = ResourceUtil.getResourceAsString( "/testPackage/test1.txt" );
        String shouldBe = "Hi Brother!";
        assertTrue( res.equals( shouldBe ) );
    }

    @Test
    public void resource2()
            throws IOException {
        String res = ResourceUtil.getResourceAsString( "/testPackage/test2.txt" );
        assertNull( res );
    }

    @Test
    public void testGetFileOrDirectoryContainingClass() {
        File jar = ResourceUtil.getLocationOfClass( ResourceUtilTest.class );
        assertNotNull( jar );
        LOG.debug( jar.getAbsolutePath() );
        jar = ResourceUtil.getLocationOfClass( Test.class );
        assertNotNull( jar );
        LOG.debug( jar.getAbsolutePath() );
    }
}
