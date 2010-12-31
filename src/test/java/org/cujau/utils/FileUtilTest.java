package org.cujau.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

public class FileUtilTest {

    @Test
    public void testCopy()
            throws FileNotFoundException, IOException {
        File junitJar = ResourceUtil.getLocationOfClass( Test.class );
        String junitMd5 = DigestUtils.md5Hex( new FileInputStream( junitJar ) );
        File junitJarCopy = new File( "/tmp/junit-copy.jar" );
        FileUtil.copy( junitJar, junitJarCopy );
        String junitCopyMd5 = DigestUtils.md5Hex( new FileInputStream( junitJarCopy ) );
        assertTrue( junitMd5.equals( junitCopyMd5 ) );
    }

    @Test
    public void testMove()
            throws FileNotFoundException, IOException {
        File junitJar = ResourceUtil.getLocationOfClass( Test.class );
        String junitMd5 = DigestUtils.md5Hex( new FileInputStream( junitJar ) );
        File junitJarCopy = new File( "/tmp/junit-copy.jar" );
        FileUtil.copy( junitJar, junitJarCopy );
        String junitCopyMd5 = DigestUtils.md5Hex( new FileInputStream( junitJarCopy ) );
        assertTrue( junitMd5.equals( junitCopyMd5 ) );

        File junitJarMoveDest = new File( "/tmp/junit-move.jar" );
        FileUtil.move( junitJarCopy, junitJarMoveDest );
        assertTrue( junitJarMoveDest.exists() );
        String junitMoveMd5 = DigestUtils.md5Hex( new FileInputStream( junitJarMoveDest ) );
        assertTrue( junitCopyMd5.equals( junitMoveMd5 ) );
    }

    @Test
    public void testIncrementFilenameIfExists()
            throws IOException {
        File tmpFile = FileUtil.createTempTextFile( "test.txt", "Dummy" );
        tmpFile.deleteOnExit();
        File tmpFileInc = FileUtil.incrementFilenameIfExists( tmpFile, false, '-' );
        assertEquals( "test-1.txt", tmpFileInc.getName() );
        assertEquals( tmpFile.getParent(), tmpFileInc.getParent() );

        tmpFile = FileUtil.createTempTextFile( "test-1.txt", "Dummy" );
        tmpFile.deleteOnExit();
        tmpFileInc = FileUtil.incrementFilenameIfExists( tmpFile, false, '-' );
        assertEquals( "test-2.txt", tmpFileInc.getName() );
        tmpFileInc = FileUtil.incrementFilenameIfExists( tmpFile, true, '-' );
        assertEquals( "test-02.txt", tmpFileInc.getName() );

        tmpFile = FileUtil.createTempTextFile( "test-2.txt", "Dummy" );
        tmpFile.deleteOnExit();
        tmpFileInc = FileUtil.incrementFilenameIfExists( tmpFile, false, '-' );
        assertEquals( "test-3.txt", tmpFileInc.getName() );
        tmpFile = FileUtil.createTempTextFile( "test-3.txt", "Dummy" );
        tmpFile.deleteOnExit();
        tmpFileInc = FileUtil.incrementFilenameIfExists( tmpFile, false, '-' );
        assertEquals( "test-4.txt", tmpFileInc.getName() );
        tmpFile = FileUtil.createTempTextFile( "test-4.txt", "Dummy" );
        tmpFile.deleteOnExit();
        tmpFileInc = FileUtil.incrementFilenameIfExists( tmpFile, false, '-' );
        assertEquals( "test-5.txt", tmpFileInc.getName() );
        tmpFile = FileUtil.createTempTextFile( "test-5.txt", "Dummy" );
        tmpFile.deleteOnExit();
        tmpFileInc = FileUtil.incrementFilenameIfExists( tmpFile, false, '-' );
        assertEquals( "test-6.txt", tmpFileInc.getName() );
        tmpFile = FileUtil.createTempTextFile( "test-6.txt", "Dummy" );
        tmpFile.deleteOnExit();
        tmpFileInc = FileUtil.incrementFilenameIfExists( tmpFile, false, '-' );
        assertEquals( "test-7.txt", tmpFileInc.getName() );
        tmpFile = FileUtil.createTempTextFile( "test-7.txt", "Dummy" );
        tmpFile.deleteOnExit();
        tmpFileInc = FileUtil.incrementFilenameIfExists( tmpFile, false, '-' );
        assertEquals( "test-8.txt", tmpFileInc.getName() );
        tmpFile = FileUtil.createTempTextFile( "test-8.txt", "Dummy" );
        tmpFile.deleteOnExit();
        tmpFileInc = FileUtil.incrementFilenameIfExists( tmpFile, false, '-' );
        assertEquals( "test-9.txt", tmpFileInc.getName() );
        tmpFile = FileUtil.createTempTextFile( "test-9.txt", "Dummy" );
        tmpFile.deleteOnExit();
        tmpFileInc = FileUtil.incrementFilenameIfExists( tmpFile, false, '-' );
        assertEquals( "test-10.txt", tmpFileInc.getName() );
        tmpFileInc = FileUtil.incrementFilenameIfExists( tmpFile, true, '-' );
        assertEquals( "test-10.txt", tmpFileInc.getName() );

        tmpFile = FileUtil.createTempTextFile( "dummy", "Dummy" );
        tmpFile.deleteOnExit();
        tmpFileInc = FileUtil.incrementFilenameIfExists( tmpFile, false, '-' );
        assertEquals( "dummy-1", tmpFileInc.getName() );
        tmpFileInc = FileUtil.incrementFilenameIfExists( tmpFile, true, '-' );
        assertEquals( "dummy-01", tmpFileInc.getName() );
    }
}
