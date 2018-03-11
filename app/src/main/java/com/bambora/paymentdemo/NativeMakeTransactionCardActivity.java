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

package com.bambora.paymentdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.Map;

import com.bambora.nativepayment.interfaces.ITransactionExtListener;
import com.bambora.nativepayment.logging.BNLog;
import com.bambora.nativepayment.managers.CreditCardManager;
import com.bambora.nativepayment.models.CardRegistrationFormGuiSetting;
import com.bambora.nativepayment.models.PaymentSettings;
import com.bambora.nativepayment.models.PaymentType;
import com.bambora.nativepayment.models.SubmitPaymentCardFormGuiSetting;
import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.network.RequestError;
import com.bambora.nativepayment.widget.TransactionCardFormLayout;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * NativeMakeTransactionCardActivity build the transaction by card page.
 */
public class NativeMakeTransactionCardActivity extends AppCompatActivity implements ITransactionExtListener, CreditCardManager.IOnCreditCardSaved {

    private DeviceStorage storage;

    final Context context = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Sample to initial the pay by card view.
        super.onCreate(savedInstanceState);
        storage = new DeviceStorage(context);
        setContentView(R.layout.activity_native_transaction_card);
        TransactionCardFormLayout transactionCardForm = (TransactionCardFormLayout) findViewById(R.id.transaction_card_form);
        PaymentSettings paymentSettings = new PaymentSettings();
        paymentSettings.amount = (int)(storage.getPayAmount()*100);
        paymentSettings.comment = "This is a pay by card transaction test.";
        paymentSettings.currency = "AUD";
        paymentSettings.cvcCode =  "123";

        // determine if this is for payment or preAuth
        Intent prevIntent = getIntent(); // gets the previously created intent
        int paymentOrPreAuth = prevIntent.getIntExtra("PaymentOrPreAuth", -1);
        if (paymentOrPreAuth == PaymentType.PaymentTypeEnum.PaymentCard.ordinal()) {
            paymentSettings.paymentType = PaymentType.PaymentTypeEnum.PaymentCard;
        } else if (paymentOrPreAuth == PaymentType.PaymentTypeEnum.PreAuthCard.ordinal()) {
            paymentSettings.paymentType = PaymentType.PaymentTypeEnum.PreAuthCard;
        } else {
            BNLog.e(getClass().getSimpleName(), "Payment or PreAuth was not specified");
            return;
        }

        JSONObject paymentJsonData = getJsonPayData();
        //setup merchant specific payment data.
        paymentSettings.paymentJsonData = paymentJsonData;
        paymentSettings.enableVisaCheckout = storage.getVisaCheckoutStatus();
        String paymentId = "test-card-payment-" + new Date().getTime();
        transactionCardForm.setTransactionParams(paymentSettings, paymentId);

        SubmitPaymentCardFormGuiSetting submitPaymentCardFormGuiSetting = storage.getSubmitPaymentCardFormCustomizationSetting();
        transactionCardForm.setFormGuiSetting(submitPaymentCardFormGuiSetting);

        transactionCardForm.setTransactionCardResultListener(this);
        transactionCardForm.setSavedCardListener(this);

    }

    @Override
    public void onTransactionSuccess(Map<String,String> response) {
        String receipt = response.get("receipt");
        Toast.makeText(getBaseContext(),"Success, The payment succeeded. Receipt: " + (receipt != null?receipt:"?") ,Toast.LENGTH_LONG).show();
        finish();
    }

    @Override

    public void onTransactionError(RequestError error) {
        Toast.makeText(this, "The payment did not succeed.", Toast.LENGTH_LONG).show();
    }

    public void onCreditCardSaved(CreditCard creditCard){
        //sample to customize saved card callback.
        Log.d("onCreditCardSaved", "Save credit card " + creditCard.getTruncatedCardNumber() + "successed");
        finish();
    }


    /**
     * The payment data as a JSON object.
     * @return the json object
     */
    private JSONObject getJsonPayData()
    {
        try {
            String data = storage.getPayDataFromStorage();
            if (!data.equals("")) {
                JSONObject obj = new JSONObject(data);
                return obj;
            }
        } catch (JSONException e) {
            BNLog.jsonParseError(getClass().getSimpleName(), e);
        }
        return null;

    }


}
