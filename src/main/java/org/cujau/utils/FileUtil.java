package org.cujau.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Useful file operations.
 */
public class FileUtil {

    private static final Logger LOG = LoggerFactory.getLogger( FileUtil.class );
    public static final String UTF8 = "UTF-8";

    public static void assertContentsOfFile( File dataFile, String contents )
            throws IOException {
        String data = getFileAsString( dataFile );
        if ( !data.equals( contents ) ) {
            throw new AssertionError(
                    "Contents of file, " + dataFile.getAbsolutePath() + ", did not match given contents." );
        }
    }

    /**
     * Rename the file from one location to another. To avoid problems with renaming files on
     * Windows where the "toFile" already exists, this method will first move the "toFile" to a file
     * named "toFile".bak and then move the "fromFile" to the "toFile". The "toFile" will be deleted
     * at the end. If the rename still can not be carried out, the original "toFile" will be
     * restored.
     *
     * @param fromFile
     *         The file to rename.
     * @param toFile
     *         The file to which it should be renamed.
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
     *         The file name of the file into which the data will be written. This value will be
     *         appended to the Java system property, <tt>java.io.tmpdir</tt>, to create the
     *         absolute path name of the temporary file.
     * @param data
     *         The text data which will be written into the temporary file.
     * @return A File representing the newly created temporary file.
     * @throws IOException
     *         If there were any problems creating or writing to the temporary file.
     */
    public static File createTempTextFile( String filename, String data )
            throws IOException {
        File tmpFile = createTempFile( filename );
        writeFile( tmpFile, data, null );
        return tmpFile;
    }

    /**
     * Create a text file containing the given text data using the platform default
     * character encoding to convert the characters from Java's Unicode (UTF-16) internal
     * representation to the on-disk representation.
     *
     * @param file
     *         The file into which the data will be written.
     * @param data
     *         The text data which will be written into the file.
     * @return A File representing the newly created file (same as parameter).
     * @throws IOException
     *         If there were any problems creating or writing to the file.
     */
    public static File createTextFile( File file, String data )
            throws IOException {
        writeFile( file, data, null );
        return file;
    }

    /**
     * Create a temporary text file containing the given text data using the given character
     * encoding to convert the characters from Java's Unicode (UTF-16) internal representation to
     * the on-disk representation.
     *
     * @param filename
     *         The file name of the file into which the data will be written. This value will be
     *         appended to the Java system property, <tt>java.io.tmpdir</tt>, to create the
     *         absolute path name of the temporary file.
     * @param data
     *         The text data which will be written into the temporary file.
     * @param charsetName
     *         The name of the character set to use in the on-disk representation of the text
     *         data.
     * @return A File representing the newly created temporary file.
     * @throws IOException
     *         If there were any problems creating or writing to the temporary file.
     */
    public static File createTempTextFile( String filename, String data, String charsetName )
            throws IOException {
        File tmpFile = createTempFile( filename );
        writeFile( tmpFile, data, charsetName );
        return tmpFile;
    }

    /**
     * Create a java {@link File} object that represents the given filename in the system temporary
     * directory. The file on the file system that the returned {@link File} object represents may
     * or may not exist.
     *
     * @param filename
     *         The name of the file in the system's temp directory.
     * @return A java {@link java.io.File}.
     */
    public static File createTempFile( String filename ) {
        File tmpDir = getTempDirectory();
        return new File( tmpDir, filename );
    }

    /**
     * Return the system's temp directory. The system temp directory is the value of the
     * <tt>java.io.tmpdir</tt> System property.
     *
     * @return A File referring to the system temp directory.
     */
    public static File getTempDirectory() {
        return new File( System.getProperty( "java.io.tmpdir" ) );
    }

    /**
     * Create a text file with the given <tt>filename</tt> containing the given text data using the
     * platforms default encoding.
     *
     * @param filename
     *         Name of the file to create and write.
     * @param data
     *         The data to write into the file.
     * @return The written file.
     * @throws IOException
     *         If there were any problems creating or writing the file.
     */
    public static File writeFile( File filename, String data )
            throws IOException {
        return writeFile( filename, data, null );
    }

    /**
     * Create a text file with the given <tt>filename</tt> containing the given text data using the
     * give character encoding to characters from Java's Unicode (UTF-16) internal representation to
     * the on-disk representation. If the given chartsetName is null, the platform's default
     * encoding will be used.
     *
     * @param filename
     *         Name of the file to create and write.
     * @param data
     *         The data to write into the file.
     * @return The written file.
     * @throws IOException
     *         If there were any problems creating or writing the file.
     */
    public static File writeFile( File filename, String data, String charsetName )
            throws IOException {
        BufferedWriter out = null;
        try {
            if ( charsetName == null ) {
                out = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( filename ) ) );
            } else {
                out = new BufferedWriter(
                        new OutputStreamWriter( new FileOutputStream( filename ), charsetName ) );
            }
            if ( data != null ) {
                out.write( data );
            }
        } finally {
            if ( out != null ) {
                out.close();
            }
        }

        return filename;
    }

    /**
     * Create a text file in the given directory containing the given text data using the given
     * character encoding to convert the characters from Java's Unicode (UTF-16) internal
     * representation to the on-disk representation.
     *
     * @param dir
     *         The directory where the file will be created.
     * @param filename
     *         The name of the file in the directory where the data will be written.
     * @param data
     *         The text data which will be written into the file.
     * @param charsetName
     *         The name of the character set to use in the on-disk representation of the text
     *         data.
     * @return A File representing the newly created file.
     * @throws IOException
     *         If there were any problems creating or writing the file.
     */
    public static File writeFile( File dir, String filename, String data, String charsetName )
            throws IOException {
        File fullfile = new File( dir, filename );
        writeFile( fullfile, data, charsetName );
        return fullfile;
    }

    /**
     * Recursively delete the contents of the given directory as well as the directory itself.
     *
     * @param path
     *         The directory path to delete.
     * @return true if the deletion completed correctly, false otherwise.
     */
    public static boolean deleteDirectory( File path ) {
        return ( deleteDirectoryContents( path ) && path.delete() );
    }

    /**
     * Recursively delete the contents of the give directory. The directory itself is not deleted.
     *
     * @param path
     *         The directory path whose contents will be deleted.
     * @return true if the deletion completed correctly, false otherwise.
     */
    public static boolean deleteDirectoryContents( File path ) {
        boolean ret = true;
        if ( path.exists() ) {
            File[] files = path.listFiles();
            if ( files != null ) {
                for ( File element : files ) {
                    if ( element.isDirectory() ) {
                        ret &= deleteDirectory( element );
                    } else {
                        ret &= element.delete();
                    }
                }
            }
        }
        return ret;
    }

    /**
     * Returns the size in bytes of the given file or directory.
     *
     * @param file
     *         a File object representing a file or directory.
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
     *         The file whose contents will be returned as a String.
     * @return A String containing the contents of the file.
     * @throws IOException
     *         If the file does not exist or there were problems reading data from the file.
     */
    public static String getFileAsString( File file )
            throws IOException {
        FileInputStream is = null;
        InputStreamReader reader;

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
     * Zip the input file or directory into the output stream.
     *
     * @param zip
     *         The output stream in which to write the zipped data. For example: <tt>new
     *         FileOutputStream("MyZipFile.zip")</tt>
     * @param inputFileOrDir
     *         The file or directory to zip.
     * @param dirIncludesSelf
     *         <tt>true</tt> means that if the <tt>inputFileOrDir</tt> is a directory, the directory will be
     *         included as the base directory in the zip. If <tt>false</tt>, the directory will be skipped
     *         and any child files/directories of this directory will be added to the (empty) root of the
     *         zip.
     * @param verbose
     *         If a log entry (info level) should be written for each zipped item.
     * @throws IOException
     */
    public static void zip( OutputStream zip, File inputFileOrDir, boolean dirIncludesSelf, boolean verbose )
            throws IOException {
        byte[] buffer = new byte[4096];
        ZipOutputStream zos = new ZipOutputStream( zip );

        if ( !inputFileOrDir.isDirectory() ) {
            // Zip a single file.
            addFile( zos, "", inputFileOrDir, verbose );
            // Close the stream.
            zos.close();
        } else {
            // Zip all files in this directory.
            String path = "";
            if ( dirIncludesSelf ) {
                path = inputFileOrDir.getName();
                // Add the base directory.
                zos.putNextEntry( new ZipEntry( path + "/" ) );
            }
            addDirectory( zos, path, inputFileOrDir, verbose );
            zos.close();
        }
    }

    private static void addDirectory( ZipOutputStream zos, String path, File dir, boolean verbose )
            throws IOException {
        for ( File f : dir.listFiles() ) {
            if ( f.isDirectory() ) {
                // Add the directory
                String nextpath = path + File.separator + f.getName();
                zos.putNextEntry( new ZipEntry( nextpath + "/" ) );
                zos.closeEntry();
                LOG.info( "zipped {}", nextpath + "/" );
                // Then add any child directories and/or files.
                addDirectory( zos, nextpath, f, verbose );
            } else {
                // Just add this file.
                addFile( zos, path + File.separator, f, verbose );
            }
        }
    }

    private static void addFile( ZipOutputStream zos, String path, File file, boolean verbose )
            throws IOException {
        byte[] buffer = new byte[4096];

        String fullPath = path + file.getName();
        ZipEntry ze = new ZipEntry( fullPath );
        zos.putNextEntry( ze );

        FileInputStream in = new FileInputStream( file );
        int len;
        while ( ( len = in.read( buffer ) ) > 0 ) {
            zos.write( buffer, 0, len );
        }
        in.close();

        zos.closeEntry();
        LOG.info( "zipped {} ({}/{})", fullPath, ze.getCompressedSize(), ze.getSize() );
    }

    /**
     * Unzip the given InputStream into the given output directory.
     *
     * @param zip
     *         The InputStream containing a zip file.
     * @param outputDir
     *         The directory into which the contents of the zip file will be extracted.
     * @param verbose
     *         If a log entry (info level) should be written for each extracted item.
     * @throws IOException
     *         If there were any problems unzipping the given zip file.
     */
    public static void unzip( InputStream zip, File outputDir, boolean verbose )
            throws IOException {
        byte[] buf = new byte[4096];
        ZipInputStream in;
        try {
            in = new ZipInputStream( zip );
            while ( true ) {
                // Read the next entry.
                ZipEntry entry = in.getNextEntry();
                if ( entry == null ) {
                    break;
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

                if ( verbose ) {
                    LOG.info( "unzipping {} ({}/{})", entry.getName(), entry.getCompressedSize(),
                              entry.getSize() );
                }

                // Close the entry.
                in.closeEntry();
            }
        } finally {
            // Don't close the zip as this will close the underlying InputStream that comes from the
            // caller. It is the callers responsibility to close that stream.
        }
    }

    /**
     * Copy the contents of the given source File to the given destination File. The destination
     * file will be overwritten if it already exists.
     *
     * @param src
     *         The source File to copy.
     * @param dest
     *         The destination File into which the contents of the source file will be copied.
     * @throws IOException
     *         If any problems arose while copying.
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
     *         The source File to move
     * @param dest
     *         The destination File to create or overwrite.
     * @throws IOException
     *         If any problems arose while copying.
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
     *         The file to check and increment.
     * @param zeroPad
     *         If a counter is appended, pad the number with 0s out to 2 places (i.e. 01, 02,
     *         etc).
     * @param counterSeparator
     *         The character used to separate the counter from the name. Typically '-' or '_'.
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
