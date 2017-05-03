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

package com.bambora.nativepayment.widget.edittext;

import android.content.Context;
import android.util.AttributeSet;

import com.bambora.nativepayment.widget.textwatcher.SecurityCodeTextWatcher;

/**
 * TODO
 */
public class SecurityCodeEditText extends CardFormEditText {

    private static final int MAX_LENGTH = 4;
    private static final String DEFAULT_HINT = "000(0)";

    public SecurityCodeEditText(Context context) {
        super(context);
        setupView();
    }

    public SecurityCodeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    public SecurityCodeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
    }

    public SecurityCodeEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupView();
    }

    @Override
    public Integer getDefaultMaxLength() {
        return MAX_LENGTH;
    }

    @Override
    public String getDefaultHint() {
        return DEFAULT_HINT;
    }

    private void setupView() {
        addTextChangedListener(new SecurityCodeTextWatcher(this));
    }
}
