package com.bambora.nativepayment.models.creditcard;

import com.bambora.nativepayment.json.JsonContainer;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;


public class RegistrationResponseTest {

    private static final String KEY_SESSION_URL = "session_url";

    @Test
    public void testFromJsonWithNoParameters() throws JSONException {
        // Given
        JsonContainer responseJson = new JsonContainer("{}");
        RegistrationResponse registrationResponse = new RegistrationResponse();

        // When
        registrationResponse.fromJson(responseJson);

        // Then
        assertNull(registrationResponse.sessionUrl);
    }

    @Test
    public void testFromJsonWithValidUrl() throws JSONException {
        // Given
        String url = "http://a.valid.url";
        JSONObject responseJson = new JSONObject();
        responseJson.put(KEY_SESSION_URL, url);
        JsonContainer responseJsonContainer = new JsonContainer(responseJson);
        RegistrationResponse registrationResponse = new RegistrationResponse();

        // When
        registrationResponse.fromJson(responseJsonContainer);

        // Then
        assertEquals(url, registrationResponse.sessionUrl);
    }

}
