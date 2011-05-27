package org.cujau.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Useful file operations.
 */
public class FileUtil {

    private static final Logger LOG = LoggerFactory.getLogger( FileUtil.class );

    /**
     * Rename the file from one location to another. To avoid problems with renaming files on
     * Windows where the "toFile" already exists, this method will first move the "toFile" to a file
     * named "toFile".bak and then move the "fromFile" to the "toFile". The "toFile" will be deleted
     * at the end. If the rename still can not be carried out, the original "toFile" will be
     * restored.
     * 
     * @param fromFile
     *            The file to rename.
     * @param toFile
     *            The file to which it should be renamed.
     * @return <tt>true</tt> if the rename worked, <tt>false</tt> otherwise.
     */
    public static boolean renameFileUsingBackup( File fromFile, File toFile ) {
        boolean exists = false;
        File tmpMoveToFile = null;
        if ( toFile.exists() ) {
            exists = true;
            tmpMoveToFile = new File( toFile.getAbsoluteFile() + ".bak" );
            toFile.renameTo( tmpMoveToFile );
        }
        boolean ret = fromFile.renameTo( toFile );
        if ( !ret ) {
            if ( exists ) {
                tmpMoveToFile.renameTo( toFile );
            }
        } else {
            if ( exists ) {
                tmpMoveToFile.delete();
            }
        }
        return ret;
    }

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
        File tmpFile = createTempFile( filename );

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
        File tmpFile = createTempFile( filename );

        BufferedWriter out =
            new BufferedWriter( new OutputStreamWriter( new FileOutputStream( tmpFile ), charsetName ) );
        out.write( data );
        out.close();

        return tmpFile;
    }

    /**
     * Create a java {@link File} object that represents the given filename in the system temporary
     * directory. The file on the file system that the returned {@link File} object represents may
     * or may not exist.
     * 
     * @param filename
     *            The name of the file in the system's temp directory.
     * @return A java {@link java.io.File}.
     */
    public static File createTempFile( String filename ) {
        File tmpDir = new File( System.getProperty( "java.io.tmpdir" ) );
        File tmpFile = new File( tmpDir, filename );
        return tmpFile;
    }

    /**
     * Create a text file in the given directory containing the given text data using the given
     * character encodingto convert the characters from Java's Unicode (UTF-16) internal
     * representation to the on-disk representation.
     * 
     * @param dir
     *            The directory where the file will be created.
     * @param filename
     *            The name of the file in the directory where the data will be written.
     * @param data
     *            The text data which will be written into the file.
     * @param charsetName
     *            The name of the character set to use in the on-disk representation of the text
     *            data.
     * @return A File representing the newly created file.
     * @throws IOException
     *             If there were any problems creating or writing the file.
     */
    public static File writeFile( File dir, String filename, String data, String charsetName )
            throws IOException {
        File fullfile = new File( dir, filename );

        BufferedWriter out =
            new BufferedWriter( new OutputStreamWriter( new FileOutputStream( fullfile ), charsetName ) );
        out.write( data );
        out.close();

        return fullfile;
    }

    /**
     * Recursively delete the contents of the given directory as well as the directory itself.
     * 
     * @param path
     *            The directory path to delete.
     * @return true if the deletion completed correctly, false otherwise.
     */
    public static boolean deleteDirectory( File path ) {
        return ( deleteDirectoryContents( path ) && path.delete() );
    }

    /**
     * Recursively delete the contents of the give directory. The directory itself is not deleted.
     * 
     * @param path
     *            The directory path whose contents will be deleted.
     * @return true if the deletion completed correctly, false otherwise.
     */
    public static boolean deleteDirectoryContents( File path ) {
        boolean ret = true;
        if ( path.exists() ) {
            File[] files = path.listFiles();
            for ( File element : files ) {
                if ( element.isDirectory() ) {
                    ret &= deleteDirectory( element );
                } else {
                    ret &= element.delete();
                }
            }
        }
        return ret;
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
        FileInputStream is = null;
        InputStreamReader reader = null;

        try {
            is = new FileInputStream( file );
            reader = new InputStreamReader( is, "UTF-8" );

            StringBuilder b = new StringBuilder();
            char[] buffer = new char[1024];
            int read;
            while ( ( read = reader.read( buffer ) ) != -1 ) {
                b.append( buffer, 0, read );
            }

            return b.toString();
        } finally {
            if ( is != null ) {
                is.close();
            }
        }
    }

    /**
     * Unzip the given InputStream into the given output directory.
     * 
     * @param zip
     *            The InputStream containing a zip file.
     * @param outputDir
     *            The directory into which the contents of the zip file will be extracted.
     * @param verbose
     *            If a log entry (info level) should be written for each extracted item.
     * @throws IOException
     *             If there were any problems unzipping the given zip file.
     */
    public static void unzip( InputStream zip, File outputDir, boolean verbose )
            throws IOException {
        byte[] buf = new byte[4096];
        ZipInputStream in = null;
        try {
            in = new ZipInputStream( zip );
            while ( true ) {
                // Read the next entry.
                ZipEntry entry = in.getNextEntry();
                if ( entry == null ) {
                    break;
                }

                if ( verbose ) {
                    LOG.info( "unzipping {} ({}/{})",
                              new Object[] { entry.getName(), entry.getCompressedSize(), entry.getSize() } );
                }

                // Write out the new file.
                File entryFile = new File( outputDir, entry.getName() );
                if ( entry.isDirectory() ) {
                    entryFile.mkdir();
                } else {
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream( entryFile );
                        int len;
                        while ( ( len = in.read( buf ) ) > 0 ) {
                            out.write( buf, 0, len );
                        }
                    } finally {
                        if ( out != null ) {
                            out.close();
                        }
                    }
                }

                // Close the entry.
                in.closeEntry();
            }
        } finally {
            // Close the input zip.
            in.close();
        }
    }

    /**
     * Copy the contents of the given source File to the given destination File. The destination
     * file will be overwritten if it already exists.
     * 
     * @param src
     *            The source File to copy.
     * @param dest
     *            The destination File into which the contents of the source file will be copied.
     * @throws IOException
     *             If any problems arose while copying.
     */
    public static void copy( File src, File dest )
            throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream( src );
            fos = new FileOutputStream( dest );
            byte[] buf = new byte[8192];
            int i = 0;
            while ( ( i = fis.read( buf ) ) != -1 ) {
                fos.write( buf, 0, i );
            }
        } finally {
            if ( fis != null ) {
                fis.close();
            }
            if ( fos != null ) {
                fos.close();
            }
        }
    }

    /**
     * Move the given source File to the given destination File. This implementation of this method
     * first performs a {@link #copy}, then a delete on the source File. The destination file will
     * be overwritten if it already exists.
     * 
     * @param src
     *            The source File to move
     * @param dest
     *            The destination File to create or overwrite.
     * @throws IOException
     *             If any problems arose while copying.
     */
    public static void move( File src, File dest )
            throws IOException {
        copy( src, dest );
        src.delete();
    }

    /**
     * If the given <tt>src</tt> File exists, add a counter before the suffix and return the new
     * file. If the File does not exist, it is returned directly.
     * 
     * @param src
     *            The file to check and increment.
     * @param zeroPad
     *            If a counter is appended, pad the number with 0s out to 2 places (i.e. 01, 02,
     *            etc).
     * @param counterSeparator
     *            The character used to separate the counter from the name. Typically '-' or '_'.
     * @return A File that does not exist.
     */
    public static File incrementFilenameIfExists( File src, boolean zeroPad, char counterSeparator ) {
        while ( src.exists() ) {
            String name = src.getName();
            int suffixIndex = name.lastIndexOf( "." );
            String suffix = "";
            if ( suffixIndex != -1 ) {
                suffix = name.substring( suffixIndex );
                name = name.substring( 0, suffixIndex );
            }
            int counter = 0;
            Pattern pat = Pattern.compile( "^(.*)(" + counterSeparator + "[0-9]+)$" );
            Matcher mat = pat.matcher( name );
            if ( mat.matches() ) {
                name = mat.group( 1 );
                String ct = mat.group( 2 ).substring( 1 );
                counter = Integer.valueOf( ct );
            }
            counter++;
            if ( zeroPad ) {
                name = String.format( "%s" + counterSeparator + "%02d%s", name, counter, suffix );
            } else {
                name = String.format( "%s" + counterSeparator + "%d%s", name, counter, suffix );
            }
            src = new File( src.getParentFile(), name );
        }
        return src;
    }
}
