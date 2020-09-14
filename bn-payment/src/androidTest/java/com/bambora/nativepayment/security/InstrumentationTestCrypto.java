package com.bambora.nativepayment.security;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.bambora.nativepayment.utils.CertificateUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

@RunWith(AndroidJUnit4.class)
public class InstrumentationTestCrypto {

    private Crypto crypto = new Crypto();

    @Test
    public void testRSAEncryptShouldHavePKCS1Padding() throws Exception {
        KeyPair rsaKeyPair = InstrumentationTestData.getKeyPairFromRSAKeystore(InstrumentationRegistry.getInstrumentation().getContext());
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
