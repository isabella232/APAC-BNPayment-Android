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

package com.bambora.nativepayment.json;

import com.bambora.nativepayment.logging.BNLog;
import com.bambora.nativepayment.utils.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * TODO
 */
public class BNJsonObject extends JSONObject {

    private static final String LOG_TAG = "BNJsonObject";

    public BNJsonObject() {
        super();
    }

    public BNJsonObject(Map copyFrom) {
        super(copyFrom);
    }

    public BNJsonObject(JSONTokener readFrom) throws JSONException {
        super(readFrom);
    }

    public BNJsonObject(String json) throws JSONException {
        super(json);
    }

    public BNJsonObject(JSONObject copyFrom, String[] names) throws JSONException {
        super(copyFrom, names);
    }

    public static BNJsonObject copyFrom(JSONObject jsonObject) {
        try {
            return new BNJsonObject(jsonObject.toString());
        } catch (JSONException exception) {
            BNLog.e(LOG_TAG, "Failed to initiate BNJsonObject.", exception);
            return new BNJsonObject();
        }
    }

    public Date optDate(String name) {
        String dateString = optString(name);
        if (!dateString.isEmpty()) {
            try {
                return DateUtils.parseISO8601DateString(dateString);
            } catch (ParseException e) {
                BNLog.e(LOG_TAG, "Failed to parse date string: " + dateString, e);
            }
        }
        return null;
    }
}
