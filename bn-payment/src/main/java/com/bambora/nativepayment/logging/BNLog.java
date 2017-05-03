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

package com.bambora.nativepayment.logging;

import android.util.Log;

import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.network.Request;
import com.bambora.nativepayment.network.Response;

/**
 * This is a helper class to handle logging for the Bambora Native SDK. It wraps some logging
 * through {@link Log} but only logs in debug mode.
 */
public class BNLog {

    private static final String DIVIDER = "--------------------------------------\n";

    public static void d(String tag, String msg) {
        if (debug()) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (debug()) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (debug()) {
            Log.e(tag, msg, throwable);
        }
    }

    public static void w(String tag, String msg, Throwable throwable) {
        if (debug()) {
            Log.w(tag, msg, throwable);
        }
    }

    public static boolean debug() {
        return BNPaymentHandler.getInstance().getDebug();
    }

    public static void requestResult(String logTag, Request request, Response response) {
        boolean success = response != null && response.error == null;
        String title = success ?
                "BN request completed successfully\n" :
                "BN request failed\n";
        StringBuilder logMessage = new StringBuilder();
        logMessage
                .append(DIVIDER)
                .append(title)
                .append(DIVIDER);
        if (response != null) {
            logMessage.append(response.toString());
        }
        if (request != null) {
            logMessage
                    .append(DIVIDER)
                    .append("Original request\n")
                    .append(DIVIDER)
                    .append(request.toString());
        }
        logMessage.append(DIVIDER);
        if (success) {
            d(logTag, logMessage.toString());
        } else {
            e(logTag, logMessage.toString());
        }
    }

    public static void jsonParseError(String logTag, Exception exception) {
        e(logTag, "Failed to parse JSON string.", exception);
    }
}
