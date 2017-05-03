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

package com.bambora.nativepayment.models.creditcard;

import android.content.Context;

import com.bambora.nativepayment.base.MockitoInstrumentationTestCase;
import com.bambora.nativepayment.interfaces.ICertificateLoadCallback;
import com.bambora.nativepayment.managers.CertificateManager;
import com.bambora.nativepayment.security.Crypto;
import com.bambora.nativepayment.security.InstrumentationTestData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * Class for testing the {@link RegistrationParams} model that is used when registering credit cards.
 */
public class RegistrationParamsInstrumentationTest extends MockitoInstrumentationTestCase {

    private RegistrationParams registrationParams;
    private Context context;

    private static final String KEY_ENCRYPTED_CARD = "encryptedCard";
    private static final String KEY_ENCRYPTED_SESSION_KEYS = "encryptedSessionKeys";
    private static final String KEY_CARD_NUMBER = "cardNumber";
    private static final String KEY_CVC_CODE = "cvcCode";
    private static final String KEY_EXPIRY_MONTH = "expiryMonth";
    private static final String KEY_EXPIRY_YEAR = "expiryYear";
    private static final String KEY_BIN_NUMBER = "binNumber";

    private CertificateManager certificateManager;

    @Override
    public void setUp() {
        super.setUp();
        certificateManager = mock(CertificateManager.class);
        this.registrationParams = new RegistrationParams(new Crypto(), certificateManager);
        this.context = Mockito.mock(Context.class);
    }

    public void testWithNullParams() {
        // Given
        registrationParams.setParametersAndEncrypt(this.context, null, null, null, null, null);

        // When
        String json = registrationParams.getSerialized();

        // Then
        Assert.assertEquals("{}", json);
    }

    public void testWithValidParameters() throws JSONException {
        // Given
        String cardNumber = "1111 2222 3333 4444";
        String binNumber = "111122";
        String expiryMonth = "06";
        String expiryYear = "26";
        String securityCode = "123";
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ICertificateLoadCallback callback = (ICertificateLoadCallback) invocation.getArguments()[1];
                callback.onCertificatesLoaded(InstrumentationTestData.getTestEncryptionCerts());
                return null;
            }
        }).when(certificateManager).getEncryptionCertificates(any(Context.class), any(ICertificateLoadCallback.class));
        registrationParams.setParametersAndEncrypt(this.context, cardNumber, expiryMonth, expiryYear, securityCode, null);

        // When
        String json = registrationParams.getSerialized();

        // Then
        JSONObject jsonObject = new JSONObject(json);
        JSONObject encryptedCard = jsonObject.optJSONObject(KEY_ENCRYPTED_CARD);
        Assert.assertNotNull(encryptedCard);

        String encryptedCardNumber = encryptedCard.optString(KEY_CARD_NUMBER);
        Assert.assertNotNull(encryptedCardNumber);
        Assert.assertNotEquals(cardNumber, encryptedCardNumber);

        String encryptedExpiryMonth = encryptedCard.getString(KEY_EXPIRY_MONTH);
        Assert.assertNotNull(encryptedExpiryMonth);
        Assert.assertNotEquals(expiryMonth, encryptedExpiryMonth);

        String encryptedExpiryYear = encryptedCard.getString(KEY_EXPIRY_YEAR);
        Assert.assertNotNull(encryptedExpiryYear);
        Assert.assertNotEquals(expiryYear, encryptedExpiryYear);

        String encryptedSecurityCode = encryptedCard.getString(KEY_CVC_CODE);
        Assert.assertNotNull(encryptedSecurityCode);
        Assert.assertNotEquals(securityCode, encryptedSecurityCode);

        JSONArray encryptedSessionKeys = jsonObject.optJSONArray(KEY_ENCRYPTED_SESSION_KEYS);
        Assert.assertNotNull(encryptedSessionKeys);
        Assert.assertTrue(encryptedSessionKeys.length() > 0);

        String generatedBinNumber = jsonObject.getString(KEY_BIN_NUMBER);
        Assert.assertNotNull(generatedBinNumber);
        Assert.assertEquals(binNumber, generatedBinNumber);
    }
}
