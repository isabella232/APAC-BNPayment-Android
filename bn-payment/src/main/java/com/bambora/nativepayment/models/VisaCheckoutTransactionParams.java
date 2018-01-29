package com.bambora.nativepayment.models;

import com.bambora.nativepayment.interfaces.IJsonRequest;
import com.bambora.nativepayment.logging.BNLog;

import org.json.JSONException;
import org.json.JSONObject;


public class VisaCheckoutTransactionParams implements IJsonRequest {

    public String encPaymentData;
    public String callid;
    public String encKey;
    public JSONObject paymentJsonData;

    @Override
    public String getSerialized() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("encPaymentData", encPaymentData);
            jsonObject.put("callid", callid);
            jsonObject.put("encKey", encKey);
            jsonObject.put("paymentJsonData", paymentJsonData);
        } catch (JSONException e) {
            BNLog.jsonParseError(getClass().getSimpleName(), e);
        }
        return jsonObject.toString();
    }
}
