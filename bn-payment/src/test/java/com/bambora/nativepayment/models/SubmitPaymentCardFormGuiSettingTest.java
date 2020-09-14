package com.bambora.nativepayment.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class SubmitPaymentCardFormGuiSettingTest {

    @Test
    public void testSetting() throws JSONException {
        String title="testTitle";
        JSONObject setting=new JSONObject();
        setting.put(SubmitPaymentCardFormGuiSettingEnum.TitleText.toString(),title);
        SubmitPaymentCardFormGuiSetting paymentGuiSetting= new SubmitPaymentCardFormGuiSetting();
        paymentGuiSetting.TitleText=setting.getString(SubmitPaymentCardFormGuiSettingEnum.TitleText.toString());
        Assert.assertEquals(title,paymentGuiSetting.TitleText);
    }
}
