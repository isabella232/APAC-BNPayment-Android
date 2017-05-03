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

package com.bambora.nativepayment.models.creditcard;

import android.test.InstrumentationTestCase;

import com.bambora.nativepayment.json.JsonContainer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * TODO
 */
public class RegistrationResponseInstrumentationTest extends InstrumentationTestCase {

    private static final String KEY_SESSION_URL = "session_url";

    public void testFromJsonWithNoParameters() throws JSONException {
        // Given
        JsonContainer responseJson = new JsonContainer("{}");
        RegistrationResponse registrationResponse = new RegistrationResponse();

        // When
        registrationResponse.fromJson(responseJson);

        // Then
        assertNull(registrationResponse.sessionUrl);
    }

    public void testFromJsonWithValidUrl() throws JSONException {
        // Given
        String url = "http://a.valid.url";
        JSONObject responseJson = new JSONObject();
        responseJson.put(KEY_SESSION_URL, url);
        JsonContainer responseJsonContainer = new JsonContainer(responseJson);
        RegistrationResponse registrationResponse = new RegistrationResponse();

        // When
        registrationResponse.fromJson(responseJsonContainer);

        // Then
        assertEquals(url, registrationResponse.sessionUrl);
    }
}
