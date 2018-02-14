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
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bambora.nativepayment.R;
import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.interfaces.VisaCheckoutDataCallback;
import com.bambora.nativepayment.interfaces.VisaCheckoutTransactionCallback;
import com.bambora.nativepayment.managers.CreditCardManager;
import com.bambora.nativepayment.models.PaymentSettings;
import com.bambora.nativepayment.interfaces.ITransactionExtListener;
import com.bambora.nativepayment.models.SubmitPaymentCardFormGuiSetting;
import com.bambora.nativepayment.models.TransactionResponse;
import com.bambora.nativepayment.models.VisaCheckoutLaunchParams;
import com.bambora.nativepayment.models.VisaCheckoutResponse;
import com.bambora.nativepayment.models.VisaCheckoutTransactionParams;
import com.bambora.nativepayment.network.RequestError;
import com.bambora.nativepayment.utils.CompatHelper;
import com.bambora.nativepayment.widget.edittext.CardFormEditText;
import com.bambora.nativepayment.widget.edittext.CardFormEditText.IOnValidationEventListener;
import com.bambora.nativepayment.widget.edittext.CardHolderEditText;
import com.bambora.nativepayment.widget.edittext.CardNumberEditText;
import com.bambora.nativepayment.widget.edittext.ExpiryDateEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * Created by ch0110593 on 7/25/2017.
 */

public class TransactionCardFormLayout extends RelativeLayout implements IOnValidationEventListener,ITransactionExtListener,VisaCheckoutDataCallback,VisaCheckoutTransactionCallback {

    private ITransactionExtListener resultListenerForInternal;
    private ITransactionExtListener resultListenerForExternal;
    private VisaCheckoutDataCallback visaCheckoutDataCallback;
    private VisaCheckoutTransactionCallback visaCheckoutTransactionCallback;

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
    private ProgressBar progressBar;
    private VisaCheckoutButton visaCheckoutButton;
    private RelativeLayout visaCheckoutLayout;
    private Activity activity;
    private LinearLayout or_UI;


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
        this.resultListenerForExternal = resultListener;
    }
    public void setSavedCardListener(CreditCardManager.IOnCreditCardSaved onSavedListener) {
        this.onSavedCardListener = onSavedListener;
    }


    public void setTransactionParams(PaymentSettings paymentSettings, String paymentId)
    {
        this.paymentId = paymentId;
        this.paymentSettings = paymentSettings;
        if(paymentSettings.enableVisaCheckout && hasVisaCheckoutSDK())
        {
            launchVisaCheckOut();
        }
        else
        {
            or_UI.setVisibility(View.GONE);
            visaCheckoutLayout.setVisibility(View.GONE);
        }
    }

    public void launchVisaCheckOut(){
        progressBar.setVisibility(View.VISIBLE);
        this.visaCheckoutDataCallback= this;
        BNPaymentHandler.getInstance().getVisaCheckoutData(getContext(),visaCheckoutDataCallback);
    }

    @Override
    public void onVisaCheckoutDataSuccess(VisaCheckoutLaunchParams visaCheckoutLaunchParams) {
        progressBar.setVisibility(View.INVISIBLE);
        visaCheckoutLaunchParams.setAmount(paymentSettings.amount);
        setupVisaCheckOutButtonWithData(visaCheckoutLaunchParams);
    }

    @Override
    public void onVisaCheckoutDataError(RequestError error) {
        progressBar.setVisibility(View.INVISIBLE);
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

        visaCheckoutLayout=(RelativeLayout)findViewById(R.id.visa_checkout_layout);
        visaCheckoutButton = (VisaCheckoutButton) findViewById(R.id.visa_checkout_button);

        or_UI=(LinearLayout)findViewById(R.id.or_UI);

        cardHolderEditText.setValidationListener(this);
        cardNumberEditText.setValidationListener(this);
        expiryDateEditText.setValidationListener(this);
        securityCodeEditText.setValidationListener(this);

        inputValidStates.put(cardHolderEditText, true);
        inputValidStates.put(cardNumberEditText, false);
        inputValidStates.put(expiryDateEditText, false);
        inputValidStates.put(securityCodeEditText, true);

        progressBar=new ProgressBar(context,null,android.R.attr.progressBarStyleLarge);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.bn_purple), PorterDuff.Mode.MULTIPLY);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addView(progressBar,params);
        progressBar.setVisibility(View.INVISIBLE);
        this.resultListenerForInternal = this;

        activity = (Activity)context;

        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard();
                return false;
            }
        });

    }

    public void setFormGuiSetting(SubmitPaymentCardFormGuiSetting guiSetting) {
        //catch private SubmitPaymentCardFormGuiSetting in the local scope .
        this.submitPaymentCardFormGuiSetting = guiSetting;
        //Vew customization.
        viewCustomization();
    }

    private void setupVisaCheckOutButtonWithData(VisaCheckoutLaunchParams visaCheckoutLaunchParams)
    {
        visaCheckoutButton.requestLayout();
        int loadingBarColor;

        if(submitPaymentCardFormGuiSetting!=null && submitPaymentCardFormGuiSetting.PayLoadingBarColor!=null && submitPaymentCardFormGuiSetting.PayLoadingBarColor.trim().length()==7)
        {
            loadingBarColor = Color.parseColor(submitPaymentCardFormGuiSetting.PayLoadingBarColor);
        }
        else
        {
            loadingBarColor = getResources().getColor(R.color.bn_purple);
        }

        visaCheckoutButton.loadUI(visaCheckoutLaunchParams,visaCheckoutLayout,loadingBarColor);
        this.visaCheckoutTransactionCallback =this;
        visaCheckoutButton.visaCheckOutResult= new VisaCheckoutButton.VisaCheckOutResult() {
            @Override
            public void visaCheckoutSuccess(String info) {
                try {
                    JSONObject jsonObject = new JSONObject(info);
                    final VisaCheckoutTransactionParams VisaCheckoutTransactionParams=new VisaCheckoutTransactionParams();
                    VisaCheckoutTransactionParams.callid=jsonObject.getString("callid");
                    VisaCheckoutTransactionParams.encKey=jsonObject.getString("encKey");
                    VisaCheckoutTransactionParams.encPaymentData=jsonObject.getString("encPaymentData");
                    VisaCheckoutTransactionParams.paymentJsonData=paymentSettings.paymentJsonData;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.VISIBLE);
                            BNPaymentHandler.getInstance().processTransaction(getContext(),VisaCheckoutTransactionParams,visaCheckoutTransactionCallback);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void visaCheckoutFail(String info) {
            }

            @Override
            public void visaCheckoutSetupComplete() {
            }
        };
    }

    @Override
    public void onVisaCheckoutTransactionSuccess(VisaCheckoutResponse visaCheckoutResponse) {
        progressBar.setVisibility(View.INVISIBLE);
        Map<String,String> responseMap = new HashMap<String,String>();
        if (visaCheckoutResponse.receipt != null ){
            responseMap.put("receipt", visaCheckoutResponse.receipt);
        }
        resultListenerForExternal.onTransactionSuccess(responseMap);
    }

    @Override
    public void onVisaCheckoutTransactionError(RequestError error) {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private boolean hasVisaCheckoutSDK(){
        try {
            Class.forName( "com.visa.checkout.hybrid.VisaCheckoutPlugin" );
            return true;
        } catch( ClassNotFoundException e ) {
            return false;
        }
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
        SubmitSinglePaymentCardLoadingBarCustomization();
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
            if(submitPaymentCardFormGuiSetting.PayByCardButtonText!=null && submitPaymentCardFormGuiSetting.PayByCardButtonText.trim().length()>0 &&
                    registrationButton!=null)
            {
                registrationButton.setText(submitPaymentCardFormGuiSetting.PayByCardButtonText);
            }
            //Todo: More validation
            if(submitPaymentCardFormGuiSetting.PayByCardButtonColor!=null && submitPaymentCardFormGuiSetting.PayByCardButtonColor.trim().length()==7)
            {
                int colorValue = Color.parseColor(submitPaymentCardFormGuiSetting.PayByCardButtonColor);
                registrationButton.setBackgroundColor(colorValue);
            }
        }
    }


    private void SubmitSinglePaymentCardLoadingBarCustomization()
    {
        //Register Button Text
        if(progressBar!=null)
        {
            if(submitPaymentCardFormGuiSetting.PayLoadingBarColor!=null && submitPaymentCardFormGuiSetting.PayLoadingBarColor.trim().length()==7)
            {
                int colorValue = Color.parseColor(submitPaymentCardFormGuiSetting.PayLoadingBarColor);
                progressBar.getIndeterminateDrawable().setColorFilter(colorValue, PorterDuff.Mode.MULTIPLY);
            }
        }
    }



    private void SubmitSinglePaymentCardSwitchButtonCustomization()
    {
        //Register Button Text
        if(isSaveCardButton!=null)
        {
            if(submitPaymentCardFormGuiSetting.SwitchButtonColor!=null && submitPaymentCardFormGuiSetting.SwitchButtonColor.trim().length()==7)
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


    private void startLoadingUI(){
        registrationButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

    }

    private void stopLoadingUI(){
        registrationButton.setEnabled(true);
        updateButtonState();
        progressBar.setVisibility(View.INVISIBLE);

    }

    private void hideKeyboard(){

        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    /**
     * This method is called when the 'Pay' button is clicked. It handles both purchase and
     * pre-auth. These actions are differentiated based on paymentType.
     */
    private OnClickListener onSubmitSinglePaymentCardButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            hideKeyboard();
            startLoadingUI();
            BNPaymentHandler.getInstance().submitSingleTransactionCard(
                    getContext(),
                    paymentId, paymentSettings, cardHolderEditText.getText().toString(),
                    cardNumberEditText.getText().toString(),
                    expiryDateEditText.getEnteredExpiryMonth(),
                    expiryDateEditText.getEnteredExpiryYear(),
                    securityCodeEditText.getText().toString(),
                    isSaveCardButton.isChecked(), resultListenerForInternal, onSavedCardListener);
        }
    };

    @Override
    public void onTransactionSuccess(Map<String, String> response) {
        stopLoadingUI();
        resultListenerForExternal.onTransactionSuccess(response);
    }

    @Override
    public void onTransactionError(RequestError error) {
        stopLoadingUI();
        resultListenerForExternal.onTransactionError(error);
    }


}
