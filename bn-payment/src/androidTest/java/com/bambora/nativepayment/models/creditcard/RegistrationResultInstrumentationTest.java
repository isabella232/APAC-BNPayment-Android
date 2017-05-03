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

import org.json.JSONException;

/**
 * TODO
 */
public class RegistrationResultInstrumentationTest extends InstrumentationTestCase {

    private static final String KEY_CARD_NUMBER = "truncatedcardnumber";
    private static final String KEY_EXPIRY_MONTH = "expmonth";
    private static final String KEY_EXPIRY_YEAR = "expyear";
    private static final String KEY_PAYMENT_TYPE = "paymenttype";
    private static final String KEY_TRANSACTION_ID = "transactionid";
    private static final String KEY_SUBSCRIPTION_ID = "subscriptionid";
    private static final String KEY_ORIGIN_IP = "originip";

    public void testFromJsonWithoutParameters() throws JSONException {
        // Given
        String resultJson = "{}";

        // When
        RegistrationResult registrationResult = RegistrationResult.fromJson(resultJson);

        // Then
        assertNull(registrationResult.meta);
        assertNull(registrationResult.truncatedCardNumber);
        assertNull(registrationResult.expiryMonth);
        assertNull(registrationResult.expiryYear);
        assertNull(registrationResult.paymentType);
        assertNull(registrationResult.transactionId);
        assertNull(registrationResult.subscriptionId);
        assertNull(registrationResult.originIp);
    }

    public void testFromJsonWithValidParameters() throws JSONException {
        // Given
        String cardNumber = "123456789";
        Integer expiryMonth = 1;
        Integer expiryYear = 2;
        String paymentType = "payment-type";
        String transactionId = "transaction-id";
        String subscriptionId = "subscription-id";
        String originIp = "origin-ip";
        String resultJson = "{" +
                KEY_CARD_NUMBER + ":" + cardNumber + "," +
                KEY_EXPIRY_MONTH + ":" + expiryMonth + "," +
                KEY_EXPIRY_YEAR + ":" + expiryYear + "," +
                KEY_PAYMENT_TYPE + ":" + paymentType + "," +
                KEY_TRANSACTION_ID + ":" + transactionId + "," +
                KEY_SUBSCRIPTION_ID + ":" + subscriptionId + "," +
                KEY_ORIGIN_IP + ":" + originIp + "}";

        // When
        RegistrationResult registrationResult = RegistrationResult.fromJson(resultJson);

        // Then
        assertEquals(cardNumber, registrationResult.truncatedCardNumber);
        assertEquals(expiryMonth, registrationResult.expiryMonth);
        assertEquals(expiryYear, registrationResult.expiryYear);
        assertEquals(paymentType, registrationResult.paymentType);
        assertEquals(transactionId, registrationResult.transactionId);
        assertEquals(subscriptionId, registrationResult.subscriptionId);
        assertEquals(originIp, registrationResult.originIp);
    }
}
