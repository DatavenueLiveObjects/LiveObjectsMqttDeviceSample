package com.orange.mqttDeviceModePublishData;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class SSLUtils {
    private static TrustManager[] getTrustManagers(InputStream caCertStream) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        java.security.cert.Certificate ca = cf.generateCertificate(new BufferedInputStream(caCertStream));

        KeyStore ksTrust = KeyStore.getInstance(KeyStore.getDefaultType());
        ksTrust.load(null, null);
        ksTrust.setCertificateEntry("ca", ca);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ksTrust);

        return tmf.getTrustManagers();
    }

    private static SSLSocketFactory getSSLSocketFactory (InputStream caCertStream) throws MqttSecurityException {
        try{
            TrustManager[] tm = SSLUtils.getTrustManagers(caCertStream);

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tm, null);

            return ctx.getSocketFactory();
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException | KeyManagementException e) {
            throw new MqttSecurityException(e);
        }
    }

    public static SocketFactory getLiveObjectsSocketFactory() throws MqttSecurityException {
        // Get the certificate from the program resource and build a SSLSocketFactory
        try {
            try (InputStream is = DataPublisher.class.getResourceAsStream("/DigiCertSHA2SecureServerCA.crt")) {
                return getSSLSocketFactory(is);
            }
        } catch (IOException e) {
            throw new MqttSecurityException(MqttException.REASON_CODE_SSL_CONFIG_ERROR, e);
        }
    }
}
