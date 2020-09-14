package com.bambora.paymentdemo;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.logging.BNLog;
import com.bambora.nativepayment.models.CardRegistrationFormGuiSetting;
import com.bambora.nativepayment.models.SubmitPaymentCardFormGuiSetting;
import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.paymentdemo.adapter.CardListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.regex.Pattern;

public class DeveloperActivity extends AppCompatActivity {
    private String currentMerchantIdName = "";
    private DeviceStorage storage;

    final Context context = this;
    private String currentEnvironment;
    RadioGroup.OnCheckedChangeListener envRadioGroupListener;

    private EditText titleText;
    private EditText cardHolderNameText;
    private EditText cardNumberText;
    private EditText expiryDateText;
    private EditText securityCodeText;
    private EditText buttonColorText;
    private EditText buttonText;

    private EditText payByCardTitleText;
    private EditText payByCardCardHolderNameText;
    private EditText payByCardCardNumberText;
    private EditText payByCardExpiryDateText;
    private EditText payByCardSecurityCodeText;
    private EditText payByCardSwitchButtonColorText;
    private EditText payByCardButtonColorText;
    private EditText payByCardButtonText;
    private EditText payLoadingBarColorText;
    private EditText cardIOColorText;
    private Switch visaCheckoutSwitch;
    private Switch cardIOSwitch;
    private EditText payAmount;

    private EditText registrationCardIOColorText;
    private Switch registrationCardIOSwitch;
    private EditText registrationLoadingBarColorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        storage = new DeviceStorage(context);

        //init registration form components.
        titleText = (EditText) findViewById(R.id.registrationFormTitleText);
        cardHolderNameText = (EditText) findViewById(R.id.regiCardHolderNameText);
        cardNumberText = (EditText) findViewById(R.id.regiCardNumberText);
        expiryDateText = (EditText) findViewById(R.id.regiExpiryDateText);
        securityCodeText = (EditText) findViewById(R.id.regiSecurityCodeText);
        buttonColorText = (EditText) findViewById(R.id.regiButtonColorText);
        buttonText = (EditText) findViewById(R.id.regiButtonText);
        registrationCardIOColorText= (EditText) findViewById(R.id.registration_cardIOColorText);
        registrationCardIOSwitch=(Switch) findViewById(R.id.registration_card_io_switch);
        registrationLoadingBarColorText= (EditText) findViewById(R.id.registration_LoadingBarColorText);

        //init pay by card form components.
        payByCardTitleText = (EditText) findViewById(R.id.payByCardFormTitleText);
        payByCardCardHolderNameText = (EditText) findViewById(R.id.payByCardHolderNameText);
        payByCardCardNumberText = (EditText) findViewById(R.id.payByCardNumberText);
        payByCardExpiryDateText = (EditText) findViewById(R.id.payByCardExpiryDateText);
        payByCardSecurityCodeText = (EditText) findViewById(R.id.payByCardSecurityCodeText);
        payByCardSwitchButtonColorText = (EditText) findViewById(R.id.payByCardSwitchButtonColorText);
        payByCardButtonColorText = (EditText) findViewById(R.id.payByCardButtonColorText);
        payByCardButtonText = (EditText) findViewById(R.id.payByCardButtonText);
        payLoadingBarColorText = (EditText) findViewById(R.id.payLoadingBarColorText);
        cardIOColorText= (EditText) findViewById(R.id.cardIOColorText);
        visaCheckoutSwitch = (Switch) findViewById(R.id.vco_switch);
        cardIOSwitch = (Switch) findViewById(R.id.card_io_switch);
        payAmount= (EditText) findViewById(R.id.amount);



        ProcessEnvironmentSettings();
        //init registration from gui setting;
        initRegistrationFormGuiSetting();
        //init pay by card from gui setting;
        initSubmitSinglePaymentCardFormGuiSetting();
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
                resetAccountAndMode();
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
            // environment has not been set, so default to UAT
            currentEnvironment = "UAT";
        }

        envRadioGroupListener = new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, final int checkedId) {
                new AlertDialog.Builder(context)
                        .setTitle("Change Environment")
                        .setMessage("All your saved cards will be deleted.")
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

        visaCheckoutSwitch.setChecked(storage.getVisaCheckoutStatus());
        visaCheckoutSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                  @Override
                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                      storage.setVisaCheckoutStatus(isChecked);
                     }});
        payAmount.setText(String.valueOf(storage.getPayAmount()));
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

        if (ok) {
            showDialog("Success", "The environment has been successfully changed.");
            resetAccountAndMode();
        } else {
            BNLog.e(getClass().getSimpleName(), "There was an error in saving the environment change");
        }
    }

    // reset the GUID and running environment (DEV/UAT/PROD)
    private void resetAccountAndMode(){
        String currentMerchantIdKeyName = getString(R.string.MERCHANT_ID_DEV_NAME);
        String url = "https://devsandbox.ippayments.com.au/rapi/";
        Boolean debugValue=true;
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
                debugValue=false;
                break;
        }
        String readMerchantID = storage.getMerchantIdFromStorage(currentMerchantIdKeyName);
        BNPaymentHandler.BNPaymentBuilder paymentBuilder = new BNPaymentHandler.BNPaymentBuilder(getApplicationContext())
                .merchantAccount(readMerchantID)
                .debug(debugValue)
                .baseUrl(url);
        BNPaymentHandler.setupBNPayments(paymentBuilder);
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
                Log.i(getClass().getSimpleName(), obj.toString());
                BNPaymentHandler.getInstance().setRegistrationJsonData(obj);
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

        EditText editText = (EditText) findViewById(R.id.regDataText);

        String readRegData = storage.getRegDataFromStorage();
        if (readRegData != null && !readRegData.isEmpty()) {

            editText.setText(readRegData);
        }

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        return false;
                }
                return false;
            }
        });

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

    //initRegistrationFormGuiSetting from storage to ui components.
    private void initRegistrationFormGuiSetting()
    {
        CardRegistrationFormGuiSetting regiGuiSetting = storage.getRegistrationFormCustomizationSetting();
        securityCodeText.setText(regiGuiSetting.SecurityCodeWatermark);
        buttonColorText.setText(regiGuiSetting.RegisterButtonColor);
        titleText.setText(regiGuiSetting.TitleText);
        cardHolderNameText.setText(regiGuiSetting.CardHolderWatermark);
        cardNumberText.setText(regiGuiSetting.CardNumberWatermark);
        expiryDateText.setText(regiGuiSetting.ExpiryDateWatermark);
        buttonText.setText(regiGuiSetting.RegisterButtonText);
        registrationLoadingBarColorText.setText(regiGuiSetting.LoadingBarColor);
        registrationCardIOColorText.setText(regiGuiSetting.CardIOColorText);
        registrationCardIOSwitch.setChecked(regiGuiSetting.CardIOEnable);
    }

    /**
     * This method is the handler for the Save registration form's customization
     * @param v the view
     */
    public void SaveRegistrationFormCustomizationClickHandler(View v) {
        CardRegistrationFormGuiSetting registrationGuiSetting = new CardRegistrationFormGuiSetting();
        registrationGuiSetting.SecurityCodeWatermark = securityCodeText.getText().toString();
        registrationGuiSetting.RegisterButtonColor = buttonColorText.getText().toString();
        registrationGuiSetting.TitleText = titleText.getText().toString();
        registrationGuiSetting.CardHolderWatermark = cardHolderNameText.getText().toString();
        registrationGuiSetting.CardNumberWatermark = cardNumberText.getText().toString();
        registrationGuiSetting.ExpiryDateWatermark = expiryDateText.getText().toString();
        registrationGuiSetting.RegisterButtonText = buttonText.getText().toString();
        registrationGuiSetting.LoadingBarColor = registrationLoadingBarColorText.getText().toString();
        registrationGuiSetting.CardIOColorText = registrationCardIOColorText.getText().toString();
        registrationGuiSetting.CardIOEnable = registrationCardIOSwitch.isChecked();
        Boolean ok = storage.saveRegistrationFormGuiSettingToStorage(registrationGuiSetting);
        if (ok) {
            showDialog("Success", "The registration form gui setting was successfully saved");
        }
        else {
            BNLog.e(getClass().getSimpleName(), "There was an error in saving the registration form gui setting");
            showDialog("Failure", "There was an error in saving the registration form gui setting");
        }
    }

    //initSubmitSinglePaymentCardFormGuiSetting from storage to ui components.
    private void initSubmitSinglePaymentCardFormGuiSetting()
    {
        SubmitPaymentCardFormGuiSetting payByCardGuiSetting = storage.getSubmitPaymentCardFormCustomizationSetting();
        payByCardButtonColorText.setText(payByCardGuiSetting.PayByCardButtonColor);
        payByCardButtonText.setText(payByCardGuiSetting.PayByCardButtonText);
        payByCardCardHolderNameText.setText(payByCardGuiSetting.CardHolderWatermark);
        payByCardCardNumberText.setText(payByCardGuiSetting.CardNumberWatermark);
        payByCardExpiryDateText.setText(payByCardGuiSetting.ExpiryDateWatermark);
        payByCardSecurityCodeText.setText(payByCardGuiSetting.SecurityCodeWatermark);
        payByCardSwitchButtonColorText.setText(payByCardGuiSetting.SwitchButtonColor);
        payByCardTitleText.setText(payByCardGuiSetting.TitleText);
        payLoadingBarColorText.setText(payByCardGuiSetting.PayLoadingBarColor);
        cardIOColorText.setText(payByCardGuiSetting.CardIOColorText);
        cardIOSwitch.setChecked(payByCardGuiSetting.CardIOEnable);
    }

    /**
     * This method is the handler for the Save pay by card form's customization
     * @param v the view
     */
    public void SaveSubmitSinglePaymentCardFormCustomizationClickHandler(View v) {
        SubmitPaymentCardFormGuiSetting payByCardGuiSetting = storage.getSubmitPaymentCardFormCustomizationSetting();
        payByCardGuiSetting.SwitchButtonColor = payByCardSwitchButtonColorText.getText().toString();
        payByCardGuiSetting.CardHolderWatermark = payByCardCardHolderNameText.getText().toString();
        payByCardGuiSetting.CardNumberWatermark = payByCardCardNumberText.getText().toString();
        payByCardGuiSetting.ExpiryDateWatermark = payByCardExpiryDateText.getText().toString();
        payByCardGuiSetting.PayByCardButtonColor = payByCardButtonColorText.getText().toString();
        payByCardGuiSetting.PayByCardButtonText = payByCardButtonText.getText().toString();
        payByCardGuiSetting.SecurityCodeWatermark = payByCardSecurityCodeText.getText().toString();
        payByCardGuiSetting.TitleText = payByCardTitleText.getText().toString();
        payByCardGuiSetting.PayLoadingBarColor = payLoadingBarColorText.getText().toString();
        payByCardGuiSetting.CardIOColorText = cardIOColorText.getText().toString();
        payByCardGuiSetting.CardIOEnable = cardIOSwitch.isChecked();
        Boolean ok = storage.saveSubmitPaymentCardFormGuiSettingToStorage(payByCardGuiSetting);
        if (ok) {
            showDialog("Success", "The SubmitSinglePaymentCard form gui setting was successfully saved");
        }
        else {
            BNLog.e(getClass().getSimpleName(), "There was an error in saving the SubmitSinglePaymentCard form gui setting");
            showDialog("Failure", "There was an error in saving the SubmitSinglePaymentCard form gui setting");
        }

        try {
            String amountString=payAmount.getText().toString();
            if(!amountString.equalsIgnoreCase(""))
            {
                float numberValue = Float.parseFloat(amountString);
                storage.setPayAmount(numberValue);
            }

        } catch (NumberFormatException e) {
            showDialog("Failure", "Please input an valid amount");
        }


     }



    /**
     * This populates the payment data text on the screen, from the value in device storage.
     */
    private void DisplayPayData() {
        // get payment data from shared preferences
        String readPayData = storage.getPayDataFromStorage();
        EditText editText = (EditText) findViewById(R.id.payDataText);
        if (readPayData != null && !readPayData.isEmpty()) {

            editText.setText(readPayData);
        }

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        return false;
                }
                return false;
            }
        });


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
