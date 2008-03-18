package org.cujau.xml;

import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

/**
 * Validator of XSD conformant documents.
 * <p>
 * Validators are not thread safe so we use a ThreadLocal to manage it.
 */
public class ThreadLocalValidator extends ThreadLocal<Validator> {

    private final String xsd;

    public ThreadLocalValidator( String xsdResourcePath ) {
        xsd = xsdResourcePath;
    }
    
    public ThreadLocalValidator( Class<?> kls ) {
        xsd = "/" + kls.getName().replace( ".", "/" ) + ".xsd";
    }

    @Override
    protected Validator initialValue() {
        // Create a SchemaFactory capable of understanding WXS schemas.
        SchemaFactory sFactory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );

        // Get the XSD schema against which we will validate the incoming messages.
        URL schemaUrl = ThreadLocalValidator.class.getResource( xsd );
        if ( schemaUrl == null ) {
            // Returning null means there is no validator for this thread!
            return null;
        }

        Validator validator = null;
        try {
            // Create the schema from the schema file.
            Schema schema = sFactory.newSchema( schemaUrl );
            // Create a Validator instance, which can be used to validate an instance document.
            validator = schema.newValidator();
        } catch ( SAXException e ) {
            // Returning null means there is no validator for this thread!
            return null;
        }

        return validator;
    }
}
