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
 * Abstract class to be used for customising and executing a network {@link Request}.
 */
public abstract class HttpClient {

    /**
     * Takes a network {@link Request} and adds default HTTP header fields.
     * <p>Called before executing a the request and is used to customise the header of all requests
     * made through this client.</p>
     *
     * @param request A {@link Request} object
     * @return The {@link Request} object with client specific customisations.
     */
    public abstract void addDefaultHeaders(Request request);

    /**
     * Method for pre-processing all request sent through this client before they are sent.
     *
     * @param request  The {@link Request} to be executed
     * @param listener Result listener
     */
    public void processRequest(Request request, final IProcessRequestListener listener) {
        addDefaultHeaders(request);
        listener.onRequestProcessed();
    }

    public interface IProcessRequestListener {
        void onRequestProcessed();

        void onRequestError(RequestError error);
    }
}
