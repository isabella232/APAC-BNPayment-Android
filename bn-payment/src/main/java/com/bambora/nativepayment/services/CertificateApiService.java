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

package com.bambora.nativepayment.services;

import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.interfaces.ICertificateLoadCallback;
import com.bambora.nativepayment.models.CertificatesResponse;
import com.bambora.nativepayment.network.ApiService;
import com.bambora.nativepayment.network.Callback;
import com.bambora.nativepayment.network.Request;
import com.bambora.nativepayment.network.RequestError;
import com.bambora.nativepayment.network.Response;
import com.bambora.nativepayment.security.EncryptionCertificate;

import java.util.List;

import static com.bambora.nativepayment.network.RequestMethod.GET;

/**
 * TODO
 */
public class CertificateApiService extends ApiService {

    /**
     * Creates a request for fetching encryption certificates from Bambora Native payments API.
     * @return A {@link Request} object
     */
    public Request<CertificatesResponse> getEncryptionCertificates() {
        return new Request<>(this, CertificatesResponse.class)
                .endpoint("certificates/")
                .method(GET);
    }

    private static CertificateApiService createService() {
        return BNPaymentHandler.getInstance().createService(CertificateApiService.class);
    }

    public interface ICertificateCallback {
        void onCertificatesLoaded(List<EncryptionCertificate> certificates);
    }

    public static class CertificateService {

        public static void getCertificates(final ICertificateLoadCallback callback) {
            Request<CertificatesResponse> request = createService().getEncryptionCertificates();
            request.execute(new Callback<CertificatesResponse>() {
                    @Override
                    public void onSuccess(Response<CertificatesResponse> response) {
                        CertificatesResponse certificatesResponse = response.getBody();
                        if (callback != null) {
                            callback.onCertificatesLoaded(certificatesResponse.getEncryptionCertificates());
                        }
                    }

                    @Override
                    public void onError(RequestError error) {
                        if (callback != null) {
                            callback.onError();
                        }
                    }
                });
        }
    }
}
