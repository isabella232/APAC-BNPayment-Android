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

package com.bambora.nativepayment.widget.textwatcher;

import android.text.Editable;
import android.text.TextWatcher;

import com.bambora.nativepayment.widget.CardInputValidator;
import com.bambora.nativepayment.widget.FormInputHelper;

import java.util.Arrays;
import java.util.List;

/**
 * A custom {@link TextWatcher} for formatting input as a card expiry date.
 */
public class ExpiryDateTextWatcher implements TextWatcher {

    private static final char DELIMITER = '/';
    private static final String VALID_INPUT = "^(0[1-9]|1[0-2])\\d{2}$";

    private CardInputValidator formatter;
    private boolean deleting;
    private String deleted;
    private int startIndex;

    public ExpiryDateTextWatcher(CardInputValidator inputFormatter) {
        this.formatter = inputFormatter;
        this.formatter.setValidationPattern(VALID_INPUT);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        deleting = after < count;
        deleted = deleting ? FormInputHelper.getDeletedChars(s, start, count) : "";
        startIndex = start;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        String formattedInput = s.toString();
        if (deleting) {
            if (deleted.equals(String.valueOf(DELIMITER)) && s.length() > 0 && startIndex > 0) {
                s.delete(startIndex - 1, startIndex);
            }
        }
        List<Integer> groupSizes = Arrays.asList(2, 2);
        if (!FormInputHelper.isFormatted(s.toString(), DELIMITER, groupSizes)) {
            formattedInput = FormInputHelper.formatNumberSequence(formattedInput, groupSizes, DELIMITER).toString();
            s.replace(0, s.length(), formattedInput);
        }
    }
}
