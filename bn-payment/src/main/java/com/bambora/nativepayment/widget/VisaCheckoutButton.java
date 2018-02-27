package com.bambora.nativepayment.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.bambora.nativepayment.R;
import com.bambora.nativepayment.models.VisaCheckoutLaunchParams;
import com.visa.checkout.hybrid.VisaCheckoutPlugin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class VisaCheckoutButton extends WebView {

    ProgressBar progressBar;
    Context layOutContext;
    VisaCheckOutResult visaCheckOutResult;

    public interface VisaCheckOutResult {
        void visaCheckoutSuccess(String info);
        void visaCheckoutFail(String info);
        void visaCheckoutSetupComplete();
    }

    public VisaCheckoutButton(Context context) {
        super(context);
        setupView(context);
    }

    public VisaCheckoutButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public VisaCheckoutButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VisaCheckoutButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupView(context);
    }

    private void setupView(Context context) {
        layOutContext=context;
    }

    public void loadUI(VisaCheckoutLaunchParams visaCheckoutLaunchParams, RelativeLayout visaCheckoutLayout,int loadingBarColor) {

        if(Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        }
        this.setVisibility(View.INVISIBLE);
        progressBar=new ProgressBar(layOutContext,null,android.R.attr.progressBarStyleLarge);
        progressBar.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        progressBar.getIndeterminateDrawable().setColorFilter(loadingBarColor, PorterDuff.Mode.MULTIPLY);
        visaCheckoutLayout.addView(progressBar,params);
        this.getSettings().setJavaScriptEnabled(true);
        this.addJavascriptInterface(new VisaCheckoutCallBack(), "visaCheckoutCallBack");
        VisaCheckoutPlugin.configure(this);
        this.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                visaCheckOutResult.visaCheckoutSetupComplete();
                view.setVisibility(View.VISIBLE);
            }
        });

        try {
            AssetManager am =  getResources().getAssets();
            InputStream is = am.open("VisaCheckOutConnector.html");
            int size = is.available();
            byte buffer[] = new byte[size];
            is.read(buffer);
            String content=new String(buffer);
            is.close();
            content=String.format(content, visaCheckoutLaunchParams.apikey, visaCheckoutLaunchParams.externalClientId, visaCheckoutLaunchParams.externalProfileId, visaCheckoutLaunchParams.currencyCode, visaCheckoutLaunchParams.subtotal, visaCheckoutLaunchParams.total, visaCheckoutLaunchParams.locale, visaCheckoutLaunchParams.collectShipping, visaCheckoutLaunchParams.message, visaCheckoutLaunchParams.buttonAction, visaCheckoutLaunchParams.buttonImageUrl, visaCheckoutLaunchParams.jsLibraryUrl);
            this.loadDataWithBaseURL("blarg://ignored", content, "text/html", "utf-8", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public class VisaCheckoutCallBack extends Object {

        @JavascriptInterface
        public void Success(String response) {
            visaCheckOutResult.visaCheckoutSuccess(response);
        }

        @JavascriptInterface
        public void Cancel(String response) {
        }

        @JavascriptInterface
        public void Error(String response) {
            visaCheckOutResult.visaCheckoutFail(response);
        }
    }



}
