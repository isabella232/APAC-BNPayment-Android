package com.bambora.nativepayment.security;

import com.bambora.nativepayment.utils.CertificateUtils;

import org.junit.Assert;
import org.junit.Test;

import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Arrays;

/**
 * TODO
 */
public class TestCertificateManager {

    @Test
    public void parsedCertificateStringShouldMatchCertfile() throws Exception {
        // Parse a certificate string using CertificateManager
        Certificate certificate = CertificateUtils.parseCertificate(TestData.getCertificateFromString());
        Assert.assertNotNull(certificate);

        // Verify that the parsed cert is equal to the cert fetched from the test key store.
        PublicKey publicKeyFromString = certificate.getPublicKey();
        PublicKey publicKeyFromKeyStore = TestData.getKeyPairFromRSAKeystore(getClass().getClassLoader()).getPublic();
        Assert.assertTrue(Arrays.equals(publicKeyFromString.getEncoded(), publicKeyFromKeyStore.getEncoded()));
    }
}
