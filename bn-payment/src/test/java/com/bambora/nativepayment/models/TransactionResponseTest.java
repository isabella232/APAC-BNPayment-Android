package com.bambora.nativepayment.models;

import com.bambora.nativepayment.json.JsonContainer;
import com.bambora.nativepayment.models.creditcard.TransactionErrorResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class TransactionResponseTest {

    private static final String KEY_PAYMENT_ID = "payment";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_STATUS = "status";
    private static final String KEY_CODE = "code";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_INFO = "info";

    @Test
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

    @Test
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

    @Test
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

    @Test
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
