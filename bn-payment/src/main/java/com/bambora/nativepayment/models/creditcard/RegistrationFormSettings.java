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

package com.bambora.nativepayment.models.creditcard;

import com.bambora.nativepayment.interfaces.IJsonRequest;
import com.bambora.nativepayment.logging.BNLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Used for customizing the credit card registration form.
 * <p>This model is to be attached in the credit card registration request so fields and input
 * group keys shouldn't be changed unless the API changes.</p>
 *
 * @author Lovisa Corp
 */
public class RegistrationFormSettings implements IJsonRequest {

    private static final String INPUT_GROUP_CARD_NUMBER =  "cardnumber";
    private static final String INPUT_GROUP_EXPIRY_DATE =  "cardexpiry";
    private static final String INPUT_GROUP_CVV =          "cardverification";

    private static final String KEY_PLATFORM = "platform";
    private static final String KEY_CSS_URL = "cssurl";
    private static final String KEY_CARD_NUMBER = "cardNumber";
    private static final String KEY_CARD_EXPIRY = "cardExpiry";
    private static final String KEY_CARD_VERIFICATION = "cardVerification";
    private static final String KEY_SUBMIT_BUTTON = "submitbutton";

    private InputGroup cardNumberInputGroup = new InputGroup(INPUT_GROUP_CARD_NUMBER);
    private InputGroup cardExpiryDateInputGroup = new InputGroup(INPUT_GROUP_EXPIRY_DATE);
    private InputGroup cardCvvInputGroup = new InputGroup(INPUT_GROUP_CVV);

    /**
     * Describes which platform the form is intended for
     */
    private static final String PLATFORM = "android";

    /**
     * URL to CSS file for styling the credit card registration form
     * <p>Use {@link #setCssUrl(String)} to set.</p>
     */
    private String cssUrl;

    /**
     * Text for the submit button
     */
    private String submitButtonText;

    /**
     * Sets URL to CSS file for formatting the registration form
     *
     * @param cssUrl    URL to CSS file
     */
    public void setCssUrl(String cssUrl) {
        this.cssUrl = cssUrl;
    }

    /**
     * Sets hint for card number input field
     * @param text      Hint text
     */
    public void setCardNumberInputHint(String text) {
        cardNumberInputGroup.placeholder = text;
    }

    /**
     * Sets hint for card expiry input field
     * @param text      Hint text
     */
    public void setCardExpiryInputHint(String text) {
        cardExpiryDateInputGroup.placeholder = text;
    }

    /**
     * Sets hint for CVV input field
     * @param text      Hint text
     */
    public void setCvvInputHint(String text) {
        cardCvvInputGroup.placeholder = text;
    }

    /**
     * Sets the text that's visible on the submit button
     * @param text  Button text
     */
    public void setSubmitButtonText(String text) {
        this.submitButtonText = text;
    }

    @Override
    public String getSerialized() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_PLATFORM, PLATFORM);
            jsonObject.put(KEY_CSS_URL, cssUrl);
            jsonObject.put(KEY_CARD_NUMBER, new JSONObject(cardNumberInputGroup.getSerialized()));
            jsonObject.put(KEY_CARD_EXPIRY, new JSONObject(cardExpiryDateInputGroup.getSerialized()));
            jsonObject.put(KEY_CARD_VERIFICATION, new JSONObject(cardCvvInputGroup.getSerialized()));
            jsonObject.put(KEY_SUBMIT_BUTTON, submitButtonText);
        } catch (JSONException e) {
            BNLog.jsonParseError(getClass().getSimpleName(), e);
        }
        return jsonObject.toString().replace("\\", "");
    }

    /**
     * Model for input group data in the credit card registration form
     */
    public class InputGroup implements IJsonRequest {

        private static final String KEY_NAME = "name";
        private static final String KEY_PLACEHOLDER = "placeholder";

        /**
         * Name of the form section class
         */
        private String name;

        /**
         * Input field placeholder text
         */
        private String placeholder;

        public InputGroup(String name) {
            this(name, "");
        }

        public InputGroup(String name, String placeholder) {
            this.name = name;
            this.placeholder = placeholder;
        }

        @Override
        public String getSerialized() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(KEY_NAME, name);
                jsonObject.put(KEY_PLACEHOLDER, placeholder);
            } catch (JSONException e) {
                BNLog.jsonParseError(getClass().getSimpleName(), e);
            }
            return jsonObject.toString();
        }
    }
}
