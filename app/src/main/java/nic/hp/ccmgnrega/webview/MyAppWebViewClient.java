package nic.hp.ccmgnrega.webview;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.common.Constant;

public class MyAppWebViewClient extends WebViewClient {

    Context context;
    private ProgressDialog prDialog;


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        context = view.getContext();
        prDialog = new ProgressDialog(context);
        prDialog.setMessage(context.getString(R.string.please_wait));
        prDialog.show();

    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (prDialog != null) {
            prDialog.dismiss();
        }
    }


}
