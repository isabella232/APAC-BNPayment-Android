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

package com.bambora.nativepayment.models;

import com.bambora.nativepayment.interfaces.IJsonResponse;
import com.bambora.nativepayment.json.JsonContainer;
import com.bambora.nativepayment.models.creditcard.TransactionErrorResponse;
import com.bambora.nativepayment.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * {@link TransactionResponse} is a model representing a transaction response in the system.
 */
public class TransactionResponse implements IJsonResponse<TransactionResponse> {

    private static final String KEY_PAYMENT_ID = "payment";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_STATUS = "status";
    private static final String KEY_CODE = "code";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_INFO = "info";
    private static final String KEY_RECEIPT = "receipt";

    public String paymentId;

    public Integer amount;

    public String currency;

    public String receipt;

    /**
     * Error response parameters
     */

    public Integer status;

    public Integer code;

    public String errorMessage;

    public String errorInfo;



    @Override
    public TransactionResponse fromJson(JsonContainer jsonContainer) throws JSONException {
        JSONObject jsonObject = jsonContainer.getJsonObject();
        paymentId = JsonUtils.getStringIfExists(jsonObject, KEY_PAYMENT_ID);
        amount = JsonUtils.getIntIfExists(jsonObject, KEY_AMOUNT);
        currency = JsonUtils.getStringIfExists(jsonObject, KEY_CURRENCY);
        status = JsonUtils.getIntIfExists(jsonObject, KEY_STATUS);
        code = JsonUtils.getIntIfExists(jsonObject, KEY_CODE);
        errorMessage = JsonUtils.getStringIfExists(jsonObject, KEY_MESSAGE);
        errorInfo = JsonUtils.getStringIfExists(jsonObject, KEY_INFO);

        receipt = JsonUtils.getStringIfExists(jsonObject, KEY_RECEIPT);
        return this;
    }

    public TransactionErrorResponse getError() {
        return new TransactionErrorResponse(
                this.status,
                this.code,
                this.errorMessage,
                this.errorInfo
        );
    }
}
