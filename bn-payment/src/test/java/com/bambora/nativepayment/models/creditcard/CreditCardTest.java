package com.bambora.nativepayment.models.creditcard;

import com.bambora.nativepayment.json.JsonContainer;
import com.bambora.nativepayment.models.TransactionResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class CreditCardTest {

    private static final String KEY_RECURRING_PAYMENT_ID = "recurringPaymentID";
    private static final String KEY_CARD_NUMBER = "cardNumber";
    private static final String KEY_CARD_TYPE = "cardType";
    private static final String KEY_EXPIRY_MONTH = "expiryMonth";
    private static final String KEY_EXPIRY_YEAR = "expiryYear";
    private static final String KEY_CARD_HOLDER_NAME = "cardHolderName";
    private static final String KEY_CARD_TOKEN = "creditCardToken";
    private static final String KEY_TRUNCATED_CARD = "truncatedCard";

    @Test
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

    @Test
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

    @Test
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

    @Test
    public void testFromJsonWithCardNumberAndName() throws JSONException {
        // Given
        String truncatedCardNumber = "1111XXXXXXXX2222";
        String holderName = "Bambora";
        CreditCard creditCard = new CreditCard();
        JSONObject creditCardJson = new JSONObject();
        creditCardJson.put(KEY_CARD_NUMBER, truncatedCardNumber);
        creditCardJson.put(KEY_CARD_HOLDER_NAME, holderName);
        // When
        creditCard.fromJson(new JsonContainer(creditCardJson));

        // Then
        assertEquals(truncatedCardNumber, creditCard.getTruncatedCardNumber());
        assertEquals(holderName, creditCard.getCardHolderName());
    }

    @Test
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

    @Test
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

    @Test
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

    @Test
    public void testIsEqualToShouldBeTrue() {
        // Given
        CreditCard creditCard1 = new CreditCard("1111xxxxxxxx2222", 8, 22, "PaymentType", "TransactionId", "123456");
        CreditCard creditCard2 = new CreditCard("3333xxxxxxxx4444", 1, 19, "PaymentType", "TransactionId", "123456");

        // When
        boolean isEqual = creditCard1.isEqualTo(creditCard2);

        // Then
        assertTrue(isEqual);
    }

    @Test
    public void testIsEqualToShouldBeFalse() {
        // Given
        CreditCard creditCard1 = new CreditCard("1111xxxxxxxx2222", 8, 22, "PaymentType", "TransactionId", "123456");
        CreditCard creditCard2 = new CreditCard("1111xxxxxxxx2222", 8, 22, "PaymentType", "TransactionId", "654321");

        // When
        boolean isEqual = creditCard1.isEqualTo(creditCard2);

        // Then
        assertFalse(isEqual);
    }

    @Test
    public void testAlias() {
        CreditCard creditCard = new CreditCard("1111xxxxxxxx2222", 8, 22, "PaymentType", "TransactionId", "123456");
        String alias="Bambora";
        creditCard.setAlias(alias);
        assertEquals(alias,creditCard.getAlias());
    }

    @Test
    public void testFromTransactionResponse() throws JSONException{
        TransactionResponse transactionResponse = new TransactionResponse();

        String holderName = "Bambora";
        String cardToken = "9765042131603803";
        String cardTruncatedNumber = "Token123";
        String cardType = "Token123";

        JSONObject responseJson = new JSONObject();
        responseJson.put(KEY_CARD_HOLDER_NAME, holderName);
        responseJson.put(KEY_CARD_TOKEN, cardToken);
        responseJson.put(KEY_TRUNCATED_CARD, cardTruncatedNumber);
        responseJson.put(KEY_CARD_TYPE, cardType);
        transactionResponse.fromJson(new JsonContainer(responseJson));
        CreditCard creditCard = new CreditCard(transactionResponse);

        assertEquals(holderName,creditCard.getCardHolderName());
        assertEquals(cardToken,creditCard.getCreditCardToken());
        assertEquals(cardTruncatedNumber,creditCard.getTruncatedCardNumber());
        assertEquals(cardType,creditCard.getPaymentType());
    }





}
