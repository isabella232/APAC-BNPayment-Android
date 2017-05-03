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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Model for a network request error response body.
 */
public class ErrorBody {

    private static final String KEY_STATUS = "status";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TYPE = "type";
    private static final String KEY_DETAIL = "detail";

    public Integer status;

    public String title;

    public String type;

    public String detail;

    public ErrorBody(JSONObject jsonObject) {
        status = jsonObject.optInt(KEY_STATUS);
        title = jsonObject.optString(KEY_TITLE);
        type = jsonObject.optString(KEY_TYPE);
        detail = jsonObject.optString(KEY_DETAIL);
    }

    public static ErrorBody fromJson(String jsonString) throws JSONException {
        JSONObject jsonObject;
        jsonObject = new JSONObject(jsonString);
        return new ErrorBody(jsonObject);
    }
}
