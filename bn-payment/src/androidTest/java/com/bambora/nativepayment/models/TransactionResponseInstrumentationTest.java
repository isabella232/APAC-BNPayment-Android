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

import com.bambora.nativepayment.json.JsonContainer;
import com.bambora.nativepayment.models.creditcard.TransactionErrorResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

/**
 * TODO
 */
public class TransactionResponseInstrumentationTest extends InstrumentationTestCase {

    private static final String KEY_PAYMENT_ID = "payment";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_STATUS = "status";
    private static final String KEY_CODE = "code";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_INFO = "info";

    public void testFromJsonWithoutParameters() throws Exception {
        // Given
        TransactionResponse transactionResponse = new TransactionResponse();
        JsonContainer responseJson = new JsonContainer("{}");

        // When
        transactionResponse.fromJson(responseJson);

        // Then
        Assert.assertNull(transactionResponse.paymentId);
        Assert.assertNull(transactionResponse.amount);
        Assert.assertNull(transactionResponse.currency);
        Assert.assertNull(transactionResponse.status);
        Assert.assertNull(transactionResponse.code);
        Assert.assertNull(transactionResponse.errorMessage);
        Assert.assertNull(transactionResponse.errorInfo);
    }

    public void testFromJsonWithValidParameters() throws Exception {
        // Given
        String paymentId = "a-payment-id";
        Integer amount = 1000;
        String currency = "SEK";
        Integer status = 2;
        Integer code = 400;
        String errorMessage = "A message";
        String errorInfo = "Some info";

        TransactionResponse transactionResponse = new TransactionResponse();
        JSONObject responseJson = new JSONObject();
        responseJson.put(KEY_PAYMENT_ID, paymentId);
        responseJson.put(KEY_AMOUNT, amount);
        responseJson.put(KEY_CURRENCY, currency);
        responseJson.put(KEY_STATUS, status);
        responseJson.put(KEY_CODE, code);
        responseJson.put(KEY_MESSAGE, errorMessage);
        responseJson.put(KEY_INFO, errorInfo);

        // When
        transactionResponse.fromJson(new JsonContainer(responseJson));

        // Then
        Assert.assertEquals(paymentId, transactionResponse.paymentId);
        Assert.assertEquals(amount, transactionResponse.amount);
        Assert.assertEquals(currency, transactionResponse.currency);
        Assert.assertEquals(status, transactionResponse.status);
        Assert.assertEquals(code, transactionResponse.code);
        Assert.assertEquals(errorMessage, transactionResponse.errorMessage);
        Assert.assertEquals(errorInfo, transactionResponse.errorInfo);
    }

    public void testGetErrorWithoutParameters() throws JSONException {
        // Given
        TransactionResponse transactionResponse = new TransactionResponse();
        JsonContainer responseJson = new JsonContainer("{}");

        // When
        transactionResponse.fromJson(responseJson);
        TransactionErrorResponse errorResponse = transactionResponse.getError();

        // Then
        Assert.assertNull(errorResponse.status);
        Assert.assertNull(errorResponse.errorCode);
        Assert.assertNull(errorResponse.errorMessage);
        Assert.assertNull(errorResponse.errorInfo);
    }

    public void testGetErrorWithValidParameters() throws JSONException {
        // Given
        String paymentId = "a-payment-id";
        Integer amount = 1000;
        String currency = "SEK";
        Integer status = 2;
        Integer code = 400;
        String errorMessage = "A message";
        String errorInfo = "Some info";

        TransactionResponse transactionResponse = new TransactionResponse();
        JSONObject responseJson = new JSONObject();
        responseJson.put(KEY_PAYMENT_ID, paymentId);
        responseJson.put(KEY_AMOUNT, amount);
        responseJson.put(KEY_CURRENCY, currency);
        responseJson.put(KEY_STATUS, status);
        responseJson.put(KEY_CODE, code);
        responseJson.put(KEY_MESSAGE, errorMessage);
        responseJson.put(KEY_INFO, errorInfo);

        // When
        transactionResponse.fromJson(new JsonContainer(responseJson));
        TransactionErrorResponse errorResponse = transactionResponse.getError();

        // Then
        Assert.assertEquals(status, errorResponse.status);
        Assert.assertEquals(code, errorResponse.errorCode);
        Assert.assertEquals(errorMessage, errorResponse.errorMessage);
        Assert.assertEquals(errorInfo, errorResponse.errorInfo);
    }
}
