package com.bambora.nativepayment.services;

import android.content.Context;
import android.util.Log;

import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.interfaces.ITransactionExtListener;
import com.bambora.nativepayment.interfaces.VisaCheckoutDataCallback;
import com.bambora.nativepayment.interfaces.VisaCheckoutTransactionCallback;
import com.bambora.nativepayment.managers.CreditCardManager;
import com.bambora.nativepayment.models.PaymentSettings;
import com.bambora.nativepayment.models.PaymentType;
import com.bambora.nativepayment.models.TransactionResponse;
import com.bambora.nativepayment.models.VisaCheckoutLaunchParams;
import com.bambora.nativepayment.models.VisaCheckoutResponse;
import com.bambora.nativepayment.models.VisaCheckoutTransactionParams;
import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.models.creditcard.RegistrationResponse;
import com.bambora.nativepayment.network.ApiService;
import com.bambora.nativepayment.network.Callback;
import com.bambora.nativepayment.network.Request;
import com.bambora.nativepayment.network.RequestError;
import com.bambora.nativepayment.network.Response;

import java.util.HashMap;
import java.util.Map;

import static com.bambora.nativepayment.network.RequestMethod.GET;
import static com.bambora.nativepayment.network.RequestMethod.POST;

public class VisaCheckoutApiService extends ApiService {

    public Request<VisaCheckoutLaunchParams> getVisaCheckoutData() {
        return new Request<>(this, VisaCheckoutLaunchParams.class)
                .endpoint("visacheckout_params")
                .method(GET);
    }

    public Request<VisaCheckoutResponse> processTransaction(VisaCheckoutTransactionParams visaCheckoutTransactionParams) {
        return new Request<>(this, VisaCheckoutResponse.class)
                .endpoint("visacheckout_transaction")
                .method(POST)
                .body(visaCheckoutTransactionParams);
    }

    private static VisaCheckoutApiService createService() {
        return BNPaymentHandler.getInstance().createService(VisaCheckoutApiService.class);
    }

    public static class VisaCheckoutService {

        public static void getVisaCheckoutData(final Context context,final VisaCheckoutDataCallback callBack) {
            Request<VisaCheckoutLaunchParams> request = createService().getVisaCheckoutData();
            request.execute(new Callback<VisaCheckoutLaunchParams>() {
                @Override
                public void onSuccess(Response<VisaCheckoutLaunchParams> response) {
                    if (callBack != null) {
                        VisaCheckoutLaunchParams visaCheckoutLaunchParams = response.getBody();
                        callBack.onVisaCheckoutDataSuccess(visaCheckoutLaunchParams);
                    }
                }
                @Override
                public void onError(RequestError error) {
                    if (callBack != null) {
                        callBack.onVisaCheckoutDataError(error);
                    }
                }
            });

        }


        public static void processTransaction(final Context context, final VisaCheckoutTransactionParams visaCheckoutTransactionParams, final VisaCheckoutTransactionCallback callBack) {
            Request<VisaCheckoutResponse> request = createService().processTransaction(visaCheckoutTransactionParams);
            request.execute(new Callback<VisaCheckoutResponse>() {
                @Override
                public void onSuccess(Response<VisaCheckoutResponse> response) {
                    if (callBack != null) {
                        VisaCheckoutResponse visaCheckoutResponse = response.getBody();
                        callBack.onVisaCheckoutTransactionSuccess(visaCheckoutResponse);
                    }
                }

                @Override
                public void onError(RequestError error) {
                    if (callBack != null) {
                        callBack.onVisaCheckoutTransactionError(error);
                    }
                }
            });
        }
    }
}
