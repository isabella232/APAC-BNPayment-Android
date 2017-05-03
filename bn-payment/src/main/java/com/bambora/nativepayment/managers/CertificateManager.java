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

package com.bambora.nativepayment.managers;

import android.content.Context;

import com.bambora.nativepayment.interfaces.ICertificateLoadCallback;
import com.bambora.nativepayment.security.EncryptionCertificate;
import com.bambora.nativepayment.security.MasterCertificates;
import com.bambora.nativepayment.services.CertificateApiService.CertificateService;
import com.bambora.nativepayment.services.CertificateApiService.ICertificateCallback;
import com.bambora.nativepayment.storage.FileStorage;
import com.bambora.nativepayment.storage.TransactionFileStorage;

import java.io.Serializable;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO
 */
public class CertificateManager {

    private static CertificateManager instance = new CertificateManager();

    private TransactionFileStorage fileStorage = new TransactionFileStorage();
    private List<EncryptionCertificate> encryptionCertificates;

    public static CertificateManager getInstance() {
        return instance;
    }

    private CertificateManager() {}

    public void getEncryptionCertificates(final Context context, final ICertificateLoadCallback callback) {
        getStoredCertificates(context, new ICertificateCallback() {
            @Override
            public void onCertificatesLoaded(List<EncryptionCertificate> certificates) {
                if (certificates == null || certificates.isEmpty() || closeToExpiry(certificates)) {
                    downloadCertificates(context, callback);
                } else {
                    encryptionCertificates = certificates;
                    if (callback != null) {
                        callback.onCertificatesLoaded(encryptionCertificates);
                    }
                }
            }
        });
    }

    public List<Certificate> getMasterCertificates() {
        return MasterCertificates.getMasterCertificates();
    }

    private boolean closeToExpiry(List<EncryptionCertificate> certificates) {
        if (certificates == null || certificates.isEmpty()) {
            return true;
        }
        for (EncryptionCertificate certificate : certificates) {
            if (certificate.isCloseToExpiry()) {
                return true;
            }
        }
        return false;
    }

    private void downloadCertificates(final Context context, final ICertificateLoadCallback callback) {
        CertificateService.getCertificates(new ICertificateLoadCallback() {
            @Override
            public void onCertificatesLoaded(List<EncryptionCertificate> certificates) {
                encryptionCertificates = getTrustedCertificates(certificates);
                storeCertificates(context, encryptionCertificates);
                if (callback != null) {
                    if (!encryptionCertificates.isEmpty()) {
                        callback.onCertificatesLoaded(encryptionCertificates);
                    } else {
                        callback.onError();
                    }
                }
            }

            @Override
            public void onError() {
                if (callback != null) {
                    callback.onError();
                }
            }
        });
    }

    private void getStoredCertificates(Context context, final ICertificateCallback callback) {
        fileStorage.getEncryptionCertificates(context, new FileStorage.IOnObjectRead() {
            @Override
            public void notifyOnObjectRead(Serializable object) {
                EncryptionCertificate[] certs = object instanceof EncryptionCertificate[] ?
                        (EncryptionCertificate[]) object : new EncryptionCertificate[0];
                callback.onCertificatesLoaded(new ArrayList<>(Arrays.asList(certs)));
            }
        });
    }

    private List<EncryptionCertificate> getTrustedCertificates(List<EncryptionCertificate> certificatesToValidate) {
        List<EncryptionCertificate> validCerts = new ArrayList<>();
        for (EncryptionCertificate certificate : certificatesToValidate) {
            if (certificate.isChainTrusted(getMasterCertificates())) {
                validCerts.add(certificate);
            }
        }
        return validCerts;
    }

    private void storeCertificates(Context context, List<EncryptionCertificate> certificates) {
        fileStorage = new TransactionFileStorage();
        EncryptionCertificate[] certificateArray = new EncryptionCertificate[certificates.size()];
        certificates.toArray(certificateArray);
        fileStorage.saveEncryptionCertificates(context, certificateArray, null);
    }
}
