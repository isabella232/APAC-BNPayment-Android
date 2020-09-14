package com.bambora.nativepayment.handlers;

import android.content.Context;
import android.content.SharedPreferences;

import com.bambora.nativepayment.interfaces.ICardRegistrationCallback;
import com.bambora.nativepayment.interfaces.ITransactionExtListener;
import com.bambora.nativepayment.managers.CertificateManager;
import com.bambora.nativepayment.managers.CreditCardManager;
import com.bambora.nativepayment.models.PaymentSettings;
import com.bambora.nativepayment.models.PaymentType;
import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.network.HttpClient;
import com.bambora.nativepayment.network.HttpHeader;
import com.bambora.nativepayment.network.Request;


import junit.framework.Assert;

import org.hamcrest.core.AnyOf;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Any;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Map;

import static com.bambora.nativepayment.handlers.BNPaymentHandler.existingBaseURLKey;
import static com.bambora.nativepayment.handlers.BNPaymentHandler.existingMerchantAccountKey;
import static com.bambora.nativepayment.handlers.BNPaymentHandler.sharedPreferencesKey;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.atLeast;

/**
 * Created by oskarhenriksson on 2016-02-29.
 */

public class BNPaymentsHandlerTest {

    @Mock
    private SharedPreferences mockSharedPreferences;

    @Mock
    private SharedPreferences.Editor mockEditor;

    @Mock
    private Context mockContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BNPaymentHandler bnPaymentHandler = BNPaymentHandler.getInstance();
        bnPaymentHandler.clearSetup();
        bnPaymentHandler.setCertificateManager(mock(CertificateManager.class));

        when(mockContext.getSharedPreferences(sharedPreferencesKey, 0)).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.getString(existingMerchantAccountKey, "")).thenReturn("");
        when(mockSharedPreferences.getString(existingBaseURLKey, "")).thenReturn("");
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
    }

    @Test
    public void testGetInstance() {
        BNPaymentHandler instance = BNPaymentHandler.getInstance();
        Assert.assertNotNull(instance);
    }

    @Test
    public void testApiToken() {
        BNPaymentHandler.BNPaymentBuilder bnPaymentBuilder = new BNPaymentHandler.BNPaymentBuilder(
                mockContext,"token");

        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);

        BNPaymentHandler instance = BNPaymentHandler.getInstance();
        Assert.assertEquals("token", instance.getApiToken());
    }

    /**
     * Verify that Api-Token (but not Merchant-Account) header is set
     */
    @Test
    public void testApiTokenHeader() {
        // Given
        BNPaymentHandler.BNPaymentBuilder bnPaymentBuilder = new BNPaymentHandler.BNPaymentBuilder(
                mockContext, "token");

        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);

        BNPaymentHandler instance = BNPaymentHandler.getInstance();
        HttpClient httpClient = instance.getHttpClient();

        Request request = new Request(null, null);

        // When
        httpClient.addDefaultHeaders(request);

        // Then
        Assert.assertNull(request.getHeader().get("Merchant-Account"));
        Assert.assertNotNull(request.getHeader().get("Api-Token"));
        Assert.assertEquals("token", request.getHeader().get("Api-Token").get(0));
    }

    /**
     * Verify that merchant account can be set on {@link com.bambora.nativepayment.handlers.BNPaymentHandler}
     */
    @Test
    public void testMerchantAccount() {
        // Given
        BNPaymentHandler.BNPaymentBuilder bnPaymentBuilder = new BNPaymentHandler.BNPaymentBuilder(
                mockContext);
        bnPaymentBuilder.merchantAccount("merchant");

        // When
        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);

        BNPaymentHandler instance = BNPaymentHandler.getInstance();

        // Then
        Assert.assertEquals("merchant", instance.getMerchantAccount());
    }

    /**
     * Verify that Merchant-Account (but not Api-Token) header is set
     */
    @Test
    public void testMerchantAccountHeader() {
        // Given
        BNPaymentHandler.BNPaymentBuilder bnPaymentBuilder = new BNPaymentHandler.BNPaymentBuilder(
                mockContext);
        bnPaymentBuilder.merchantAccount("merchant");

        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);

        BNPaymentHandler instance = BNPaymentHandler.getInstance();
        HttpClient httpClient = instance.getHttpClient();

        Request request = new Request(null, null);

        // When
        httpClient.addDefaultHeaders(request);

        // Then
        Assert.assertNull(request.getHeader().get("Api-Token"));
        Assert.assertNotNull(request.getHeader().get("Merchant-Account"));
        Assert.assertEquals("merchant", request.getHeader().get("Merchant-Account").get(0));
    }

    @Test
    public void testRegistrationJsonData() throws JSONException{
        // Given
        BNPaymentHandler.BNPaymentBuilder bnPaymentBuilder = new BNPaymentHandler.BNPaymentBuilder(
                mockContext);
        bnPaymentBuilder.merchantAccount("merchant");

        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);
        BNPaymentHandler instance = BNPaymentHandler.getInstance();
        HttpClient httpClient = instance.getHttpClient();

        Request request = new Request(null, null);

        JSONObject registrationJsonData=new JSONObject("{\n" +
                "  \"Username\":\"bn-secure.rest.api.dev\",\n" +
                "  \"Password\":\"MobileIsGr8\",\n" +
                "  \"TokenisationAlgorithmId\":2\n" +
                "}");
        instance.setRegistrationJsonData(registrationJsonData);
        // When
        httpClient.addDefaultHeaders(request);

        // Then
        Assert.assertNull(request.getHeader().get("Api-Token"));
        Assert.assertNotNull(request.getHeader().get("Merchant-Account"));
        Assert.assertEquals("merchant", request.getHeader().get("Merchant-Account").get(0));
        Assert.assertEquals(registrationJsonData, instance.getRegistrationJsonData());
    }

    @Test
    public void testRegisterCreditCardWithError() {
        BNPaymentHandler.BNPaymentBuilder bnPaymentBuilder = new BNPaymentHandler.BNPaymentBuilder(
                mockContext);
        bnPaymentBuilder.merchantAccount("merchant0");
        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);
        String cardNumber="4242 4242 4242 4242";

        String expiryMonth="12";
        String expiryYear="20";
        String securityCode="888";
        String holder="Bambora";

        ICardRegistrationCallback withHolderListener=mock(ICardRegistrationCallback.class);
        BNPaymentHandler.getInstance().registerCreditCard(mockContext,holder,cardNumber,expiryMonth,expiryYear,securityCode,withHolderListener);
        verify(withHolderListener,atLeast(1)).onRegistrationError(null);

        ICardRegistrationCallback withOutHolderListener=mock(ICardRegistrationCallback.class);
        BNPaymentHandler.getInstance().registerCreditCard(mockContext,cardNumber,expiryMonth,expiryYear,securityCode,withOutHolderListener);
        verify(withOutHolderListener,atLeast(1)).onRegistrationError(null);
    }

    @Test
    public void testPaymentWithError() throws JSONException{
        BNPaymentHandler.BNPaymentBuilder bnPaymentBuilder = new BNPaymentHandler.BNPaymentBuilder(
                mockContext);
        bnPaymentBuilder.merchantAccount("merchant");
        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);
        BNPaymentHandler instance = BNPaymentHandler.getInstance();
        String cardNumber="4242 4242 4242 4242";
        String expiryMonth="12";
        String expiryYear="20";
        String securityCode="888";
        String holderName="Bambora";
        String paymentIdentifier="";

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
        paymentSettings.paymentType = PaymentType.PaymentTypeEnum.PaymentToken;
        ITransactionExtListener iTransactionExtListener=mock(ITransactionExtListener.class);
        CreditCardManager.IOnCreditCardSaved iOnCreditCardSaved=mock(CreditCardManager.IOnCreditCardSaved.class);

        BNPaymentHandler.getInstance().submitSingleTransactionCard(
                mockContext,
                paymentIdentifier, paymentSettings,holderName,
                cardNumber,
                expiryMonth,
                expiryYear,
                securityCode,
                false, iTransactionExtListener, iOnCreditCardSaved);
        verify(iTransactionExtListener,atLeast(1)).onTransactionError(null);
    }
}