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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A wrapper of a {@link HashMap} that simplifies creation and logging of an HTTP header.
 */
public class HttpHeader extends HashMap<String, List<String>> {

    public HttpHeader() {
        // Default constructor
    }

    /**
     * Instantiates an {@link HttpHeader} based on an existing Map.
     *
     * @param map   A {@link Map}
     */
    public HttpHeader(Map<? extends String, ? extends List<String>> map) {
        super(map);
    }

    /**
     * Adds a header field with given key an value. Will not create duplicate values for a key.
     *
     * @param key   Header field key
     * @param value Header field value
     */
    public void addField(String key, String value) {
        List<String> valueSet = get(key);
        if (valueSet != null && !valueSet.contains(value)) {
            valueSet.add(value);
        } else {
            List<String> newValueSet = new ArrayList<>();
            newValueSet.add(value);
            put(key, newValueSet);
        }
    }

    /**
     * Creates a String suitable for logging the header data.
     *
     * @return A {@link String} including all keys and their values.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String key : keySet()) {
            List<String> valueSet = get(key);
            builder
                    .append("\t\t");
            if (key != null) {
                builder
                    .append(key)
                    .append(": ");
            }
            for (String value : valueSet) {
                builder.append(value).append(", ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
