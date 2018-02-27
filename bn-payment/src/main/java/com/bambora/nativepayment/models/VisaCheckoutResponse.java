package com.bambora.nativepayment.models;

import android.util.Log;

import com.bambora.nativepayment.interfaces.IJsonResponse;
import com.bambora.nativepayment.json.JsonContainer;
import com.bambora.nativepayment.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;


public class VisaCheckoutResponse implements IJsonResponse<VisaCheckoutResponse> {

    public String region;
    public String merchant;
    public String payment;
    public String state;
    public String currency;
    public Integer amount;
    public String comment;
    public String captures;
    public String receipt;
    public String cardType;
    public String cardHolderName;
    public String creditCardToken;
    public String truncatedCard;


    @Override
    public VisaCheckoutResponse fromJson(JsonContainer jsonContainer) throws JSONException {

        JSONObject jsonObject = jsonContainer.getJsonObject();
        region = JsonUtils.getStringIfExists(jsonObject, "region");
        merchant = JsonUtils.getStringIfExists(jsonObject, "merchant");
        payment = JsonUtils.getStringIfExists(jsonObject,"payment");
        state = JsonUtils.getStringIfExists(jsonObject, "state");
        currency = JsonUtils.getStringIfExists(jsonObject, "currency");
        comment = JsonUtils.getStringIfExists(jsonObject, "comment");
        receipt = JsonUtils.getStringIfExists(jsonObject, "receipt");
        cardType = JsonUtils.getStringIfExists(jsonObject, "cardType");
        cardHolderName = JsonUtils.getStringIfExists(jsonObject, "cardHolderName");
        creditCardToken = JsonUtils.getStringIfExists(jsonObject, "creditCardToken");
        truncatedCard = JsonUtils.getStringIfExists(jsonObject, "truncatedCard");
        amount = JsonUtils.getIntIfExists(jsonObject, "amount");
        captures = JsonUtils.getStringIfExists(jsonObject, "captures");
        return this;
    }


}
