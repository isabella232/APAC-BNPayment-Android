package com.bambora.nativepayment.models;

/**
 * enum CardRegistrationFormGuiSettingEnum
 */
public enum CardRegistrationFormGuiSettingEnum {
    TitleText ("RegistrationFormTitleText"),
    RegisterButtonText ("RegistrationFormRegisterButtonText"),
    CardHolderWatermark ("RegistrationFormCardHolderWatermark"),
    CardNumberWatermark ("RegistrationFormCardNumberWatermark"),
    ExpiryDateWatermark ("RegistrationFormExpiryDateWatermark"),
    SecurityCodeWatermark ("RegistrationFormSecurityCodeWatermark"),
    RegisterButtonColor ("RegistrationFormRegisterButtonColor");

    private final String name;

    CardRegistrationFormGuiSettingEnum(String s) {
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