package com.bambora.nativepayment.models;

import com.bambora.nativepayment.logging.BNLog;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class CardRegistrationFormGuiSettingTest {

    @Test
    public void testSetting() throws JSONException {
        String title="testTitle";
        JSONObject setting=new JSONObject();
        setting.put(CardRegistrationFormGuiSettingEnum.TitleText.toString(),title);
        CardRegistrationFormGuiSetting regiGuiSetting= new CardRegistrationFormGuiSetting();
        regiGuiSetting.TitleText=setting.getString(CardRegistrationFormGuiSettingEnum.TitleText.toString());
        Assert.assertEquals(title,regiGuiSetting.TitleText);
    }

}
