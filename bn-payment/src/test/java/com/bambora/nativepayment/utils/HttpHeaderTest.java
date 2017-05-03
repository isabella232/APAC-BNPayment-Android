package com.bambora.nativepayment.utils;

import com.bambora.nativepayment.network.HttpHeader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HttpHeaderTest {

    private HttpHeader header;

    @Before
    public void setup() {
        header = new HttpHeader();
    }

    @Test
    public void testAddField() {
        String key1 = "test-key";
        String key2 = "another-test-key";
        String value1 = "test-value";
        String value2 = "test-value-2";
        String value3 = "another-test-value";

        header.addField(key1, value1);
        Assert.assertEquals(1, header.size());

        // Try with a duplicate value
        header.addField(key1, value1);
        Assert.assertEquals(1, header.size());

        // Add a new value to an existing key
        header.addField(key1, value2);
        Assert.assertEquals(1, header.size());

        // New key, new value
        header.addField(key2, value3);
        Assert.assertEquals(2, header.size());
    }
}
