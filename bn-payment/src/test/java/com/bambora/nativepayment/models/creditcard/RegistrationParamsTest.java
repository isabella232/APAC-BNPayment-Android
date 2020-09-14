package com.bambora.nativepayment.models.creditcard;

import android.content.Context;

import com.bambora.nativepayment.interfaces.ICertificateLoadCallback;
import com.bambora.nativepayment.managers.CertificateManager;
import com.bambora.nativepayment.security.Crypto;
import com.bambora.nativepayment.security.EncryptionCertificate;
import com.bambora.nativepayment.utils.CertificateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;


public class RegistrationParamsTest {

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

    @Before
    public void setup() {
        certificateManager = mock(CertificateManager.class);
        this.registrationParams = new RegistrationParams(new Crypto(), certificateManager);
        this.context = Mockito.mock(Context.class);
    }

    @Test
    public void testWithNullParams() {
        // Given
        registrationParams.setParametersAndEncrypt(this.context, null, null, null, null, null);

        // When
        String json = registrationParams.getSerialized();

        // Then
        Assert.assertEquals("{}", json);
    }

    @Test
    public void testWithValidParameters() throws JSONException {
        // Given
        String cardNumber = "1111 2222 3333 4444";
        String binNumber = "111122";
        String expiryMonth = "06";
        String expiryYear = "26";
        String securityCode = "123";
        String holderName = "Mark";
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ICertificateLoadCallback callback = (ICertificateLoadCallback) invocation.getArguments()[1];
                callback.onCertificatesLoaded(getTestEncryptionCerts());
                return null;
            }
        }).when(certificateManager).getEncryptionCertificates(any(Context.class), any(ICertificateLoadCallback.class));
        registrationParams.setParametersAndEncrypt(this.context, holderName,cardNumber,expiryMonth,expiryYear, securityCode,null);
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

    public static List<EncryptionCertificate> getTestEncryptionCerts() {
        List<EncryptionCertificate> certificates = new ArrayList<>();
        X509Certificate certificate = (X509Certificate) CertificateUtils.parseCertificate(PUBLIC_TEST_CERT_AS_STRING);
        X509Certificate[] certificateArray = { certificate };
        certificates.add(new EncryptionCertificate("TestFingerprint", certificateArray, null, 0));
        return certificates;
    }

    private static final String PUBLIC_TEST_CERT_AS_STRING =
            "-----BEGIN CERTIFICATE-----\n" +
                    "MIIFYjCCA0oCCQDgdWrZfzM9JzANBgkqhkiG9w0BAQUFADBzMQswCQYDVQQGEwJT\n" +
                    "RTETMBEGA1UECBMKU29tZS1TdGF0ZTESMBAGA1UEBxMJU3RvY2tob2xtMRAwDgYD\n" +
                    "VQQKEwdCYW1ib3JhMRQwEgYDVQQLEwtEZXZlbG9wbWVudDETMBEGA1UEAxMKaXJv\n" +
                    "bnBvb2RsZTAeFw0xNjA0MTgxMjE3MjZaFw0yNjA0MTYxMjE3MjZaMHMxCzAJBgNV\n" +
                    "BAYTAlNFMRMwEQYDVQQIEwpTb21lLVN0YXRlMRIwEAYDVQQHEwlTdG9ja2hvbG0x\n" +
                    "EDAOBgNVBAoTB0JhbWJvcmExFDASBgNVBAsTC0RldmVsb3BtZW50MRMwEQYDVQQD\n" +
                    "Ewppcm9ucG9vZGxlMIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA5m9C\n" +
                    "PilTJ/knAp1S+51BZ+vFc5uriZi9L7gpzDwMoKt5SV0c4JlpgV0+IFQwOfL1BQaS\n" +
                    "AKqr/Z5gQG0AcLMA6SGMoNUvt4SsYqdP0zFquH5eTUA8/HoPBt5WC2jegLJJqyGE\n" +
                    "2o2b3EgNHemZapndJNE7Pmt8oNvdPAPDDn9SGSNKRyAcV9rKnJjZ0qK9vPZIps86\n" +
                    "DHtN2QKcs+NF4IOltrauSrUimFRxxOEoQm7FnmPeGkYwOjFVUwSmZVVaXLIU9/sQ\n" +
                    "EAahDH9XgsEBa4v6nImLUyFR1eBJyKj36XNdQhBlHRr+w1XPCzBiA31RKQ8OjB+9\n" +
                    "ItwwhngrkrxYlUZ6M8ZhoqOzDAO4Mb5Y34C1PQdO+7jkmH1C4v5zf2t7iEmt6IVI\n" +
                    "pKOJStvmyZc5ieqgWjQ7KmfqVOzHeKvyyrkr1FEvy2/MUFe0mr9rvQM4oP1OYG9n\n" +
                    "VBOjT2ScU+8Fn9N7YxcrsMWqDax7c7aUYo2lD4llOcWSl0uytz1IhNBaCVc7gbgp\n" +
                    "wuAf++51wtXf04Wy8FV6cL3q9pQnenmZUyIDQrWtV3eQ8OOgiaYcwxIrbrDmYdz/\n" +
                    "MhgqvCwfHyJ/CMdHEzJI+9wUpfOplYHo2yk+k3etWK3mc5Xb5KriJ8aM2w0YH8jr\n" +
                    "CpoJrChQId+4zUo7SbZtq73LlRTEymPPZo1aBvMCAwEAATANBgkqhkiG9w0BAQUF\n" +
                    "AAOCAgEAOgl0GBX53QmoMIVmTXz5HiJezFFRbOewt9bpY1fn3BHERJKuE7uaRj3X\n" +
                    "/cGZMyA97DvE8RJTKB+tI3g5EgT8zSfCoV+h/OpRueBwSpHqjHp4IMrP93v+1FeF\n" +
                    "qGfaYR1v+64H1m2isnbmzr0sGbviKFRXRJQgW9uSoXc+XuoRHbpvmjAS+YLZcVyx\n" +
                    "UjBk6cOGq1MpdyroLk4XHCRnaMVSWows/vp30CLShm/JNi143t78eKlvWPRhFqM3\n" +
                    "b0LiwPIzwl1VRfZM3wQBDDkgc3DqdoIe+1p3zvmZcecWDurKSeuY38Nmbwpg1yU/\n" +
                    "o1KaYWL2o/yMPkdIIrLofPywHoNxoqeAudd1rjNsl72Tx+2Z0jmukuFw9/oObnp3\n" +
                    "8R9qOuVnpcD88gK5isT2HZBDTQ6kQ2EKhHfao1xFb+B3undEhg1Gg870kuMHfAai\n" +
                    "lHW6VOjQOqPXOzQDtjN9DDlT6N6WGCPkNZKMLbd4mjvi1Zr7piuIhskIvfqwF9v5\n" +
                    "SRvs9QSKFQSyK5dZwOHEBsuwA2zstmF91WYiAvebTnxxjYmJhxsqTokcCW+olCzD\n" +
                    "Q+dXm7kw1lSEkUsg5Llj7XC2EwsIXa72vhlfIcQI6Zm2oGidH1CqzdtQaPdi5qQt\n" +
                    "jCgvIXX2q+5rAsyX9R0AgpWzk9yyACL5INq7AvCLIBrxCsF2HOc=\n" +
                    "-----END CERTIFICATE-----\n";

}
