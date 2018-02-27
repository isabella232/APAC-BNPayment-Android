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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.handlers.BNPaymentHandler.BNPaymentBuilder;
import com.bambora.nativepayment.interfaces.ICardRegistrationCallback;
import com.bambora.nativepayment.interfaces.ITransactionExtListener;
import com.bambora.nativepayment.logging.BNLog;
import com.bambora.nativepayment.managers.CreditCardManager;
import com.bambora.nativepayment.models.PaymentSettings;
import com.bambora.nativepayment.models.PaymentType;
import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.network.RequestError;
import com.bambora.paymentdemo.adapter.CardListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    /**
     * This is a test merchant number that can be used for testing Native Payment.
     * Please replace this with your own merchant number after signing up with Bambora.
     */

    private DeviceStorage storage;
    final Context context = this;
    private ProgressBar progressBar;
    RelativeLayout mainLayout;
    Button submitPreAuthTokenButton;
    Button nativeRegistrationButton;
    Button makeTransactionButton;
    Button listCreditCardsButton;
    Button developerPageButton;
    Button makeTransactionCardButton;
    Button submitPreAuthCardButton;
    Button testSdkApiButton;

    Boolean testSdkApiPayCardFinish;
    Boolean testSdkApiPreAuthCardFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        storage = new DeviceStorage(context);
        String currentEnvironment = storage.getEnvironmentNameFromStorage();
        if (currentEnvironment == null || currentEnvironment.isEmpty() ||
                (!currentEnvironment.equals("DEV") && !currentEnvironment.equals("UAT") && !currentEnvironment.equals("PROD"))) {
            // environment has not been set, so default to UAT
            currentEnvironment = "UAT";
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

        nativeRegistrationButton = (Button) findViewById(R.id.native_registration_button);
        nativeRegistrationButton.setOnClickListener(mNativeRegistrationButtonListener);

        makeTransactionButton = (Button) findViewById(R.id.make_transaction_button);
        makeTransactionButton.setOnClickListener(mMakeTransactionListener);

        submitPreAuthTokenButton = (Button) findViewById(R.id.submit_pre_auth_token_button);
        submitPreAuthTokenButton.setOnClickListener(mSubmitPreAuthTokenListener);

        listCreditCardsButton = (Button) findViewById(R.id.list_credit_cards_button);
        listCreditCardsButton.setOnClickListener(mListCreditCardsListener);

        developerPageButton = (Button) findViewById(R.id.developer_button);
        developerPageButton.setOnClickListener(mDeveloperButtonListener);

        makeTransactionCardButton = (Button) findViewById(R.id.make_transaction_card_button);
        makeTransactionCardButton.setOnClickListener(mMakeTransactionCardListener);

        submitPreAuthCardButton = (Button) findViewById(R.id.submit_pre_auth_card_button);
        submitPreAuthCardButton.setOnClickListener(mSubmitPreAuthCardListener);

        testSdkApiButton = (Button) findViewById(R.id.test_sdk_api_button);
        testSdkApiButton.setOnClickListener(mSdkApiTesting);

        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        progressBar=new ProgressBar(context,null,android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mainLayout.addView(progressBar,params);
        progressBar.setVisibility(View.INVISIBLE);


    }

    private void showHostedPaymentPage() {
        Intent intent = new Intent(this, HostedPaymentPageActivity.class);
        startActivity(intent);
    }

    private void showNativeCardRegistration() {
        Intent intent = new Intent(this, NativeCardRegistrationActivity.class);
        startActivity(intent);
    }

    private void showNativeMakeTransactionCard(PaymentType.PaymentTypeEnum paymentorPreAuth) {
        Intent intent = new Intent(this, NativeMakeTransactionCardActivity.class);

        intent.putExtra("PaymentOrPreAuth", paymentorPreAuth.ordinal());

        startActivity(intent);
    }


    private void startLoadingUI(Button button){
        button.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void stopLoadingUI(Button button){
        button.setEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);
    }




    private void submitSinglePaymentToken(CreditCard creditCard) {
        startLoadingUI(makeTransactionButton);
        String paymentId = "test-payment-token-" + new Date().getTime();
        PaymentSettings paymentSettings = new PaymentSettings();
        paymentSettings.amount = 200;
        paymentSettings.comment = "This is a test transaction by token.";
        paymentSettings.creditCardToken = creditCard.getCreditCardToken();
        paymentSettings.currency = "AUD";
        paymentSettings.cvcCode =  "123";
        JSONObject paymentJsonData = getJsonPayData();
        Log.i(getClass().getSimpleName(), paymentJsonData.toString());
        paymentSettings.paymentJsonData = paymentJsonData;
        BNPaymentHandler.getInstance().submitSinglePaymentToken(paymentId, paymentSettings, new ITransactionExtListener() {
                @Override
                public void onTransactionSuccess(Map<String, String> responseDictionary) {
                    stopLoadingUI(makeTransactionButton);
                    String receipt = responseDictionary.get("receipt");
                    showDialog("Success", "The payment succeeded. Receipt: " + (receipt != null?receipt:"?"));
                }

                @Override
                public void onTransactionError(RequestError error) {
                    stopLoadingUI(makeTransactionButton);
                    showDialog("Failure", "The payment did not succeed.");
                }
        });
    }






    private void submitPreAuthToken(CreditCard creditCard) {
        startLoadingUI(submitPreAuthTokenButton);
        String paymentId = "test-pre-auth-token-" + new Date().getTime();
        PaymentSettings paymentSettings = new PaymentSettings();
        paymentSettings.amount = 100;
        paymentSettings.comment = "This is a test token PreAuth.";
        paymentSettings.creditCardToken = creditCard.getCreditCardToken();
        paymentSettings.currency = "AUD";
        paymentSettings.cvcCode =  "123";
        JSONObject paymentJsonData = getJsonPayData();
        Log.i(getClass().getSimpleName(), paymentJsonData.toString());
        paymentSettings.paymentJsonData = paymentJsonData;
        BNPaymentHandler.getInstance().submitPreAuthToken(paymentId, paymentSettings, new ITransactionExtListener() {
            @Override
            public void onTransactionSuccess(Map<String, String> responseDictionary) {
                stopLoadingUI(submitPreAuthTokenButton);
                String receipt = responseDictionary.get("receipt");
                showDialog("Success", "The PreAuth succeeded. Receipt: " + (receipt != null?receipt:"?"));
            }

            @Override
            public void onTransactionError(RequestError error) {
                stopLoadingUI(submitPreAuthTokenButton);
                showDialog("Failure", "The PreAuth did not succeed.");
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

    private void showCardListDialogForSubmitPaymentToken(final List<CreditCard> creditCardList) {
        CardListAdapter listAdapter = new CardListAdapter(MainActivity.this, creditCardList);
        Builder builder = new Builder(MainActivity.this);
        builder.setTitle("Token Payment - Select a card");
        builder.setNegativeButton("Cancel", null);
        builder.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitSinglePaymentToken(creditCardList.get(which));
            }
        });
        builder.create().show();
    }

    /*
    * showCardListDialogForSubmitPreAuthToken
    * handle display the list of credit card, and pre-auth click handling.
    * */
    private void showCardListDialogForSubmitPreAuthToken(final List<CreditCard> creditCardList) {
        CardListAdapter listAdapter = new CardListAdapter(MainActivity.this, creditCardList);
        Builder builder = new Builder(MainActivity.this);
        builder.setTitle("PreAuth - Select a card");
        builder.setNegativeButton("Cancel", null);
        builder.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitPreAuthToken(creditCardList.get(which));
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
                        showCardListDialogForSubmitPaymentToken(creditCards);
                    } else {
                        showDialog("No credit card registered", "Please register a credit card in order to make a purchase.");
                    }
                }
            });
        }
    };

    /*
    * mSubmitPreAuthTokenListener, handle PreAuth button click.
    * */
    Button.OnClickListener mSubmitPreAuthTokenListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BNPaymentHandler.getInstance().getRegisteredCreditCards(MainActivity.this, new CreditCardManager.IOnCreditCardRead() {
                @Override
                public void onCreditCardRead(List<CreditCard> creditCards) {
                    if (creditCards != null && creditCards.size() > 0) {
                        showCardListDialogForSubmitPreAuthToken(creditCards);
                    } else {
                        showDialog("No credit card registered", "Please register a credit card in order to make a pre-auth.");
                    }
                }
            });
        }
    };

    Button.OnClickListener mMakeTransactionCardListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            showNativeMakeTransactionCard(PaymentType.PaymentTypeEnum.PaymentCard);
        }
    };

    Button.OnClickListener mSubmitPreAuthCardListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            showNativeMakeTransactionCard(PaymentType.PaymentTypeEnum.PreAuthCard);
        }
    };

    /*
     * Used to programmatically test the SDK API.
     */
    Button.OnClickListener mSdkApiTesting = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startLoadingUI(testSdkApiButton);
            testSdkApiPayCard();
            testSdkApiPreAuthCard();
        }
    };

    /*
     * This method performs a $1 payment by card using the programmatic API.
     */

    private void checkTestSDKAPIFinish(){
        if(testSdkApiPayCardFinish && testSdkApiPreAuthCardFinish)
        {
            stopLoadingUI(testSdkApiButton);
        }

    }



    private void testSdkApiPayCard() {
        testSdkApiPayCardFinish=false;
        PaymentSettings paymentSettings = new PaymentSettings();
        paymentSettings.amount = 100;
        paymentSettings.comment = "This is a test payment of $1";
        paymentSettings.currency = "AUD";
        JSONObject paymentJsonData = getJsonPayData();
        paymentSettings.paymentJsonData = paymentJsonData;
        Boolean isRequestToken = false;

        BNPaymentHandler.getInstance().submitSinglePaymentCard(
                getApplicationContext(),
                "APIpay",
                paymentSettings,
                "Card Holder Name",
                "4242424242424242",
                "12",  //Expiry Month
                "20",  //Expiry Year
                "123", //CVC
                isRequestToken,
                new ITransactionExtListener() {
                    @Override
                    public void onTransactionSuccess(Map<String, String> responseDictionary) {
                        testSdkApiPayCardFinish=true;
                        checkTestSDKAPIFinish();
                        String receipt = responseDictionary.get("receipt");
                        showDialog("Success", "The Pay succeeded. Receipt: " + (receipt != null?receipt:"?"));
                    }

                    @Override
                    public void onTransactionError(RequestError error) {
                        testSdkApiPayCardFinish=true;
                        checkTestSDKAPIFinish();
                        showDialog("Failure", "The Pay did not succeed.");
                    }
                },
            new CreditCardManager.IOnCreditCardSaved() {
                @Override
                public void onCreditCardSaved(CreditCard creditCard) {
                    testSdkApiPayCardFinish=true;
                    checkTestSDKAPIFinish();
                }
            }
        );
      }

    /*
     * This method performs a $1 pre-auth by card using the programmatic API.
     */
    private void testSdkApiPreAuthCard() {
        testSdkApiPreAuthCardFinish=false;
        PaymentSettings paymentSettings = new PaymentSettings();
        paymentSettings.amount = 100;
        paymentSettings.comment = "This is a test preAuth of $1";
        paymentSettings.currency = "AUD";
        JSONObject paymentJsonData = getJsonPayData();
        paymentSettings.paymentJsonData = paymentJsonData;
        Boolean isRequestToken = false;

        BNPaymentHandler.getInstance().submitSinglePreAuthCard(
                getApplicationContext(),
                "APIpreauth",
                paymentSettings,
                "Card Holder Name",
                "4242424242424242",
                "12",  //Expiry Month
                "20",  //Expiry Year
                "123", //CVC
                isRequestToken,
                new ITransactionExtListener() {
                    @Override
                    public void onTransactionSuccess(Map<String, String> responseDictionary) {
                        testSdkApiPreAuthCardFinish=true;
                        checkTestSDKAPIFinish();
                        String receipt = responseDictionary.get("receipt");
                        showDialog("Success", "The PreAuth succeeded. Receipt: " + (receipt != null?receipt:"?"));
                    }

                    @Override
                    public void onTransactionError(RequestError error) {
                        testSdkApiPreAuthCardFinish=true;
                        checkTestSDKAPIFinish();
                        showDialog("Failure", "The PreAuth did not succeed.");
                    }
                },
                new CreditCardManager.IOnCreditCardSaved() {
                    @Override
                    public void onCreditCardSaved(CreditCard creditCard) {
                        testSdkApiPreAuthCardFinish=true;
                        checkTestSDKAPIFinish();
                    }
                }
        );
    }

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
