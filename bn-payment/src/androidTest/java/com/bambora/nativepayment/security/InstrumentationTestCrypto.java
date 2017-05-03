package com.bambora.nativepayment.security;

import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import com.bambora.nativepayment.utils.CertificateUtils;

import org.junit.Assert;
import org.junit.runner.RunWith;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

/**
 * TODO
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentationTestCrypto extends InstrumentationTestCase {

    private Crypto crypto = new Crypto();

    public void testRSAEncryptShouldHavePKCS1Padding() throws Exception {
        KeyPair rsaKeyPair = InstrumentationTestData.getKeyPairFromRSAKeystore(getInstrumentation().getContext());
        PrivateKey rsaPrivateKey = rsaKeyPair.getPrivate();
        PublicKey rsaPublicKey = CertificateUtils.parseCertificate(InstrumentationTestData.getCertificateFromString()).getPublicKey();
        String stringToEncrypt = "String to encrypt";
        byte[] encrypted = crypto.RSAEncrypt(toBytes(stringToEncrypt), rsaPublicKey);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
        byte[] decrypted = cipher.doFinal(encrypted);

        String decryptedString = new String(decrypted, Crypto.UTF8_CHARSET);
        Assert.assertEquals(stringToEncrypt, decryptedString);
    }

    private byte[] toBytes(String string) throws UnsupportedEncodingException {
        return string.getBytes(Crypto.UTF8_CHARSET);
    }
}
