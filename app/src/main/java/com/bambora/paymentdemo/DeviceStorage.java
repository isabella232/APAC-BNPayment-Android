package com.bambora.paymentdemo;

import android.content.SharedPreferences;
import android.content.Context;
import com.bambora.nativepayment.logging.BNLog;
import com.bambora.nativepayment.models.CardRegistrationFormGuiSetting;
import com.bambora.nativepayment.models.CardRegistrationFormGuiSettingEnum;
import com.bambora.nativepayment.models.SubmitPaymentCardFormGuiSetting;
import com.bambora.nativepayment.models.SubmitPaymentCardFormGuiSettingEnum;

/**
 * Created by pe010193 on 6/06/2017.
 */

public class DeviceStorage {
    public static final String PREFS_NAME = "BNPrefsFile";
    private SharedPreferences settings;
    private Context context;
    public static final String ENVIRONMENT_NAME = "EnvironmentStore";
    public static final String REGISTRATION_DATA_NAME = "RegistrationDataStore";
    public static final String PAYMENT_DATA_NAME = "PaymentDataStore";

    /**
     * Constructor
     * @param context
     */
    public DeviceStorage(Context context) {
        this.context = context;
        settings = context.getSharedPreferences(PREFS_NAME, 0);
    }

    /********************************* Merchant ID ****************************************************/

    /**
     * Returns the merchant id string from device storage which matches the name of the key in which it was stored
     * @param storageKey the name of the storage key
     * @return the merchant id string, or empty string if it was empty or there was an error obtaining it.
     */
    public String getMerchantIdFromStorage(String storageKey) {
        try {
            return settings.getString(storageKey, "");
        } catch (Exception e) {
            BNLog.e(getClass().getSimpleName(), "Internal error; failed to get merchant id", e);
        }
        return "";
    }

    /**
     * Saves the merchant id string to device storage, with the specified storage key.
     * @param storageKey the name of the storage key
     * @param merchantId the merchant ID string
     * @return true if saved successfully, otherwise false.
     */
    public boolean saveMerchantIdFToStorage(String storageKey, String merchantId) {
        Boolean ok = false;
        try {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(storageKey, merchantId);
            ok = editor.commit();
        } catch (Exception e) {
            BNLog.e(getClass().getSimpleName(), "Internal error; failed to save merchant id", e);
        }
        return ok;
    }


    /********************************* Registration Form Customization ****************************************************/
    /**
     * Saves the registration form's gui setting to device storage, with the specified storage key for each individual properties.
     * @param guiSetting the guiSetting of registration form.
     * @return true if saved successfully, otherwise false.
     */
    public boolean saveRegistrationFormGuiSettingToStorage(CardRegistrationFormGuiSetting guiSetting) {
        Boolean ok = false;
        try {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(CardRegistrationFormGuiSettingEnum.TitleText.toString(), guiSetting.TitleText);
            editor.putString(CardRegistrationFormGuiSettingEnum.RegisterButtonText.toString(), guiSetting.RegisterButtonText);
            editor.putString(CardRegistrationFormGuiSettingEnum.CardHolderWatermark.toString(), guiSetting.CardHolderWatermark);
            editor.putString(CardRegistrationFormGuiSettingEnum.CardNumberWatermark.toString(), guiSetting.CardNumberWatermark);
            editor.putString(CardRegistrationFormGuiSettingEnum.ExpiryDateWatermark.toString(), guiSetting.ExpiryDateWatermark);
            editor.putString(CardRegistrationFormGuiSettingEnum.SecurityCodeWatermark.toString(), guiSetting.SecurityCodeWatermark);
            editor.putString(CardRegistrationFormGuiSettingEnum.RegisterButtonColor.toString(), guiSetting.RegisterButtonColor);
            ok = editor.commit();
        } catch (Exception e) {
            BNLog.e(getClass().getSimpleName(), "Internal error; failed to CardRegistrationFormGuiSetting", e);
        }
        return ok;
    }

    /**
     * Returns the registration form customization setting.
     * @return CardRegistrationFormGuiSetting.
     */
    public CardRegistrationFormGuiSetting getRegistrationFormCustomizationSetting() {
        CardRegistrationFormGuiSetting regiGuiSetting= new CardRegistrationFormGuiSetting();
        try {
            regiGuiSetting.TitleText =  settings.getString(CardRegistrationFormGuiSettingEnum.TitleText.toString(), "");
            regiGuiSetting.RegisterButtonText =  settings.getString(CardRegistrationFormGuiSettingEnum.RegisterButtonText.toString(), "");
            regiGuiSetting.CardHolderWatermark =  settings.getString(CardRegistrationFormGuiSettingEnum.CardHolderWatermark.toString(), "");
            regiGuiSetting.CardNumberWatermark =  settings.getString(CardRegistrationFormGuiSettingEnum.CardNumberWatermark.toString(), "");
            regiGuiSetting.ExpiryDateWatermark =  settings.getString(CardRegistrationFormGuiSettingEnum.ExpiryDateWatermark.toString(), "");
            regiGuiSetting.SecurityCodeWatermark =  settings.getString(CardRegistrationFormGuiSettingEnum.SecurityCodeWatermark.toString(), "");
            regiGuiSetting.RegisterButtonColor =  settings.getString(CardRegistrationFormGuiSettingEnum.RegisterButtonColor.toString(), "");
        } catch (Exception e) {
            BNLog.e(getClass().getSimpleName(), "Internal error; failed to get CardRegistrationFormGuiSetting", e);
        }
        return regiGuiSetting;
    }



    /********************************* Submit Payment by Card Form Customization ****************************************************/
    /**
     * Saves the SubmitPayment by Card form's gui setting to device storage, with the specified storage key for each individual properties.
     * @param guiSetting the guiSetting of submit single payment card form.
     * @return true if saved successfully, otherwise false.
     */
    public boolean saveSubmitPaymentCardFormGuiSettingToStorage(SubmitPaymentCardFormGuiSetting guiSetting) {
        Boolean ok = false;
        try {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(SubmitPaymentCardFormGuiSettingEnum.TitleText.toString(), guiSetting.TitleText);
            editor.putString(SubmitPaymentCardFormGuiSettingEnum.PayByCardButtonText.toString(), guiSetting.PayByCardButtonText);
            editor.putString(SubmitPaymentCardFormGuiSettingEnum.CardHolderWatermark.toString(), guiSetting.CardHolderWatermark);
            editor.putString(SubmitPaymentCardFormGuiSettingEnum.CardNumberWatermark.toString(), guiSetting.CardNumberWatermark);
            editor.putString(SubmitPaymentCardFormGuiSettingEnum.ExpiryDateWatermark.toString(), guiSetting.ExpiryDateWatermark);
            editor.putString(SubmitPaymentCardFormGuiSettingEnum.SecurityCodeWatermark.toString(), guiSetting.SecurityCodeWatermark);
            editor.putString(SubmitPaymentCardFormGuiSettingEnum.SwitchButtonColor.toString(), guiSetting.SwitchButtonColor);
            editor.putString(SubmitPaymentCardFormGuiSettingEnum.PayByCardButtonColor.toString(), guiSetting.PayByCardButtonColor);
            ok = editor.commit();
        } catch (Exception e) {
            BNLog.e(getClass().getSimpleName(), "Internal error; failed to SubmitPaymentCardFormGuiSetting", e);
        }
        return ok;
    }

    /**
     * Returns the SubmitPaymentCard form customization setting.
     * @return CardRegistrationFormGuiSetting.
     */
    public SubmitPaymentCardFormGuiSetting getSubmitPaymentCardFormCustomizationSetting() {
        SubmitPaymentCardFormGuiSetting payByCardGuiSetting= new SubmitPaymentCardFormGuiSetting();
        try {
            payByCardGuiSetting.TitleText =  settings.getString(SubmitPaymentCardFormGuiSettingEnum.TitleText.toString(), "");
            payByCardGuiSetting.PayByCardButtonText =  settings.getString(SubmitPaymentCardFormGuiSettingEnum.PayByCardButtonText.toString(), "");
            payByCardGuiSetting.CardHolderWatermark =  settings.getString(SubmitPaymentCardFormGuiSettingEnum.CardHolderWatermark.toString(), "");
            payByCardGuiSetting.CardNumberWatermark =  settings.getString(SubmitPaymentCardFormGuiSettingEnum.CardNumberWatermark.toString(), "");
            payByCardGuiSetting.ExpiryDateWatermark =  settings.getString(SubmitPaymentCardFormGuiSettingEnum.ExpiryDateWatermark.toString(), "");
            payByCardGuiSetting.SecurityCodeWatermark =  settings.getString(SubmitPaymentCardFormGuiSettingEnum.SecurityCodeWatermark.toString(), "");
            payByCardGuiSetting.SwitchButtonColor =  settings.getString(SubmitPaymentCardFormGuiSettingEnum.SwitchButtonColor.toString(), "");
            payByCardGuiSetting.PayByCardButtonColor =  settings.getString(SubmitPaymentCardFormGuiSettingEnum.PayByCardButtonColor.toString(), "");
        } catch (Exception e) {
            BNLog.e(getClass().getSimpleName(), "Internal error; failed to get SubmitPaymentCardFormGuiSetting", e);
        }
        return payByCardGuiSetting;
    }

    /********************************* Environment ****************************************************/

    /**
     * Returns the environment name from device storage
     * @return the environment name string, or empty string if it was empty or there was an error obtaining it.
     */
    public String getEnvironmentNameFromStorage() {
        try {
            return settings.getString(ENVIRONMENT_NAME, "");
        } catch (Exception e) {
            BNLog.e(getClass().getSimpleName(), "Internal error; failed to get environment name", e);
        }
        return "";
    }

    /**
     * Saves the name of the current environment to device storage.
     * @param envName the name of the environment
     * @return true if saved successfully, otherwise false.
     */
    public boolean saveEnvironmentNameToStorage(String envName) {
        Boolean ok = false;
        try {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(ENVIRONMENT_NAME, envName);
            ok = editor.commit();
        } catch (Exception e) {
            BNLog.e(getClass().getSimpleName(), "Internal error; failed to save the environment name", e);
        }
        return ok;
    }

    /********************************* Custom Data ****************************************************/

    /**
     * Returns the registration data from device storage
     * @return the registration data string, or empty string if it was empty or there was an error obtaining it.
     */
    public String getRegDataFromStorage() {
        try {
            return settings.getString(REGISTRATION_DATA_NAME, "");
        } catch (Exception e) {
            BNLog.e(getClass().getSimpleName(), "Internal error; failed to get custom registration data", e);
        }
        return "";
    }

    /**
     * Saves the custom registration data to device storage.
     * @param regData the custom registration data
     * @return true if saved successfully, otherwise false.
     */
    public boolean saveRegDataToStorage(String regData) {
        Boolean ok = false;
        try {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(REGISTRATION_DATA_NAME, regData);
            ok = editor.commit();
        } catch (Exception e) {
            BNLog.e(getClass().getSimpleName(), "Internal error; failed to save the custom registration data", e);
        }
        return ok;
    }

    /**
     * Returns the payment data from device storage
     * @return the payment data string, or empty string if it was empty or there was an error obtaining it.
     */
    public String getPayDataFromStorage() {
        try {
            return settings.getString(PAYMENT_DATA_NAME, "");
        } catch (Exception e) {
            BNLog.e(getClass().getSimpleName(), "Internal error; failed to get custom payment data", e);
        }
        return "";
    }

    /**
     * Saves the custom payment data to device storage.
     * @param payData the custom payment data
     * @return true if saved successfully, otherwise false.
     */
    public boolean savePayDataToStorage(String payData) {
        Boolean ok = false;
        try {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(PAYMENT_DATA_NAME, payData);
            ok = editor.commit();
        } catch (Exception e) {
            BNLog.e(getClass().getSimpleName(), "Internal error; failed to save the custom payment data", e);
        }
        return ok;
    }


}
