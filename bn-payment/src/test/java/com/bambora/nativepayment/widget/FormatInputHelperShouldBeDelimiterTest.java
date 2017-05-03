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
public class FormatInputHelperShouldBeDelimiterTest {

    private int index;
    private List<Integer> groupSizes;
    private boolean expectedResult;

    public FormatInputHelperShouldBeDelimiterTest(int index, List<Integer> groupSizes, boolean expectedResult) {
        this.index = index;
        this.groupSizes = groupSizes;
        this.expectedResult = expectedResult;
    }

    @Parameters
    public static Iterable<Object[]> data() {
        // 0123 4567 8901 2345 6
        // 0123 456789 01234 5
        return Arrays.asList(new Object[][] {
                {0, Arrays.asList(4, 4, 4, 4), false},
                {1, Arrays.asList(4, 4, 4, 4), false},
                {2, Arrays.asList(4, 4, 4, 4), false},
                {3, Arrays.asList(4, 4, 4, 4), false},
                {4, Arrays.asList(4, 4, 4, 4), true},
                {5, Arrays.asList(4, 4, 4, 4), false},
                {8, Arrays.asList(4, 4, 4, 4), true},
                {9, Arrays.asList(4, 4, 4, 4), false},
                {12, Arrays.asList(4, 4, 4, 4), true},
                {15, Arrays.asList(4, 4, 4, 4), false},
                {16, Arrays.asList(4, 4, 4, 4), true},
                {1, Arrays.asList(4, 6, 5), false},
                {4, Arrays.asList(4, 6, 5), true},
                {8, Arrays.asList(4, 6, 5), false},
                {10, Arrays.asList(4, 6, 5), true},
                {12, Arrays.asList(4, 6, 5), false},
                {15, Arrays.asList(4, 6, 5), true},
        });
    }

    @Test
    public void shouldBeDelimiter() {
        Assert.assertEquals(expectedResult, FormInputHelper.shouldBeDelimiter(index, groupSizes));
    }
}
