package com.bambora.nativepayment.interfaces;

import com.bambora.nativepayment.models.VisaCheckoutLaunchParams;
import com.bambora.nativepayment.network.RequestError;

import java.util.Map;

/**
 * Created by maxingchen on 15/1/18.
 */

public interface VisaCheckoutDataCallback {

    void onVisaCheckoutDataSuccess(VisaCheckoutLaunchParams visaCheckoutLaunchParams);

    void onVisaCheckoutDataError(RequestError error);


}
