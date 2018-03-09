package com.bambora.nativepayment.widget;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bambora.nativepayment.widget.edittext.CardHolderEditText;
import com.bambora.nativepayment.widget.edittext.CardNumberEditText;
import com.bambora.nativepayment.widget.edittext.ExpiryDateEditText;

import java.util.HashMap;
import java.util.Map;

import io.card.payment.CardIOActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardRegistrationFormFragment extends Fragment implements CardFormEditText.IOnValidationEventListener,ICardRegistrationCallback {

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
    private RelativeLayout registrationCardFormLayout;
    private int cameraSCANCODE=0;
    private Boolean viewLoaded=false;
    private Button cameraButton;
    private int cardIOColor;

    public CardRegistrationFormFragment() {
        // Required empty public constructor
    }

    public void setRegistrationResultListener(ICardRegistrationCallback resultListener) {
        this.resultListenerForExternal = resultListener;
    }

    public void setTitle(String title) {
        if (pageTitle != null) pageTitle.setText(title);
    }

    public void setFormGuiSetting(CardRegistrationFormGuiSetting registrationGuiSetting) {
        this.registrationGuiSetting = registrationGuiSetting;
        if(viewLoaded)
        {
            viewCustomization();
        }
    }
    @Override
    public void onFocusChanged(EditText view, boolean hasFocus, boolean inputValid) {
        if (hasFocus) {
            view.setTextColor(CompatHelper.getColor(getActivity(), R.color.bn_black, null));
        } else if (!inputValid) {
            view.setTextColor(CompatHelper.getColor(getActivity(), R.color.bn_red, null));
        }
    }

    @Override
    public void onInputValidated(EditText view, boolean inputValid) {
        inputValidStates.put(view, inputValid);
        updateButtonState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.native_card_registration_form, container, false);
        registrationCardFormLayout=(RelativeLayout)view.findViewById(R.id.registrationCardFormLayout);
        pageTitle = (TextView) view.findViewById(R.id.tv_title);
        cardHolderEditText = (CardHolderEditText) view.findViewById(R.id.et_card_holder);
        cardNumberEditText = (CardNumberEditText) view.findViewById(R.id.et_card_number);
        expiryDateEditText = (ExpiryDateEditText) view.findViewById(R.id.et_expiry_date);
        securityCodeEditText = (CardFormEditText) view.findViewById(R.id.et_security_code);
        cameraButton= (Button) view.findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(onCameraScanClickListener);

        registrationButton = (Button) view.findViewById(R.id.btn_register);
        registrationButton.setOnClickListener(onRegisterButtonClickListener);

        cardHolderEditText.setValidationListener(this);
        cardNumberEditText.setValidationListener(this);
        expiryDateEditText.setValidationListener(this);
        securityCodeEditText.setValidationListener(this);

        inputValidStates.put(cardHolderEditText, true);
        inputValidStates.put(cardNumberEditText, false);
        inputValidStates.put(expiryDateEditText, false);
        inputValidStates.put(securityCodeEditText, true);

        progressBar=new ProgressBar(getActivity(),null,android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        registrationCardFormLayout.addView(progressBar,params);
        progressBar.setVisibility(View.INVISIBLE);
        this.resultListenerForInternal = this;
        if(this.registrationGuiSetting!=null)
        {
            viewCustomization();
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewLoaded=true;
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
        LoadingBarCustomization();
        CardIOCustomization();
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

    private void LoadingBarCustomization()
    {
        if(progressBar!=null)
        {
            if(registrationGuiSetting.LoadingBarColor!=null && registrationGuiSetting.LoadingBarColor.trim().length()==7)
            {
                int colorValue = Color.parseColor(registrationGuiSetting.LoadingBarColor);
                progressBar.getIndeterminateDrawable().setColorFilter(colorValue, PorterDuff.Mode.MULTIPLY);
            }
        }
    }

    private void CardIOCustomization()
    {
        if(registrationGuiSetting.CardIOEnable==null || registrationGuiSetting.CardIOEnable)
        {
            if(registrationGuiSetting.CardIOColorText!=null && registrationGuiSetting.CardIOColorText.trim().length()==7)
            {
                cardIOColor = Color.parseColor(registrationGuiSetting.CardIOColorText);
            }
            else
            {
                cardIOColor = getResources().getColor(R.color.bn_purple);
            }
        }
        else
        {
            cameraButton.setVisibility(View.GONE);
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

    private View.OnClickListener onRegisterButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startLoadingUI();
            BNPaymentHandler.getInstance().registerCreditCard(
                    getActivity(),
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

    private View.OnClickListener onCameraScanClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent scanIntent = new Intent(getActivity(), CardIOActivity.class);
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
            scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true);
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false);
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, false);
            scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true);
            scanIntent.putExtra(CardIOActivity.EXTRA_GUIDE_COLOR, cardIOColor);
            scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true);
            scanIntent.putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, false);
            scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION,false);
            startActivityForResult(scanIntent, cameraSCANCODE);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == cameraSCANCODE) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                io.card.payment.CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                String scanedNumber=scanResult.getFormattedCardNumber();
                cardNumberEditText.setText(scanedNumber);
                cardNumberEditText.requestFocus();
                String expiryMonth=String.valueOf(scanResult.expiryMonth);
                if(expiryMonth.length()==1)
                {
                    expiryMonth="0"+expiryMonth;
                }
                String expiryYear=String.valueOf(scanResult.expiryYear).substring(2);
                String expireDate=expiryMonth+expiryYear;
                expiryDateEditText.setText(expireDate);
                expiryDateEditText.requestFocus();
            }
        }
    }
}
