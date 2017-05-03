/*
 * Copyright (c) 2016 Bambora ( http://bambora.com/ )
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.bambora.nativepayment.utils;

import com.bambora.nativepayment.logging.BNLog;
import com.bambora.nativepayment.security.EncryptionCertificate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;

/**
 * TODO
 */
public class CertificateUtils {

    public static final String CERT_TYPE_X509 = "X.509";
    public static final String CERT_TYPE_PKCS7 = "PKCS7";
    public static final String UTF8_ENCODING = "UTF-8";

    public static Certificate loadCertificateFromResources(ClassLoader classLoader, String path) throws CertificateException {
        InputStream inputStream = classLoader.getResourceAsStream(path);
        CertificateFactory certificateFactory = CertificateFactory.getInstance(CERT_TYPE_X509);
        return certificateFactory.generateCertificate(inputStream);
    }

    public static X509Certificate[] loadCertChainFromResources(ClassLoader classLoader, String path) throws CertificateException {
        InputStream inputStream = classLoader.getResourceAsStream(path);
        CertificateFactory certificateFactory = CertificateFactory.getInstance(CERT_TYPE_X509);
        Collection<? extends Certificate> certs = certificateFactory.generateCertificates(inputStream);
        return toX509CertificateArray(certs);
    }

    public static Certificate parseCertificate(String certificateString) {
        InputStream inputStream = null;
        Certificate certificate = null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance(CERT_TYPE_X509);
            inputStream = new ByteArrayInputStream(certificateString.getBytes(UTF8_ENCODING));
            certificate = certificateFactory.generateCertificate(inputStream);
        } catch (CertificateException | UnsupportedEncodingException e) {
            BNLog.e(EncryptionCertificate.class.getSimpleName(), "Failed to parse encryption certificate.", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                BNLog.e(EncryptionCertificate.class.getSimpleName(), "Failed to close InputStream.", e);
            }
        }
        return certificate;
    }

    public static X509Certificate[] parseCertificateChain(String certificateString) {
        InputStream inputStream = null;
        Collection<? extends Certificate> certificates = null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance(CERT_TYPE_X509);
            inputStream = new ByteArrayInputStream(certificateString.getBytes(UTF8_ENCODING));
            certificates = certificateFactory.generateCertificates(inputStream);
        } catch (CertificateException | UnsupportedEncodingException e) {
            BNLog.e(EncryptionCertificate.class.getSimpleName(), "Failed to parse encryption certificate.", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                BNLog.e(EncryptionCertificate.class.getSimpleName(), "Failed to close InputStream.", e);
            }
        }
        return toX509CertificateArray(certificates);
    }

    private static X509Certificate[] toX509CertificateArray(Collection<? extends Certificate> collection) {
        if (collection == null) {
            return new X509Certificate[0];
        }
        X509Certificate[] x509Certificates = new X509Certificate[collection.size()];
        Iterator iterator = collection.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Certificate cert = (Certificate) iterator.next();
            if (cert instanceof X509Certificate) {
                x509Certificates[i] = (X509Certificate) cert;
                i++;
            }
        }
        return x509Certificates;
    }
}
