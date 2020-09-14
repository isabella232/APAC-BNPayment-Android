package com.bambora.nativepayment.utils;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class InstrumentationTestDateUtils {

    @Test
    public void testParseISO8601DateString() throws ParseException {
        Map<String, Long> timeTestValues = new HashMap<>();
        timeTestValues.put("2016-05-17T12:00:00Z", 1463486400000L); // String to parse, time value
        timeTestValues.put("2016-05-17T12:00:00+01:00", 1463482800000L);
        timeTestValues.put("2016-05-17T12:00:00+0100", 1463482800000L);

        for (String dateString : timeTestValues.keySet()) {
            Date parsedDate = DateUtils.parseISO8601DateString(dateString);
            long expectedTime = timeTestValues.get(dateString);
            Assert.assertEquals(expectedTime, parsedDate.getTime());
        }
    }
}
