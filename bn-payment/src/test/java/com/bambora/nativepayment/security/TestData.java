package com.bambora.nativepayment.security;

import com.bambora.nativepayment.utils.CertificateUtils;

import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * TODO
 */
public class TestData {

    private static final String KEY_STORE_TYPE =        "PKCS12";
    private static final String KEY_STORE_PASSWORD =    "1234";
    private static final String KEY_ALIAS =             "1";
    private static final String KEY_PASSWORD =          "1234";

    private static final String PATH_ROOT_CA_CERT =             "rootCACert.pem";
    private static final String PATH_ROOT_CA_SIGNED_CERT =      "rootCASignedCert.pem";
    private static final String PATH_VALID_SIGNED_CHAIN =       "validSignedCertChain.pem";
    private static final String PATH_INVALID_CERTIFICATE =      "invalidCert.cer";

    private static final String PATH_TEST_PRIVATE_KEY = "TestPKCS.p12";
    private static final String PUBLIC_TEST_CERT_AS_STRING =
                    "-----BEGIN CERTIFICATE-----\n" +
                    "MIIFYjCCA0oCCQDgdWrZfzM9JzANBgkqhkiG9w0BAQUFADBzMQswCQYDVQQGEwJT\n" +
                    "RTETMBEGA1UECBMKU29tZS1TdGF0ZTESMBAGA1UEBxMJU3RvY2tob2xtMRAwDgYD\n" +
                    "VQQKEwdCYW1ib3JhMRQwEgYDVQQLEwtEZXZlbG9wbWVudDETMBEGA1UEAxMKaXJv\n" +
                    "bnBvb2RsZTAeFw0xNjA0MTgxMjE3MjZaFw0yNjA0MTYxMjE3MjZaMHMxCzAJBgNV\n" +
                    "BAYTAlNFMRMwEQYDVQQIEwpTb21lLVN0YXRlMRIwEAYDVQQHEwlTdG9ja2hvbG0x\n" +
                    "EDAOBgNVBAoTB0JhbWJvcmExFDASBgNVBAsTC0RldmVsb3BtZW50MRMwEQYDVQQD\n" +
                    "Ewppcm9ucG9vZGxlMIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA5m9C\n" +
                    "PilTJ/knAp1S+51BZ+vFc5uriZi9L7gpzDwMoKt5SV0c4JlpgV0+IFQwOfL1BQaS\n" +
                    "AKqr/Z5gQG0AcLMA6SGMoNUvt4SsYqdP0zFquH5eTUA8/HoPBt5WC2jegLJJqyGE\n" +
                    "2o2b3EgNHemZapndJNE7Pmt8oNvdPAPDDn9SGSNKRyAcV9rKnJjZ0qK9vPZIps86\n" +
                    "DHtN2QKcs+NF4IOltrauSrUimFRxxOEoQm7FnmPeGkYwOjFVUwSmZVVaXLIU9/sQ\n" +
                    "EAahDH9XgsEBa4v6nImLUyFR1eBJyKj36XNdQhBlHRr+w1XPCzBiA31RKQ8OjB+9\n" +
                    "ItwwhngrkrxYlUZ6M8ZhoqOzDAO4Mb5Y34C1PQdO+7jkmH1C4v5zf2t7iEmt6IVI\n" +
                    "pKOJStvmyZc5ieqgWjQ7KmfqVOzHeKvyyrkr1FEvy2/MUFe0mr9rvQM4oP1OYG9n\n" +
                    "VBOjT2ScU+8Fn9N7YxcrsMWqDax7c7aUYo2lD4llOcWSl0uytz1IhNBaCVc7gbgp\n" +
                    "wuAf++51wtXf04Wy8FV6cL3q9pQnenmZUyIDQrWtV3eQ8OOgiaYcwxIrbrDmYdz/\n" +
                    "MhgqvCwfHyJ/CMdHEzJI+9wUpfOplYHo2yk+k3etWK3mc5Xb5KriJ8aM2w0YH8jr\n" +
                    "CpoJrChQId+4zUo7SbZtq73LlRTEymPPZo1aBvMCAwEAATANBgkqhkiG9w0BAQUF\n" +
                    "AAOCAgEAOgl0GBX53QmoMIVmTXz5HiJezFFRbOewt9bpY1fn3BHERJKuE7uaRj3X\n" +
                    "/cGZMyA97DvE8RJTKB+tI3g5EgT8zSfCoV+h/OpRueBwSpHqjHp4IMrP93v+1FeF\n" +
                    "qGfaYR1v+64H1m2isnbmzr0sGbviKFRXRJQgW9uSoXc+XuoRHbpvmjAS+YLZcVyx\n" +
                    "UjBk6cOGq1MpdyroLk4XHCRnaMVSWows/vp30CLShm/JNi143t78eKlvWPRhFqM3\n" +
                    "b0LiwPIzwl1VRfZM3wQBDDkgc3DqdoIe+1p3zvmZcecWDurKSeuY38Nmbwpg1yU/\n" +
                    "o1KaYWL2o/yMPkdIIrLofPywHoNxoqeAudd1rjNsl72Tx+2Z0jmukuFw9/oObnp3\n" +
                    "8R9qOuVnpcD88gK5isT2HZBDTQ6kQ2EKhHfao1xFb+B3undEhg1Gg870kuMHfAai\n" +
                    "lHW6VOjQOqPXOzQDtjN9DDlT6N6WGCPkNZKMLbd4mjvi1Zr7piuIhskIvfqwF9v5\n" +
                    "SRvs9QSKFQSyK5dZwOHEBsuwA2zstmF91WYiAvebTnxxjYmJhxsqTokcCW+olCzD\n" +
                    "Q+dXm7kw1lSEkUsg5Llj7XC2EwsIXa72vhlfIcQI6Zm2oGidH1CqzdtQaPdi5qQt\n" +
                    "jCgvIXX2q+5rAsyX9R0AgpWzk9yyACL5INq7AvCLIBrxCsF2HOc=\n" +
                    "-----END CERTIFICATE-----\n";

    public static KeyPair getKeyPairFromRSAKeystore(ClassLoader classLoader) throws Exception {
        KeyStore keyStore = loadTestKeyStore(classLoader);
        PublicKey publicKey = keyStore.getCertificate(KEY_ALIAS).getPublicKey();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS, KEY_PASSWORD.toCharArray());
        return new KeyPair(publicKey, privateKey);
    }

    public static String getCertificateFromString() {
        return PUBLIC_TEST_CERT_AS_STRING;
    }

    public static Certificate getValidEncryptionCertificate(ClassLoader classLoader) throws CertificateException {
        return CertificateUtils.loadCertificateFromResources(classLoader, PATH_ROOT_CA_SIGNED_CERT);
    }

    public static Certificate getInvalidEncryptionCertificate(ClassLoader classLoader) throws CertificateException {
        return CertificateUtils.loadCertificateFromResources(classLoader, PATH_INVALID_CERTIFICATE);
    }

    public static X509Certificate[] getValidCertificateChain(ClassLoader classLoader) throws CertificateException {
        X509Certificate[] x509Certificates = CertificateUtils.loadCertChainFromResources(classLoader, PATH_VALID_SIGNED_CHAIN);
        return x509Certificates;
    }

    public static Certificate getValidMasterCertificate(ClassLoader classLoader) throws CertificateException {
        return CertificateUtils.loadCertificateFromResources(classLoader, PATH_ROOT_CA_CERT);
    }


    private static KeyStore loadTestKeyStore(ClassLoader classLoader) throws Exception {
        InputStream inputStream = classLoader.getResourceAsStream(PATH_TEST_PRIVATE_KEY);
        KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
        keyStore.load(inputStream, KEY_STORE_PASSWORD.toCharArray());
        inputStream.close();
        return keyStore;
    }
}
