package com.bambora.nativepayment.mock;

import com.bambora.nativepayment.managers.CreditCardManager;
import com.bambora.nativepayment.models.creditcard.CreditCard;

/**
 * @author Lovisa Corp
 */
public class OnCreditCardSavedMock implements CreditCardManager.IOnCreditCardSaved {

    private boolean onCreditCardSavedCalled;

    public boolean wasOnCreditCardSavedCalled() {
        return onCreditCardSavedCalled;
    }

    @Override
    public void onCreditCardSaved(CreditCard creditCard) {
        onCreditCardSavedCalled = true;
    }
}
