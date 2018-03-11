package com.bambora.nativepayment.models;

/**
 * SubmitPaymentCardFormGuiSetting
 * add flexibility to configure the standard forms' GUI elements
 */
public class SubmitPaymentCardFormGuiSetting {
    public String TitleText;
    public String PayByCardButtonText;
    public String CardHolderWatermark;
    public String CardNumberWatermark;
    public String ExpiryDateWatermark;
    public String SecurityCodeWatermark;

    //Color Hex Code ie. #00ff00
    public String SwitchButtonColor;
    public String PayByCardButtonColor;
    public String PayLoadingBarColor;
    public String CardIOColorText;
    public Boolean CardIOEnable;
}

