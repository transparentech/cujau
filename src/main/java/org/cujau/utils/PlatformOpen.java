package org.cujau.utils;

// ///////////////////////////////////////////////////////
// Bare Bones Browser Launch //
// Version 1.5 (December 10, 2005) //
// By Dem Pilafian //
// Supports: Mac OS X, GNU/Linux, Unix, Windows XP //
// Example Usage: //
// String url = "http://www.centerkey.com/"; //
// BareBonesBrowserLaunch.openURL(url); //
// Public Domain Software -- Free to Use as You Like //
// ///////////////////////////////////////////////////////

import java.lang.reflect.Method;
import java.net.URI;

public class PlatformOpen {

    /**
     * Bare Bones Browser Launch
     * 
     * Version 1.5 (December 10, 2005)
     * 
     * By Dem Pilafian
     * 
     * Supports: Mac OS X, GNU/Linux, Unix, Windows XP
     * 
     * @param url
     */
    public static void openURL( String url ) {
        String osName = System.getProperty( "os.name" );
        try {
            if ( osName.startsWith( "Mac OS" ) ) {
                Class<?> fileMgr = Class.forName( "com.apple.eio.FileManager" );
                Method openURL = fileMgr.getDeclaredMethod( "openURL", new Class[] { String.class } );
                openURL.invoke( null, new Object[] { url } );
            } else if ( osName.startsWith( "Windows" ) ) {
                Runtime.getRuntime().exec( "rundll32 url.dll,FileProtocolHandler " + url );
            } else { // assume Unix or Linux
                String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
                String browser = null;
                for ( int count = 0; ( count < browsers.length ) && ( browser == null ); count++ ) {
                    if ( Runtime.getRuntime().exec( new String[] { "which", browsers[count] } ).waitFor() == 0 ) {
                        browser = browsers[count];
                    }
                }
                if ( browser == null ) {
                    throw new IllegalStateException( "Could not find web browser on '" + osName + "'" );
                } else {
                    Runtime.getRuntime().exec( new String[] { browser, url } );
                }
            }
        } catch ( Exception e ) {
            throw new IllegalStateException( "Unable to open external browser for url: " + url );
        }
    }

    /**
     * Open a string URL
     * 
     * @param surl
     *            URL to open (as String)
     * @param parent
     *            parent component for error message dialog
     */
    public static void open( String surl ) {
        // Try java desktop API first (new in Java 1.6)
        // basically: java.awt.Desktop.getDesktop().browse(new URI(url));
        try {
            Class<?> desktop = Class.forName( "java.awt.Desktop" );
            Method getDesktop = desktop.getDeclaredMethod( "getDesktop", new Class[] {} );
            Object desktopInstance = getDesktop.invoke( null, new Object[] {} );
            Method browse = desktop.getDeclaredMethod( "browse", new Class[] { URI.class } );
            URI uri = new URI( surl );
            // logger.fine("Using Java Desktop API to open URL '"+url+"'");
            browse.invoke( desktopInstance, new Object[] { uri } );
            return;
        } catch ( Exception e ) {
        }

        // Failed, resort to executing the browser manually
        String osName = System.getProperty( "os.name" );
        try {
            String[] cmd = null;
            
            // Mac OS has special Java class
            if ( osName.startsWith( "Mac OS" ) ) {
//                Class<?> fileMgr = Class.forName( "com.apple.eio.FileManager" );
//                Method openURL = fileMgr.getDeclaredMethod( "openURL", new Class[] { String.class } );
//                // logger.fine("Using "+fileMgr+" to open URL '"+url+"'");
//                openURL.invoke( null, new Object[] { surl } );
                cmd = new String[] { "open", surl };
//                return;
            } else 


            // Windows execs url.dll
            if ( osName.startsWith( "Windows" ) ) {
                cmd = new String[] { "rundll32", "url.dll,FileProtocolHandler", surl };

                // else assume unix/linux: call one of the available browsers
            } else {
                String[] browsers = {
                // Freedesktop, http://portland.freedesktop.org/xdg-utils-1.0/xdg-open.html
                                     "xdg-open",
                                     // Debian
                                     "sensible-browser",
                                     // Otherwise call browsers directly
                                     "firefox",
                                     "opera",
                                     "konqueror",
                                     "epiphany",
                                     "mozilla",
                                     "netscape" };
                String browser = null;
                for ( int count = 0; ( count < browsers.length ) && ( browser == null ); count++ ) {
                    if ( Runtime.getRuntime().exec( new String[] { "which", browsers[count] } ).waitFor() == 0 ) {
                        browser = browsers[count];
                    }
                }

                if ( browser == null ) {
                    // logger.warning("No web browser found");
                    throw new Exception( "Could not find web browser" );
                }

                cmd = new String[] { browser, surl };
            }

            if ( Runtime.getRuntime().exec( cmd ).waitFor() != 0 ) {
                throw new Exception( "Error opening page: " + surl );
            }
        } catch ( Exception e ) {
            throw new RuntimeException( "Problem opening: " + surl, e );
        }
    }

}
