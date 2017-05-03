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

package com.bambora.nativepayment.viewmodels;

import android.content.Context;
import android.util.Log;

import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.managers.CreditCardManager;
import com.bambora.nativepayment.managers.CreditCardManager.IOnCreditCardSaved;
import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.models.creditcard.RegistrationFormError;
import com.bambora.nativepayment.models.creditcard.RegistrationFormSettings;
import com.bambora.nativepayment.models.creditcard.RegistrationResult;
import com.bambora.nativepayment.models.creditcard.RegistrationResultAction.ActionCode;
import com.bambora.nativepayment.services.PaymentApiService.IHppResultListener;
import com.bambora.nativepayment.webview.CreditCardRegistrationJsInterface.IJavascriptCallbackListener;
import com.bambora.nativepayment.webview.ICreditCardRegistrationView;

/**
 * @author Lovisa Corp
 */
public class CreditCardRegistrationViewModel implements IJavascriptCallbackListener {

    private Context context;
    private ICreditCardRegistrationView view;
    private CreditCardManager creditCardManager;
    private RegistrationFormSettings formSettings = new RegistrationFormSettings();

    public CreditCardRegistrationViewModel(Context context, ICreditCardRegistrationView view) {
        this.context = context;
        this.view = view;
        this.creditCardManager = new CreditCardManager();
    }

    /**
     * Sets the URL to a CSS file that is used for styling the registration form.
     * @param cssUrl    URL to CSS file
     */
    public void setCssUrl(String cssUrl) {
        this.formSettings.setCssUrl(cssUrl);
    }

    /**
     * Sets hint for card number input field
     * @param cardNumberHint    Hint text
     */
    public void setCardNumberHint(String cardNumberHint) {
        this.formSettings.setCardNumberInputHint(cardNumberHint);
    }

    /**
     * Sets hint for card expiry input field
     * @param cardExpiryHint      Hint text
     */
    public void setCardExpiryHint(String cardExpiryHint) {
        this.formSettings.setCardExpiryInputHint(cardExpiryHint);
    }

    /**
     * Sets hint for CVV input field
     * @param cardCvvHint      Hint text
     */
    public void setCardCvvHint(String cardCvvHint) {
        this.formSettings.setCvvInputHint(cardCvvHint);
    }

    /**
     * Sets the text that's visible on the submit button
     * @param text  Button text
     */
    public void setSubmitButtonText(String text) {
        this.formSettings.setSubmitButtonText(text);
    }

    @Override
    public void onJavascriptCall(RegistrationResult result) {
        handleRegistrationResult(result);
    }

    /**
     * Initiates credit card registration form by fetching a user session specific URL for the
     * Hosted Payment Pages.
     */
    public void getRegistrationFormUrl() {
        BNPaymentHandler.getInstance().initiateHostedPaymentPage(formSettings, new IHppResultListener() {
            @Override
            public void onHppInitiated(String url) {
                view.loadRegistrationUrl(url);
            }

            @Override
            public void onError(RegistrationFormError registrationFormError) {
                view.notifyError(registrationFormError);
            }
        });
    }

    /**
     * Parses a status code from a {@link RegistrationResult} and notifies the
     * {@link ICreditCardRegistrationView}
     *
     * @param result {@link RegistrationResult}
     */
    private void handleRegistrationResult(RegistrationResult result) {
        ActionCode actionCode = result != null ? result.getActionCode() : ActionCode.UNKNOWN;
        switch (actionCode) {
            case SUBMISSION_STARTED:
                view.notifyRegistrationStarted();
                break;
            case SUCCESS:
                handleSuccessResult(result);
                break;
            case SUBMISSION_DECLINED:
                view.notifyError(RegistrationFormError.SUBMISSION_DECLINED);
                break;
            case SYSTEM_ERROR:
                view.notifyError(RegistrationFormError.SYSTEM_ERROR);
                break;
            case SESSION_ERROR:
                view.notifyError(RegistrationFormError.SESSION_ERROR);
                break;
            default:
                view.notifyError(RegistrationFormError.UNKNOWN);
        }
    }

    private void handleSuccessResult(RegistrationResult result) {
        if (result != null) {
            CreditCard creditCard = new CreditCard(result);
            saveCreditCard(creditCard);
        } else {
            Log.e(getClass().getSimpleName(), "Error parsing registration result. No CreditCard attached.");
            view.notifyError(RegistrationFormError.UNKNOWN);
        }
    }

    private void saveCreditCard(final CreditCard creditCard) {
        creditCardManager.saveCreditCard(context, creditCard, new IOnCreditCardSaved() {
            @Override
            public void onCreditCardSaved(CreditCard creditCard) {
                view.notifyRegistrationSuccess(creditCard);
            }
        });
    }
}
