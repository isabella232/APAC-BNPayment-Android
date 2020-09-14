package com.bambora.nativepayment.models;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;

import org.junit.Before;
import org.junit.Test;

public class PaymentSettingsTest {

    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_COMMENT = "comment";
    private static final String KEY_TOKEN = "token";

    @Before
    public void setup() {

    }

    @Test
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

    @Test
    public void testGetSerializedWithValidParameters() throws JSONException {
        // Given
        PaymentSettings paymentSettings = new PaymentSettings();
        int amount = 1200;
        String currency = "SEK";
        String comment = "A comment";
        String token = "token";
        String paymentJsonString="{\"Username\":\"YOUR_NAME\",\"Password\":\"YOUR_PASSWORD\",\"TokenisationAlgorithmId\":2,\"CustomerStorageNumber\":\"\",\"AccountNumber\":\"YOUR_ACCOUNT\",\"MerchantNumber\":\"\",\"CustNumber\":\"YOUR_NUMBER\",\"TrnType\":1,\"CustRef\":\"test-ud-002\",\"UserDefined\":{\"reference1\":\"abc123\"}}";
        JSONObject paymentJsonData=new JSONObject(paymentJsonString);

        paymentSettings.amount = amount;
        paymentSettings.currency = currency;
        paymentSettings.comment = comment;
        paymentSettings.creditCardToken = token;
        paymentSettings.paymentJsonData=paymentJsonData;

        // When
        String json = paymentSettings.getSerialized();
        JSONObject jsonObject = new JSONObject(json);

        // Then
        Assert.assertEquals(amount, jsonObject.getInt(KEY_AMOUNT));
        Assert.assertEquals(currency, jsonObject.getString(KEY_CURRENCY));
        Assert.assertEquals(comment, jsonObject.getString(KEY_COMMENT));
        Assert.assertEquals(token, jsonObject.getString(KEY_TOKEN));
    }

    @Test
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
