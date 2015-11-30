package org.cujau.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtilTest {

    private static final Logger LOG = LoggerFactory.getLogger( FileUtilTest.class );

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

    @Test
    public void testZipSingleFile()
            throws IOException {
        String fileName = "FileUtilTest-single.txt";
        String fileContents = "Now is the time\nFor all good men\nTo come to the aid\nof their country.";

        File txtFile = FileUtil.createTempFile( fileName );
        File zipFile = FileUtil.createTempFile( fileName + ".zip" );

        FileUtil.createTextFile( txtFile, fileContents );
        FileUtil.zip( new FileOutputStream( zipFile ), txtFile, false, true );
        assertTrue( zipFile.exists() );
        assertTrue( txtFile.delete() );
        assertFalse( txtFile.exists() );

        FileUtil.unzip( new FileInputStream( zipFile ), FileUtil.getTempDirectory(), true );
        assertTrue( txtFile.exists() );
        FileUtil.assertContentsOfFile( txtFile, fileContents );

        assertTrue( zipFile.delete() );
        assertTrue( txtFile.delete() );
        assertFalse( zipFile.exists() );
        assertFalse( txtFile.exists() );
    }

    @Test
    public void testZipMultiplefilesIncludeBase()
            throws IOException {
        Map<File, String> tempFiles = new HashMap<File, String>();
        File baseDir = FileUtil.createTempFile( "FileUtilTest-multi" );

        File outZip = setupAndZipDirectory( baseDir, tempFiles, true );
        assertZipFile( outZip, baseDir, FileUtil.getTempDirectory(), tempFiles );
    }

    @Test
    public void testZipMultiplefilesNoBase()
            throws IOException {
        Map<File, String> tempFiles = new HashMap<File, String>();
        File baseDir = FileUtil.createTempFile( "FileUtilTest-multi" );

        File outZip = setupAndZipDirectory( baseDir, tempFiles, false );
        // Make the basedir again so we can unzip into it.
        baseDir.mkdirs();
        assertZipFile( outZip, baseDir, baseDir, tempFiles );
    }

    private void assertZipFile( File outZip, File baseDir, File unzipDir, Map<File, String> tempFiles )
            throws IOException {
        // Unzip the zip file.
        FileUtil.unzip( new FileInputStream( outZip ), unzipDir, true );

        assertTrue( baseDir.exists() );
        assertFile( baseDir, tempFiles, true );

        // Cleanup the zip file and the unzipped directory.
        FileUtil.deleteDirectory( baseDir );
        outZip.delete();
        assertFalse( baseDir.exists() );
        assertFalse( outZip.exists() );
    }

    private File setupAndZipDirectory( File baseDir, Map<File, String> tempFiles, boolean includeBaseDir )
            throws IOException {
        baseDir.mkdirs();

        // Create the tmp files in the directory.
        for ( int i = 0; i < 3; i++ ) {
            File tmpFile = new File( baseDir, "file-" + i + ".txt" );
            String tmpStr = String.format( "%1$s%1$s%1$s%1$s%1$s%1$s", i );
            FileUtil.createTextFile( tmpFile, tmpStr );
            tempFiles.put( tmpFile, tmpStr );
            LOG.debug( "Wrote: {}", tmpFile.getAbsoluteFile() );

            File tmpDir = new File( baseDir, "file-" + i );
            tmpDir.mkdirs();

            for ( int j = 0; j < 3; j++ ) {
                File tmpFile2 = new File( tmpDir, "file-" + i + "_" + j + ".txt" );
                String tmpStr2 = String.format( "%1$s%1$s%1$s%2$s%2$s%2$s", i, j );
                FileUtil.createTextFile( tmpFile2, tmpStr2 );
                tempFiles.put( tmpFile2, tmpStr2 );
                LOG.debug( "Wrote: {}", tmpFile2.getAbsoluteFile() );
            }
        }

        // Zip the directory.
        File outZip = FileUtil.createTempFile( baseDir.getName() + ".zip" );
        assertFalse( outZip.exists() );
        FileUtil.zip( new FileOutputStream( outZip ), baseDir, includeBaseDir, true );
        assertTrue( outZip.exists() );
        // Cleanup the original unzipped directory.
        FileUtil.deleteDirectory( baseDir );
        assertFalse( baseDir.exists() );

        return outZip;
    }

    private void assertFile( File f, Map<File, String> tempFiles, boolean assertDirectory )
            throws IOException {
        if ( assertDirectory ) {
            assertTrue( "expected " + f.getPath() + " to exist", f.exists() );
        }
        if ( f.isDirectory() ) {
            for ( File f2 : f.listFiles() ) {
                assertFile( f2, tempFiles, true );
            }
        } else {
            FileUtil.assertContentsOfFile( f, tempFiles.get( f ) );
        }
    }
}
