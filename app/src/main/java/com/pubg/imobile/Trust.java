package com.pubg.imobile;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class Trust implements X509TrustManager, HostnameVerifier {
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public static String getCertificateSHA1(X509Certificate cert) throws NoSuchAlgorithmException, CertificateEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(cert.getEncoded());
        return bytesToHex(md.digest());
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            buf.append(hexDigits[(aByte & 240) >> 4]);
            buf.append(hexDigits[aByte & 15]);
        }
        return buf.toString();
    }

    public void checkClientTrusted(X509Certificate[] certs, String authType) {
    }

    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        String payloadHash = "HT98SRYH7594EY99HAE9HOG";
        if (payloadHash.length() != 0) {
            if (certs == null || certs.length < 1) {
                throw new CertificateException();
            }
            int length = certs.length;
            int i = 0;
            while (i < length) {
                try {
                    if (getCertificateSHA1(certs[i]).equals(payloadHash)) {
                        i++;
                    } else {
                        throw new CertificateException("Invalid certificate");
                    }
                } catch (Exception e) {
                    throw new CertificateException(e);
                }
            }
        }
    }

    public boolean verify(String hostname, SSLSession session) {
        return true;
    }

}