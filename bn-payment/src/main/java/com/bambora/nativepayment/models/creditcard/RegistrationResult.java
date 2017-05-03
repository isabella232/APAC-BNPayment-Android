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

import com.bambora.nativepayment.models.creditcard.RegistrationResultAction.ActionCode;
import com.bambora.nativepayment.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Lovisa Corp
 */
public class RegistrationResult {

    private static final String KEY_META = "meta";
    private static final String KEY_CARD_NUMBER = "truncatedcardnumber";
    private static final String KEY_CARD_HOLDER_NAME = "cardHolderName";
    private static final String KEY_EXPIRY_MONTH = "expmonth";
    private static final String KEY_EXPIRY_YEAR = "expyear";
    private static final String KEY_PAYMENT_TYPE = "paymenttype";
    private static final String KEY_TRANSACTION_ID = "transactionid";
    private static final String KEY_SUBSCRIPTION_ID = "subscriptionid";
    private static final String KEY_ORIGIN_IP = "originip";

    /**
     * {@link RegistrationResultMeta} object
     */
    public final RegistrationResultMeta meta;

    /**
     * Truncated credit card number
     */
    public final String truncatedCardNumber;


    /**
     * Optional Card holder name
     */
    public final String cardHolderName;


    /**
     * Card expiry month
     */
    public final Integer expiryMonth;

    /**
     * Card axpry year
     */
    public final Integer expiryYear;

    /**
     * Payment type, such as Visa or MasterCard
     */
    public final String paymentType;

    /**
     * ID of transaction created at the time of card registration
     */
    public final String transactionId;

    /**
     * Subsription identifier
     */
    public final String subscriptionId;

    /**
     * Origin IP address from where credit card registration was made
     */
    public final String originIp;

    public RegistrationResult(JSONObject jsonObject) throws JSONException {
        JSONObject metaJson = JsonUtils.getJSONObjectIfExists(jsonObject, KEY_META);
        meta = metaJson != null ? new RegistrationResultMeta(metaJson) : null;
        truncatedCardNumber = JsonUtils.getStringIfExists(jsonObject, KEY_CARD_NUMBER);
        cardHolderName = JsonUtils.getStringIfExists(jsonObject, KEY_CARD_HOLDER_NAME);
        expiryMonth = JsonUtils.getIntIfExists(jsonObject,KEY_EXPIRY_MONTH);
        expiryYear = JsonUtils.getIntIfExists(jsonObject, KEY_EXPIRY_YEAR);
        paymentType = JsonUtils.getStringIfExists(jsonObject, KEY_PAYMENT_TYPE);
        transactionId = JsonUtils.getStringIfExists(jsonObject, KEY_TRANSACTION_ID);
        subscriptionId = JsonUtils.getStringIfExists(jsonObject, KEY_SUBSCRIPTION_ID);
        originIp = JsonUtils.getStringIfExists(jsonObject, KEY_ORIGIN_IP);
    }

    public static RegistrationResult fromJson(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        return new RegistrationResult(jsonObject);
    }

    public ActionCode getActionCode() {
        if (meta != null && meta.action != null) {
            return meta.action.getActionCode();
        } else {
            return ActionCode.UNKNOWN;
        }
    }
}
