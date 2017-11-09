package com.bambora.nativepayment.models;
import java.util.HashMap;

public class PaymentType{
    /**
     * PaymentType constants
     */
    public static enum PaymentTypeEnum {
        //payment
        PaymentToken,
        PaymentCard,

        //PreAuth
        PreAuthToken,
        PreAuthCard
    }


    /**
     * paymentTypeMapping used to map the payment type to backend transation type id.
     */
    private static HashMap<PaymentTypeEnum, String> paymentTypeMapping;
    public static HashMap<PaymentTypeEnum, String> PaymentTypeHash() {
        if(paymentTypeMapping==null)
        {
            paymentTypeMapping = new HashMap<>();
            //payment.
            paymentTypeMapping.put(PaymentTypeEnum.PaymentCard, "1");
            paymentTypeMapping.put(PaymentTypeEnum.PaymentToken, "1");
            //PreAuth
            paymentTypeMapping.put(PaymentTypeEnum.PreAuthCard, "2");
            paymentTypeMapping.put(PaymentTypeEnum.PreAuthToken, "2");

        }
        return paymentTypeMapping;
    }
}





