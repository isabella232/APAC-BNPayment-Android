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

package com.bambora.nativepayment.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.bambora.nativepayment.R;
import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.models.creditcard.RegistrationFormError;
import com.bambora.nativepayment.viewmodels.CreditCardRegistrationViewModel;
import com.bambora.nativepayment.webview.CreditCardRegistrationWebViewClient.IOnPageFinishedListener;

/**
 * A WebView for wrapping credit card registration.
 * <p>This class will initiate credit card registration by fetching a user specific URL to
 * the Bambora Hosted Payment Pages. The URL will be loaded into this WebView when view is attached
 * to window.</p>
 * <p>Use {@link CreditCardRegistrationWebView#setStateChangeListener(IStateChangeListener)} to listen
 * for registration state and results.</p>
 */
public class CreditCardRegistrationWebView extends WebView implements ICreditCardRegistrationView,
        IOnPageFinishedListener {

    private CreditCardRegistrationViewModel viewModel;

    /**
     * Listener for registration status. Notified when the registration state changes.
     */
    private IStateChangeListener stateChangeListener;

    public CreditCardRegistrationWebView(Context context) {
        super(context);
        initView(context, null);
    }

    public CreditCardRegistrationWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CreditCardRegistrationWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    /**
     * Sets registration status listener.
     * @param listener The {@link IStateChangeListener} that is to receive registration status
     */
    public void setStateChangeListener(IStateChangeListener listener) {
        this.stateChangeListener = listener;
    }

    /**
     * Sets the URL to a CSS file that is used for styling the registration form.
     * @param cssUrl    URL to CSS file
     */
    public void setCssUrl(String cssUrl) {
        viewModel.setCssUrl(cssUrl);
    }

    /**
     * Sets hint for card number input field
     * @param cardNumberHint    Hint text
     */
    public void setCardNumberHint(String cardNumberHint) {
        viewModel.setCardNumberHint(cardNumberHint);
    }

    /**
     * Sets hint for card expiry input field
     * @param cardExpiryHint      Hint text
     */
    public void setCardExpiryHint(String cardExpiryHint) {
        viewModel.setCardExpiryHint(cardExpiryHint);
    }

    /**
     * Sets hint for CVV input field
     * @param cardCvvHint      Hint text
     */
    public void setCardCvvHint(String cardCvvHint) {
        viewModel.setCardCvvHint(cardCvvHint);
    }

    /**
     * Sets the text that's visible on the submit button
     * @param text  Button text
     */
    public void setSubmitButtonText(String text) {
        viewModel.setSubmitButtonText(text);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        viewModel.getRegistrationFormUrl();
    }

    @Override
    public void loadRegistrationUrl(String url) {
        loadUrl(url);
    }

    @Override
    public void onPageFinished() {
        if (stateChangeListener != null) {
            stateChangeListener.onPageFinished();
        }
    }

    @Override
    public void notifyRegistrationStarted() {
        if (stateChangeListener != null) {
            stateChangeListener.onRegistrationStarted();
        }
    }

    @Override
    public void notifyRegistrationSuccess(CreditCard creditCard) {
        if (stateChangeListener != null) {
            stateChangeListener.onRegistrationCompleted(creditCard);
        }

    }

    @Override
    public void notifyError(RegistrationFormError error) {
        if (stateChangeListener != null) {
            stateChangeListener.onFailure(error);
        }
    }

    /**
     * Enables JavaScript and exposes javascript interfaces in
     * {@link CreditCardRegistrationJsInterface} for JavaScript calls.
     * <p>It is important not to load any other URL then the one returned from
     * the credit card registration initiation API in this webview. Since JavaScript is enabled
     * the view is otherwise vulnerable for JavaScript attacks.</p>
     */
    @SuppressLint({
            "SetJavaScriptEnabled",
            "AddJavascriptInterface"
    })
    private void initView(Context context, AttributeSet attributeSet) {
        viewModel = new CreditCardRegistrationViewModel(getContext(), this);
        setWebViewClient(new CreditCardRegistrationWebViewClient(this));
        getAttributes(context, attributeSet);
        getSettings().setJavaScriptEnabled(true);
        addJavascriptInterface(new CreditCardRegistrationJsInterface(viewModel), "Android");
    }

    /**
     * Gets styleable XML attributes and stores them and updates HPP form settings.
     * @param context   App Context object
     * @param attrs     XML attribute set
     */
    private void getAttributes(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.CreditCardRegistrationWebView);
        try {
            setCardNumberHint(styledAttributes.getString(R.styleable.CreditCardRegistrationWebView_card_number_hint));
            setCardExpiryHint(styledAttributes.getString(R.styleable.CreditCardRegistrationWebView_card_expiry_hint));
            setCardCvvHint(styledAttributes.getString(R.styleable.CreditCardRegistrationWebView_card_cvv_hint));
            setSubmitButtonText(styledAttributes.getString(R.styleable.CreditCardRegistrationWebView_submit_button_text));
        } finally {
            styledAttributes.recycle();
        }
    }

    /**
     * Interface for registration status.
     */
    public interface IStateChangeListener {
        void onPageFinished();
        void onRegistrationStarted();
        void onRegistrationCompleted(CreditCard creditCard);
        void onFailure(RegistrationFormError error);
    }
}
