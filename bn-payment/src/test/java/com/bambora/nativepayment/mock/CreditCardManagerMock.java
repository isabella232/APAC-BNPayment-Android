package com.bambora.nativepayment.mock;

import android.content.Context;

import com.bambora.nativepayment.managers.CreditCardManager;
import com.bambora.nativepayment.models.creditcard.CreditCard;

/**
 * @author Lovisa Corp
 */
public class CreditCardManagerMock extends CreditCardManager {

    private CreditCard savedCreditCard;

    @Override
    public void saveCreditCard(Context context, CreditCard creditCard, IOnCreditCardSaved onSavedListener) {
        savedCreditCard = creditCard;
        if (onSavedListener != null) {
            onSavedListener.onCreditCardSaved(savedCreditCard);
        }
    }

    public CreditCard getSavedCreditCard() {
        return savedCreditCard;
    }
}
