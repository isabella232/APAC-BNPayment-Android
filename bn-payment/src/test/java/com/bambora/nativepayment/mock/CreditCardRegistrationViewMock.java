package com.bambora.nativepayment.mock;

import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.models.creditcard.RegistrationFormError;
import com.bambora.nativepayment.webview.ICreditCardRegistrationView;

/**
 * @author Lovisa Corp
 */
public class CreditCardRegistrationViewMock implements ICreditCardRegistrationView {

    private boolean onNotifyRegistrationStartedCalled;
    private boolean onNotifySuccessCalled;
    private boolean onNotifyFailureCalled;
    private RegistrationFormError registrationFormError;
    private CreditCard registeredCard;

    @Override
    public void loadRegistrationUrl(String url) {
        // Never called in tests
    }

    @Override
    public void notifyRegistrationStarted() {
        onNotifyRegistrationStartedCalled = true;
    }

    @Override
    public void notifyRegistrationSuccess(CreditCard creditCard) {
        onNotifySuccessCalled = true;
        registeredCard = creditCard;
    }

    @Override
    public void notifyError(RegistrationFormError error) {
        onNotifyFailureCalled = true;
        this.registrationFormError = error;
    }

    public boolean wasOnNotifyRegistrationStartedCalled() {
        return onNotifyRegistrationStartedCalled;
    }

    public boolean wasOnNotifySuccessCalled() {
        return onNotifySuccessCalled;
    }

    public boolean wasOnNotifyFailureCalled() {
        return onNotifyFailureCalled;
    }

    public RegistrationFormError getRegistrationFormError() {
        return registrationFormError;
    }

    public CreditCard getRegisteredCard() {
        return registeredCard;
    }
}
