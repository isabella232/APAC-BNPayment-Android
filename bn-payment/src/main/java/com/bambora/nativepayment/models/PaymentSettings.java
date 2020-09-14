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

package com.bambora.nativepayment.models;


import android.util.Log;

import com.bambora.nativepayment.interfaces.IJsonRequest;
import com.bambora.nativepayment.logging.BNLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * {@link PaymentSettings} is a model representing the information needed to initiate a transaction.
 *
 * Created by oskarhenriksson on 15/10/15.
 */
public class PaymentSettings implements IJsonRequest {

    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_COMMENT = "comment";
    private static final String KEY_TOKEN = "token";

    private static final String KEY_PAYMENT_JSON_DATA= "paymentJsonData";
    private static final String KEY_CARD_PAYMENT_JSON_DATA= "cardJsonData";
    private String KEY_TRANSACTION_TYPE = "TrnType";

    public Integer amount;
    public String currency;
    public String comment;
    public String creditCardToken;
    public JSONObject paymentJsonData;
    public JSONObject cardPaymentJsonData;
    public Boolean enableVisaCheckout=false;
    public PaymentType.PaymentTypeEnum paymentType;
    //NOTE:
    // If 'comment' is null it will not be serialised => {"amount":100,"token":"92699060984307713","currency":"SEK"}
    // if 'comment' is "" it will be serialised as follow => {"comment":"","amount":100,"token":"92699060984307713","currency":"SEK"}

    @Override
    public String getSerialized() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_AMOUNT, amount);
            jsonObject.put(KEY_CURRENCY, currency);
            jsonObject.put(KEY_COMMENT, comment);
            jsonObject.put(KEY_TOKEN, creditCardToken);

            if (paymentJsonData != null) {
                //set transaction type.
                setTransactionType();
                //set paymentJsonData.
                jsonObject.put(KEY_PAYMENT_JSON_DATA, paymentJsonData);
            }
            if (isCardPayment() && cardPaymentJsonData != null) {
                jsonObject.put(KEY_CARD_PAYMENT_JSON_DATA, cardPaymentJsonData);
            }

        } catch (JSONException e) {
            BNLog.jsonParseError(getClass().getSimpleName(), e);
        }
        return jsonObject.toString();
    }

    /**
     * Mapping backend transaction type based on the frontend payment type
     */
    private void setTransactionType()
    {
        if(paymentJsonData!=null)
        {
            try{
                    paymentJsonData.put(KEY_TRANSACTION_TYPE, PaymentType.PaymentTypeHash().get(paymentType));
            }
            catch (JSONException e){
                    Log.e("Set Payment Type", "unexpected JSON exception", e);
            }

        }
    }

    private boolean isCardPayment(){
        if(paymentType!=null &&
          (paymentType==PaymentType.PaymentTypeEnum.PaymentCard || paymentType==PaymentType.PaymentTypeEnum.PreAuthCard))
        {
            return true;
        }
        return false;
    };
}
