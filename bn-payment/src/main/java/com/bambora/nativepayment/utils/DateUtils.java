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

package com.bambora.nativepayment.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This is a class containing utils regarding Dates
 *
 * Created by oskarhenriksson on 14/10/15.
 */
public class DateUtils {

    private static final String SHORT_ISO_8601_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HHmmssZ";

    private DateUtils() {}

    /**
     * This is a helper method for converting a Date to a String that can be used
     * in the date http header field. Currently used for generating an auth header.
     *
     * @param date The date to be formatted to a string
     * @return A String representation of a date that can be used as a date http header.
     */
    public static String getDateHeader(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dateHeader = dateFormat.format(date);
        dateHeader += " GMT";
        return dateHeader;
    }

    public static Date parseISO8601DateString(String dateString) throws ParseException {
        String shortIso = dateString.replace(":", "").replace("Z", "+0000");
        DateFormat dateFormat = new SimpleDateFormat(SHORT_ISO_8601_DATE_TIME_FORMAT, Locale.ENGLISH);
        return dateFormat.parse(shortIso);
    }

    public static int getDifferenceInDays(Date date1, Date date2) {
        long millis = date2.getTime() - date1.getTime();
        return (int) (millis/1000/60/60/24);
    }

    public static Date addDays(Date date, int nrOfDays) {
        long millis = nrOfDays*24*60*60*1000;
        return new Date(date.getTime() + millis);
    }
}
