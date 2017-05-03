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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * TODO
 */
public class JsonContainer {

    private Object json;

    public JsonContainer(String jsonString) throws JSONException {
        json = new JSONTokener(jsonString).nextValue();
    }

    public JsonContainer(JSONObject jsonObject) {
        this.json = jsonObject;
    }

    public JsonContainer(JSONArray jsonArray) {
        this.json = jsonArray;
    }

    public boolean isJsonObject() {
        if (this.json instanceof JSONObject) {
            return true;
        }
        return false;
    }

    public boolean isJsonArray() {
        if (this.json instanceof JSONArray) {
            return true;
        }
        return false;
    }

    public JSONObject getJsonObject() throws JSONException {
        if (isJsonObject()) {
            return (JSONObject) this.json;
        }
        throw new JSONException("Expected JSONObject but was " + (json != null ? json.getClass() : "null"));
    }

    public JSONArray getJsonArray() throws JSONException {
        if (isJsonArray()) {
            return (JSONArray) this.json;
        }
        throw new JSONException("Expected JSONArray but was " + (json != null ? json.getClass() : "null"));
    }
}
