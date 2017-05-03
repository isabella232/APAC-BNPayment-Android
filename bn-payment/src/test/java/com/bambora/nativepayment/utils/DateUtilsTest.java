package com.bambora.nativepayment.utils;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oskarhenriksson on 27/11/15.
 */
public class DateUtilsTest {

    @Test
    public void dateHeaderShouldHaveCorrectFormat() {
        long timestamp = 1433160000;
        String correctFormat = "Mon, 01 Jun 2015 12:00:00 GMT";
        String dateStringToTest = DateUtils.getDateHeader(new Date(timestamp*1000));

        Assert.assertEquals(correctFormat, dateStringToTest);
    }

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
