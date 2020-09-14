package com.bambora.nativepayment.widget;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Fragment;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.bambora.nativepayment.interfaces.ITransactionExtListener;
import com.bambora.nativepayment.interfaces.VisaCheckoutDataCallback;
import com.bambora.nativepayment.interfaces.VisaCheckoutTransactionCallback;
import com.bambora.nativepayment.managers.CreditCardManager;
import com.bambora.nativepayment.models.PaymentSettings;
import com.bambora.nativepayment.models.SubmitPaymentCardFormGuiSetting;
import com.bambora.nativepayment.models.VisaCheckoutLaunchParams;
import com.bambora.nativepayment.models.VisaCheckoutResponse;
import com.bambora.nativepayment.models.VisaCheckoutTransactionParams;
import com.bambora.nativepayment.network.RequestError;
import com.bambora.nativepayment.utils.CompatHelper;
import com.bambora.nativepayment.widget.edittext.CardFormEditText;
import com.bambora.nativepayment.widget.edittext.CardHolderEditText;
import com.bambora.nativepayment.widget.edittext.CardNumberEditText;
import com.bambora.nativepayment.widget.edittext.ExpiryDateEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionCardFormFragment extends Fragment implements CardFormEditText.IOnValidationEventListener,ITransactionExtListener,VisaCheckoutDataCallback,VisaCheckoutTransactionCallback {

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
    private Button cameraButton;
    private int cameraSCANCODE=0;
    private int cardIOColor;
    private RelativeLayout transactionCardFormLayout;
    private Boolean viewLoaded=false;

    public TransactionCardFormFragment() {
        // Required empty public constructor
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
    }

    public void launchVisaCheckOut(){
        progressBar.setVisibility(View.VISIBLE);
        this.visaCheckoutDataCallback= this;
        BNPaymentHandler.getInstance().getVisaCheckoutData(getActivity(),visaCheckoutDataCallback);
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
        if (pageTitle != null) pageTitle.setText(title);
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
        View view=inflater.inflate(R.layout.native_submit_single_payment_card_form, container, false);
        transactionCardFormLayout=(RelativeLayout)view.findViewById(R.id.transactionCardFormLayout);
        pageTitle = (TextView) view.findViewById(R.id.tv_title);
        cardHolderEditText = (CardHolderEditText) view.findViewById(R.id.et_card_holder);
        cardNumberEditText = (CardNumberEditText) view.findViewById(R.id.et_card_number);
        expiryDateEditText = (ExpiryDateEditText) view.findViewById(R.id.et_expiry_date);
        securityCodeEditText = (CardFormEditText) view.findViewById(R.id.et_security_code);
        isSaveCardButton = (ToggleButton) view.findViewById(R.id.togg_is_save_card_btn);
        registrationButton = (Button) view.findViewById(R.id.btn_register);
        registrationButton.setOnClickListener(onSubmitSinglePaymentCardButtonClickListener);
        cameraButton= (Button) view.findViewById(R.id.camera_button);


        visaCheckoutLayout=(RelativeLayout)view.findViewById(R.id.visa_checkout_layout);
        visaCheckoutButton = (VisaCheckoutButton) view.findViewById(R.id.visa_checkout_button);

        or_UI=(LinearLayout)view.findViewById(R.id.or_UI);

        cardHolderEditText.setValidationListener(this);
        cardNumberEditText.setValidationListener(this);
        expiryDateEditText.setValidationListener(this);
        securityCodeEditText.setValidationListener(this);

        inputValidStates.put(cardHolderEditText, true);
        inputValidStates.put(cardNumberEditText, false);
        inputValidStates.put(expiryDateEditText, false);
        inputValidStates.put(securityCodeEditText, true);

        progressBar=new ProgressBar(getActivity(),null,android.R.attr.progressBarStyleLarge);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.bn_purple), PorterDuff.Mode.MULTIPLY);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        transactionCardFormLayout.addView(progressBar,params);
        progressBar.setVisibility(View.INVISIBLE);
        this.resultListenerForInternal = this;

        activity = (Activity)getActivity();
        if(paymentSettings.enableVisaCheckout && hasVisaCheckoutSDK())
        {
            launchVisaCheckOut();
        }
        else
        {
            or_UI.setVisibility(View.GONE);
            visaCheckoutLayout.setVisibility(View.GONE);
        }

        cameraButton.setOnClickListener(onCameraScanClickListener);

        transactionCardFormLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard();
                return false;
            }
        });

        if(this.submitPaymentCardFormGuiSetting!=null)
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

    public void setFormGuiSetting(SubmitPaymentCardFormGuiSetting guiSetting) {
        this.submitPaymentCardFormGuiSetting = guiSetting;
        if(viewLoaded)
        {
            viewCustomization();
        }
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
                            BNPaymentHandler.getInstance().processTransaction(getActivity(),VisaCheckoutTransactionParams,visaCheckoutTransactionCallback);
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
        CardIOCustomization();
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

            int colorValue = CompatHelper.getCustomizedColor(getActivity(),submitPaymentCardFormGuiSetting.PayByCardButtonColor,"Submit Button is invalid!");
            registrationButton.setAlpha((float)0.5);
            registrationButton.setBackgroundColor(colorValue);
        }
    }


    private void SubmitSinglePaymentCardLoadingBarCustomization()
    {
        //Register Button Text
        if(progressBar!=null)
        {
            int colorValue = CompatHelper.getCustomizedColor(getActivity(),submitPaymentCardFormGuiSetting.PayLoadingBarColor,"Loading Bar Color is invalid!");
            progressBar.getIndeterminateDrawable().setColorFilter(colorValue, PorterDuff.Mode.MULTIPLY);
        }
    }



    private void SubmitSinglePaymentCardSwitchButtonCustomization()
    {
        //Register Button Text
        if(isSaveCardButton!=null)
        {
            isSaveCardButton.setBackgroundColor(CompatHelper.getCustomizedColor(getActivity(),submitPaymentCardFormGuiSetting.SwitchButtonColor,"Save Card Button Color is invalid!"));
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

    private void CardIOCustomization()
    {
        if(submitPaymentCardFormGuiSetting.CardIOEnable==null || submitPaymentCardFormGuiSetting.CardIOEnable)
        {
            cardIOColor=CompatHelper.getCustomizedColor(getActivity(),submitPaymentCardFormGuiSetting.CardIOColorText,"CardIO Frame Color is invalid!");
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
        if(enabled)
        {
            registrationButton.setAlpha(1);
        }
        else
        {
            registrationButton.setAlpha((float)0.5);
        }
        registrationButton.setEnabled(enabled);
    }


    private void startLoadingUI(){
        registrationButton.setAlpha((float)0.5);
        registrationButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

    }

    private void stopLoadingUI(){
        registrationButton.setAlpha(1);
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
    private View.OnClickListener onSubmitSinglePaymentCardButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            hideKeyboard();
            startLoadingUI();
            BNPaymentHandler.getInstance().submitSingleTransactionCard(
                    getActivity(),
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

    private View.OnClickListener onCameraScanClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent scanIntent = new Intent(activity, CardIOActivity.class);
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
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
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
