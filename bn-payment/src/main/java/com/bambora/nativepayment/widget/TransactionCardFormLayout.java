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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import com.bambora.nativepayment.R;
import com.bambora.nativepayment.managers.CreditCardManager;
import com.bambora.nativepayment.models.PaymentSettings;
import com.bambora.nativepayment.interfaces.ITransactionExtListener;
import com.bambora.nativepayment.models.SubmitPaymentCardFormGuiSetting;

public class TransactionCardFormLayout extends FrameLayout{

    TransactionCardFormFragment transactionCardFormFragment;

    public TransactionCardFormLayout(Context context) {
        super(context);
        setupView(context);
    }

    public TransactionCardFormLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public TransactionCardFormLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TransactionCardFormLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupView(context);
    }

    private void setupView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.native_transaction_frame_form, this);
        Activity activity = (Activity)context;
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        transactionCardFormFragment = new TransactionCardFormFragment();
        fragmentTransaction.add(R.id.frame_layout, transactionCardFormFragment).commit();
    }

    public void setTransactionCardResultListener(ITransactionExtListener resultListener) {
        transactionCardFormFragment.setTransactionCardResultListener(resultListener);
    }

    public void setSavedCardListener(CreditCardManager.IOnCreditCardSaved onSavedListener) {
        transactionCardFormFragment.setSavedCardListener(onSavedListener);
    }

    public void setTransactionParams(PaymentSettings paymentSettings, String paymentId)
    {
        transactionCardFormFragment.setTransactionParams(paymentSettings,paymentId);
    }

    public void setFormGuiSetting(SubmitPaymentCardFormGuiSetting guiSetting) {
        transactionCardFormFragment.setFormGuiSetting(guiSetting);
    }

}
