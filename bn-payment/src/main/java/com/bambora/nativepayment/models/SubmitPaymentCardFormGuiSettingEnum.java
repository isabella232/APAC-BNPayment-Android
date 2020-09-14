package com.bambora.nativepayment.models;

/**
 * enum SubmitPaymentCardFormGuiSettingEnum
 */
public enum SubmitPaymentCardFormGuiSettingEnum {
    TitleText ("PayByCardFormTitleText"),
    PayByCardButtonText ("PayByCardFormRegisterButtonText"),
    CardHolderWatermark ("PayByCardFormCardHolderWatermark"),
    CardNumberWatermark ("PayByCardFormCardNumberWatermark"),
    ExpiryDateWatermark ("PayByCardFormExpiryDateWatermark"),
    SecurityCodeWatermark ("PayByCardFormSecurityCodeWatermark"),
    SwitchButtonColor("PayByCardSwitchButtonColor"),
    PayByCardButtonColor ("PayByCardFormRegisterButtonColor"),
    PayLoadingBarColor ("PayLoadingBarColor"),
    CardIOColorText ("PayCardIOColorText"),
    CardIOEnable ("PayCardIOColorEnable");
    private final String name;

    SubmitPaymentCardFormGuiSettingEnum(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}


