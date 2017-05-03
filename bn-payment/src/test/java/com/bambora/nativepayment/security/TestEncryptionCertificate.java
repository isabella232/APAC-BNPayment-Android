package com.bambora.nativepayment.security;

import com.bambora.nativepayment.utils.DateUtils;

import org.junit.Assert;
import org.junit.Test;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO
 */
public class TestEncryptionCertificate {

    private List<Certificate> masterCertificates;

    public TestEncryptionCertificate() throws CertificateException {
        masterCertificates = new ArrayList<>();
        masterCertificates.add(TestData.getValidMasterCertificate(getClass().getClassLoader()));
    }

    @Test
    public void certificateShouldBeTrusted() throws CertificateException {
        Certificate certificate = TestData.getValidEncryptionCertificate(getClass().getClassLoader());
        X509Certificate[] chain = {(X509Certificate) certificate};
        EncryptionCertificate encryptionCertificate = new EncryptionCertificate(null, chain, null, 0);
        Assert.assertTrue(encryptionCertificate.isChainTrusted(masterCertificates));
    }

    @Test
    public void certificateShouldNotBeTrusted() throws CertificateException {
        Certificate certificate = TestData.getInvalidEncryptionCertificate(getClass().getClassLoader());
        X509Certificate[] chain = {(X509Certificate) certificate};
        EncryptionCertificate encryptionCertificate = new EncryptionCertificate(null, chain, null, 0);
        Assert.assertFalse(encryptionCertificate.isChainTrusted(masterCertificates));
    }

    @Test
    public void certificateShouldNotHaveExpired() throws CertificateException {
        Date now = new Date();
        Date inTenDays = DateUtils.addDays(now, 10);
        int updateInterval = 5;
        Certificate certificate = TestData.getValidEncryptionCertificate(getClass().getClassLoader());
        X509Certificate[] chain = {(X509Certificate) certificate};
        EncryptionCertificate encryptionCertificate = new EncryptionCertificate(null, chain, inTenDays, updateInterval);
        Assert.assertFalse(encryptionCertificate.isCloseToExpiry());
    }

    @Test
    public void certificateShouldHaveExpired() throws CertificateException {
        Date now = new Date();
        Date yesterday = DateUtils.addDays(now, -1);
        int updateInterval = 0;
        Certificate certificate = TestData.getValidEncryptionCertificate(getClass().getClassLoader());
        X509Certificate[] chain = {(X509Certificate) certificate};
        EncryptionCertificate encryptionCertificate = new EncryptionCertificate(null, chain, yesterday, updateInterval);
        Assert.assertTrue(encryptionCertificate.isCloseToExpiry());
    }

    @Test
    public void certificateShouldHaveAlmostExpired() throws CertificateException {
        Date now = new Date();
        Date inFiveDays = DateUtils.addDays(now, 5);
        int updateInterval = 6;
        Certificate certificate = TestData.getValidEncryptionCertificate(getClass().getClassLoader());
        X509Certificate[] chain = {(X509Certificate) certificate};
        EncryptionCertificate encryptionCertificate = new EncryptionCertificate(null, chain, inFiveDays, updateInterval);
        Assert.assertTrue(encryptionCertificate.isCloseToExpiry());
    }

    @Test
    public void certificateChainShouldBeTrusted() throws Exception {
        X509Certificate[] certificateChain = TestData.getValidCertificateChain(getClass().getClassLoader());
        EncryptionCertificate encryptionCertificate = new EncryptionCertificate(null, certificateChain, null, 0);
        Assert.assertTrue(encryptionCertificate.isChainTrusted(masterCertificates));
    }
}
