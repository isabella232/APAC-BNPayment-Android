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

package com.bambora.nativepayment.widget;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.List;

/**
 * TODO
 */
@RunWith(Parameterized.class)
public class FormatInputHelperIsFormattedTest {

    private String sequence;
    private List<Integer> groupSizes;
    private boolean expectedResult;

    public FormatInputHelperIsFormattedTest(String sequence, List<Integer> groupSizes, boolean expectedResult) {
        this.sequence = sequence;
        this.groupSizes = groupSizes;
        this.expectedResult = expectedResult;
    }

    @Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"", Arrays.asList(4, 4, 4, 4), true},
                {" ", Arrays.asList(4, 4, 4, 4), false},
                {"1 ", Arrays.asList(4, 4, 4, 4), false},
                {"1234", Arrays.asList(4, 4, 4, 4), true},
                {"12345", Arrays.asList(4, 4, 4, 4), false},
                {"1234 5", Arrays.asList(4, 4, 4, 4), true},
                {"1234 56", Arrays.asList(4, 4, 4, 4), true},
                {"1234 56789", Arrays.asList(4, 4, 4, 4), false},
                {"1234 5678 9", Arrays.asList(4, 4, 4, 4), true},
                {"1234 5678 90123", Arrays.asList(4, 4, 4, 4), false},
                {"1234 5678 9012 ", Arrays.asList(4, 4, 4, 4), true},
                {"1234 5678 9012 3", Arrays.asList(4, 4, 4, 4), true},
                {"1234 5678 9012 34567", Arrays.asList(4, 4, 4, 4), false},
                {"1234 5678 9012 3456", Arrays.asList(4, 4, 4, 4), true},
                {"1234", Arrays.asList(4, 6, 5), true},
                {"12345", Arrays.asList(4, 6, 5), false},
                {"1234 123456", Arrays.asList(4, 6, 5), true},
                {"1234 1234567", Arrays.asList(4, 6, 5), false},
                {"1234 123456 1234", Arrays.asList(4, 6, 5), true},
                {"1234 123456 12345", Arrays.asList(4, 6, 5), true},
                {"1234 123456 123456", Arrays.asList(4, 6, 5), false},
                {"1234 123456 12345 6", Arrays.asList(4, 6, 5), true},
        });
    }

    @Test
    public void isFormatted() {
        char delimiter = ' ';
        Assert.assertEquals(expectedResult, FormInputHelper.isFormatted(sequence, delimiter, groupSizes));
    }
}
