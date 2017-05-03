package com.bambora.nativepayment.interfaces;

import com.bambora.nativepayment.network.RequestError;

import java.util.Map;

/**
 * {@link ITransactionListener} is an interface defining the the callback method
 * for a transaction operation.
 *
 * Created by oskarhenriksson on 16/10/15.
 */
public interface ITransactionExtListener {
    /**
     * A callback method executed after a transaction operation is finished.
     */
    void onTransactionSuccess(Map<String,String> response);
    void onTransactionError(RequestError error);
}

