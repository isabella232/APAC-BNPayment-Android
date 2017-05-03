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

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.bambora.nativepayment.interfaces.IJsonResponse;
import com.bambora.nativepayment.logging.BNLog;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * A Class for executing a network {@link Request}.
 */
public class RequestExecutor<T extends IJsonResponse<T>> {

    private static final int BUFFER_SIZE = 5000;
    private static final String LOG_TAG = "BNRequestExecutor";
    private static final String UTF8_CHAR_ENCODING = "UTF-8";

    /**
     * The Class of the expected response body.
     */
    private Class<T> responseBodyClass;

    /**
     * Request result listener.
     */
    private Callback<T> callback;

    /**
     * An {@link HttpClient} used for customising the {@link Request} to be executed.
     */
    private HttpClient httpClient;

    /**
     * Default constructor.
     *
     * @param client    An {@link HttpClient}
     */
    public RequestExecutor(HttpClient client) {
        this.httpClient = client;
    }

    /**
     * Customises the {@link Request} by through the {@link HttpClient} and then executes the
     * request. Only JSON formatted response content is supported.
     *
     * @param request       The {@link Request} to execute
     * @param responseClass Class of the expected response body
     * @param callback      Result listener
     */
    public void executeRequest(final Request<T> request, Class<T> responseClass, final Callback<T> callback) {
        this.callback = callback;
        this.responseBodyClass = responseClass;
        processRequest(request, new HttpClient.IProcessRequestListener() {
            @Override
            public void onRequestProcessed() {
                new DownloadJsonTask(request).execute();
            }

            @Override
            public void onRequestError(RequestError error) {
                callback.onError(error);
            }
        });
    }

    private void processRequest(Request request, HttpClient.IProcessRequestListener listener) {
        if (httpClient != null) {
            httpClient.processRequest(request, listener);
        } else {
            listener.onRequestProcessed();
        }
    }

    /**
     * Notifies listener of the result when request has been executed. The result is posted back to
     * the main thread. A response is always created even when the request fails.
     *
     * @param response A request {@link Response}.
     */
    private void notifyListener(final Response<T> response) {
        if (callback == null) {
            return;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (response.error != null) {
                    callback.onError(response.error);
                } else {
                    callback.onSuccess(response);
                }
            }
        });
    }

    /**
     * An {@link AsyncTask} for executing the {@link Request} asynchronously.
     */
    class DownloadJsonTask extends AsyncTask<Void, Void, Response<T>> {

        /**
         * The {@link Request} to be executed
         */
        private Request<T> request;

        public DownloadJsonTask(Request<T> request) {
            this.request = request;
        }

        @Override
        protected Response<T> doInBackground(Void... params) {
            return downloadJson();
        }

        @Override
        protected void onPostExecute(Response<T> response) {
            notifyListener(response);
        }

        private Response<T> downloadJson() {
            Response<T> response = new Response<>();
            HttpsURLConnection connection = null;
            try {
                connection = (HttpsURLConnection) request.getUrl().openConnection();
                connection.setRequestMethod(request.getMethod());
                addHttpHeader(connection, request.getHeader());
                addRequestBody(connection, request.getRawBody());

                connection.connect();
                readErrorResponse(connection, response);
                response.setResponseCode(connection.getResponseCode());
                response.header = new HttpHeader(connection.getHeaderFields());
                if (response.error == null) {
                    readResponseBody(connection, response);
                }
            } catch (IOException exception) {
                response.error = new RequestError(exception);
            } finally {
                if (connection != null)
                    connection.disconnect();
            }
            BNLog.requestResult(LOG_TAG, request, response);
            return response;
        }

        private void addHttpHeader(URLConnection connection, HttpHeader header) {
            for (String key : header.keySet()) {
                for (String value : header.get(key)) {
                    connection.addRequestProperty(key, value);
                }
            }
        }

        private void addRequestBody(URLConnection connection, String body) {
            if (body != null) {
                DataOutputStream outputStream = null;
                try {
                    outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.write(body.getBytes(UTF8_CHAR_ENCODING));
                    outputStream.flush();
                } catch (IOException e) {
                    BNLog.e(LOG_TAG, "Failed to write to output stream.", e);
                } finally {
                    try {
                        if (outputStream != null)
                            outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        BNLog.e(LOG_TAG, "Failed to close output stream.", e);
                    }
                }
            }
        }

        private void readResponseBody(HttpsURLConnection connection, Response<T> response) {
            if (connection == null) {
                return;
            }
            InputStream inputStream = null;
            try {
                inputStream = new BufferedInputStream(connection.getInputStream());
                String jsonBody = readInputStream(inputStream);
                if (jsonBody == null || jsonBody.isEmpty() || "null".equalsIgnoreCase(jsonBody)){
                    BNLog.d(LOG_TAG, "Null or emoty response body received. (" + jsonBody + ")");
                } else {
                    response.setBody(jsonBody, responseBodyClass);
                }
            } catch (IOException e) {
                BNLog.d(LOG_TAG, "No response body received. (" + e.getMessage() + ")");
            } finally {
                try {
                    if (inputStream != null)
                        inputStream.close();
                } catch (IOException e) {
                    BNLog.e(LOG_TAG, "Failed to close input stream.", e);
                }
            }
        }

        private void readErrorResponse(HttpsURLConnection connection, Response<T> response) {
            if (connection == null) {
                return;
            }
            InputStream errorInputStream = connection.getErrorStream();
            if (errorInputStream == null) {
                return;
            }
            RequestError error = new RequestError();
            try {
                String jsonBody = readInputStream(errorInputStream);
                error.setBody(jsonBody);
            } catch (IOException e) {
                BNLog.e(LOG_TAG, "Failed to read error input stream.", e);
            } finally {
                try {
                    errorInputStream.close();
                } catch (IOException e) {
                    BNLog.e(LOG_TAG, "Failed to close error input stream.", e);
                }
            }
            response.error = error;
        }

        private String readInputStream(InputStream inputStream) throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            Reader reader = new InputStreamReader(inputStream, UTF8_CHAR_ENCODING);
            readNext(reader, stringBuilder);
            return stringBuilder.toString();
        }

        private void readNext(Reader reader, StringBuilder stringBuilder) throws IOException {
            char[] buffer = new char[BUFFER_SIZE];
            int count = reader.read(buffer);
            boolean endOfReader = count < 0;
            if (!endOfReader) {
                stringBuilder.append(buffer, 0, count);
                readNext(reader, stringBuilder);
            }
        }
    }
}
