package org.cujau.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Useful file operations.
 */
public class FileUtil {
    /**
     * Create a temporary text file containing the given text data using the platform default
     * character encoding to convert the characters from Java's Unicode (UTF-16) internal
     * representation to the on-disk representation.
     * 
     * @param filename
     *            The file name of the file into which the data will be written. This value will be
     *            appended to the Java system property, <tt>java.io.tmpdir</tt>, to create the
     *            absolute path name of the temporary file.
     * @param data
     *            The text data which will be written into the temporary file.
     * @return A File representing the newly created temporary file.
     * @throws IOException
     *             If there were any problems creating or writing to the temporary file.
     */
    public static File createTempTextFile( String filename, String data )
            throws IOException {
        File tmpDir = new File( System.getProperty( "java.io.tmpdir" ) );
        File tmpFile = new File( tmpDir, filename );

        BufferedWriter out = new BufferedWriter( new FileWriter( tmpFile ) );
        out.write( data );
        out.close();

        return tmpFile;
    }

    /**
     * Create a temporary text file containing the given text data using the given character
     * encoding to convert the characters from Java's Unicode (UTF-16) internal representation to
     * the on-disk representation.
     * 
     * @param filename
     *            The file name of the file into which the data will be written. This value will be
     *            appended to the Java system property, <tt>java.io.tmpdir</tt>, to create the
     *            absolute path name of the temporary file.
     * @param data
     *            The text data which will be written into the temporary file.
     * @param charsetName
     *            The name of the character set to use in the on-disk representation of the text
     *            data.
     * @return A File representing the newly created temporary file.
     * @throws IOException
     *             If there were any problems creating or writing to the temporary file.
     */
    public static File createTempTextFile( String filename, String data, String charsetName )
            throws IOException {
        File tmpDir = new File( System.getProperty( "java.io.tmpdir" ) );
        File tmpFile = new File( tmpDir, filename );

        BufferedWriter out =
            new BufferedWriter( new OutputStreamWriter( new FileOutputStream( tmpFile ), charsetName ) );
        out.write( data );
        out.close();

        return tmpFile;
    }

    /**
     * Recursively delete the given directory.
     * 
     * @param path
     *            The directory path to delete.
     * @return true if the deletion completed correctly, false otherwise.
     */
    public static boolean deleteDirectory( File path ) {
        if ( path.exists() ) {
            File[] files = path.listFiles();
            for ( File element : files ) {
                if ( element.isDirectory() ) {
                    deleteDirectory( element );
                } else {
                    element.delete();
                }
            }
        }
        return ( path.delete() );
    }

    /**
     * Returns the size in bytes of the given file or directory.
     * 
     * @param file
     *            a File object representing a file or directory.
     * @return the size of the given file or directory as a long value.
     */
    public static long getFileOrDirectorySize( File file ) {
        if ( file.isFile() ) {
            return file.length();
        }
        File[] files = file.listFiles();
        long size = 0;
        if ( files != null ) {
            for ( File f : files ) {
                size += getFileOrDirectorySize( f );
            }
        }
        return size;
    }

    /**
     * Returns the contents of the given File as a String. The File is assumed to be UTF-8 encoded.
     * 
     * @param file
     *            The file whose contents will be returned as a String.
     * @return A String containing the contents of the file.
     * @throws IOException
     *             If the file does not exist or there were problems reading data from the file.
     */
    public static String getFileAsString( File file )
            throws IOException {

        FileInputStream is = new FileInputStream( file );
        InputStreamReader reader = new InputStreamReader( is, "UTF-8" );

        StringBuilder b = new StringBuilder();
        char[] buffer = new char[1024];
        int read;
        while ( ( read = reader.read( buffer ) ) != -1 ) {
            b.append( buffer, 0, read );
        }

        return b.toString();
    }
}
