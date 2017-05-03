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

import com.bambora.nativepayment.interfaces.IJsonResponse;
import com.bambora.nativepayment.json.JsonContainer;
import com.bambora.nativepayment.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Model for response received when initiating a hosted payment page
 */
public class RegistrationResponse implements IJsonResponse<RegistrationResponse> {

    private static final String KEY_SESSION_URL = "session_url";

    public String sessionUrl;

    @Override
    public RegistrationResponse fromJson(JsonContainer jsonContainer) throws JSONException {
        JSONObject jsonObject = jsonContainer.getJsonObject();
        sessionUrl = JsonUtils.getStringIfExists(jsonObject, KEY_SESSION_URL);
        return this;
    }
}
