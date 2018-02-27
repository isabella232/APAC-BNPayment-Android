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

package com.bambora.nativepayment.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bambora.nativepayment.R;
import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.interfaces.ICardRegistrationCallback;
import com.bambora.nativepayment.models.CardRegistrationFormGuiSetting;
import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.network.RequestError;
import com.bambora.nativepayment.utils.CompatHelper;
import com.bambora.nativepayment.widget.edittext.CardFormEditText;
import com.bambora.nativepayment.widget.edittext.CardFormEditText.IOnValidationEventListener;
import com.bambora.nativepayment.widget.edittext.CardHolderEditText;
import com.bambora.nativepayment.widget.edittext.CardNumberEditText;
import com.bambora.nativepayment.widget.edittext.ExpiryDateEditText;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 */
public class CardRegistrationFormLayout extends RelativeLayout implements IOnValidationEventListener,ICardRegistrationCallback {

    private ICardRegistrationCallback resultListenerForInternal;
    private ICardRegistrationCallback resultListenerForExternal;
    private TextView pageTitle;
    private CardHolderEditText cardHolderEditText;
    private CardNumberEditText cardNumberEditText;
    private ExpiryDateEditText expiryDateEditText;
    private CardFormEditText securityCodeEditText;
    private Map<EditText, Boolean> inputValidStates = new HashMap<>();
    private Button registrationButton;
    private CardRegistrationFormGuiSetting registrationGuiSetting;
    private ProgressBar progressBar;
    public CardRegistrationFormLayout(Context context) {
        super(context);
        setupView(context);
    }

    public CardRegistrationFormLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public CardRegistrationFormLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CardRegistrationFormLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupView(context);
    }

    public void setRegistrationResultListener(ICardRegistrationCallback resultListener) {
        this.resultListenerForExternal = resultListener;
    }

    public void setTitle(String title) {
        TextView titleTextView = (TextView) findViewById(R.id.tv_title);
        if (titleTextView != null) titleTextView.setText(title);
    }

    public void setFormGuiSetting(CardRegistrationFormGuiSetting registrationGuiSetting) {
        //catch private registrationGuiSetting in the local scope .
        this.registrationGuiSetting = registrationGuiSetting;
        //Vew customization.
        viewCustomization();
    }
    @Override
    public void onFocusChanged(EditText view, boolean hasFocus, boolean inputValid) {
        if (hasFocus) {
            view.setTextColor(CompatHelper.getColor(getContext(), R.color.bn_black, null));
        } else if (!inputValid) {
            view.setTextColor(CompatHelper.getColor(getContext(), R.color.bn_red, null));
        }
    }

    @Override
    public void onInputValidated(EditText view, boolean inputValid) {
        inputValidStates.put(view, inputValid);
        updateButtonState();
    }

    private void setupView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.native_card_registration_form, this);
        pageTitle = (TextView) findViewById(R.id.tv_title);
        cardHolderEditText = (CardHolderEditText) findViewById(R.id.et_card_holder);
        cardNumberEditText = (CardNumberEditText) findViewById(R.id.et_card_number);
        expiryDateEditText = (ExpiryDateEditText) findViewById(R.id.et_expiry_date);
        securityCodeEditText = (CardFormEditText) findViewById(R.id.et_security_code);

        registrationButton = (Button) findViewById(R.id.btn_register);
        registrationButton.setOnClickListener(onRegisterButtonClickListener);

        cardHolderEditText.setValidationListener(this);
        cardNumberEditText.setValidationListener(this);
        expiryDateEditText.setValidationListener(this);
        securityCodeEditText.setValidationListener(this);

        inputValidStates.put(cardHolderEditText, true);
        inputValidStates.put(cardNumberEditText, false);
        inputValidStates.put(expiryDateEditText, false);
        inputValidStates.put(securityCodeEditText, true);

        progressBar=new ProgressBar(context,null,android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addView(progressBar,params);
        progressBar.setVisibility(View.INVISIBLE);

        this.resultListenerForInternal = this;
    }

    private void viewCustomization()
    {
        if(registrationGuiSetting==null)
        {
            return;
        }
        TitleCustomization();
        RegisterButtonCustomization();
        CardHolderCustomization();
        CardNumberCustomization();
        ExpiryDateCustomization();
        SecurityCodeCustomization();
    }

    private void TitleCustomization()
    {
        //Title Text
        if(registrationGuiSetting.TitleText!=null &&
           registrationGuiSetting.TitleText.trim().length()>0 &&
                pageTitle!=null)
        {
            pageTitle.setText(registrationGuiSetting.TitleText);
        }
    }

    private void RegisterButtonCustomization()
    {
         //Register Button Text
        if(registrationButton!=null)
        {
            if(registrationGuiSetting.RegisterButtonText!=null && registrationGuiSetting.RegisterButtonText.trim().length()>0 &&
                    registrationButton!=null)
            {
               registrationButton.setText(registrationGuiSetting.RegisterButtonText);
            }
            //Todo: More validation
            if(registrationGuiSetting.RegisterButtonColor!=null && registrationGuiSetting.RegisterButtonColor.trim().length()==7)
            {
                int colorValue = Color.parseColor(registrationGuiSetting.RegisterButtonColor);
                registrationButton.setBackgroundColor(colorValue);
            }
        }
    }

    private void CardHolderCustomization()
    {
        //Card Holder Name Watermark
        if(cardHolderEditText!=null)
        {
            if(registrationGuiSetting.CardHolderWatermark!=null &&
                    registrationGuiSetting.CardHolderWatermark.trim().length()>0)
            {
                cardHolderEditText.setHint(registrationGuiSetting.CardHolderWatermark);
            }

        }
    }

    private void CardNumberCustomization()
    {
        //CardNumber Watermark
        if(registrationGuiSetting.CardNumberWatermark!=null &&
                registrationGuiSetting.CardNumberWatermark.trim().length()>0 &&
                cardNumberEditText!=null)
        {
            cardNumberEditText.setHint(registrationGuiSetting.CardNumberWatermark);
        }
    }

    private void ExpiryDateCustomization()
    {
        //ExpiryDate Watermark
        if(registrationGuiSetting.ExpiryDateWatermark!=null &&
                registrationGuiSetting.ExpiryDateWatermark.trim().length()>0 &&
                expiryDateEditText!=null)
        {
            expiryDateEditText.setHint(registrationGuiSetting.ExpiryDateWatermark);
        }
    }

    private void SecurityCodeCustomization()
    {
        //SecurityCode Watermark
        if(registrationGuiSetting.SecurityCodeWatermark!=null &&
                registrationGuiSetting.SecurityCodeWatermark.trim().length()>0 &&
                securityCodeEditText!=null)
        {
            securityCodeEditText.setHint(registrationGuiSetting.SecurityCodeWatermark);
        }
    }

    private void updateButtonState() {
        boolean enabled = true;
        for (EditText key : inputValidStates.keySet()) {
            if (!inputValidStates.get(key)) {
                enabled = false;
                break;
            }
        }
        registrationButton.setEnabled(enabled);
    }


    private void startLoadingUI(){
        registrationButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

    }

    private void stopLoadingUI(){
        registrationButton.setEnabled(true);
        updateButtonState();
        progressBar.setVisibility(View.INVISIBLE);

    }


    private OnClickListener onRegisterButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            startLoadingUI();
            BNPaymentHandler.getInstance().registerCreditCard(
                    getContext(),
                    cardHolderEditText.getText().toString(),
                    cardNumberEditText.getText().toString(),
                    expiryDateEditText.getEnteredExpiryMonth(),
                    expiryDateEditText.getEnteredExpiryYear(),
                    securityCodeEditText.getText().toString(),
                    resultListenerForInternal);
        }
    };


    @Override
    public void onRegistrationSuccess(CreditCard creditCard) {
        stopLoadingUI();
        resultListenerForExternal.onRegistrationSuccess(creditCard);
    }

    @Override
    public void onRegistrationError(RequestError error) {
        stopLoadingUI();
        resultListenerForExternal.onRegistrationError(error);
    }



}
