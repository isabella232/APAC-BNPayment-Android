package com.bambora.nativepayment.models;

import com.bambora.nativepayment.interfaces.IJsonResponse;
import com.bambora.nativepayment.json.JsonContainer;
import com.bambora.nativepayment.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class VisaCheckoutLaunchParams implements IJsonResponse<VisaCheckoutLaunchParams> {

    public VisaCheckoutLaunchParams() {

    }

    public String apikey;
    public String currencyCode;
    public String subtotal;
    public String externalClientId;
    public String externalProfileId;
    public String total;
    public String locale;
    public String collectShipping;
    public String message;
    public String buttonAction;
    public String buttonImageUrl;
    public String jsLibraryUrl;

    public void setAmount(int amount){
        total=String.valueOf(((float)amount)/100.00);
        subtotal=total;
    }

    @Override
    public VisaCheckoutLaunchParams fromJson(JsonContainer jsonContainer) throws JSONException {

        JSONObject jsonObject = jsonContainer.getJsonObject();
        apikey = JsonUtils.getStringIfExists(jsonObject,"apikey");
        currencyCode = JsonUtils.getStringIfExists(jsonObject, "currencyCode");
        externalClientId = JsonUtils.getStringIfExists(jsonObject, "externalClientId");
        externalProfileId = JsonUtils.getStringIfExists(jsonObject, "externalProfileId");
        locale = JsonUtils.getStringIfExists(jsonObject, "locale");
        collectShipping = JsonUtils.getStringIfExists(jsonObject, "collectShipping");
        message = JsonUtils.getStringIfExists(jsonObject, "message");
        buttonAction = JsonUtils.getStringIfExists(jsonObject, "buttonAction");
        buttonImageUrl = JsonUtils.getStringIfExists(jsonObject, "buttonImageUrl");
        jsLibraryUrl = JsonUtils.getStringIfExists(jsonObject, "jsLibraryUrl");
        return this;
    }
}
