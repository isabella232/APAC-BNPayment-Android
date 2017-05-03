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

package com.bambora.nativepayment.storage;

import android.content.Context;

import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.security.EncryptionCertificate;

/**
 * Extension of {@link FileStorage} for handling transaction lib specific files
 *
 * @author Lovisa Corp
 */
public class TransactionFileStorage extends FileStorage {

    private static final String CREDIT_CARD_FILE_NAME = "credit-card";
    private static final String CERTIFICATES_FILE_NAME = "certificates";

    /**
     * Saves an array of {@link CreditCard}s to disk
     * @param context        The context to be used
     * @param creditCards    Array of credit cards to be stored
     */
    public void saveCreditCards(Context context, CreditCard[] creditCards, IOnObjectSaved listener) {
        saveObjectToDisk(context, CREDIT_CARD_FILE_NAME, creditCards, listener);
    }

    public void saveCreditCards(Context context, CreditCard[] creditCards) {
        saveCreditCards(context, creditCards, null);
    }

    /**
     * Retrieves an array of all stored {@link CreditCard}s from disk and notifies result listener
     * when completed. Returns null if no list is found.
     * @param context     The context to be used
     * @param listener    The result listener
     */
    public void getCreditCards(Context context, IOnObjectRead listener) {
        getObjectFromDisk(context, CREDIT_CARD_FILE_NAME, listener);
    }

    /**
     * TODO
     * @param context
     * @param listener
     */
    public void getEncryptionCertificates(Context context, IOnObjectRead listener) {
        getObjectFromDisk(context, CERTIFICATES_FILE_NAME, listener);
    }

    /**
     * TODO
     * @param context
     * @param certificates
     * @param listener
     */
    public void saveEncryptionCertificates(Context context, EncryptionCertificate[] certificates,
                                           IOnObjectSaved listener) {
        saveObjectToDisk(context, CERTIFICATES_FILE_NAME, certificates, listener);
    }
}
