package org.cujau.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class StreamUtil {

    public static void copyReader( Reader reader, Writer writer )
            throws IOException {
        copyReader( reader, writer, 8192 );
    }

    public static void copyReader( Reader reader, Writer writer, int bufferSize )
            throws IOException {
        char[] buffer = new char[bufferSize];
        int read;
        while ( ( read = reader.read( buffer ) ) != -1 ) {
            writer.write( buffer, 0, read );
        }
    }

    public static void streamCopy( InputStream inputStream, OutputStream outputStream )
            throws IOException {
        streamCopy( inputStream, outputStream, 8192 );
    }

    public static void streamCopy( InputStream inputStream, OutputStream outputStream, int bufferSize )
            throws IOException {
        int read = -1;
        byte[] readBuffer = new byte[bufferSize];
        while ( ( read = inputStream.read( readBuffer, 0, bufferSize ) ) != -1 ) {
            outputStream.write( readBuffer, 0, read );
        }
    }

}
