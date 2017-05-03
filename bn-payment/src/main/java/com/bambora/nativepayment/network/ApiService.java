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

/**
 * Abstract class for services handling network requests.
 */
public abstract class ApiService {

    /**
     * Base URL to host that the service is to communicate with.
     */
    private String baseUrl;

    /**
     * An {@link HttpClient} to be used for customising all requests sent from the service.
     */
    private HttpClient client;

    /**
     * Initiates the service by setting base URL and client.
     *
     * @param baseUrl   Host base URL
     * @param client    {@link HttpClient} for customising requests
     */
    public void initiate(String baseUrl, HttpClient client) {
        this.baseUrl = baseUrl;
        this.client = client;
    }

    protected String getBaseUrl() {
        return baseUrl;
    }

    protected HttpClient getClient() {
        return client;
    }

    protected String createUrl(String endpoint) {
        return baseUrl + endpoint;
    }
}
