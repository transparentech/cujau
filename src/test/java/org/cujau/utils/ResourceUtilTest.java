package org.cujau.utils;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

public class ResourceUtilTest {

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

}
