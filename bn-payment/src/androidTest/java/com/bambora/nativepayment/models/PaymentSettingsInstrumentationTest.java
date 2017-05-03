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

package com.bambora.nativepayment.models;

import android.test.InstrumentationTestCase;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

/**
 * TODO
 */
public class PaymentSettingsInstrumentationTest extends InstrumentationTestCase {

    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_COMMENT = "comment";
    private static final String KEY_TOKEN = "token";

    public void testGetSerializedWithNoParameters() throws JSONException {
        // Given
        PaymentSettings paymentSettings = new PaymentSettings();

        // When
        String json = paymentSettings.getSerialized();
        JSONObject jsonObject = new JSONObject(json);

        // Then
        Assert.assertNotNull(jsonObject);
        Assert.assertEquals(0, jsonObject.length());
    }

    public void testGetSerializedWithValidParameters() throws JSONException {
        // Given
        PaymentSettings paymentSettings = new PaymentSettings();
        int amount = 1200;
        String currency = "SEK";
        String comment = "A comment";
        String token = "token";
        paymentSettings.amount = amount;
        paymentSettings.currency = currency;
        paymentSettings.comment = comment;
        paymentSettings.creditCardToken = token;

        // When
        String json = paymentSettings.getSerialized();
        JSONObject jsonObject = new JSONObject(json);

        // Then
        Assert.assertEquals(amount, jsonObject.getInt(KEY_AMOUNT));
        Assert.assertEquals(currency, jsonObject.getString(KEY_CURRENCY));
        Assert.assertEquals(comment, jsonObject.getString(KEY_COMMENT));
        Assert.assertEquals(token, jsonObject.getString(KEY_TOKEN));
    }

    public void testGetSerializedWithMissingParameters() throws JSONException {
        // Given
        PaymentSettings paymentSettings = new PaymentSettings();
        int amount = 1200;
        String currency = "SEK";
        paymentSettings.amount = amount;
        paymentSettings.currency = currency;

        // When
        String json = paymentSettings.getSerialized();
        JSONObject jsonObject = new JSONObject(json);

        // Then
        Assert.assertEquals(amount, jsonObject.getInt(KEY_AMOUNT));
        Assert.assertEquals(currency, jsonObject.getString(KEY_CURRENCY));
        try {
            jsonObject.getString(KEY_COMMENT);
            Assert.fail("Key " + KEY_TOKEN + " was set but was expected to be missing.");
        } catch (JSONException e) {
            // Expected exception
        }

        try {
            jsonObject.getString(KEY_TOKEN);
            Assert.fail("Key " + KEY_TOKEN + " was set but was expected to be missing.");
        } catch (JSONException e) {
            // Expected exception
        }
    }
}
