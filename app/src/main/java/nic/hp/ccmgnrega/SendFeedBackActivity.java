package nic.hp.ccmgnrega;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import nic.hp.ccmgnrega.adapter.CustomListingAdapter;
import nic.hp.ccmgnrega.common.Api;
import nic.hp.ccmgnrega.common.Constant;
import nic.hp.ccmgnrega.common.GPSTracker;
import nic.hp.ccmgnrega.common.GlobalLocationService;
import nic.hp.ccmgnrega.common.Helper;
import nic.hp.ccmgnrega.common.MyAlert;
import nic.hp.ccmgnrega.common.MySharedPref;
import nic.hp.ccmgnrega.common.RedAsterisk;
import nic.hp.ccmgnrega.common.TokenInterceptor;
import nic.hp.ccmgnrega.fragment.BaseFragment;
import nic.hp.ccmgnrega.model.ConnectionDetector;
import nic.hp.ccmgnrega.model.ResponseData;
import nic.hp.ccmgnrega.model.TokenData;
import nic.hp.ccmgnrega.model.UploadAssetFeedbackModel;
import nic.hp.ccmgnrega.model.UploadWorkerQueryModel;
import nic.hp.ccmgnrega.views.spinner.SearchableSpinner;
import nic.hp.ccmgnrega.webview.MyAppWebViewClient;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SendFeedBackActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    protected String strLongitude = "", strLatitude = "", assetName = "", assetID = "", workType = "", assetExist = "";
    protected double log = 0.0;
    protected int sizeReduce = 512;
    TextView heading;
    RatingBar ratingBar;
    ImageView uploadImage;
    LinearLayout assetlayoutRating1, assetlayoutRating2, assetlayoutRating3, assetlayoutRating4;
    Integer rating = 0, rating1 = 0, rating2 = 0, rating3 = 0, rating4 = 0;
    boolean isBrowse = false;
    boolean hasRated1 = false;
    boolean hasRated2 = false;
    boolean hasRated3 = false;
    boolean hasRated4 = false;
    Bitmap bitmapImage = null;
    String ISCIBExist = "C";
    String IsCompleted = "C";
    String DiscriptionMatch = "C";
    String IsUseFull = "C";

    protected ConnectionDetector connectionDetector;
    protected String mobile = "";
    protected String status;
    protected String statusMessage;
    int statusCode = 0;
    BaseFragment baseFragment = null;

    SearchableSpinner spinnerAssets;
    WebView mWebView;
    View dialogLayout;
    AlertDialog alertDialog;
    GPSTracker gpsTracker;

    List<String> listSRNO = new ArrayList<>();
    List<String> listWorkCode = new ArrayList<>();
    List<String> listWorkName = new ArrayList<>();
    List<String> listAssetID = new ArrayList<>();
    List<String> listAssetName = new ArrayList<>();
    List<String> listCustomText = new ArrayList<>();
    String validateMessage = "";
    TextView infoTV;
    TextView assetExistOrNotTV, assetFeedBack1, assetFeedBack2, assetFeedBack3, assetFeedBack4;
    RadioGroup radioAsset;
    RadioButton radioAssetExist;
    RadioButton radioAssetNotExist;
    RadioButton radioAssetCantSay;
    TextView viewAssetTV;
    TextView assetTV;
    LinearLayout llAsset;
    LinearLayout linearFeedbackSection;
    Button btnGetOtp,  btnSendFeedBack;
    TextInputEditText etMobile, etOtp, etName;
    TextInputLayout etNameLayout,etMobileLayout,etOtpLayout;
    Handler handler;
    Runnable runnable;
    Context context;
    private Double dLatitude = 0.0, dLongitude = 0.0, dAccuracy = 0.0;
    private Timer mTimer1;
    private TimerTask mTt1;
    private Handler mTimerHandler = new Handler();
    final int PERMISSION_REQUEST_CODE = 100;
    String strActivityCode="04-";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feed_back);
        context = getApplicationContext();
        loadActionBar();
        baseFragment = new BaseFragment();
        connectionDetector = new ConnectionDetector(SendFeedBackActivity.this);
        gpsTracker = new GPSTracker(SendFeedBackActivity.this);
        dialogLayout = View.inflate(SendFeedBackActivity.this, R.layout.webview_dialog, null);
        /*     Give    Asset Feedback Start */
        mWebView = dialogLayout.findViewById(R.id.webview);
        mWebView.setWebViewClient(new MyAppWebViewClient());
        mWebView.setClickable(true);
        mWebView.getSettings().setAllowFileAccess(false);
        heading = findViewById(R.id.heading);
        viewAssetTV = findViewById(R.id.viewAssetTV);
        infoTV = findViewById(R.id.infoTV);
        assetTV = findViewById(R.id.assetTV);
        spinnerAssets = findViewById(R.id.spinnerAssets);
        assetExistOrNotTV = findViewById(R.id.assetExistOrNotTV);
        radioAsset = findViewById(R.id.radioAsset);
        radioAssetExist = findViewById(R.id.radioAssetExist);
        radioAssetNotExist = findViewById(R.id.radioAssetNotExist);
        radioAssetCantSay = findViewById(R.id.radioAssetCantSay);
        llAsset = findViewById(R.id.llAsset);
        /*     Give    Asset Feedback End */
        Button browseBtn = findViewById(R.id.browseBtn);
        uploadImage = findViewById(R.id.uploadImage);
        ratingBar = findViewById(R.id.ratingBar);
        assetlayoutRating1 = findViewById(R.id.assetlayoutRating1);
        assetFeedBack1 = findViewById(R.id.assetFeedBack1);
        RadioGroup radioAssetFeedBack1 = findViewById(R.id.radioAssetFeedBack1);
        RadioButton radioAssetFeedBack1Exist = findViewById(R.id.radioAssetFeedBack1Exist);
        RadioButton radioAssetFeedBack1NotExist = findViewById(R.id.radioAssetFeedBack1NotExist);
        RadioButton radioAssetFeedBack1CantSay = findViewById(R.id.radioAssetFeedBack1CantSay);
        assetlayoutRating2 = findViewById(R.id.assetlayoutRating2);
        assetFeedBack2 = findViewById(R.id.assetFeedBack2);
        RadioGroup radioAssetFeedBack2 = findViewById(R.id.radioAssetFeedBack2);
        RadioButton radioAssetFeedBack2Exist = findViewById(R.id.radioAssetFeedBack2Exist);
        RadioButton radioAssetFeedBack2NotExist = findViewById(R.id.radioAssetFeedBack2NotExist);
        assetlayoutRating3 = findViewById(R.id.assetlayoutRating3);
        assetFeedBack3 = findViewById(R.id.assetFeedBack3);
        RadioGroup radioAssetFeedBack3 = findViewById(R.id.radioAssetFeedBack3);
        RadioButton radioAssetFeedBack3Exist = findViewById(R.id.radioAssetFeedBack3Exist);
        RadioButton radioAssetFeedBack3NotExist = findViewById(R.id.radioAssetFeedBack3NotExist);
        RadioButton radioAssetFeedBack3CantSay = findViewById(R.id.radioAssetFeedBack3CantSay);
        assetlayoutRating4 = findViewById(R.id.assetlayoutRating4);
        assetFeedBack4 = findViewById(R.id.assetFeedBack4);
        RadioGroup radioAssetFeedBack4 = findViewById(R.id.radioAssetFeedBack4);
        RadioButton radioAssetFeedBack4Exist = findViewById(R.id.radioAssetFeedBack4Exist);
        RadioButton radioAssetFeedBack4NotExist = findViewById(R.id.radioAssetFeedBack4NotExist);
        RadioButton radioAssetFeedBack4CantSay = findViewById(R.id.radioAssetFeedBack4CantSay);
        RadioButton radioAssetFeedBack2CantSay = findViewById(R.id.radioAssetFeedBack2CantSay);
        linearFeedbackSection = findViewById(R.id.linearFeedbackSection);
        etName = findViewById(R.id.etName);
        etMobile = findViewById(R.id.etMobile);
        etOtp = findViewById(R.id.etOtp);
        etNameLayout = findViewById(R.id.etNameLayout);
        etMobileLayout = findViewById(R.id.etMobileLayout);
        etOtpLayout = findViewById(R.id.etOtpLayout);

        btnGetOtp = findViewById(R.id.btnGetOtp);
        //   btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        btnSendFeedBack = findViewById(R.id.btnSendFeedBack);
        /*     Give    Asset Feedback Start*/
        heading.setText(" " + getApplicationContext().getString(R.string.asset_feedback));
        viewAssetTV.setText(getApplicationContext().getString(R.string.view_asset) + "   ");

        radioAssetExist.setText(context.getString(R.string.yes));
        radioAssetNotExist.setText(context.getString(R.string.no));
        radioAssetCantSay.setText(context.getString(R.string.cant_say));
        spinnerAssets.setTitle(context.getString(R.string.search_assets));
        setMandatoryMark();
         loadGPS();


        viewAssetTV.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onClick(View view) {

                    if (spinnerAssets.getSelectedItemPosition() == 0)
                        MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_select_asset), strActivityCode+"013");
                    else {
                        if (connectionDetector.isConnectingToInternet()) {
                        if (dialogLayout.getParent() != null) {
                            ((ViewGroup) dialogLayout.getParent()).removeView(dialogLayout);
                        }
                        mWebView.getSettings().setJavaScriptEnabled(true);
                        mWebView.getSettings().setAllowFileAccess(false);
                        String url = Api.ASSET_DETAIL_URL + listSRNO.get(spinnerAssets.getSelectedItemPosition());
                        mWebView.loadUrl(url, null);
                       //https://bhuvan-app2.nrsc.gov.in/mgnrega/usrtasks/nrega_phase2/get/getDetailsnic.php?sno=115829505
                        alertDialog = new AlertDialog.Builder(SendFeedBackActivity.this).setTitle("").setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).create();
                        alertDialog.setTitle(listAssetName.get(spinnerAssets.getSelectedItemPosition()));

                        mWebView.setWebChromeClient(new WebChromeClient() {
                            @Override
                            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                                return super.onJsAlert(view, url, message, result);
                            }
                            public void onProgressChanged(WebView view, int progress) {
                                if (progress == 100) {
                                    alertDialog.setView(dialogLayout);
                                    alertDialog.show();
                                }
                            }
                        });
                    } else
                    MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.warning), context.getString(R.string.no_internet), strActivityCode+"02");

                    }

            }
        });
        radioAsset.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioAssetExist) {
                    if (spinnerAssets.getSelectedItemPosition() == 0) {
                        MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_select_asset), strActivityCode+"03");
                        radioAsset.clearCheck();
                    } else {
                        assetName = listAssetName.get(spinnerAssets.getSelectedItemPosition());
                         assetID = listAssetID.get(spinnerAssets.getSelectedItemPosition());
                        workType = listWorkCode.get(spinnerAssets.getSelectedItemPosition());
                        assetExist = "Y";
                        rating = 1;rating1 = 0;rating2 = 0;rating3 = 0;rating4 = 0;
                        ratingBar.setRating(rating + rating1 + rating2 + rating3 + rating4);
                        ratingBar.setVisibility(View.VISIBLE);
                        assetlayoutRating1.setVisibility(View.VISIBLE);
                        assetlayoutRating2.setVisibility(View.VISIBLE);
                        assetlayoutRating3.setVisibility(View.VISIBLE);
                        assetlayoutRating4.setVisibility(View.VISIBLE);
                        linearFeedbackSection.setVisibility(View.VISIBLE);
                        browseBtn.setVisibility(View.GONE);
                        uploadImage.setVisibility(View.GONE);
                        btnSendFeedBack.setVisibility(View.VISIBLE);

                    }
                } else if (checkedId == R.id.radioAssetNotExist) {

                    if (spinnerAssets.getSelectedItemPosition() == 0) {
                        MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_select_asset), strActivityCode+"014");
                        radioAsset.clearCheck();
                    } else {
                        assetName = listAssetName.get(spinnerAssets.getSelectedItemPosition());
                        assetID = listAssetID.get(spinnerAssets.getSelectedItemPosition());
                        workType = listWorkCode.get(spinnerAssets.getSelectedItemPosition());
                        assetExist = "N";
                        rating = 0;rating1 = 0;rating2 = 0;rating3 = 0;rating4 = 0;
                        ratingBar.setRating(rating + rating1 + rating2 + rating3 + rating4);
                        ratingBar.setVisibility(View.GONE);
                        assetlayoutRating1.setVisibility(View.GONE);
                        assetlayoutRating2.setVisibility(View.GONE);
                        assetlayoutRating3.setVisibility(View.GONE);
                        assetlayoutRating4.setVisibility(View.GONE);
                        radioAssetFeedBack1.clearCheck();
                        radioAssetFeedBack2.clearCheck();
                        radioAssetFeedBack3.clearCheck();
                        radioAssetFeedBack4.clearCheck();
                        ISCIBExist = "C";
                        IsCompleted = "C";
                        DiscriptionMatch = "C";
                        IsUseFull = "C";
                        uploadImage.setVisibility(View.VISIBLE);
                        browseBtn.setVisibility(View.VISIBLE);
                        linearFeedbackSection.setVisibility(View.VISIBLE);
                        btnSendFeedBack.setVisibility(View.VISIBLE);
                    }
                } else if (checkedId == R.id.radioAssetCantSay) {
                    if (spinnerAssets.getSelectedItemPosition() == 0) {
                        MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_select_asset), strActivityCode+"015");
                        radioAsset.clearCheck();
                    } else {
                        assetName = listAssetName.get(spinnerAssets.getSelectedItemPosition());
                        assetID = listAssetID.get(spinnerAssets.getSelectedItemPosition());
                        workType = listWorkCode.get(spinnerAssets.getSelectedItemPosition());
                        assetExist = "";
                        rating = 0;rating1 = 0;rating2 = 0;rating3 = 0;rating4 = 0;
                        ratingBar.setRating(rating + rating1 + rating2 + rating3 + rating4);
                        ratingBar.setVisibility(View.GONE);
                        assetlayoutRating1.setVisibility(View.GONE);
                        assetlayoutRating2.setVisibility(View.GONE);
                        assetlayoutRating3.setVisibility(View.GONE);
                        assetlayoutRating4.setVisibility(View.GONE);
                        radioAssetFeedBack1.clearCheck();
                        radioAssetFeedBack2.clearCheck();
                        radioAssetFeedBack3.clearCheck();
                        radioAssetFeedBack4.clearCheck();
                        ISCIBExist = "C";
                        IsCompleted = "C";
                        DiscriptionMatch = "C";
                        IsUseFull = "C";
                        uploadImage.setVisibility(View.VISIBLE);
                        browseBtn.setVisibility(View.VISIBLE);
                        linearFeedbackSection.setVisibility(View.VISIBLE);
                        btnSendFeedBack.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        /*     Give    Asset Feedback End*/
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                etMobile.setEnabled(true);
                etOtp.setEnabled(true);
                btnGetOtp.setText(context.getString(R.string.resend));
                btnGetOtp.setClickable(true);
                btnGetOtp.setBackgroundColor(context.getResources().getColor(R.color.green_color, getApplicationContext().getTheme()));

            }
        };

        browseBtn.setText(context.getString(R.string.browse_asset_image));
        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _goToCamera();
            }
        });


        radioAssetFeedBack1Exist.setText(context.getString(R.string.yes));
        radioAssetFeedBack1NotExist.setText(context.getString(R.string.no));
        radioAssetFeedBack1CantSay.setText(context.getString(R.string.cant_say));
        radioAssetFeedBack1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                hasRated1 = true;
                if (checkedId == R.id.radioAssetFeedBack1Exist) {
                    rating1 = 1;
                    ISCIBExist = "Y";
                } else if (checkedId == R.id.radioAssetFeedBack1NotExist) {
                    rating1 = 0;
                    ISCIBExist = "N";
                } else if (checkedId == R.id.radioAssetFeedBack1CantSay) {
                    rating1 = 0;
                    ISCIBExist = "C";
                }
                ratingBar.setRating(rating + rating1 + rating2 + rating3 + rating4);
            }
        });


        radioAssetFeedBack2Exist.setText(context.getString(R.string.yes));
        radioAssetFeedBack2NotExist.setText(context.getString(R.string.no));
        radioAssetFeedBack2CantSay.setText(context.getString(R.string.cant_say));
        radioAssetFeedBack2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                hasRated2 = true;
                if (checkedId == R.id.radioAssetFeedBack2Exist) {
                    rating2 = 1;
                    IsCompleted = "Y";
                } else if (checkedId == R.id.radioAssetFeedBack2NotExist) {
                    rating2 = 0;
                    IsCompleted = "N";
                } else if (checkedId == R.id.radioAssetFeedBack2CantSay) {
                    rating2 = 0;
                    IsCompleted = "C";
                }
                ratingBar.setRating(rating + rating1 + rating2 + rating3 + rating4);
            }
        });


        radioAssetFeedBack3Exist.setText(context.getString(R.string.yes));
        radioAssetFeedBack3NotExist.setText(context.getString(R.string.no));
        radioAssetFeedBack3CantSay.setText(context.getString(R.string.cant_say));

        radioAssetFeedBack3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                hasRated3 = true;
                if (checkedId == R.id.radioAssetFeedBack3Exist) {
                    rating3 = 1;
                    DiscriptionMatch = "Y";
                } else if (checkedId == R.id.radioAssetFeedBack3NotExist) {
                    rating3 = 0;
                    DiscriptionMatch = "N";
                } else if (checkedId == R.id.radioAssetFeedBack3CantSay) {
                    rating3 = 0;
                    DiscriptionMatch = "C";
                }
                ratingBar.setRating(rating + rating1 + rating2 + rating3 + rating4);
            }
        });


        radioAssetFeedBack4Exist.setText(context.getString(R.string.yes));
        radioAssetFeedBack4NotExist.setText(context.getString(R.string.no));
        radioAssetFeedBack4CantSay.setText(context.getString(R.string.cant_say));
        radioAssetFeedBack4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                hasRated4 = true;
                if (checkedId == R.id.radioAssetFeedBack4Exist) {
                    rating4 = 1;
                    IsUseFull = "Y";
                } else if (checkedId == R.id.radioAssetFeedBack4NotExist) {
                    rating4 = 0;
                    IsUseFull = "N";
                } else if (checkedId == R.id.radioAssetFeedBack4CantSay) {
                    rating4 = 0;
                    IsUseFull = "C";
                }
                ratingBar.setRating(rating + rating1 + rating2 + rating3 + rating4);
            }

        });

        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (etMobile.getText().toString().trim().isEmpty()) {
                    etMobile.setFocusable(true);
                    etMobile.requestFocus();
                    MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.get_otp) + " " + context.getString(R.string.warning), context.getString(R.string.please_fill_mobile), strActivityCode+"01");
                } else if (etMobile.getText().toString().trim().length() < 10) {
                    etMobile.setFocusable(true);
                    etMobile.requestFocus();
                    MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.get_otp) + " " + context.getString(R.string.warning), context.getString(R.string.please_fill_valid_mobile), strActivityCode+"017");
                } else {
                    if (connectionDetector.isConnectingToInternet()) {
                        new GetOtpFromServer().execute();
                        btnGetOtp.setClickable(false);
                        btnGetOtp.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.grey, getApplicationContext().getTheme()));
                        try {
                            handler.postDelayed(runnable, 15 * 1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.get_otp) + " " + context.getString(R.string.warning), context.getString(R.string.no_internet), strActivityCode+"018");

                    }
                }
            }
        });


        btnSendFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerAssets.getSelectedItemPosition() == 0) {
                    MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_select_asset), strActivityCode+"019");
                } else if (!assetExist.equals("Y") && bitmapImage == null) {
                    MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_choose_feed_back_image), strActivityCode+"020");

                } else if (assetExist.equals("Y") && radioAssetFeedBack1.getCheckedRadioButtonId() == (-1)) {
                    MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_select) + context.getString(R.string.asset_feed_back_1), strActivityCode+"021");
                } else if (assetExist.equals("Y") && radioAssetFeedBack2.getCheckedRadioButtonId() == (-1)) {
                    MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_select) + context.getString(R.string.asset_feed_back_2), strActivityCode+"09");
                } else if (assetExist.equals("Y") && radioAssetFeedBack3.getCheckedRadioButtonId() == (-1)) {
                    MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_select) + context.getString(R.string.asset_feed_back_3), strActivityCode+"08");
                } else if (assetExist.equals("Y") && radioAssetFeedBack4.getCheckedRadioButtonId() == (-1)) {
                    MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_select) + context.getString(R.string.asset_feed_back_4), strActivityCode+"07");
                } else if (etName.getText().toString().trim().isEmpty()) {
                    etName.setFocusable(true);
                    etName.requestFocus();
                    MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), getApplicationContext().getString(R.string.please_fill_name), strActivityCode+"06");
                } else if (etMobile.getText().toString().trim().isEmpty()) {
                    etMobile.setFocusable(true);
                    etMobile.requestFocus();
                    MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), getApplicationContext().getString(R.string.please_fill_mobile), strActivityCode+"05");
                } else if (etMobile.getText().toString().trim().length() < 10) {
                    etMobile.setFocusable(true);
                    etMobile.requestFocus();
                    MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), getApplicationContext().getString(R.string.please_fill_valid_mobile), strActivityCode+"04");
                } else if (etOtp.getText().toString().trim().isEmpty()) {
                    etOtp.setFocusable(true);
                    etOtp.requestFocus();
                    MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), getApplicationContext().getString(R.string.please_fill_otp), strActivityCode+"010");
                } else {
                    if (connectionDetector.isConnectingToInternet()) {
                        final Dialog dialogAlert = new Dialog(SendFeedBackActivity.this);
                        MyAlert.dialogForCancelOk
                                (context, R.mipmap.icon_warning, context.getString(R.string.submission_alert),
                                        context.getString(R.string.submit_confirmation),
                                        dialogAlert,
                                        context.getString(R.string.submit),
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialogAlert.dismiss();
                                                etOtp.setEnabled(false);
                                                new VerifyOtpByServer(etMobile.getText().toString(), etOtp.getText().toString(), MySharedPref.getOtpID(context)).execute();
                                            }
                                        },
                                        context.getString(R.string.cancel),
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialogAlert.dismiss();
                                            }
                                        },
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialogAlert.dismiss();
                                            }
                                        }, strActivityCode+"022");

                    } else
                        MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), getApplicationContext().getString(R.string.no_internet), strActivityCode+"011");

                }
            }
        });

    }

    private void _goToCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isBrowse = false;
        try {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (bitmap != null) {
                bitmap = _getResizedBitmap(bitmap, sizeReduce);
            }
            uploadImage.setImageBitmap(bitmap);
            bitmapImage = bitmap;
        } catch (Exception e) {
            uploadImage.setImageBitmap(null);
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap _getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String getResponse(String Url) {
        try {
            URL url = new URL(Url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            statusCode = conn.getResponseCode();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            return baseFragment.convertStreamToString(in);
        } catch (Exception e) {
            return "Error " + e.getMessage();
        }

    }



    public void loadActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_white_back);
        @SuppressWarnings("deprecation")
        Drawable d = getResources().getDrawable(R.drawable.header_bg2);
        getSupportActionBar().setBackgroundDrawable(d);
        getSupportActionBar().setTitle(Html.fromHtml("<center><font color='#FFFFFF'><b>&nbsp;" + context.getString(R.string.app_name) + "</b></font></center>"));
        getSupportActionBar().setIcon(R.mipmap.ic_india_emblem);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }

    private void visibleWidgets() {
        //  infoTV.setBackgroundColor(SendFeedBackActivity.this.getResources().getColor(R.color.green_color, SendFeedBackActivity.this.getTheme()));
        infoTV.setTextColor(SendFeedBackActivity.this.getResources().getColor(R.color.green_color, SendFeedBackActivity.this.getTheme()));
        infoTV.setText(validateMessage);
        assetExistOrNotTV.setVisibility(View.VISIBLE);
        radioAsset.setVisibility(View.VISIBLE);
        radioAssetExist.setVisibility(View.VISIBLE);
        radioAssetNotExist.setVisibility(View.VISIBLE);
        radioAssetCantSay.setVisibility(View.VISIBLE);
        viewAssetTV.setVisibility(View.VISIBLE);
        llAsset.setVisibility(View.VISIBLE);


    }


    private void hideWidgets() {
        //infoTV.setBackgroundColor(SendFeedBackActivity.this.getResources().getColor(R.color.error_background, SendFeedBackActivity.this.getTheme()));
        infoTV.setTextColor(SendFeedBackActivity.this.getResources().getColor(R.color.red, SendFeedBackActivity.this.getTheme()));
        infoTV.setText(validateMessage);

        assetExistOrNotTV.setVisibility(View.GONE);
        radioAsset.setVisibility(View.GONE);
        radioAssetExist.setVisibility(View.GONE);
        radioAssetNotExist.setVisibility(View.GONE);
        radioAssetCantSay.setVisibility(View.GONE);
        viewAssetTV.setVisibility(View.GONE);
        llAsset.setVisibility(View.GONE);
    }
    protected void loadGPS() {
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            Boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!isGPSEnabled) {
                validateMessage = getApplicationContext().getString(R.string.enable_gps);
                hideWidgets();
                dLatitude = 0.0;
                dLongitude = 0.0;
                dAccuracy = 0.0;
                gpsTracker.showSettingsAlert();
            } else
                requestPermission();
        } catch (Exception e) {
            validateMessage = getApplicationContext().getString(R.string.no_internet);
            hideWidgets();
        }
    }



    @SuppressLint("StaticFieldLeak")
    class InitAssetTask extends AsyncTask<Void, Void, String> {
        String strLongitude, strLatitude;
        private final ProgressDialog dialog = new ProgressDialog(SendFeedBackActivity.this);

        InitAssetTask(String strLongitude, String strLatitude) {
            this.strLongitude = strLongitude;
            this.strLatitude = strLatitude;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage(context.getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String response;
            try {
                String apiUrl = Api.ASSET_NEAR_BY_XY_URL + URLEncoder.encode(this.strLongitude, "UTF-8") + "&yy=" + URLEncoder.encode(this.strLatitude, "UTF-8");
                //  apiUrl=  "https://bhuvan-app2.nrsc.gov.in/janmnrega/bufferassets.php?xx=82.2879318&yy=22.2345058";
       //  apiUrl = "https://bhuvan-app2.nrsc.gov.in/janmnrega_v1/bufferassets.php?xx=82.2982361&yy=22.2411385";
     //    apiUrl = "https://bhuvan-app2.nrsc.gov.in/janmnrega_v1/get-asset-list.php?x=78.050012&y=17.04500&buff=1000";
                response = getResponse(apiUrl);
                if (statusCode == 200) {
                    JSONArray assetArray = new JSONArray(response);
                    try {
                        if (assetArray.length() > 0) {
                            listCustomText.clear();listSRNO.clear();listAssetID.clear();
                            listAssetName.clear();listWorkCode.clear();listWorkName.clear();
                            listCustomText.add(getApplicationContext().getString(R.string.select));listSRNO.add("0");listAssetID.add("0");
                            listAssetName.add("0");listWorkCode.add("0");listWorkName.add("0");
                            for (int a = 0; a < assetArray.length(); a++) {
                                JSONObject assetObject = assetArray.getJSONObject(a);
                                String assetName;
                                assetName = context.getString(R.string.asset_name) + " : " + assetObject.getString("asset_name");
                                assetName += "\n" + context.getString(R.string.asset_code) + " : " + assetObject.getString("asset_id");
                                assetName += "\n" + context.getString(R.string.work_name) + " : " + assetObject.getString("work_name");
                                assetName += "\n" + context.getString(R.string.work_code) + " : " + assetObject.getString("work_code");

                                listCustomText.add(assetName);
                                listSRNO.add(assetObject.getString("collection_sno"));
                                listAssetID.add(assetObject.getString("asset_id"));
                                listAssetName.add(assetObject.getString("asset_name"));
                                listWorkCode.add(assetObject.getString("work_code"));
                                listWorkName.add(assetObject.getString("work_name"));
                            }
                            return "true";
                        } else {
                            return "norecord";
                        }


                    } catch (Exception e) {
                        return "Error: " + e.getMessage();

                    }
                } else {
                    return getApplicationContext().getString(R.string.data_not_found);
                }
            } catch (Exception e) {

                return "Error " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            if (result.contains("true")) {
                final CustomListingAdapter spinnerArrayAdapter = new CustomListingAdapter(SendFeedBackActivity.this, listCustomText);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinnerAssets.setAdapter(spinnerArrayAdapter);
                validateMessage = getApplicationContext().getString(R.string.asset_exist_msg) + "Latitude: " + strLatitude + ",Longitude: " + strLongitude;
                visibleWidgets();
            } else {
                validateMessage = getApplicationContext().getString(R.string.no_asset_msg) + getApplicationContext().getString(R.string.latitude) + strLatitude + "," + getApplicationContext().getString(R.string.longitude) + strLongitude;
                hideWidgets();

            }
        }
    }

    class GetOtpFromServer extends AsyncTask<Void, Void, String> {
        final private ProgressDialog dialog = new ProgressDialog(SendFeedBackActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage(context.getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String response;
            try {
                String apiUrl = (Helper.getBaseURL() + "CCMGNERGA/PublicService.svc/SaveOTP?")
                        + "mobile=" + URLEncoder.encode(baseFragment.encrypt(etMobile.getText().toString()), "UTF-8");
                response = getResponse(apiUrl);
                if (statusCode == 200) {
                    JSONObject jObject = new JSONObject(response);
                    JSONObject msgObject = jObject.getJSONObject("message");
                    statusMessage = msgObject.getString("message");
                    status = msgObject.getString("status");
                    if (status.equals("true") /*&& statusMessage.equals("OTP Sent Successfully")*/) {
                        try {
                            JSONArray otpJsonArray = jObject.getJSONArray("otp");
                            for (int i = 0; i < otpJsonArray.length(); i++) {
                                JSONObject otpObject = otpJsonArray.getJSONObject(i);
                                MySharedPref.setOtpID(context, otpObject.getString("OTPID"));
                            }
                            return "true";
                        } catch (Exception e) {
                            return statusMessage = "Error: " + e.getMessage();
                        }
                    }
                    else
                        return statusMessage;


                } else {
                    statusMessage = getApplicationContext().getString(R.string.otp_not_received);
                    return statusMessage;
                }
            } catch (Exception e) {
                return statusMessage = "Error " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            if (result.contains("true")) {
                etMobile.setEnabled(false);
                Toast.makeText(SendFeedBackActivity.this, statusMessage, Toast.LENGTH_LONG).show();
            } else {
                etMobile.setEnabled(true);
                Toast.makeText(SendFeedBackActivity.this, statusMessage, Toast.LENGTH_LONG).show();
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    class VerifyOtpByServer extends AsyncTask<Void, Void, String> {
        String MobNo;
        String otpid;
        String pwd;

        final private ProgressDialog dialog = new ProgressDialog(SendFeedBackActivity.this);

        VerifyOtpByServer(String MobileNo, String otppwd, String id) {
            this.MobNo = MobileNo;
            this.pwd = otppwd;
            this.otpid = id;

        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage(context.getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String response;
            try {
                String apiUrl = (Helper.getBaseURL() + "CCMGNERGA/PublicService.svc/CheckOTP?")
                        + "MobileNo=" + URLEncoder.encode(baseFragment.encrypt(this.MobNo), "UTF-8")
                        + "&userpwd=" + URLEncoder.encode(baseFragment.encrypt(this.pwd), "UTF-8")
                        + "&otpId=" + URLEncoder.encode(this.otpid, "UTF-8");
                response = getResponse(apiUrl);
                Log.e("VerifyOtpByServer", response);
                if (statusCode == 200) {
                    JSONObject jObject = new JSONObject(response);
                    JSONObject message = jObject.getJSONObject("message");
                    statusMessage = message.getString("message");
                    status = message.getString("status");
                    return statusMessage;
                } else {
                    return statusMessage = getApplicationContext().getString(R.string.something_went_wrong);
                }
            } catch (Exception e) {
                return statusMessage = "Error " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            if (statusMessage.equals("OTP Successfully Matched")) {
                final Dialog dialogAlert2 = new Dialog(SendFeedBackActivity.this);
                MyAlert.dialogForOk
                        (context, R.mipmap.icon_info, context.getString(R.string.submit_info),
                                context.getString(R.string.otp_matched) + "\n" + context.getString(R.string.please_submit_form),
                                dialogAlert2,
                                context.getString(R.string.submit),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogAlert2.dismiss();
                                          getToken();
                                    }
                                }, strActivityCode+"024");

            }
            else {
                etOtp.setEnabled(true);
                MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), statusMessage, strActivityCode+"016");
            }

        }
    }

    private void setMandatoryMark() {
        RedAsterisk.setAstrikOnTextView(assetTV, context.getString(R.string.asset_work));
        RedAsterisk.setAstrikOnTextView(assetExistOrNotTV, context.getString(R.string.r_u_s_see_asset));
        RedAsterisk.setAstrikOnTextView(assetFeedBack1, context.getString(R.string.asset_feed_back_1));
        RedAsterisk.setAstrikOnTextView(assetFeedBack2, context.getString(R.string.asset_feed_back_2));
        RedAsterisk.setAstrikOnTextView(assetFeedBack3, context.getString(R.string.asset_feed_back_3));
        RedAsterisk.setAstrikOnTextView(assetFeedBack4, context.getString(R.string.asset_feed_back_4));
        RedAsterisk.setAstrikOnTextInputTextInputLayout(etNameLayout, context.getString(R.string.your_name));
        RedAsterisk.setAstrikOnTextInputTextInputLayout(etMobileLayout, context.getString(R.string.mobile_number));
        RedAsterisk.setAstrikOnTextInputTextInputLayout(etOtpLayout, "OTP");
      /*  RedAsterisk.setAstrikOnTextInputEditText(etName, context.getString(R.string.your_name));
        RedAsterisk.setAstrikOnTextInputEditText(etMobile, context.getString(R.string.mobile_number));
        RedAsterisk.setAstrikOnTextInputEditText(etOtp, "OTP");*/
    }
    private void stopTimer() {
        if (mTimer1 != null) {
            mTimer1.cancel();
            mTimer1.purge();
        }
    }

    private void startTimer() {
        Constant.startVolleyDialog(SendFeedBackActivity.this);
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run() {
                        Intent serviceIntent = new Intent(SendFeedBackActivity.this, GlobalLocationService.class);
                        startService(serviceIntent);
                        if (Constant.dLatitude != null) {
                            dLatitude = (Math.round(Constant.dLatitude * 1000000.0) / 1000000.0);
                            dLongitude = (Math.round(Constant.dLongitude * 1000000.0) / 1000000.0);
                            dAccuracy = (Math.round(Constant.dAccuracy * 100.0) / 100.0);
                            if ((dLatitude > 0.0) && (dLongitude > 0.0)/* && (dAccuracy < 25.0)*/) {
                                stopTimer();
                                stopService(serviceIntent);
                                Constant.dismissVolleyDialog();
                                strLongitude =String.valueOf(dLongitude);
                                strLatitude =String.valueOf(dLatitude);
                                if (connectionDetector.isConnectingToInternet()) {
                                    new InitAssetTask(strLongitude, strLatitude).execute();
                                } else {
                                    validateMessage = getApplicationContext().getString(R.string.no_internet);
                                    hideWidgets();
                                    Toast.makeText(SendFeedBackActivity.this, getApplicationContext().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });
            }
        };
        mTimer1.schedule(mTt1, 1, 5000);

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (locationAccepted)
                    startTimer();

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTimer();

    }

    private void getToken() {
        Constant.startVolleyDialog(this);
        Call<TokenData> call = new Retrofit.Builder()
                .baseUrl(Helper.getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api.class)
                .getToken(SplashActivity.strTName,SplashActivity.strTPwd,Api.grant_type);
        call.enqueue(new Callback<TokenData>() {
            @Override
            public void onResponse(Call<TokenData> call, Response<TokenData> response) {
                if (response.isSuccessful()) {
                    submitFeedback(response.body().getAccess_token());

                } else {
                    MyAlert.showAlert(getApplicationContext(), R.mipmap.icon_warning, getApplicationContext().getString(R.string.submit_warning), getApplicationContext().getString(R.string.network_req_failed), strActivityCode + "04");
                }
                Constant.dismissVolleyDialog();
            }

            @Override
            public void onFailure(Call<TokenData> call, Throwable t) {
                Constant.dismissVolleyDialog();
                MyAlert.showAlert(getApplicationContext(), R.mipmap.icon_warning, getApplicationContext().getString(R.string.submit_warning), getApplicationContext().getString(R.string.network_req_failed), strActivityCode + "05");
            }
        });
    }

    private void submitFeedback(String token) {
        if (connectionDetector.isConnectingToInternet()) {
            try {
                String file = "";
                if (!assetExist.equals("Y")) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] data = bos.toByteArray();
                    file = Base64.encodeToString(data, Base64.DEFAULT);
                }
                Constant.startVolleyDialog(this);
                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(new TokenInterceptor(token))
                        .build();
                Call<ResponseData> call = new Retrofit.Builder()
                        .baseUrl(Helper.getBaseURL())
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build()
                        .create(Api.class)
                        .uploadAssetFeedbackModel(new UploadAssetFeedbackModel(baseFragment.encrypt(etMobile.getText().toString().trim()), baseFragment.encrypt(assetID), baseFragment.encrypt(workType), baseFragment.encrypt(strLongitude), baseFragment.encrypt(strLatitude), baseFragment.encrypt((int) ratingBar.getRating() + ""), baseFragment.encrypt(ISCIBExist), baseFragment.encrypt(IsCompleted), baseFragment.encrypt(DiscriptionMatch), baseFragment.encrypt(IsUseFull), file));
                // .uploadAssetFeedbackModel(new UploadAssetFeedbackModel("Pf5HM/H0TcQrhfHtpzC92w==", "tuR/9O1Dyn+ptgXkN3dOzA==","h4FvbOPfdEr/AHAsfNdbylg+aEc2BvGfyNWEyfNO3fE=","3mDpf3QFvTaVk1M6mDkFSg==","9KEF8An/FPq2FaG+hkHxDQ==", "QHbGST0KqAfU6GUF0o7h2Q==", "QAnkYv/dCgbVKzx0KOWrUw==", "QAnkYv/dCgbVKzx0KOWrUw==","8u1ioxVLVGuFi3Iv72EVAw==","8u1ioxVLVGuFi3Iv72EVAw==", ""));
                call.enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        ResponseData responseData = response.body();
                        if (response.isSuccessful()) {
                            final Dialog dialogAlert2 = new Dialog(SendFeedBackActivity.this);
                            MyAlert.dialogForOk
                                    (context, R.mipmap.icon_info, context.getString(R.string.submit_info),
                                            responseData.getMessage(),
                                            dialogAlert2,
                                            context.getString(R.string.ok),
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialogAlert2.dismiss();
                                                    finish();
                                                }
                                            }, strActivityCode + "021");

                            Constant.dismissVolleyDialog();
                        } else {
                            MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), responseData.getMessage() + "\n" + response.message(), strActivityCode + "012");
                            Constant.dismissVolleyDialog();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), t.getCause() + "\n" + t.getMessage(), strActivityCode + "013");
                        Constant.dismissVolleyDialog();
                    }
                });
            }
            catch (Exception e){
                MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), "Exception: "+e.getMessage(), strActivityCode + "032");

            }
        }
        else
            MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.no_internet), strActivityCode + "014");

    }

   /* private String getResponse2(String Url) {
        try {
            URL url = new URL(Url);//https://nregarep2.nic.in/CCMGNERGA/SaveAssetFeedback.aspx
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            String file = "";
            if (!assetExist.equals("Y")) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] data = bos.toByteArray();
                file = Base64.encodeToString(data, Base64.DEFAULT);
            }
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("MobileNo", baseFragment.encrypt(etMobile.getText().toString().trim()))
                    .appendQueryParameter("AssetID", baseFragment.encrypt(assetID))
                    .appendQueryParameter("WorkCode", baseFragment.encrypt(workType))
                    .appendQueryParameter("Longitude", baseFragment.encrypt(strLongitude))
                    .appendQueryParameter("Latitude", baseFragment.encrypt(strLatitude))
                    .appendQueryParameter("RatingScore", baseFragment.encrypt((int) ratingBar.getRating() + ""))
                    .appendQueryParameter("ISCIBExist", baseFragment.encrypt(ISCIBExist))
                    .appendQueryParameter("IsCompleted", baseFragment.encrypt(IsCompleted))
                    .appendQueryParameter("DiscriptionMatch", baseFragment.encrypt(DiscriptionMatch))
                    .appendQueryParameter("IsUseFull", baseFragment.encrypt(IsUseFull))
                    .appendQueryParameter("UploadImage", file);

            String query = builder.build().getEncodedQuery();
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            assert query != null;

            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            statusCode = conn.getResponseCode();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            return baseFragment.convertStreamToString(in);
        } catch (Exception e) {
            return "Error " + e.getMessage();
        }
    }
    @SuppressLint("StaticFieldLeak")
    class SendAssetFeedback extends AsyncTask<Void, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(SendFeedBackActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage(context.getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String response;
            try {
                response = getResponse2((Helper.getBaseURL() + "CCMGNERGA/SaveAssetFeedback.aspx"));
                if (statusCode == 200) {
                    JSONObject message = new JSONObject(response);
                    if (MySharedPref.getAppLangCode(context).equals("en")) {
                        statusMessage = message.getString("message");
                    } else {
                        statusMessage = message.getString("messageLocal");
                    }
                    status = message.getString("status");
                    if (status.equals("resend")) {
                        return context.getString(R.string.already_send_feed_back);
                    } else if (!status.equals("true")) {
                        return statusMessage;
                    } else
                        return "true";
                } else {
                    return context.getString(R.string.data_not_found);
                }
            } catch (Exception e) {
                return "Error " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            if (result.contains("true")) {
                final Dialog dialogAlert2 = new Dialog(SendFeedBackActivity.this);
                MyAlert.dialogForOk
                        (context, R.mipmap.icon_info, context.getString(R.string.submit_info),
                                context.getString(R.string.msg_success_send_feed_bacK),
                                dialogAlert2,
                                context.getString(R.string.ok),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogAlert2.dismiss();
                                        Intent intent = new Intent(SendFeedBackActivity.this, MainActivity.class);
                                        intent.putExtra("cLong", strLongitude);
                                        intent.putExtra("cLat", strLatitude);
                                        intent.putExtra("displayPosition", "0");
                                        startActivity(intent);
                                    }
                                }, strActivityCode+"023");
            } else {
                MyAlert.showAlert(SendFeedBackActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), result, strActivityCode+"012");
            }
        }


    }*/
}

