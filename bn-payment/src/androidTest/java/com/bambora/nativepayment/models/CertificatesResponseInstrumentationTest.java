package com.bambora.nativepayment.models;

import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import com.bambora.nativepayment.json.JsonContainer;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.runner.RunWith;

/**
 * TODO
 */
@RunWith(AndroidJUnit4.class)
public class CertificatesResponseInstrumentationTest extends InstrumentationTestCase {

    private CertificatesResponse certificatesResponse;

    public void setUp() {
        this.certificatesResponse = new CertificatesResponse();
    }

    public void testFromJsonWithEmptyJson() throws JSONException {
        // Given
        JsonContainer jsonContainer = new JsonContainer("{}");

        // When
        try {
            this.certificatesResponse.fromJson(jsonContainer);
            Assert.fail(); // Should throw exception
        } catch (JSONException jsonException) {
            // Expected exception
        }
    }

    public void testFromJsonWithInvalidJson() throws JSONException {
        // Given
        JSONArray invalidJsonArray = new JSONArray();
        JSONObject certificateObject = new JSONObject();
        certificateObject.put("fingerprint", "E44AADA38EDABBA65C01AA503975039D46A5557A");
        certificateObject.put("certificate", "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\\r\\nioj3WcrT6Fw3/84f2FFeqA==\\r\\n-----END CERTIFICATE-----");
        certificateObject.put("validFrom", "2016-05-17T12:00:00Z");
        certificateObject.put("validTo", "2016-08-15T12:00:00Z");
        certificateObject.put("updateInterval", "45");
        invalidJsonArray.put(certificateObject);
        JsonContainer certificateJson = new JsonContainer(invalidJsonArray);

        // When
        try {
            this.certificatesResponse.fromJson(certificateJson);
            Assert.assertNotNull(this.certificatesResponse.getEncryptionCertificates());
            Assert.assertEquals(1, this.certificatesResponse.getEncryptionCertificates().size());
            Assert.assertNull(this.certificatesResponse.getEncryptionCertificates().get(0).getCertificate());
        } catch (JSONException jsonException) {
            Assert.fail(); // Should throw exception
        }
    }

    public void testFromJsonWithEmptyArray() {
        // Given
        JSONArray jsonArray = new JSONArray();
        JsonContainer emptyJsonArrayContainer = new JsonContainer(jsonArray);

        // When
        try {
            this.certificatesResponse.fromJson(emptyJsonArrayContainer);
            Assert.assertNotNull(this.certificatesResponse.getEncryptionCertificates());
            Assert.assertEquals(0, this.certificatesResponse.getEncryptionCertificates().size());
        } catch (JSONException jsonException) {
            Assert.fail();
        }
    }

    public void testFromJsonWithValidJsonArray() throws JSONException {
        // Given
        JSONArray jsonArray = new JSONArray();
        JSONObject certificate1 = new JSONObject();
        certificate1.put("fingerprint", "E44AADA38EDABBA65C01AA503975039D46A5557A");
        certificate1.put("certificate", "-----BEGIN CERTIFICATE-----\\r\\n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\\r\\nioj3WcrT6Fw3/84f2FFeqA==\\r\\n-----END CERTIFICATE-----");
        certificate1.put("validFrom", "2016-05-17T12:00:00Z");
        certificate1.put("validTo", "2016-08-15T12:00:00Z");
        certificate1.put("updateInterval", "45");

        JSONObject certificate2 = new JSONObject();
        certificate2.put("fingerprint", "E44AADA38EDABBA65C01AA503975039D46A5557A");
        certificate2.put("certificate", "-----BEGIN CERTIFICATE-----\\r\\n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\\r\\nioj3WcrT6Fw3/84f2FFeqA==\\r\\n-----END CERTIFICATE-----");
        certificate2.put("validFrom", "2016-05-17T12:00:00Z");
        certificate2.put("validTo", "2016-08-15T12:00:00Z");
        certificate2.put("updateInterval", "45");
        jsonArray.put(certificate1);
        jsonArray.put(certificate2);
        JsonContainer certificateJson = new JsonContainer(jsonArray);

        // When
        try {
            this.certificatesResponse.fromJson(certificateJson);
            Assert.assertNotNull(this.certificatesResponse.getEncryptionCertificates());
            Assert.assertEquals(2, this.certificatesResponse.getEncryptionCertificates().size());
        } catch (JSONException jsonException) {
            Assert.fail();
        }
    }
}