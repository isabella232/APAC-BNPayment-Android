package com.bambora.paymentdemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.logging.BNLog;
import com.bambora.nativepayment.managers.CreditCardManager;
import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.paymentdemo.adapter.CardListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class DeveloperActivity extends AppCompatActivity {
    private String currentMerchantIdName = "";
    private DeviceStorage storage;

    final Context context = this;
    private String currentEnvironment;
    RadioGroup.OnCheckedChangeListener envRadioGroupListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        storage = new DeviceStorage(context);

        ProcessEnvironmentSettings();
        ShowBuildInfo();
    }

    /**
     * This method is the handler for the Save merchant ID button
     * @param v the view
     */
    public void SaveMerchantIDClickHandler(View v) {
        // store Merchant ID to shared preferences
        EditText editText = (EditText) findViewById(R.id.merchantIDText);
        String merchID = editText.getText().toString();

        if(isValidGUID(merchID)) {
            Boolean ok = storage.saveMerchantIdFToStorage(currentMerchantIdName, merchID);
            if (ok) {
                showDialog("Success", "The Merchant ID was successfully saved");
            } else {
                BNLog.e(getClass().getSimpleName(), "There was an error in saving the Merchant ID");
                showDialog("Failure", "There was an error in saving the Merchant ID");
            }
        } else {
            showDialog("Invalid ID", "The Merchant ID is not in the correct format");
        }
    }

    /**
     * This method is used to pop up a modal dialog box
     * @param title This is shows in the top line
     * @param message this is the message shown below the title
     */
    private void showDialog(String title, String message) {
        final AlertDialog.Builder adb = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        adb.show();
    }

    /**
     * This function determines whether the specified guid is valid or not.
     * @param guid the string to evaluate
     * @return true if a valid guid, otherwise false.
     */
    private boolean isValidGUID(String guid) {
        if(!Pattern.matches("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}", guid)) {
            /* this is not a valid guid */
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method displays the current environment and sets up the handlers to be used to support change of
     * the environment.
     */
    private void ProcessEnvironmentSettings() {
        // get environment from shared preferences
        currentEnvironment = storage.getEnvironmentNameFromStorage();
        if (currentEnvironment == null || currentEnvironment.isEmpty() ||
                (!currentEnvironment.equals("DEV") && !currentEnvironment.equals("UAT") && !currentEnvironment.equals("PROD"))) {
            // environment has not been set, so default to DEV
            currentEnvironment = "DEV";
        }

        envRadioGroupListener = new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, final int checkedId) {
                new AlertDialog.Builder(context)
                        .setTitle("Change Environment")
                        .setMessage("All your saved cards will be deleted.\nPlease restart the app for the change to take effect")
                        // Add the buttons
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                HandleEnvironmentChange(checkedId);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog. Change back the selected radio button
                                UpdateToCurrentEnvironment();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        };

        UpdateToCurrentEnvironment();
        DisplayCustomData();

        // Setup listener for radio group
        RadioGroup rGroup = (RadioGroup)findViewById(R.id.radioGroup);
        rGroup.setOnCheckedChangeListener(envRadioGroupListener);
    }

    private void DisplayCurrentMerchantID() {
        // get merchant ID from shared preferences and use it to populate the merchant ID field.
        String readMerchantID = storage.getMerchantIdFromStorage(currentMerchantIdName);
        if (readMerchantID != null && !readMerchantID.isEmpty()) {
            EditText editText = (EditText) findViewById(R.id.merchantIDText);
            editText.setText(readMerchantID);
        }
    }

    /**
     * This method changes the environment and writes those to device storage.
     * It also deletes all registered cards from the device.
     * @param envToChangeTo This is the new environment that will be changed to.
     */
    private void HandleEnvironmentChange(int envToChangeTo) {
        String env = "";
        switch (envToChangeTo) {
            case R.id.devEnvBtn:
                env = "DEV";
                break;
            case R.id.uatEnvBtn:
                env = "UAT";
                break;
            case R.id.prodEnvBtn:
                env = "PROD";
                break;
            default:
                BNLog.e(getClass().getSimpleName(), "An invalid ID was returned from radio group");
                return;
        }
        currentEnvironment = env; // this is now the new current environment

        // save the name of the current environment to device storage
        Boolean ok = storage.saveEnvironmentNameToStorage(currentEnvironment);

        UpdateToCurrentEnvironment();

        DeleteAllCards();

        if (ok) {
            showDialog("Success", "The environment has been successfully changed. Please restart the app");
        } else {
            BNLog.e(getClass().getSimpleName(), "There was an error in saving the environment change");
        }
    }

    private void DeleteAllCards() {
        final BNPaymentHandler paymentHandler = BNPaymentHandler.getInstance();
        paymentHandler.getRegisteredCreditCards(this, new CreditCardManager.IOnCreditCardRead() {
            @Override
            public void onCreditCardRead(List<CreditCard> creditCards) {
                if (creditCards != null) {
                    for (int ccNum = 0; ccNum < creditCards.size(); ccNum++) {
                        CreditCard cc = creditCards.get(ccNum);
                        BNPaymentHandler.getInstance().deleteCreditCard(context /*null*/, cc.getCreditCardToken(), null);
                    }
                }
            }
        });
    }

    /**
     * This selects the environment radio button and sets the merchantid storage name corresponding to the currentEnvironment object variable.
     */
    private void UpdateToCurrentEnvironment() {

        // Disable listener for radio group, so that we don't get called back since we only want that on user input.
        RadioGroup rGroup = (RadioGroup)findViewById(R.id.radioGroup);
        rGroup.setOnCheckedChangeListener(null);

        // indicate the correct environment on the appropriate radio button
        switch (currentEnvironment) {
            case "DEV":
                currentMerchantIdName = getString(R.string.MERCHANT_ID_DEV_NAME);
                RadioButton devBtn = (RadioButton) findViewById(R.id.devEnvBtn);
                devBtn.setChecked(true);
                break;
            case "UAT":
                currentMerchantIdName = getString(R.string.MERCHANT_ID_UAT_NAME);
                RadioButton uatBtn = (RadioButton) findViewById(R.id.uatEnvBtn);
                uatBtn.setChecked(true);
                break;
            case "PROD":
                currentMerchantIdName = getString(R.string.MERCHANT_ID_PROD_NAME);
                RadioButton prodBtn = (RadioButton) findViewById(R.id.prodEnvBtn);
                prodBtn.setChecked(true);
                break;
        }

        DisplayCurrentMerchantID();

        // Reenable listener for radio group
        rGroup.setOnCheckedChangeListener(envRadioGroupListener);
    }

    /********************************* Custom Data ****************************************************/

    /**
     * This method is the handler for the Save RegData button
     * @param v the view
     */
    public void SaveRegDataClickHandler(View v) {
        // store registration data to shared preferences
        EditText editText = (EditText) findViewById(R.id.regDataText);
        String regData = editText.getText().toString();
        Boolean ok = true;

        // validate that this is valid JSON (if non-empty)
        try {
            if (!regData.equals("")) {
                JSONObject obj = new JSONObject(regData);
            }
            ok = storage.saveRegDataToStorage(regData);
        } catch (JSONException e) {
            ok = false;
        }

        if (ok) {
            showDialog("Success", "The custom registration data was successfully saved");
        } else {
            BNLog.e(getClass().getSimpleName(), "There was an error in saving the custom registration data");
            showDialog("Failure", "There was an error in saving the custom registration data");
        }
    }

    /**
     * This populates the registration data text on the screen, from the value in device storage.
     */
    private void DisplayRegData() {
        // get registration data from shared preferences
        String readRegData = storage.getRegDataFromStorage();
        if (readRegData != null && !readRegData.isEmpty()) {
            EditText editText = (EditText) findViewById(R.id.regDataText);
            editText.setText(readRegData);
        }
    }

    /**
     * This method is the handler for the Save PayData button
     * @param v the view
     */
    public void SavePayDataClickHandler(View v) {
        // store payment data to shared preferences
        EditText editText = (EditText) findViewById(R.id.payDataText);
        String payData = editText.getText().toString();
        Boolean ok = true;

        // validate that this is valid JSON (if non-empty)
        try {
            if (!payData.equals("")) {
                JSONObject obj = new JSONObject(payData);
            }
            ok = storage.savePayDataToStorage(payData);
        } catch (JSONException e) {
            ok = false;
        }

        if (ok) {
            showDialog("Success", "The custom payment data was successfully saved");
        } else {
            BNLog.e(getClass().getSimpleName(), "There was an error in saving the custom payment data");
            showDialog("Failure", "There was an error in saving the custom payment data");
        }
    }

    /**
     * This populates the payment data text on the screen, from the value in device storage.
     */
    private void DisplayPayData() {
        // get payment data from shared preferences
        String readPayData = storage.getPayDataFromStorage();
        if (readPayData != null && !readPayData.isEmpty()) {
            EditText editText = (EditText) findViewById(R.id.payDataText);
            editText.setText(readPayData);
        }
    }

    private void DisplayCustomData() {
        DisplayRegData();
        DisplayPayData();
    }

    private void ShowBuildInfo() {
        TextView info = (TextView) findViewById(R.id.BuildInfo);
        Date buildDate = new Date(BuildConfig.TIMESTAMP);
        String infoString = String.format("%d (%td/%tm/%ty)", BuildConfig.TIMESTAMP, buildDate, buildDate, buildDate);
        info.setText(infoString);
    }

    /*********************************************************************************************/

}
