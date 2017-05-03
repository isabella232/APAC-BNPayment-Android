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

package com.bambora.nativepayment.webview;

import android.webkit.JavascriptInterface;

import com.bambora.nativepayment.logging.BNLog;
import com.bambora.nativepayment.models.creditcard.RegistrationResult;

import org.json.JSONException;

/**
 * Class for JavaScript callback interface handling credit card registration status.
 * @author Lovisa Corp
 */
public class CreditCardRegistrationJsInterface {

    private IJavascriptCallbackListener resultListener;

    public interface IJavascriptCallbackListener {
        void onJavascriptCall(RegistrationResult result);
    }

    public CreditCardRegistrationJsInterface(IJavascriptCallbackListener resultListener) {
        this.resultListener = resultListener;
    }

    /**
     * Gets called by credit card registration page when registration state changes.
     * @param stateChangeJson Json String describing a {@link RegistrationResult}
     */
    @JavascriptInterface
    public void paymentFormStatusChanged(String stateChangeJson) {
        try {
            RegistrationResult result = RegistrationResult.fromJson(stateChangeJson);
            resultListener.onJavascriptCall(result);
        } catch (JSONException e) {
            BNLog.e(getClass().getSimpleName(), "Failed to parse Javascript JSON callback.", e);
            resultListener.onJavascriptCall(null);
        }
    }
}
