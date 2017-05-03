package com.bambora.nativepayment.handlers;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by oskarhenriksson on 2016-02-29.
 */
public class BNPaymentsHandlerTest {

    @Test
    public void testGetInstance() {
        BNPaymentHandler instance = BNPaymentHandler.getInstance();
        Assert.assertNotNull(instance);
    }

}