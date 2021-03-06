package org.cujau.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamUtil {

    private static final Logger LOG = LoggerFactory.getLogger(StreamUtil.class);
    static final int BUFFER_SIZE = 8192;

    public static void copyReader(Reader reader, Writer writer)
            throws IOException {
        copyReader(reader, writer, BUFFER_SIZE);
    }

    public static void copyReader(Reader reader, Writer writer, int bufferSize)
            throws IOException {
        char[] buffer = new char[bufferSize];
        int read;
        while ((read = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, read);
        }
    }

    /**
     * Copy the given input stream into the given StringBuilder, returning a new InputStream that is
     * a copy of the InputStream passed in.
     *
     * @param in
     *         The input stream to copy to a String.
     * @param buf
     *         The StringBuilder that will hold the string.
     * @return A copy of the InputStream.
     * @throws IOException
     *         If there was a problem copying into the String.
     */
    public static InputStream copyStreamToString(InputStream in, StringBuilder buf)
            throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        streamCopy(in, bout);
        bout.close();
        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        buf.append(bout.toString(StandardCharsets.UTF_8));
        return bin;
    }

    public static void streamCopy(InputStream inputStream, OutputStream outputStream)
            throws IOException {
        streamCopy(inputStream, outputStream, BUFFER_SIZE);
    }

    public static void streamCopy(InputStream inputStream, OutputStream outputStream, int bufferSize)
            throws IOException {
        byte[] readBuffer = new byte[bufferSize];

        // Read bytes from the input stream in bufferSize chunks and write
        // them into the output stream.
        int read;
        while ((read = inputStream.read(readBuffer, 0, bufferSize)) != -1) {
            outputStream.write(readBuffer, 0, read);
        }
    }

    public static byte[] getStreamBytes(InputStream inputStream)
            throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(BUFFER_SIZE);
        streamCopy(inputStream, outputStream);

        // Convert the contents of the output stream into a byte array.
        byte[] byteData = outputStream.toByteArray();

        // Close the streams
        inputStream.close();
        outputStream.close();

        return byteData;
    }

    /**
     * Return the given InputStream contents as a String.
     * <p>
     * This method assumes that the InputStream contents is a text encoded in UTF-8.
     * </p>
     *
     * @param is
     *         The InputStream whose contents is converted to a String.
     * @return The String.
     * @throws IOException
     *         If there was a problem converting to a String.
     */
    public static String getStreamAsString(InputStream is)
            throws IOException {
        if (is == null) {
            return null;
        }
        InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
        return getReaderAsString(reader);
    }

    /**
     * Return the given Reader contents as a String.
     *
     * @param reader
     *         The Reader whose contents is converted to a String.
     * @return The String.
     * @throws IOException
     *         If there was a problem converting to a String.
     */
    public static String getReaderAsString(Reader reader)
            throws IOException {
        StringBuilder b = new StringBuilder();
        char[] buffer = new char[BUFFER_SIZE];
        int read;
        while ((read = reader.read(buffer)) != -1) {
            b.append(buffer, 0, read);
        }
        return b.toString();
    }

    /**
     * Return the given InputStream contents as a String.
     * If there was a problem converting to a String, <code>null</code> is returned.
     * <p>
     * This method assumes that the contents of the InputStream is a text encoded in UTF-8.
     * </p>
     *
     * @param is
     *         The InputStream whose contents is converted to a String.
     * @return The String or <code>null</code>.
     */
    public static String asString(InputStream is) {
        try {
            return getStreamAsString(is);
        } catch (IOException e) {
            LOG.warn("Exception converting InputStream to String.", e);
            return null;
        }
    }

    /**
     * Return the given Reader contents as a String.
     * If there was a problem converting to a String, <code>null</code> is returned.
     *
     * @param reader
     *         The Reader whose contents is converted to a String.
     * @return The String or <code>null</code>.
     */
    public static String asString(Reader reader) {
        try {
            return getReaderAsString(reader);
        } catch (IOException e) {
            LOG.warn("Exception converting Reader to String.", e);
            return null;
        }
    }
}
