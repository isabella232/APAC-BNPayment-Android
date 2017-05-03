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

import com.bambora.nativepayment.interfaces.IJsonResponse;
import com.bambora.nativepayment.json.JsonContainer;
import com.bambora.nativepayment.logging.BNLog;

/**
 * Model of a network request response.
 */
public class Response<T extends IJsonResponse<T>> {

    private static final String LOG_TAG = "BNNetworkResponse";

    /**
     * HTTP header of the response
     */
    public HttpHeader header;

    /**
     * Error object
     */
    public RequestError error;

    /**
     * Response HTTP status code
     */
    private int responseCode;

    /**
     * JSON formatted response body of the request
     */
    private String rawBody;

    /**
     * Deserialized response body object
     */
    private T body;

    /**
     * Takes and sets a JSON formatted {@link String} and deserializes it with the given class as
     * model.
     *
     * @param json
     * @param bodyClass
     */
    public void setBody(String json, Class<T> bodyClass) {
        this.rawBody = json;
        this.body = parseBody(json, bodyClass);
    }

    /**
     * Returns the deserialized response body
     *
     * @return Deserialized body object
     */
    public T getBody() {
        return this.body;
    }

    /**
     * Sets the response status code
     *
     * @param responseCode  An HTTP status code
     */
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * Returns the response body as a JSON formatted {@link String}
     *
     * @return The body as A JSON string
     */
    public String getRawBody() {
        return rawBody;
    }

    /**
     * Creates a log friendly String of this request.
     *
     * @return A {@link String} representation of the class.
     */
    @Override
    public String toString() {
        final int maxBodySize = 500;
        StringBuilder builder = new StringBuilder()
                .append("Response code: ")
                .append(responseCode).append("\n\n");

        if (error != null) {
            builder.append(error.toString());
        }
        if (header != null) {
            builder
                .append("Response header:\n")
                .append(header.toString()).append("\n");
        }
        if (rawBody != null && rawBody.length() > 0) {
            String truncatedBody = rawBody.length() > maxBodySize ? rawBody.substring(0, maxBodySize) : rawBody;
            builder
                .append("Response body: (first " + maxBodySize + " characters)\n")
                .append(truncatedBody).append("\n");
        }
        return builder.toString();
    }

    private T parseBody(String json, Class<T> bodyClass) {
        try {
            T instance = bodyClass.getConstructor().newInstance();
            return instance.fromJson(new JsonContainer(json));
        } catch (Exception exception) {
            BNLog.e(LOG_TAG, "Failed to parse JSON object " +
                    bodyClass.getSimpleName() + " from string.", exception);
            return null;
        }
    }
}
