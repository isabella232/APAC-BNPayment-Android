package com.bambora.nativepayment.json;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import java.util.Date;

public class BNJsonObjectTest {

    @Test
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

    @Test
    public void testOptDateWithNullValue() throws JSONException {
        String dateKey = "date";
        String dateValue = "null";

        String json = createJsonString(dateKey, dateValue);
        BNJsonObject jsonObject = new BNJsonObject(json);
        Date date = jsonObject.optDate(dateKey);
        Assert.assertNull(date);
    }

    @Test
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

    @Test
    public void testcopyFromWithNullValue() throws JSONException {
        String dateKey = "date";
        String dateValue = "2016-05-17T12:00:00Z";
        String json = createJsonString(dateKey, dateValue);
        JSONObject jsonObject=new JSONObject(json);
        BNJsonObject object = BNJsonObject.copyFrom(jsonObject);
        Assert.assertEquals(object.toString(),new BNJsonObject(json).toString());
    }

}
