package com.bambora.nativepayment.handlers;

import android.support.test.runner.AndroidJUnit4;

import com.bambora.nativepayment.base.MockitoInstrumentationTestCase;
import com.bambora.nativepayment.managers.CertificateManager;
import com.bambora.nativepayment.network.HttpClient;
import com.bambora.nativepayment.network.Request;

import junit.framework.Assert;

import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class BNPaymentsHandlerInstrumentationTest extends MockitoInstrumentationTestCase {

    @Override
    public void setUp() {
        super.setUp();
        BNPaymentHandler bnPaymentHandler = BNPaymentHandler.getInstance();
        bnPaymentHandler.clearSetup();
        bnPaymentHandler.setCertificateManager(Mockito.mock(CertificateManager.class));
    }

    public void testApiToken() {
        BNPaymentHandler.BNPaymentBuilder bnPaymentBuilder = new BNPaymentHandler.BNPaymentBuilder(
                getInstrumentation().getContext(), "token");

        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);

        BNPaymentHandler instance = BNPaymentHandler.getInstance();
        Assert.assertEquals("token", instance.getApiToken());
    }

    /**
     * Verify that Api-Token (but not Merchant-Account) header is set
     */
    public void testApiTokenHeader() {
        // Given
        BNPaymentHandler.BNPaymentBuilder bnPaymentBuilder = new BNPaymentHandler.BNPaymentBuilder(
                getInstrumentation().getContext(), "token");

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
    public void testMerchantAccount() {
        // Given
        BNPaymentHandler.BNPaymentBuilder bnPaymentBuilder = new BNPaymentHandler.BNPaymentBuilder(
                getInstrumentation().getContext());
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
    public void testMerchantAccountHeader() {
        // Given
        BNPaymentHandler.BNPaymentBuilder bnPaymentBuilder = new BNPaymentHandler.BNPaymentBuilder(
                getInstrumentation().getContext());
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
}