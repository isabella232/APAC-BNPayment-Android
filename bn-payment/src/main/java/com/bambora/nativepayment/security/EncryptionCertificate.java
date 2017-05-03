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

package com.bambora.nativepayment.security;

import com.bambora.nativepayment.json.BNJsonObject;
import com.bambora.nativepayment.logging.BNLog;
import com.bambora.nativepayment.utils.CertificateUtils;
import com.bambora.nativepayment.utils.DateUtils;

import org.json.JSONObject;

import java.io.Serializable;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

/**
 * A class for storing a {@link Certificate} together with its fingerprint
 */
public class EncryptionCertificate implements Serializable {

    private static final String KEY_FINGERPRINT = "fingerprint";
    private static final String KEY_CERTIFICATE = "certificate";
    private static final String KEY_VALID_FROM = "validFrom";
    private static final String KEY_VALID_TO = "validTo";
    private static final String KEY_UPDATE_INTERVAL = "updateInterval";

    private String fingerprint;
    private X509Certificate[] certificateChain;
    private Date validFrom;
    private Date validTo;
    private int updateInterval;

    public EncryptionCertificate(JSONObject jsonObject) {
        BNJsonObject bnJsonObject = BNJsonObject.copyFrom(jsonObject);
        fingerprint = bnJsonObject.optString(KEY_FINGERPRINT);
        certificateChain = CertificateUtils.parseCertificateChain(bnJsonObject.optString(KEY_CERTIFICATE));
        validFrom = bnJsonObject.optDate(KEY_VALID_FROM);
        validTo = bnJsonObject.optDate(KEY_VALID_TO);
        updateInterval = bnJsonObject.optInt(KEY_UPDATE_INTERVAL);
    }

    public EncryptionCertificate(String fingerprint, X509Certificate[] certificateChain, Date validTo,
                                 int updateInterval) {
        this.fingerprint = fingerprint;
        this.certificateChain = certificateChain;
        this.validTo = validTo;
        this.updateInterval = updateInterval;
    }

    /**
     * Returns the fingerprint of the {@link Certificate}
     * @return The fingerprint as a {@link String}
     */
    public String getFingerprint() {
        return fingerprint.toUpperCase();
    }

    /**
     * Returns the {@link Certificate}
     * @return {@link Certificate}
     */
    public Certificate getCertificate() {
        if (certificateChain != null && certificateChain.length > 0) {
            return certificateChain[0];
        } else {
            return null;
        }
    }

    /**
     * Returns the {@link PublicKey} of the certificate
     * @return {@link PublicKey}
     */
    public PublicKey getPublicKey() {
        Certificate certificate = getCertificate();
        return certificate != null ? certificate.getPublicKey() : null;
    }

    public boolean isCloseToExpiry() {
        if (validTo != null) {
            Date now = new Date();
            return DateUtils.getDifferenceInDays(now, validTo) < updateInterval;
        }
        return true;
    }

    private boolean isTrusted(List<Certificate> masterCertificates, Certificate certificateToVerify) {
        for (Certificate masterCertificate : masterCertificates) {
            try {
                certificateToVerify.verify(masterCertificate.getPublicKey());
                return true;
            } catch (Exception e) {
                BNLog.w(getClass().getSimpleName(), "Certificate is not trusted.", e);
            }
        }
        return false;
    }

    public boolean isChainTrusted(List<Certificate> masterCertificates) {
        for (Certificate certificate : certificateChain) {
            if (isTrusted(masterCertificates, certificate)) {
                return true;
            }
        }
        return false;
    }
}
