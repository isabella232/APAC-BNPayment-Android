package com.bambora.nativepayment.mock;

import android.content.Context;

import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.storage.TransactionFileStorage;

/**
 * @author Lovisa Corp
 */
public class TransactionFileStorageMock extends TransactionFileStorage {

    private CreditCard[] savedCreditCards = new CreditCard[0];

    @Override
    public void saveCreditCards(Context context, CreditCard[] creditCards, IOnObjectSaved listener) {
        this.savedCreditCards = creditCards;
        if (listener != null) {
            listener.notifyOnObjectSaved();
        }
    }

    @Override
    public void getCreditCards(Context context, IOnObjectRead listener) {
        listener.notifyOnObjectRead(savedCreditCards);
    }

    public int getNrOfCreditCards() {
        return savedCreditCards.length;
    }

    public CreditCard getCreditCard(String token) {
        for (CreditCard creditCard : savedCreditCards) {
            if (creditCard.getCreditCardToken().equals(token)) {
                return creditCard;
            }
        }
        return null;
    }
}
