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

package com.bambora.paymentdemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.models.creditcard.RegistrationFormError;
import com.bambora.nativepayment.webview.CreditCardRegistrationWebView;

public class HostedPaymentPageActivity extends AppCompatActivity implements CreditCardRegistrationWebView.IStateChangeListener {

    private CreditCardRegistrationWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosted_payment_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupView() {
        mWebView = (CreditCardRegistrationWebView) findViewById(R.id.register_credit_card_webview);
        mWebView.setStateChangeListener(this);
    }

    private void showDialog(String title, String message, final boolean navigateUp) {
        final HostedPaymentPageActivity instance = this;

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (navigateUp) {
                            NavUtils.navigateUpFromSameTask(instance);
                        }

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onPageFinished() {
        Toast.makeText(this, "Page finished loading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegistrationStarted() {
        Log.d(getClass().getSimpleName(), "Loading started");
        Toast.makeText(this, "Loading started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegistrationCompleted(CreditCard creditCard) {
        Log.d(getClass().getSimpleName(), "Registration completed successfully");
        Toast.makeText(this, "Registration completed successfully", Toast.LENGTH_SHORT).show();
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void onFailure(RegistrationFormError error) {
        Log.d(getClass().getSimpleName(), "Failure");
        Toast.makeText(this, "Credit card registration failed: " + error, Toast.LENGTH_SHORT).show();
    }
}
