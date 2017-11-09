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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bambora.nativepayment.R;
import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.managers.CreditCardManager;
import com.bambora.nativepayment.models.CardRegistrationFormGuiSetting;
import com.bambora.nativepayment.models.PaymentSettings;
import com.bambora.nativepayment.interfaces.ITransactionExtListener;
import com.bambora.nativepayment.models.PaymentType;
import com.bambora.nativepayment.models.SubmitPaymentCardFormGuiSetting;
import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.utils.CompatHelper;
import com.bambora.nativepayment.widget.edittext.CardFormEditText;
import com.bambora.nativepayment.widget.edittext.CardFormEditText.IOnValidationEventListener;
import com.bambora.nativepayment.widget.edittext.CardHolderEditText;
import com.bambora.nativepayment.widget.edittext.CardNumberEditText;
import com.bambora.nativepayment.widget.edittext.ExpiryDateEditText;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ch0110593 on 7/25/2017.
 */

public class TransactionCardFormLayout extends RelativeLayout implements IOnValidationEventListener {

    private ITransactionExtListener resultListener;
    private CreditCardManager.IOnCreditCardSaved onSavedCardListener;
    private PaymentSettings paymentSettings;
    private String paymentId;
    private TextView pageTitle;
    private CardHolderEditText cardHolderEditText;
    private CardNumberEditText cardNumberEditText;
    private ExpiryDateEditText expiryDateEditText;
    private CardFormEditText securityCodeEditText;
    private ToggleButton isSaveCardButton;
    private Map<EditText, Boolean> inputValidStates = new HashMap<>();
    private Button registrationButton;
    private SubmitPaymentCardFormGuiSetting submitPaymentCardFormGuiSetting;

    public TransactionCardFormLayout(Context context) {
        super(context);
        setupView(context);
    }

    public TransactionCardFormLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public TransactionCardFormLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TransactionCardFormLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupView(context);
    }

    public void setTransactionCardResultListener(ITransactionExtListener resultListener) {
        this.resultListener = resultListener;
    }
    public void setSavedCardListener(CreditCardManager.IOnCreditCardSaved onSavedListener) {
        this.onSavedCardListener = onSavedListener;
    }


    public void setTransactionParams(PaymentSettings paymentSettings, String paymentId)
    {
        this.paymentId = paymentId;
        this.paymentSettings = paymentSettings;
    }


    public void setTitle(String title) {
        TextView titleTextView = (TextView) findViewById(R.id.tv_title);
        if (titleTextView != null) titleTextView.setText(title);
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
        inflater.inflate(R.layout.native_submit_single_payment_card_form, this);
        pageTitle = (TextView) findViewById(R.id.tv_title);
        cardHolderEditText = (CardHolderEditText) findViewById(R.id.et_card_holder);
        cardNumberEditText = (CardNumberEditText) findViewById(R.id.et_card_number);
        expiryDateEditText = (ExpiryDateEditText) findViewById(R.id.et_expiry_date);
        securityCodeEditText = (CardFormEditText) findViewById(R.id.et_security_code);
        isSaveCardButton = (ToggleButton) findViewById(R.id.togg_is_save_card_btn);
        registrationButton = (Button) findViewById(R.id.btn_register);
        registrationButton.setOnClickListener(onSubmitSinglePaymentCardButtonClickListener);

        cardHolderEditText.setValidationListener(this);
        cardNumberEditText.setValidationListener(this);
        expiryDateEditText.setValidationListener(this);
        securityCodeEditText.setValidationListener(this);

        inputValidStates.put(cardHolderEditText, true);
        inputValidStates.put(cardNumberEditText, false);
        inputValidStates.put(expiryDateEditText, false);
        inputValidStates.put(securityCodeEditText, true);
    }

    public void setFormGuiSetting(SubmitPaymentCardFormGuiSetting guiSetting) {
        //catch private SubmitPaymentCardFormGuiSetting in the local scope .
        this.submitPaymentCardFormGuiSetting = guiSetting;
        //Vew customization.
        viewCustomization();
    }

    private void viewCustomization()
    {
        if(submitPaymentCardFormGuiSetting==null)
        {
            return;
        }
        TitleCustomization();
        SubmitSinglePaymentCardButtonCustomization();
        SubmitSinglePaymentCardSwitchButtonCustomization();
        CardHolderCustomization();
        CardNumberCustomization();
        ExpiryDateCustomization();
        SecurityCodeCustomization();
    }

    private void TitleCustomization()
    {
        //Title Text
        if(submitPaymentCardFormGuiSetting.TitleText!=null &&
                submitPaymentCardFormGuiSetting.TitleText.trim().length()>0 &&
                pageTitle!=null)
        {
            pageTitle.setText(submitPaymentCardFormGuiSetting.TitleText);
        }
    }

    private void SubmitSinglePaymentCardButtonCustomization()
    {
        //Register Button Text
        if(registrationButton!=null)
        {
            if(submitPaymentCardFormGuiSetting.PayByCardButtonText.trim().length()>0 &&
                    registrationButton!=null)
            {
                registrationButton.setText(submitPaymentCardFormGuiSetting.PayByCardButtonText);
            }
            //Todo: More validation
            if(submitPaymentCardFormGuiSetting.PayByCardButtonColor.trim().length()==7)
            {
                int colorValue = Color.parseColor(submitPaymentCardFormGuiSetting.PayByCardButtonColor);
                registrationButton.setBackgroundColor(colorValue);
            }
        }
    }

    private void SubmitSinglePaymentCardSwitchButtonCustomization()
    {
        //Register Button Text
        if(isSaveCardButton!=null)
        {
            if(submitPaymentCardFormGuiSetting.SwitchButtonColor.trim().length()==7)
            {
                int colorValue = Color.parseColor(submitPaymentCardFormGuiSetting.SwitchButtonColor);
                isSaveCardButton.setBackgroundColor(colorValue);
            }
        }
    }

    private void CardHolderCustomization()
    {
        //Card Holder Name Watermark
        if(cardHolderEditText!=null)
        {
            if(submitPaymentCardFormGuiSetting.CardHolderWatermark!=null &&
                    submitPaymentCardFormGuiSetting.CardHolderWatermark.trim().length()>0)
            {
                cardHolderEditText.setHint(submitPaymentCardFormGuiSetting.CardHolderWatermark);
            }

        }
    }

    private void CardNumberCustomization()
    {
        //CardNumber Watermark
        if(submitPaymentCardFormGuiSetting.CardNumberWatermark!=null &&
                submitPaymentCardFormGuiSetting.CardNumberWatermark.trim().length()>0 &&
                cardNumberEditText!=null)
        {
            cardNumberEditText.setHint(submitPaymentCardFormGuiSetting.CardNumberWatermark);
        }
    }

    private void ExpiryDateCustomization()
    {
        //ExpiryDate Watermark
        if(submitPaymentCardFormGuiSetting.ExpiryDateWatermark!=null &&
                submitPaymentCardFormGuiSetting.ExpiryDateWatermark.trim().length()>0 &&
                expiryDateEditText!=null)
        {
            expiryDateEditText.setHint(submitPaymentCardFormGuiSetting.ExpiryDateWatermark);
        }
    }

    private void SecurityCodeCustomization()
    {
        //SecurityCode Watermark
        if(submitPaymentCardFormGuiSetting.SecurityCodeWatermark!=null &&
                submitPaymentCardFormGuiSetting.SecurityCodeWatermark.trim().length()>0 &&
                securityCodeEditText!=null)
        {
            securityCodeEditText.setHint(submitPaymentCardFormGuiSetting.SecurityCodeWatermark);
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

    /**
     * This method is called when the 'Pay' button is clicked. It handles both purchase and
     * pre-auth. These actions are differentiated based on paymentType.
     */
    private OnClickListener onSubmitSinglePaymentCardButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            BNPaymentHandler.getInstance().submitSingleTransactionCard(
                    getContext(),
                    paymentId, paymentSettings, cardHolderEditText.getText().toString(),
                    cardNumberEditText.getText().toString(),
                    expiryDateEditText.getEnteredExpiryMonth(),
                    expiryDateEditText.getEnteredExpiryYear(),
                    securityCodeEditText.getText().toString(),
                    isSaveCardButton.isChecked(), resultListener, onSavedCardListener);
        }
    };
}
