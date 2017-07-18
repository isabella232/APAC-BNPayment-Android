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

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.handlers.BNPaymentHandler.BNPaymentBuilder;
import com.bambora.nativepayment.interfaces.ITransactionExtListener;
import com.bambora.nativepayment.logging.BNLog;
import com.bambora.nativepayment.managers.CreditCardManager;
import com.bambora.nativepayment.models.PaymentSettings;
import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.network.RequestError;
import com.bambora.paymentdemo.adapter.CardListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    /**
     * This is a test merchant number that can be used for testing Native Payment.
     * Please replace this with your own merchant number after signing up with Bambora.
     */

    private DeviceStorage storage;
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        storage = new DeviceStorage(context);
        String currentEnvironment = storage.getEnvironmentNameFromStorage();
        if (currentEnvironment == null || currentEnvironment.isEmpty() ||
                (!currentEnvironment.equals("DEV") && !currentEnvironment.equals("UAT") && !currentEnvironment.equals("PROD"))) {
            // environment has not been set, so default to DEV
            currentEnvironment = "DEV";
        }
        String currentMerchantIdKeyName = getString(R.string.MERCHANT_ID_DEV_NAME);
        String url = "https://devsandbox.ippayments.com.au/rapi/";
        switch (currentEnvironment) {
            case "DEV":
                currentMerchantIdKeyName = getString(R.string.MERCHANT_ID_DEV_NAME);
                url = "https://devsandbox.ippayments.com.au/rapi/";
                break;
            case "UAT":
                currentMerchantIdKeyName = getString(R.string.MERCHANT_ID_UAT_NAME);
                url = "https://uat.ippayments.com.au/rapi/";
                break;
            case "PROD":
                currentMerchantIdKeyName = getString(R.string.MERCHANT_ID_PROD_NAME);
                url = "https://www.ippayments.com.au/rapi/";
                break;
        }
        String readMerchantID = storage.getMerchantIdFromStorage(currentMerchantIdKeyName);
        if (readMerchantID == null || readMerchantID.isEmpty()) {
            BNLog.e(getClass().getSimpleName(), "There was an error getting the Merchant ID");
        }

        // Setup BNPaymentHandler
        BNPaymentBuilder paymentBuilder = new BNPaymentBuilder(getApplicationContext())
                .merchantAccount(readMerchantID)
                .debug(true)
                .baseUrl(url);

        BNPaymentHandler.setupBNPayments(paymentBuilder);

        // ADD the JSON registration custom data
        {
            JSONObject registrationJsonData = getJsonRegData();
            if (registrationJsonData != null) {
                Log.i(getClass().getSimpleName(), registrationJsonData.toString());
                BNPaymentHandler.getInstance().setRegistrationJsonData(registrationJsonData);
            } else {
                BNLog.e(getClass().getSimpleName(), "The registration data is not set and must be entered before using the app");
            }
        }
        setupView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupView() {
        Button hppButton = (Button) findViewById(R.id.hpp_button);
        hppButton.setOnClickListener(mHppButtonListener);


        // No HPP in APAC SDK
        {
            hppButton.setVisibility(View.GONE);
        }

        Button nativeRegistrationButton = (Button) findViewById(R.id.native_registration_button);
        nativeRegistrationButton.setOnClickListener(mNativeRegistrationButtonListener);

        Button makeTransactionButton = (Button) findViewById(R.id.make_transaction_button);
        makeTransactionButton.setOnClickListener(mMakeTransactionListener);

        Button listCreditCardsButton = (Button) findViewById(R.id.list_credit_cards_button);
        listCreditCardsButton.setOnClickListener(mListCreditCardsListener);

        Button developerPageButton = (Button) findViewById(R.id.developer_button);
        developerPageButton.setOnClickListener(mDeveloperButtonListener);
    }

    private void showHostedPaymentPage() {
        Intent intent = new Intent(this, HostedPaymentPageActivity.class);
        startActivity(intent);
    }

    private void showNativeCardRegistration() {
        Intent intent = new Intent(this, NativeCardRegistrationActivity.class);
        startActivity(intent);
    }

    private void makeTransaction(CreditCard creditCard) {
        String paymentId = "test-payment-" + new Date().getTime();
        PaymentSettings paymentSettings = new PaymentSettings();
        paymentSettings.amount = 100;
        paymentSettings.comment = "This is a test transaction.";
        paymentSettings.creditCardToken = creditCard.getCreditCardToken();
        paymentSettings.currency = "AUD";
        paymentSettings.cvcCode =  "123";

        JSONObject paymentJsonData = getJsonPayData();
        Log.i(getClass().getSimpleName(), paymentJsonData.toString());
        paymentSettings.paymentJsonData = paymentJsonData;

        BNPaymentHandler.getInstance().submitSinglePaymentToken(paymentId, paymentSettings, new ITransactionExtListener() {
            @Override
            public void onTransactionSuccess(Map<String, String> responseDictionary) {
                String receipt = responseDictionary.get("receipt");
                showDialog("Success", "The payment succeeded. Receipt: " + (receipt != null?receipt:"?"));
            }

            @Override
            public void onTransactionError(RequestError error) {
                showDialog("Failure", "The payment did not succeed.");
            }
        });
    }

    private void listCreditCards() {
        Intent intent = new Intent(this, CreditCardListActivity.class);
        startActivity(intent);
    }

    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showCardListDialog(final List<CreditCard> creditCardList) {
        CardListAdapter listAdapter = new CardListAdapter(MainActivity.this, creditCardList);
        Builder builder = new Builder(MainActivity.this);
        builder.setTitle("Select a card");
        builder.setNegativeButton("Cancel", null);
        builder.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                makeTransaction(creditCardList.get(which));
            }
        });
        builder.create().show();
    }

    Button.OnClickListener mHppButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showHostedPaymentPage();
        }
    };

    Button.OnClickListener mNativeRegistrationButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showNativeCardRegistration();
        }
    };

    Button.OnClickListener mMakeTransactionListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BNPaymentHandler.getInstance().getRegisteredCreditCards(MainActivity.this, new CreditCardManager.IOnCreditCardRead() {
                @Override
                public void onCreditCardRead(List<CreditCard> creditCards) {
                    if (creditCards != null && creditCards.size() > 0) {
                        showCardListDialog(creditCards);
                    } else {
                        showDialog("No credit card registered", "Please register a credit card in order to make a purchase.");
                    }
                }
            });
        }
    };

    Button.OnClickListener mListCreditCardsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            listCreditCards();
        }
    };

    private void showDeveloperPage() {
        Intent intent = new Intent(this, DeveloperActivity.class);
        startActivity(intent);
    }

    Button.OnClickListener mDeveloperButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showDeveloperPage();
        }
     };

    /**
     * The registration data as a JSON object.
     * @return the json object
     */
    private JSONObject getJsonRegData()
    {
        try {
            String data = storage.getRegDataFromStorage();
            if (!data.equals("")) {
                JSONObject obj = new JSONObject(data);
                return obj;
            }
        } catch (JSONException e) {
            BNLog.jsonParseError(getClass().getSimpleName(), e);
        }
        return null;
    }

    /**
     * The payment data as a JSON object.
     * @return the json object
     */
    private JSONObject getJsonPayData()
    {
        try {
            String data = storage.getPayDataFromStorage();
            if (!data.equals("")) {
                JSONObject obj = new JSONObject(data);
                return obj;
            }
        } catch (JSONException e) {
            BNLog.jsonParseError(getClass().getSimpleName(), e);
        }
        return null;

    }
}
