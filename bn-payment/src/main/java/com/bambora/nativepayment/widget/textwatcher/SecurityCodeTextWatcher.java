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

import java.util.regex.Pattern;

/**
 * A custom {@link TextWatcher} for formatting input as a card security code.
 */
public class SecurityCodeTextWatcher implements TextWatcher {

    private static final String FORMAT = "^\\d{1,4}$";
    private static final String VALID_INPUT = "^$|^\\d{3}\\d?$";

    private CardInputValidator formatter;

    public SecurityCodeTextWatcher(CardInputValidator inputFormatter) {
        this.formatter = inputFormatter;
        this.formatter.setValidationPattern(VALID_INPUT);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if (!Pattern.compile(FORMAT).matcher(s).matches()) {
            CharSequence sequence = FormInputHelper.clearNonDigits(s.toString());
            s.replace(0, s.length(), sequence);
        }
    }
}
