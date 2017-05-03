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

import com.bambora.nativepayment.logging.BNLog;

import org.json.JSONException;

/**
 * Model class for HTTP request errors.
 */
public class RequestError {

    private static final String LOG_TAG = "BNRequestError";

    /**
     * If a request fails due to an exception this field holds the exception.
     */
    private Throwable throwable;

    /**
     * If a request fails with an error response this field holds the response body.
     */
    private ErrorBody body;

    /**
     * If a request fails with an error response this field holds the raw response body.
     */
    private String rawBody;

    /**
     * Default constructor
     */
    public RequestError() {}

    public RequestError(Throwable throwable) {
        this.throwable = throwable;
    }

    /**
     * Returns the Throwable error of this request.
     *
     * @return A {@link Throwable} that was cast while executing a {@link Request}. Returns
     * null if the request received a response.
     */
    public Throwable getThrowable() {
        return throwable;
    }

    /**
     * Returns the error body of a {@link Request} if a response was received.
     *
     * @return An {@link ErrorBody}
     */
    public ErrorBody getBody() {
        return body;
    }

    /**
     * Takes a JSON formatted string and parses it to {@link ErrorBody}
     *
     * @param jsonBody  JSON formatted body of the sent {@link Request}
     */
    public void setBody(String jsonBody) {
        try {
            this.rawBody = jsonBody;
            this.body = ErrorBody.fromJson(jsonBody);
        } catch (JSONException e) {
            BNLog.e(LOG_TAG, "Failed to parse JSON object " +
                    ErrorBody.class.getSimpleName() + " from string." , e);
        }
    }

    /**
     * Creates a log friendly String of this request.
     *
     * @return A {@link String} representation of the class.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (body != null) {
            builder.append("Error body:\n");
            builder.append("\t\tError status: ").append(body.status).append("\n");
            if (body.title != null) {
                builder.append("\t\t").append(body.title).append("\n");
            }
            if (body.detail != null) {
                builder.append("\t\t").append(body.detail).append("\n");
            }
            if (body.type != null) {
                builder.append("\t\t").append(body.type).append("\n");
            }
        } else {
            builder
                .append("Raw error body:\n")
                .append(rawBody).append("\n");
        }
        if (throwable != null) {
            builder.append("Exception:");
            builder
                .append("\t\t").append(throwable.getMessage())
                .append(":\n").append(throwable.getClass());
        }
        return builder.toString();
    }
}
