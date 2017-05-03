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

import java.util.List;

/**
 * TODO
 */
public class FormInputHelper {

    public static String clearNonDigits(String input) {
        return input.replaceAll("[^\\d]", "");
    }

    public static String getDeletedChars(CharSequence sequence, int startBefore, int countBefore) {
        StringBuilder deletingChars = new StringBuilder();
        for (int i = startBefore; i < startBefore + countBefore; i++) {
            deletingChars.append(sequence.charAt(i));
        }
        return deletingChars.toString();
    }

    public static CharSequence formatNumberSequence(String sequence, List<Integer> groupSizes, char groupDelimiter) {
        sequence = clearNonDigits(sequence);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sequence.length() + 1; i++) {
            if (shouldBeDelimiter(i, groupSizes)) {
                stringBuilder.append(String.valueOf(groupDelimiter));
            }
            if (i < sequence.length()) {
                stringBuilder.append(sequence.charAt(i));
            }
        }
        return stringBuilder;
    }

    public static boolean isFormatted(String sequence, char delimiter, List<Integer> groupSizes) {
        int delimiterPosition;
        int groupIndex = 0;
        if (groupSizes.size() > 0) {
            delimiterPosition = groupSizes.get(groupIndex);
        } else {
            return true;
        }
        for (int i = 0; i < sequence.length(); i++) {
            if (i == delimiterPosition) {
                if (sequence.charAt(i) != delimiter) {
                    return false;
                } else if (groupSizes.size() > groupIndex + 1) {
                    groupIndex++;
                    delimiterPosition += groupSizes.get(groupIndex) + 1;
                }
            } else {
                if (sequence.charAt(i) == delimiter) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean shouldBeDelimiter(int index, List<Integer> groupSizes) {
        int delimiterPosition = 0;
        for (int i = 0; i < groupSizes.size(); i++) {
            delimiterPosition += groupSizes.get(i);
            if (index == delimiterPosition) {
                return true;
            }
        }
        return false;
    }
}
