/*
 * Copyright (c) 2016 Bambora ( http://bambora.com/ )
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.bambora.nativepayment.managers;

import android.content.Context;

import com.bambora.nativepayment.interfaces.ICardRegistrationCallback;
import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.models.creditcard.RegistrationParams;
import com.bambora.nativepayment.models.creditcard.RegistrationParams.IOnEncryptionListener;
import com.bambora.nativepayment.security.Crypto;
import com.bambora.nativepayment.network.RequestError;
import com.bambora.nativepayment.services.PaymentApiService;
import com.bambora.nativepayment.storage.FileStorage;
import com.bambora.nativepayment.storage.FileStorage.IOnObjectSaved;
import com.bambora.nativepayment.storage.TransactionFileStorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Helper class for fetching and storing credit cards from local storage.
 */
public class CreditCardManager {

    private TransactionFileStorage fileStorage = new TransactionFileStorage();

    /**
     * Registers a credit card with the given details for future recurring payments.
     * <p>When card has been registered a truncated version of the card is saved to disk.</p>
     *
     * @param context       App Context object
     * @param cardNumber    Card number
     * @param expiryMonth   Expiry month of the card
     * @param expiryYear    Expiry year of the card
     * @param securityCode  Security code (CVC/CVV)
     * @param listener      Result listener
     */
    //[[]]c
    public void registerCreditCard(final Context context, String cardNumber, String expiryMonth, String expiryYear,
                                   String securityCode, final ICardRegistrationCallback listener) {
        final RegistrationParams params = new RegistrationParams(new Crypto(), CertificateManager.getInstance());
        params.setParametersAndEncrypt(context, cardNumber, expiryMonth, expiryYear, securityCode,
                new IOnEncryptionListener() {
                    @Override
                    public void onEncryptionComplete() {
                        registerCreditCard(context, params, listener);
                    }

                    @Override
                    public void onEncryptionError() {
                        if (listener != null) listener.onRegistrationError(null);
                    }
                });
    }

    /**
     * Registers a credit card with the given details for future recurring payments.
     * <p>When card has been registered a truncated version of the card is saved to disk.</p>
     *
     * @param context       App Context object
     * @param cardHolder    Card holder
     * @param cardNumber    Card number
     * @param expiryMonth   Expiry month of the card
     * @param expiryYear    Expiry year of the card
     * @param securityCode  Security code (CVC/CVV)
     * @param listener      Result listener
     */
    public void registerCreditCard(final Context context,  String cardHolder, String cardNumber, String expiryMonth, String expiryYear,
                                   String securityCode, final ICardRegistrationCallback listener) {
        final RegistrationParams params = new RegistrationParams(new Crypto(), CertificateManager.getInstance());
        params.setParametersAndEncrypt(context, cardHolder, cardNumber, expiryMonth, expiryYear, securityCode,
                new IOnEncryptionListener() {
                    @Override
                    public void onEncryptionComplete() {
                        registerCreditCard(context, params, listener);
                    }

                    @Override
                    public void onEncryptionError() {
                        if (listener != null) listener.onRegistrationError(null);
                    }
                });
    }

    private void registerCreditCard(final Context context, RegistrationParams params,
                                    final ICardRegistrationCallback listener) {
        PaymentApiService.PaymentService.registerCreditCard(params, new ICardRegistrationCallback() {
            @Override
            public void onRegistrationSuccess(CreditCard creditCard) {
                saveCreditCard(context, creditCard, new IOnCreditCardSaved() {
                    @Override
                    public void onCreditCardSaved(CreditCard creditCard) {
                        if (listener != null) {
                            listener.onRegistrationSuccess(creditCard);
                        }
                    }
                });
            }

            @Override
            public void onRegistrationError(RequestError error) {
                if (listener != null) {
                    listener.onRegistrationError(error);
                }
            }
        });
    }

    /**
     * Adds a credit card to existing stored credit cards.
     * @param context    App Context object
     * @param creditCard Credit card object to be stored
     */
    public void saveCreditCard(final Context context, final CreditCard creditCard, final IOnCreditCardSaved onSavedListener) {
        getCreditCards(context, new IOnCreditCardRead() {
            @Override
            public void onCreditCardRead(List<CreditCard> storedCreditCards) {
                int location = getLocationInList(storedCreditCards, creditCard);
                if (location >= 0) {
                    // If the card already exists, replace it
                    storedCreditCards.remove(location);
                    storedCreditCards.add(location, creditCard);
                } else {
                    storedCreditCards.add(creditCard);
                }
                OnCardSavedNotifier onCardSaved = new OnCardSavedNotifier(creditCard, onSavedListener);
                fileStorage.saveCreditCards(context, toArray(storedCreditCards), onCardSaved);
            }
        });
    }

    /**
     * Reads all stored credit cards from local storage asynchronously and notifies listener.
     * @param context  App Context object
     * @param listener Callback listener; called when list of cards have been loaded
     */
    public void getCreditCards(Context context, final IOnCreditCardRead listener) {
        if (listener == null) {
            return;
        }
        fileStorage.getCreditCards(context, new FileStorage.IOnObjectRead() {
            @Override
            public void notifyOnObjectRead(Serializable object) {
                CreditCard[] creditCards = object instanceof CreditCard[] ? (CreditCard[]) object : new CreditCard[0];
                listener.onCreditCardRead(new ArrayList<>(Arrays.asList(creditCards)));
            }
        });
    }

    /**
     * Deletes a stored credit card with given token from local disk.
     * @param context           App context object
     * @param creditCardToken   Token for credit card to be deleted
     * @param listener          Result listener
     */
    public void deleteCreditCard(final Context context, final String creditCardToken, final IOnCreditCardDeleted listener) {
        final IOnObjectSaved onObjectSavedListener = new IOnObjectSaved() {
            @Override
            public void notifyOnObjectSaved() {
                if (listener != null) {
                    listener.onCreditCardDeleted();
                }
            }
        };
        getCreditCards(context, new IOnCreditCardRead() {
            @Override
            public void onCreditCardRead(List<CreditCard> creditCards) {
                List<CreditCard> newList = removeFromList(creditCards, creditCardToken);
                fileStorage.saveCreditCards(context, toArray(newList), onObjectSavedListener);
            }
        });
    }

    /**
     * Updates credit card alias for card with given token.
     * @param context   App Context object
     * @param alias     Name/Alias for the card
     * @param token     Credit card token
     * @param listener  Status listener. Called when update is completed.
     */
    public void setAlias(final Context context, final String alias, final String token, final IOnCreditCardSaved listener) {
        getCreditCards(context, new IOnCreditCardRead() {
            @Override
            public void onCreditCardRead(List<CreditCard> creditCards) {
                CreditCard creditCard = findCreditCardInList(creditCards, token);
                if (creditCard != null) {
                    creditCard.setAlias(alias);
                    OnCardSavedNotifier onCardUpdated = new OnCardSavedNotifier(creditCard, listener);
                    fileStorage.saveCreditCards(context, toArray(creditCards), onCardUpdated);
                }
            }
        });
    }

    /**
     * Checks if given {@link CreditCard} exists in given array
     * @param listToSearch  Credit card array to check
     * @param cardToFind    Credit card to find
     * @return True if card exists in array
     */
    private boolean existsIn(List<CreditCard> listToSearch, CreditCard cardToFind) {
        for (CreditCard card : listToSearch) {
            if (card.isEqualTo(cardToFind)) {
                return true;
            }
        }
        return false;
    }

    private CreditCard[] toArray(List<CreditCard> creditCardList) {
        CreditCard[] creditCardArray = new CreditCard[creditCardList.size()];
        return creditCardList.toArray(creditCardArray);
    }

    private List<CreditCard> removeFromList(List<CreditCard> creditCardList, String token) {
        Iterator<CreditCard> iterator = creditCardList.iterator();
        while (iterator.hasNext()) {
            CreditCard creditCard = iterator.next(); // must be called before you can call i.remove()
            if (creditCard.getCreditCardToken().equals(token)) {
                iterator.remove();
            }
        }
        return creditCardList;
    }

    private int getLocationInList(List<CreditCard> creditCardList, CreditCard creditCard) {
        for (int location = 0; location < creditCardList.size(); location++) {
            if (creditCardList.get(location).isEqualTo(creditCard)) {
                return location;
            }
        }
        return -1;
    }

    private CreditCard findCreditCardInList(List<CreditCard> creditCardList, String token) {
        for (CreditCard creditCard : creditCardList) {
            if (creditCard.getCreditCardToken().equals(token)) {
                return creditCard;
            }
        }
        return null;
    }

    /**
     * Interface for callback listener when loading credit card from local storage
     */
    public interface IOnCreditCardRead {
        void onCreditCardRead(List<CreditCard> creditCards);
    }

    /**
     * Interface for callback listener when saving/updating a credit card. Called when task is
     * completed.
     */
    public interface IOnCreditCardSaved {
        void onCreditCardSaved(CreditCard creditCard);
    }

    /**
     * Interface for callback listener when deleting a credit card. Called when task is
     * completed.
     */
    public interface IOnCreditCardDeleted {
        void onCreditCardDeleted();
    }


    /**
     * Helper class for notifying listener when a {@link CreditCard} has been saved. The notifier
     * returns the card that was saved.
     */
    private class OnCardSavedNotifier implements IOnObjectSaved {
        private CreditCard creditCard;
        private IOnCreditCardSaved onCreditCardSaved;

        public OnCardSavedNotifier(CreditCard creditCard, IOnCreditCardSaved onCreditCardSaved) {
            this.creditCard = creditCard;
            this.onCreditCardSaved = onCreditCardSaved;
        }

        @Override
        public void notifyOnObjectSaved() {
            if (onCreditCardSaved != null) {
                onCreditCardSaved.onCreditCardSaved(this.creditCard);
            }
        }
    }
}
