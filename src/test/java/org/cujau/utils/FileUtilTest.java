package org.cujau.utils;

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
}
