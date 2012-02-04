package org.cujau.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.cujau.xml.ThreadLocalXMLTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XMLUtil {

    private static final Logger LOG = LoggerFactory.getLogger( XMLUtil.class );
    private static final ThreadLocalXMLTransformer TRANSFORMERS = new ThreadLocalXMLTransformer();

    public static Document resourceToDocument( String resourceName )
            throws ParserConfigurationException, SAXException, IOException {
        InputStream is = XMLUtil.class.getResourceAsStream( resourceName );
        DocumentBuilder builder = getNewDocumentBuilder( false );
        Document doc = builder.parse( is );
        return doc;
    }

    public static DocumentBuilder getNewDocumentBuilder( boolean isNamespaceAware )
            throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware( isNamespaceAware );
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db;
    }

    public static String toString( Node dom ) {
        try {
            // Get the Transformer instance for this thread.
            Transformer transformer = TRANSFORMERS.get();
            // Create a output stream to store the transformed result.
            StringWriter sw = new StringWriter();
            StreamResult out = new StreamResult( sw );
            // Transform the DOM document into xml.
            transformer.transform( new DOMSource( dom ), out );
            // Return the transformed document.
            return sw.toString();
        } catch ( TransformerException e ) {
            LOG.warn( "Could not transform DOM into XML String.", e );
            return null;
        }
    }
}
