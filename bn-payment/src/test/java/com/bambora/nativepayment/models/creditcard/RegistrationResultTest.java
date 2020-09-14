package com.bambora.nativepayment.models.creditcard;

import android.util.Log;

import org.json.JSONException;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class RegistrationResultTest {

    private static final String KEY_CARD_NUMBER = "truncatedcardnumber";
    private static final String KEY_EXPIRY_MONTH = "expmonth";
    private static final String KEY_EXPIRY_YEAR = "expyear";
    private static final String KEY_PAYMENT_TYPE = "paymenttype";
    private static final String KEY_TRANSACTION_ID = "transactionid";
    private static final String KEY_SUBSCRIPTION_ID = "subscriptionid";
    private static final String KEY_ORIGIN_IP = "originip";

    @Test
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

    @Test
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
                KEY_CARD_NUMBER + ":\"" + cardNumber + "\"," +
                KEY_EXPIRY_MONTH + ":\"" + expiryMonth + "\"," +
                KEY_EXPIRY_YEAR + ":\"" + expiryYear + "\"," +
                KEY_PAYMENT_TYPE + ":\"" + paymentType + "\"," +
                KEY_TRANSACTION_ID + ":\"" + transactionId + "\"," +
                KEY_SUBSCRIPTION_ID + ":\"" + subscriptionId + "\"," +
                KEY_ORIGIN_IP + ":\"" + originIp + "\"}";

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
        assertEquals(RegistrationResultAction.ActionCode.UNKNOWN, registrationResult.getActionCode());
    }
}
