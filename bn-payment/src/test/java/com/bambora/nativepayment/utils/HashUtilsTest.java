package com.bambora.nativepayment.utils;

import org.junit.Assert;
import org.junit.Test;

import java.security.GeneralSecurityException;

/**
 * Class for testing {@link HashUtils}
 */
public class HashUtilsTest {

    private static final String HASH_ALGORITHM = "HmacSHA256";
    private static final String TEST_KEY = "SecretKey";
    private static final String TEST_STRING_TO_HASH = "A message to hash.";
    private static final String EXPECTED_RESULT = "87276fcff3d5f8bf47359a1f61106e8605be6b1b9fa8ab047e75d01178068ab9";

    @Test
    public void shouldGenerateValidHmac() throws GeneralSecurityException {
        String hmac = HashUtils.generateHmac(HASH_ALGORITHM, TEST_STRING_TO_HASH, TEST_KEY);
        Assert.assertEquals(EXPECTED_RESULT, hmac);
    }
}
