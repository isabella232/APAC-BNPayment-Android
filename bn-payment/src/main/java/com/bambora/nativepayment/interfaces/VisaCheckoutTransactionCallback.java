package com.bambora.nativepayment.interfaces;

import com.bambora.nativepayment.models.VisaCheckoutLaunchParams;
import com.bambora.nativepayment.models.VisaCheckoutResponse;
import com.bambora.nativepayment.network.RequestError;

/**
 * Created by maxingchen on 16/1/18.
 */

public interface VisaCheckoutTransactionCallback {

    void onVisaCheckoutTransactionSuccess(VisaCheckoutResponse visaCheckoutResponse);

    void onVisaCheckoutTransactionError(RequestError error);
}
