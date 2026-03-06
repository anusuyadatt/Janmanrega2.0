package nic.hp.ccmgnrega;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.goodiebag.pinview.Pinview;

import nic.hp.ccmgnrega.common.Api;
import nic.hp.ccmgnrega.common.MyAlert;
import nic.hp.ccmgnrega.common.MySharedPref;
import nic.hp.ccmgnrega.fragment.HomeFragment;
import nic.hp.ccmgnrega.model.ConnectionDetector;
import nic.hp.ccmgnrega.model.GetAssetMap;
import nic.hp.ccmgnrega.webview.MyAppWebViewClient;

public class ViewSearchedAssetsActivity extends AppCompatActivity {

    protected String menu="",cLong = "",cLat = "", panchayat = "",panchayatName = "",block = "",workType = "",workCode = "",asset = "",category = "",finYear = "";
    GetAssetMap map;
    WebView webView;
    ImageView ivActionFeedBack;
    protected ConnectionDetector connectionDetector;
    TextView headingTv;
    String strActivityCode="07-";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_searched_assets);
        loadActionBar();
        connectionDetector = new ConnectionDetector(ViewSearchedAssetsActivity.this);
        headingTv =findViewById(R.id.heading);
        webView = findViewById(R.id.webview);
        ivActionFeedBack = findViewById(R.id.ivActionFeedBack);

        Bundle args = getIntent().getExtras();;
        if (args != null) {
            if (args.getString("menu") != null)
                menu = args.getString("menu");
            if (args.getString("cLong") != null)
                cLong = args.getString("cLong");
            if (args.getString("cLat") != null)
                cLat = args.getString("cLat");
            if (args.getString("panchayat") != null)
                panchayat = args.getString("panchayat");
            if (args.getString("panchayatName") != null)
                panchayatName = args.getString("panchayatName");
            if (args.getString("block") != null)
                block = args.getString("block");
            if (args.getString("workCode") != null)
                workCode = args.getString("workCode");
            if (args.getString("workType") != null)
                workType = args.getString("workType");
            if (args.getString("asset") != null)
                asset = args.getString("asset");
            if (args.getString("category") != null)
                category = args.getString("category");
            if (args.getString("finYear") != null)
                finYear = args.getString("finYear");

        }

        headingTv.setText(" " + getApplicationContext().getString(R.string.search_assets));
        webView.setWebViewClient(new MyAppWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setAllowFileAccess(false);
        webView.setClickable(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

        map = new GetAssetMap(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            String url = Api.ASSET_NEAR_BY_URL + panchayat + "&work=" + workCode + "&asset=" + asset + "&wc=" + category + "&ws=" + workType + "&fy=" + finYear;
            webView.loadUrl(url, null);
            //https://bhuvan-app2.nrsc.gov.in/janmnrega/nrega_v1.php?pc=3165013006&work=3165013006/AV/958486255823021556&asset=31015413145&wc=17&ws=17045&fy=2021-2022
        }
        else {
            webView.loadData(map.internentNotEnable(), "text/html; charset=utf-8", "utf-8");
        }

        ivActionFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionDetector.isConnectingToInternet())
                {
                    if(MySharedPref.getLoginPin(getApplicationContext()).isEmpty()){
                        final Dialog dialogAlert2 = new Dialog(ViewSearchedAssetsActivity.this);
                        MyAlert.dialogForOk
                                (getApplicationContext(), R.mipmap.icon_warning, getApplicationContext().getString(R.string.warning),
                                        getApplicationContext().getString(R.string.please_register_first),
                                        dialogAlert2,
                                        getApplicationContext().getString(R.string.ok),
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogAlert2.dismiss();
                                                final Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                                                mainIntent.putExtra("displayPosition", "6");
                                                startActivity(mainIntent);
                                                finish();
                                            }
                                        }, strActivityCode + "02");

                    }
                    else
                        verifyLoginPin(getApplicationContext());
                }
                else
                    MyAlert.showAlert(getApplicationContext(), R.mipmap.icon_warning, getApplicationContext().getString(R.string.warning), getApplicationContext().getString(R.string.no_internet),strActivityCode+"01");


            }
        });
    }

    public void loadActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_white_back);
        @SuppressWarnings("deprecation")
        Drawable d = getResources().getDrawable(R.drawable.header_bg2);
        getSupportActionBar().setBackgroundDrawable(d);
        getSupportActionBar().setTitle(Html.fromHtml("<center><font color='#FFFFFF'><b>&nbsp;" + getApplicationContext().getString(R.string.app_name) + "</b></font></center>"));
        getSupportActionBar().setIcon(R.mipmap.ic_india_emblem);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            onBackPressed();
        return true;
    }
    private void verifyLoginPin(Context context) {
        final Dialog dialog = new Dialog(ViewSearchedAssetsActivity.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.login_pin_dialog);
        Button submitButton = (Button) dialog.findViewById(R.id.buttonSubmit);
        Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
        Pinview pinview = dialog.findViewById(R.id.pinview);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputLoginPin = pinview.getValue();
                if (inputLoginPin.isEmpty()) {
                    MyAlert.showAlert(ViewSearchedAssetsActivity.this, R.mipmap.icon_warning, context.getString(R.string.warning), context.getString(R.string.please_enter_pin), strActivityCode + "07");
                    pinview.requestFocus();
                    pinview.clearValue();
                }  else if(!inputLoginPin.isEmpty()&&inputLoginPin.length()<4) {
                    pinview.requestFocus();
                    pinview.clearValue();
                    MyAlert.showAlert(ViewSearchedAssetsActivity.this, R.mipmap.icon_warning, context.getString(R.string.warning), context.getString(R.string.please_enter_valid_pin), strActivityCode + "08");
                }   else if(!inputLoginPin.isEmpty() && !inputLoginPin.equalsIgnoreCase(MySharedPref.getLoginPin(context))) {
                    pinview.requestFocus();
                    pinview.clearValue();
                    MyAlert.showAlert(ViewSearchedAssetsActivity.this, R.mipmap.icon_warning, context.getString(R.string.warning), context.getString(R.string.input_pin_wrong), strActivityCode + "09");
                }  else {
                    dialog.dismiss();
                    Intent intent = new Intent(ViewSearchedAssetsActivity.this, SendFeedBackActivity.class);
                    intent.putExtra("cLong", cLong);
                    intent.putExtra("cLat", cLat);
                    startActivity(intent);

                }

            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });
        pinview.requestPinEntryFocus();
        dialog.show();



    }

}