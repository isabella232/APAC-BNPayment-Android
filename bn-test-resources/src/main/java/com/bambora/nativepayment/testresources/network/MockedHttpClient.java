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

package com.bambora.nativepayment.testresources.network;

import com.bambora.nativepayment.testresources.models.Scenario;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

/**
 * Created by oskarhenriksson on 14/12/15.
 */
public class MockedHttpClient extends OkHttpClient {

    private LocalResponseInterceptor mInterceptor;

    public MockedHttpClient() {
        super();
        mInterceptor = new LocalResponseInterceptor();
        this.interceptors().add(mInterceptor);
    }

    public void setScenario(Scenario scenario) {
        if(mInterceptor != null) {
            mInterceptor.setScenario(scenario);
        }
    }

    private class LocalResponseInterceptor implements Interceptor {
        private Scenario scenario;

        public void setScenario(Scenario scenario) {
            this.scenario = scenario;
        }

        @Override
        public com.squareup.okhttp.Response intercept(Chain chain) {
            Request request = chain.request();
            String jsonBody = scenario.getJsonBody();
            String mimeType = scenario.getMimeType();

            return new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(scenario.getHttpStatus())
                    .body(ResponseBody.create(MediaType.parse(mimeType), jsonBody.getBytes()))
                    .build();
        }
    }
}
