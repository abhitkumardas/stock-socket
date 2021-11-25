package in.stockpe.stocksocket.truedata.utils;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.*;
import java.security.cert.X509Certificate;

public class NaiveSSLContext {
    private NaiveSSLContext() {
    }

    public static SSLContext getInstance(String protocol) throws NoSuchAlgorithmException {
        return init(SSLContext.getInstance(protocol));
    }

    public static SSLContext getInstance(String protocol, Provider provider) throws NoSuchAlgorithmException {
        return init(SSLContext.getInstance(protocol, provider));
    }

    public static SSLContext getInstance(String protocol, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
        return init(SSLContext.getInstance(protocol, provider));
    }

    private static SSLContext init(SSLContext context) {
        try {
            context.init((KeyManager[])null, new TrustManager[]{new NaiveSSLContext.NaiveTrustManager()}, (SecureRandom)null);
            return context;
        } catch (KeyManagementException var2) {
            throw new RuntimeException("Failed to initialize an SSLContext.", var2);
        }
    }

    private static class NaiveTrustManager implements X509TrustManager {
        private NaiveTrustManager() {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }
}
