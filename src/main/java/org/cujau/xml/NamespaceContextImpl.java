package org.cujau.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

/**
 * Map-based NamespaceContext implementation.
 */
public class NamespaceContextImpl implements NamespaceContext {
    private Map<String, String> prefixToURI = new HashMap<String, String>();
    private Map<String, String> uriToPrefix = new HashMap<String, String>();

    @Override
    public String getNamespaceURI( String prefix ) {
        return prefixToURI.get( prefix );
    }

    @Override
    public String getPrefix( String namespaceURI ) {
        return uriToPrefix.get( namespaceURI );
    }

    @Override
    public Iterator<String> getPrefixes( String namespaceURI ) {
        return prefixToURI.keySet().iterator();
    }

    public void addNamespace( String prefix, String uri ) {
        prefixToURI.put( prefix, uri );
        uriToPrefix.put( uri, prefix );
    }

}
