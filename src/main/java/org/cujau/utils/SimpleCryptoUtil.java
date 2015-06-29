package org.cujau.utils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Crypto utility for easily encrypting/decrypting strings.
 * <p>
 * This utility uses AES 256-bit encryption.
 * </p>
 * <p>
 * NOTE: To use this class, you need the Unlimited Strength Jurisdiction Policy Files to be installed.
 * </p>
 */
public class SimpleCryptoUtil {

    static final String ALGO = "AES";
    static final String CIPHER = "AES/CBC/PKCS5Padding";
    static final String DIGEST = "SHA-256";
    static final int IV_LEN = 16;
    static final int SALT_LEN = 16;

    static final SecureRandom random = new SecureRandom();

    /**
     * Encrypt the given clear text String data using the given key.
     *
     * @param key
     *         The key to use for encryption.
     * @param clearTextData
     *         The text data to encrypt.
     * @return The encrypted data as a base64 encoded String or <tt>null</tt> if any problem occurred
     * during the encryption process.
     */
    public static String encrypt( String key, String clearTextData ) {
        return encrypt( StringUtil.toUtf8( key ), clearTextData );
    }

    /**
     * Encrypt the given clear text String data using the given key.
     *
     * @param rawKey
     *         The raw byte array to use as the key.
     * @param clearTextData
     *         The text data to encrypt.
     * @return The encrypted data as a base64 encoded String or <tt>null</tt> if any problem occurred
     * during the encryption process.
     */
    public static String encrypt( byte[] rawKey, String clearTextData ) {
        try {
            // Create an byte array output container.
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            // Create an initialization vector (IV) for use in the block cipher
            // process.
            byte[] iv = genNewIV();
            // Write the IV to the output stream so it can be recovered and used
            // in the decryption phase.
            stream.write( iv );

            // Create the cipher used to encrypt the field.
            Cipher cipher = buildAESCipher( Cipher.ENCRYPT_MODE, rawKey, iv );

            // Create the full byte array to encrypt (salt + data);
            byte[] clearBytes = concat( genRandomSalt(), StringUtil.toUtf8( clearTextData ) );

            // Encrypt and write the bytes.
            stream.write( cipher.doFinal( clearBytes ) );

            // Get our full byte array.
            // | IV | Encrypted data |
            byte[] bytes = stream.toByteArray();
            stream.close();

            // Base64 encode the full byte array.
            return Base64.encodeBytes( bytes );
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Decrypt the given encrypted String using the given key.
     *
     * @param key
     *         The key to use for decryption.
     * @param encryptedData
     *         The encrypted, base64 encoded String to decrypt.
     * @return The unencrypted String data or <tt>null</tt> if any problem occurred during the
     * encryption process.
     */
    public static String decrypt( String key, String encryptedData ) {
        return decrypt( StringUtil.toUtf8( key ), encryptedData );
    }

    /**
     * Decrypt the given encrypted String using the given key.
     *
     * @param rawKey
     *         The raw byte array to use as the key.
     * @param encryptedData
     *         The encrypted, base64 encoded String to decrypt.
     * @return The unencrypted String data or <tt>null</tt> if any problem occurred during the
     * encryption process.
     */
    public static String decrypt( byte[] rawKey, String encryptedData ) {
        try {
            // Base64 decode the full byte array.
            byte[] bytes = Base64.decode( encryptedData.trim() );
            // Wrap the bytes for easy access.
            ByteBuffer buf = ByteBuffer.wrap( bytes );

            // Get the initialization vector (IV) portion of the full byte
            // array.
            byte[] iv = new byte[IV_LEN];
            buf.get( iv );

            // Create the cipher used to decrypt the bytes.
            Cipher cipher = buildAESCipher( Cipher.DECRYPT_MODE, rawKey, iv );

            // Get the encrypted portion of the full byte array.
            int aeslen = bytes.length - IV_LEN;
            byte[] aesVal = new byte[aeslen];
            buf.get( aesVal );

            // Decrypt the encrypted portion of the byte array.
            byte[] decVal = cipher.doFinal( aesVal );
            buf = ByteBuffer.wrap( decVal );

            // Remove the salt from the decrypted bytes.
            byte[] header = new byte[SALT_LEN];
            buf.get( header );

            // Get the decrypted data.
            int datalen = decVal.length - SALT_LEN;
            byte[] data = new byte[datalen];
            buf.get( data );

            // Convert to String.
            return new String( data, StringUtil.UTF8 );
        } catch ( Exception e ) {
            return null;
        }
    }

    /**
     * AES 256bit encryption requires a 256-bit (32 byte) key. Normal passwords are rarely that
     * exact length. So generate a SHA-256 hash (which returns a 256-bit hash) from the password and
     * return it for use as the key.
     *
     * @param rawKey
     *         The original password bytes.
     * @return A 256-bit (32 byte) key to use in the Cipher.
     * @throws NoSuchAlgorithmException
     */
    private static byte[] buildAESKeyFromRawKey( byte[] rawKey )
            throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance( DIGEST );
        return sha256.digest( rawKey );
    }

    private static Cipher buildAESCipher( int mode, byte[] rawKey, byte[] rawIv )
            throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            NoSuchPaddingException {
        byte[] aesKey = buildAESKeyFromRawKey( rawKey );

        SecretKeySpec key = new SecretKeySpec( aesKey, ALGO );
        IvParameterSpec iv = new IvParameterSpec( rawIv );

        Cipher cipher = Cipher.getInstance( CIPHER );
        cipher.init( mode, key, iv );

        return cipher;
    }

    private static byte[] genNewIV() {
        byte iv[] = new byte[IV_LEN];
        random.nextBytes( iv );
        return iv;
    }

    private static byte[] genRandomSalt() {
        byte salt[] = new byte[SALT_LEN];
        random.nextBytes( salt );
        return salt;
    }

    private static byte[] concat( byte[] a, byte[] b ) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy( a, 0, c, 0, a.length );
        System.arraycopy( b, 0, c, a.length, b.length );
        return c;
    }

}
