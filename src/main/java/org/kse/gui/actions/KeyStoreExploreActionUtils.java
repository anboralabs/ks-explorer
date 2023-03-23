package org.kse.gui.actions;

import org.apache.commons.io.FileUtils;
import org.kse.crypto.x509.X509CertUtil;

import java.io.File;
import java.io.IOException;
import java.security.cert.X509Certificate;

public class KeyStoreExploreActionUtils {

    private KeyStoreExploreActionUtils() {}

    public static X509Certificate[] openCertificate(File certificateFile) {
        try {
            return openCertificate(FileUtils.readFileToByteArray(certificateFile), certificateFile.getName());
        } catch (IOException ex) {
            return new X509Certificate[0];
        }
    }

    protected static X509Certificate[] openCertificate(byte[] data, String name) {
        try {
            return X509CertUtil.loadCertificates(data);
        } catch (Exception ex) {
            return new X509Certificate[0];
        }
    }
}
