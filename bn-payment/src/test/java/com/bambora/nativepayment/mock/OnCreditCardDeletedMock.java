package com.bambora.nativepayment.mock;

import com.bambora.nativepayment.managers.CreditCardManager;

/**
 * @author Lovisa Corp
 */
public class OnCreditCardDeletedMock implements CreditCardManager.IOnCreditCardDeleted {

    private boolean onCreditCardDeletedCalled;

    public boolean wasOnCreditCardDeletedCalled() {
        return onCreditCardDeletedCalled;
    }

    @Override
    public void onCreditCardDeleted() {
        onCreditCardDeletedCalled = true;
    }
}
