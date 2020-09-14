package com.bambora.nativepayment.handlers;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.bambora.nativepayment.base.MockitoInstrumentationTestCase;
import com.bambora.nativepayment.managers.CertificateManager;
import com.bambora.nativepayment.network.HttpClient;
import com.bambora.nativepayment.network.Request;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class BNPaymentsHandlerInstrumentationTest extends MockitoInstrumentationTestCase {

    @Before
    public void setUp() {
        super.setUp();
        BNPaymentHandler bnPaymentHandler = BNPaymentHandler.getInstance();
        bnPaymentHandler.clearSetup();
        bnPaymentHandler.setCertificateManager(Mockito.mock(CertificateManager.class));
    }

    @Test
    public void testApiToken() {
        BNPaymentHandler.BNPaymentBuilder bnPaymentBuilder = new BNPaymentHandler.BNPaymentBuilder(
                InstrumentationRegistry.getInstrumentation().getContext(), "token");

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
                InstrumentationRegistry.getInstrumentation().getContext(), "token");

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
                InstrumentationRegistry.getInstrumentation().getContext());
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
                InstrumentationRegistry.getInstrumentation().getContext());
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