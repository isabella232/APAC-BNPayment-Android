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

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.bambora.nativepayment.widget.CardInputValidator;
import com.bambora.nativepayment.widget.FormInputHelper;

import java.util.regex.Pattern;

/**
 * TODO
 */
public abstract class CardFormEditText extends EditText implements CardInputValidator,
        OnFocusChangeListener {

    private Pattern validationPattern;
    private IOnValidationEventListener validationEventListener;

    public CardFormEditText(Context context) {
        super(context);
        setupView();
    }

    public CardFormEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    public CardFormEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CardFormEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupView();
    }

    public abstract Integer getDefaultMaxLength();

    public abstract String getDefaultHint();

    public void setValidationListener(IOnValidationEventListener validationListener) {
        this.validationEventListener = validationListener;
    }

    @Override
    public void setValidationPattern(String validationPattern) {
        if (validationPattern != null) {
            this.validationPattern = Pattern.compile(validationPattern);
        }
    }

    @Override
    public boolean isValid() {
        String input = getText().toString();
        return validationPattern == null || validationPattern.matcher(FormInputHelper.clearNonDigits(input)).matches();
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (validationEventListener != null) {
            validationEventListener.onFocusChanged(this, hasFocus, isValid());
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (validationEventListener != null) {
            validationEventListener.onInputValidated(this, isValid());
        }
    }

    private void setupView() {
        setInputType(InputType.TYPE_CLASS_PHONE);
        setOnFocusChangeListener(this);
        setMaxLengthFilter(getDefaultMaxLength());
        setDefaultHint();
    }

    private void setDefaultHint() {
        CharSequence hint = getHint();
        if (hint == null) {
            setHint(getDefaultHint());
        }
    }

    protected void setMaxLengthFilter(int maxLength) {
        InputFilter[] filterArray = {new InputFilter.LengthFilter(maxLength)};
        setFilters(filterArray);
    }

    public interface IOnValidationEventListener {
        void onFocusChanged(EditText view, boolean hasFocus, boolean inputValid);
        void onInputValidated(EditText view, boolean inputValid);
    }
}
