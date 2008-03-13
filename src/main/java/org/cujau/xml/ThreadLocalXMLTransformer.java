package org.cujau.xml;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

/**
 * Utility for using XML Transformers in a thread safe way.
 * <p>
 * Typical usage is as follows:
 * 
 * <pre>
 * private static final ThreadLocalXMLTransformer TRANSFORMERS = new ThreadLocalXMLTransformer();
 * 
 * ...
 * 
 * try {
 *     // Get the Transformer instance for this thread.
 *     Transformer transformer = TRANSFORMERS.get();
 *     // Create a output stream to store the transformed result.
 *     StringWriter sw = new StringWriter();
 *     StreamResult out = new StreamResult( sw );
 *     // Transform the DOM document into xml.
 *     transformer.transform( new DOMSource( doc ), out );
 *     // Return the transformed document.
 *     return sw.toString();
 * } catch ( TransformerException e ) {
 *     // handle exception.
 * }
 * </pre>
 */
public class ThreadLocalXMLTransformer extends ThreadLocal<Transformer> {

    private final TransformerFactory factory;

    public ThreadLocalXMLTransformer() {
        factory = TransformerFactory.newInstance();
        // This is to work-around a bug in Java 5 JVMs which don't indent, but can throw an
        // IllegalArgumentException if the factory doesn't know how to handle it.
        try {
            factory.setAttribute( "indent-number", new Integer( 4 ) );
        } catch ( Exception e ) {
            // Ignore this! But document won't be nicely indented.
        }
    }

    @Override
    protected Transformer initialValue() {
        Transformer transformer;
        try {
            transformer = factory.newTransformer();
        } catch ( TransformerConfigurationException e ) {
            return null;
        }
        transformer.setOutputProperty( OutputKeys.METHOD, "xml" );
        transformer.setOutputProperty( OutputKeys.ENCODING, "UTF-8" );
        transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
        return transformer;
    }

}
