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
public class CreditCardTest extends InstrumentationTestCase {

    private static final String KEY_RECURRING_PAYMENT_ID = "recurringPaymentID";
    private static final String KEY_CARD_NUMBER = "cardNumber";
    private static final String KEY_CARD_TYPE = "cardType";
    private static final String KEY_EXPIRY_MONTH = "expiryMonth";
    private static final String KEY_EXPIRY_YEAR = "expiryYear";

    public void testConstructorWithParameterInput() {
        // Given
        String truncatedCardNumber = "1111XXXXXXXX2222";
        Integer expiryMonth = 8;
        Integer expiryYear = 16;
        String paymentType = "PaymentType";
        String transactionId = "123456789";
        String creditCardToken = "Token123";

        // When
        CreditCard creditCard = new CreditCard(truncatedCardNumber, expiryMonth, expiryYear,
                paymentType, transactionId, creditCardToken);

        // Then
        assertEquals(truncatedCardNumber, creditCard.getTruncatedCardNumber());
        assertEquals(expiryMonth, creditCard.getExpiryMonth());
        assertEquals(expiryYear, creditCard.getExpiryYear());
        assertEquals(paymentType, creditCard.getPaymentType());
        assertEquals(transactionId, creditCard.getTransactionId());
        assertEquals(creditCardToken, creditCard.getCreditCardToken());
    }

    public void testConstructorWithRegistrationResult() throws JSONException {
        // Given
        String truncatedCardNumber = "1111XXXXXXXX2222";
        Integer expiryMonth = 8;
        Integer expiryYear = 16;
        String paymentType = "PaymentType";
        String transactionId = "123456789";
        String creditCardToken = "Token123";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("truncatedcardnumber", truncatedCardNumber);
        jsonObject.put("expmonth", expiryMonth);
        jsonObject.put("expyear", expiryYear);
        jsonObject.put("paymenttype", paymentType);
        jsonObject.put("transactionid", transactionId);
        jsonObject.put("subscriptionid", creditCardToken);
        RegistrationResult registrationResult = new RegistrationResult(jsonObject);

        // When
        CreditCard creditCard = new CreditCard(registrationResult);

        // Then
        assertEquals(truncatedCardNumber, creditCard.getTruncatedCardNumber());
        assertEquals(expiryMonth, creditCard.getExpiryMonth());
        assertEquals(expiryYear, creditCard.getExpiryYear());
        assertEquals(paymentType, creditCard.getPaymentType());
        assertEquals(transactionId, creditCard.getTransactionId());
        assertEquals(creditCardToken, creditCard.getCreditCardToken());
    }

    public void testFromJsonWithEmptyParameters() throws JSONException {
        // Given
        JsonContainer emptyJson = new JsonContainer("{}");
        CreditCard creditCard = new CreditCard();

        // When
        creditCard.fromJson(emptyJson);

        // Then
        assertNull(creditCard.getTruncatedCardNumber());
        assertNull(creditCard.getExpiryMonth());
        assertNull(creditCard.getExpiryYear());
        assertNull(creditCard.getPaymentType());
        assertNull(creditCard.getTransactionId());
        assertNull(creditCard.getCreditCardToken());
    }

    public void testFromJsonWithCardNumber() throws JSONException {
        // Given
        String truncatedCardNumber = "1111XXXXXXXX2222";
        CreditCard creditCard = new CreditCard();
        JSONObject creditCardJson = new JSONObject();
        creditCardJson.put(KEY_CARD_NUMBER, truncatedCardNumber);

        // When
        creditCard.fromJson(new JsonContainer(creditCardJson));

        // Then
        assertEquals(truncatedCardNumber, creditCard.getTruncatedCardNumber());
    }

    public void testFromJsonWithExpiryDate() throws JSONException {
        // Given
        Integer expiryMonth = 8;
        Integer expiryYear = 16;
        CreditCard creditCard = new CreditCard();
        JSONObject creditCardJson = new JSONObject();
        creditCardJson.put(KEY_EXPIRY_MONTH, expiryMonth);
        creditCardJson.put(KEY_EXPIRY_YEAR, expiryYear);

        // When
        creditCard.fromJson(new JsonContainer(creditCardJson));

        // Then
        assertEquals(expiryMonth, creditCard.getExpiryMonth());
        assertEquals(expiryYear, creditCard.getExpiryYear());
    }

    public void testFromJsonWithPaymentType() throws JSONException {
        // Given
        String paymentType = "a-payment-type";
        CreditCard creditCard = new CreditCard();
        JSONObject creditCardJson = new JSONObject();
        creditCardJson.put(KEY_CARD_TYPE, paymentType);

        // When
        creditCard.fromJson(new JsonContainer(creditCardJson));

        // Then
        assertEquals(paymentType, creditCard.getPaymentType());
    }

    public void testFromJsonWithCardToken() throws JSONException {
        // Given
        String cardToken = "a-card-token";
        CreditCard creditCard = new CreditCard();
        JSONObject creditCardJson = new JSONObject();
        creditCardJson.put(KEY_RECURRING_PAYMENT_ID, cardToken);

        // When
        creditCard.fromJson(new JsonContainer(creditCardJson));

        // Then
        assertEquals(cardToken, creditCard.getCreditCardToken());
    }

    public void testIsEqualToShouldBeTrue() {
        // Given
        CreditCard creditCard1 = new CreditCard("1111xxxxxxxx2222", 8, 22, "PaymentType", "TransactionId", "123456");
        CreditCard creditCard2 = new CreditCard("3333xxxxxxxx4444", 1, 19, "PaymentType", "TransactionId", "123456");

        // When
        boolean isEqual = creditCard1.isEqualTo(creditCard2);

        // Then
        assertTrue(isEqual);
    }

    public void testIsEqualToShouldBeFalse() {
        // Given
        CreditCard creditCard1 = new CreditCard("1111xxxxxxxx2222", 8, 22, "PaymentType", "TransactionId", "123456");
        CreditCard creditCard2 = new CreditCard("1111xxxxxxxx2222", 8, 22, "PaymentType", "TransactionId", "654321");

        // When
        boolean isEqual = creditCard1.isEqualTo(creditCard2);

        // Then
        assertFalse(isEqual);
    }
}
