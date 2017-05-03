package com.bambora.nativepayment.json;

import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.runner.RunWith;

import java.util.Date;

/**
 * TODO
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentationTestBNJsonObject extends InstrumentationTestCase {

    public void testOptDateWithValidDate() throws JSONException {
        long expectedTime = 1463486400000L;
        String dateKey = "date";
        String dateValue = "2016-05-17T12:00:00Z";

        String json = createJsonString(dateKey, dateValue);
        BNJsonObject jsonObject = new BNJsonObject(json);
        Date date = jsonObject.optDate(dateKey);
        Assert.assertNotNull(date);
        Assert.assertEquals(expectedTime, date.getTime());
    }

    public void testOptDateWithNullValue() throws JSONException {
        String dateKey = "date";
        String dateValue = "null";

        String json = createJsonString(dateKey, dateValue);
        BNJsonObject jsonObject = new BNJsonObject(json);
        Date date = jsonObject.optDate(dateKey);
        Assert.assertNull(date);
    }

    public void testOptDateOnEmptyJson() throws JSONException {
        BNJsonObject jsonObject = new BNJsonObject("{}");
        Date date = jsonObject.optDate("invalidKey");
        Assert.assertNull(date);
    }

    private String createJsonString(String key, String value) {
        String jsonString = "{\"<key>\":\"<value>\"}";
        return jsonString
                .replace("<key>", key)
                .replace("<value>", value);
    }
}
