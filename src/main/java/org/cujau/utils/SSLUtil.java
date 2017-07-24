package org.cujau.utils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSLUtil {
    private static final Logger LOG = LoggerFactory.getLogger( SSLUtil.class );

    private static final TrustManager[] UNQUESTIONING_TRUST_MANAGER = new TrustManager[] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted( X509Certificate[] certs, String authType ) {
                }

                public void checkServerTrusted( X509Certificate[] certs, String authType ) {
                }
            } };

    public static void turnOffSslVerification() {
        try {
            // Install the all-trusting trust manager
            final SSLContext sc = SSLContext.getInstance( "SSL" );
            sc.init( null, UNQUESTIONING_TRUST_MANAGER, null );
            HttpsURLConnection.setDefaultSSLSocketFactory( sc.getSocketFactory() );
        } catch ( Exception e ) {
            LOG.warn( "Problem turning off SSL Verification.", e );
        }
    }

    public static TrustManager[] getUnquestioningTrustManagers() {
        return UNQUESTIONING_TRUST_MANAGER;
    }

    public static void turnOnSslVerification()
            throws KeyManagementException, NoSuchAlgorithmException {
        // Return it to the initial state (discovered by reflection, now hardcoded)
        SSLContext.getInstance( "SSL" ).init( null, null, null );
    }

    private SSLUtil() {
        throw new UnsupportedOperationException( "Do not instantiate libraries." );
    }
}
