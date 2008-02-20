package org.cujau.utils;

public class ReflectionException extends Exception {

    private static final long serialVersionUID = 1L;

    public ReflectionException( String message, Throwable t ) {
        super( message, t );
    }

    public ReflectionException( Throwable t ) {
        super( t );
    }
}
