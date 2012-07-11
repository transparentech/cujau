package org.cujau.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SimpleCryptoUtilTest {

    private static final int MAX = 10000;

    @Test
    public void testVariousKeyLengths()
            throws UnsupportedEncodingException {
        // Short key test.
        String clearStr = "Something I would like to keep secret.";
        String passwordStr = "ABC123-987ZYX";
        String encryptedStr = SimpleCryptoUtil.encrypt( passwordStr.getBytes( "UTF-8" ), clearStr );
        assertNotNull( encryptedStr );
        String decryptedStr = SimpleCryptoUtil.decrypt( passwordStr.getBytes( "UTF-8" ), encryptedStr );
        assertEquals( clearStr, decryptedStr );
        // 16 byte key test.
        passwordStr = "ABC123-987ZYX123";
        encryptedStr = SimpleCryptoUtil.encrypt( passwordStr.getBytes( "UTF-8" ), clearStr );
        assertNotNull( encryptedStr );
        decryptedStr = SimpleCryptoUtil.decrypt( passwordStr.getBytes( "UTF-8" ), encryptedStr );
        assertEquals( clearStr, decryptedStr );
        // Middle length key test.
        passwordStr = "ABC123-987ZYX12345678";
        encryptedStr = SimpleCryptoUtil.encrypt( passwordStr.getBytes( "UTF-8" ), clearStr );
        assertNotNull( encryptedStr );
        decryptedStr = SimpleCryptoUtil.decrypt( passwordStr.getBytes( "UTF-8" ), encryptedStr );
        assertEquals( clearStr, decryptedStr );
        // 32 byte key test.
        passwordStr = "ABC123-987ZYX123ABC123-987ZYX123";
        encryptedStr = SimpleCryptoUtil.encrypt( passwordStr.getBytes( "UTF-8" ), clearStr );
        assertNotNull( encryptedStr );
        decryptedStr = SimpleCryptoUtil.decrypt( passwordStr.getBytes( "UTF-8" ), encryptedStr );
        assertEquals( clearStr, decryptedStr );
        // Long key test.
        passwordStr = "ABC123-987ZYX123ABC123-987ZYX123ABC123-987ZYX123ABC123-98";
        encryptedStr = SimpleCryptoUtil.encrypt( passwordStr.getBytes( "UTF-8" ), clearStr );
        assertNotNull( encryptedStr );
        decryptedStr = SimpleCryptoUtil.decrypt( passwordStr.getBytes( "UTF-8" ), encryptedStr );
        assertEquals( clearStr, decryptedStr );
    }

    @Test
    public void testKeyAsString() {
        // Short key test.
        String clearStr = "Something I would like to keep secret.";
        String passwordStr = "ABC123-987ZYX";
        String encryptedStr = SimpleCryptoUtil.encrypt( passwordStr, clearStr );
        assertNotNull( encryptedStr );
        String decryptedStr = SimpleCryptoUtil.decrypt( passwordStr, encryptedStr );
        assertEquals( clearStr, decryptedStr );
        // 16 byte key test.
        passwordStr = "ABC123-987ZYX123";
        encryptedStr = SimpleCryptoUtil.encrypt( passwordStr, clearStr );
        assertNotNull( encryptedStr );
        decryptedStr = SimpleCryptoUtil.decrypt( passwordStr, encryptedStr );
        assertEquals( clearStr, decryptedStr );
        // Middle length key test.
        passwordStr = "ABC123-987ZYX12345678";
        encryptedStr = SimpleCryptoUtil.encrypt( passwordStr, clearStr );
        assertNotNull( encryptedStr );
        decryptedStr = SimpleCryptoUtil.decrypt( passwordStr, encryptedStr );
        assertEquals( clearStr, decryptedStr );
        // 32 byte key test.
        passwordStr = "ABC123-987ZYX123ABC123-987ZYX123";
        encryptedStr = SimpleCryptoUtil.encrypt( passwordStr, clearStr );
        assertNotNull( encryptedStr );
        decryptedStr = SimpleCryptoUtil.decrypt( passwordStr, encryptedStr );
        assertEquals( clearStr, decryptedStr );
        // Long key test.
        passwordStr = "ABC123-987ZYX123ABC123-987ZYX123ABC123-987ZYX123ABC123-98";
        encryptedStr = SimpleCryptoUtil.encrypt( passwordStr, clearStr );
        assertNotNull( encryptedStr );
        decryptedStr = SimpleCryptoUtil.decrypt( passwordStr, encryptedStr );
        assertEquals( clearStr, decryptedStr );
    }

    @Test
    public void testVariousTextLengths()
            throws UnsupportedEncodingException {
        String clearStr = "";
        String passwordStr = "ABC123-987ZYX";
        String encryptedStr = SimpleCryptoUtil.encrypt( passwordStr.getBytes( "UTF-8" ), clearStr );
        assertNotNull( encryptedStr );
        String decryptedStr = SimpleCryptoUtil.decrypt( passwordStr.getBytes( "UTF-8" ), encryptedStr );
        assertEquals( clearStr, decryptedStr );

        clearStr = ".";
        passwordStr = "ABC123-987ZYX";
        encryptedStr = SimpleCryptoUtil.encrypt( passwordStr.getBytes( "UTF-8" ), clearStr );
        assertNotNull( encryptedStr );
        decryptedStr = SimpleCryptoUtil.decrypt( passwordStr.getBytes( "UTF-8" ), encryptedStr );
        assertEquals( clearStr, decryptedStr );

        clearStr = "Now is the time for all good men to come to the aid of their country.";
        passwordStr = "ABC123-987ZYX";
        encryptedStr = SimpleCryptoUtil.encrypt( passwordStr.getBytes( "UTF-8" ), clearStr );
        assertNotNull( encryptedStr );
        decryptedStr = SimpleCryptoUtil.decrypt( passwordStr.getBytes( "UTF-8" ), encryptedStr );
        assertEquals( clearStr, decryptedStr );

        StringBuilder buf = new StringBuilder();
        for ( int i = 0; i < MAX; i++ ) {
            buf.append( "Now is the time for all good men to come to the aid of their country." );
            buf.append( "\n" );
        }
        clearStr = buf.toString();
        passwordStr = "ABC123-987ZYX";
        encryptedStr = SimpleCryptoUtil.encrypt( passwordStr.getBytes( "UTF-8" ), clearStr );
        assertNotNull( encryptedStr );
        decryptedStr = SimpleCryptoUtil.decrypt( passwordStr.getBytes( "UTF-8" ), encryptedStr );
        assertEquals( clearStr, decryptedStr );
    }

    @Test
    public void testNeverSameEncryptedResult()
            throws UnsupportedEncodingException {
        String clearStr = "Something I would like to keep secret.";
        String passwordStr = "ABC123-987ZYX";
        Map<String, String> encStrings = new HashMap<String, String>();

        for ( int i = 0; i < MAX; i++ ) {
            String encryptedStr = SimpleCryptoUtil.encrypt( passwordStr.getBytes( "UTF-8" ), clearStr );
            assertNotNull( encryptedStr );
            assertNull( encStrings.get( encryptedStr ) );
            String decryptedStr = SimpleCryptoUtil.decrypt( passwordStr.getBytes( "UTF-8" ), encryptedStr );
            assertEquals( clearStr, decryptedStr );

            encStrings.put( encryptedStr, encryptedStr );
        }
    }
}
