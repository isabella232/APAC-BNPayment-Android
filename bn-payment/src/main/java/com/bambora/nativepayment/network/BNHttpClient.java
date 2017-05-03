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

package com.bambora.nativepayment.network;

import android.content.Context;

/**
 * {@link BNHttpClient} is used for customizing requests to the Bambora API. The main purpose
 * is adding a merchant account or an API token to the request. This is required in order to
 * communicate with the back-end.
 */
public class BNHttpClient extends HttpClient {

    private static final int API_VERSION = 2;

    /**
     * The API token used by this client.
     */
    private String mApiToken;

    /**
     * The Merchant account used by this client.
     */
    private String mMerchantAccount;

    /**
     * A constructor
     *
     * @param context
     * @param apiToken
     * @deprecated Since 1.4.0
     */
    @Deprecated
    public BNHttpClient(Context context, final String apiToken) {
        this.mApiToken = apiToken != null ? apiToken : "";
    }

    /**
     * A constructor with a merchant account
     *
     * @since 1.4.0
     *
     * @param merchantAccount The merchant account to use, e.g. "T012345678"
     */
    public BNHttpClient(String merchantAccount) {
        this.mMerchantAccount = merchantAccount;
    }

    /**
     * Adds headers for all requests sent using this client.
     *
     * @param request A {@link Request} object
     */
    @Override
    public void addDefaultHeaders(Request request) {
        request
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Api-Version", String.valueOf(API_VERSION));
        if (mApiToken != null && !"".equals(mApiToken)) {
            request.header("Api-Token", mApiToken);
        }
        if (mMerchantAccount != null && !"".equals(mMerchantAccount)) {
            request.header("Merchant-Account", mMerchantAccount);
        }
    }
}
