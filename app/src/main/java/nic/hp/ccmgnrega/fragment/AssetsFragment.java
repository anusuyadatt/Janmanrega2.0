package nic.hp.ccmgnrega.fragment;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;
import static android.os.SystemClock.sleep;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.goodiebag.pinview.Pinview;

import java.util.Timer;
import java.util.TimerTask;

import nic.hp.ccmgnrega.GiveFeedbackWorkerReportActivity;
import nic.hp.ccmgnrega.MainActivity;
import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.SendFeedBackActivity;
import nic.hp.ccmgnrega.common.Api;
import nic.hp.ccmgnrega.common.Constant;
import nic.hp.ccmgnrega.common.GPSTracker;
import nic.hp.ccmgnrega.common.GlobalLocationService;
import nic.hp.ccmgnrega.common.MyAlert;
import nic.hp.ccmgnrega.common.MySharedPref;
import nic.hp.ccmgnrega.model.ConnectionDetector;
import nic.hp.ccmgnrega.common.DbHelper;
import nic.hp.ccmgnrega.model.GetAssetMap;
import nic.hp.ccmgnrega.webview.MyAppWebViewClient;

@SuppressLint("SetTextI18n")
public class AssetsFragment extends BaseFragment {
    protected View rootView;
    protected String cLong = "0",cLat = "0", buffer = "1000";
    protected Spinner spinnerDistance;
    String[] meterArray;
    WebView mWebView;
    GetAssetMap map;
    ImageView ivActionFeedBack;
    GPSTracker gpsTracker;
    TextView infoTV;
    private Double dLatitude = 0.0, dLongitude = 0.0, dAccuracy = 1000.0;
    private Timer mTimer1;
    private TimerTask mTt1;
    private Handler mTimerHandler = new Handler();
    String strActivityCode="02-";
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        rootView = inflater.inflate(R.layout.fragment_assets, container, false);
        connectionDetector = new ConnectionDetector(getActivity());
        dbHelper = new DbHelper(getActivity());
        dbReader = dbHelper.getReadableDatabase();
        dbWriter = dbHelper.getWritableDatabase();
        gpsTracker = new GPSTracker(getActivity());
        Bundle args = getArguments();
        if (args != null) {
            if (args.getString("menu") != null)
                menu = args.getString("menu");
            if (args.getString("cLong") != null) 
                cLong = args.getString("cLong");
            if (args.getString("cLat") != null) 
                cLat = args.getString("cLat");
            if (args.getString("buffer") != null) 
                buffer = args.getString("buffer");
        }

        if (Double.parseDouble(cLong) == 0 || Double.parseDouble(cLat) == 0) {
            Toast.makeText(getActivity(), getContext().getString(R.string.gps_reading), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("displayPosition", "0");
            startActivity(intent);

        }

        infoTV = rootView.findViewById(R.id.infoTV);
        ivActionFeedBack = rootView.findViewById(R.id.actionFeedBack);

        TextView headingTv = rootView.findViewById(R.id.heading);
        mWebView = rootView.findViewById(R.id.webview);
        spinnerDistance = rootView.findViewById(R.id.spinnerDistance);
        headingTv.setText(" "+menu);
        
        map = new GetAssetMap(getActivity());
        meterArray = getResources().getStringArray(R.array.meter);
        mWebView.setWebViewClient(new MyAppWebViewClient());
        mWebView.getSettings().setBuiltInZoomControls(true);
        // mWebView.getSettings().setDisplayZoomControls(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(false);
        mWebView.setClickable(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        loadGPS();
        spinnerDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                buffer = meterArray[position].replace("meter", "").trim();
                if (connectionDetector.isConnectingToInternet()) {
                    if (Double.parseDouble(cLong) > 0 && Double.parseDouble(cLat) > 0) {
                        String url= Api.ASSET_XY_URL + cLong + "&y=" + cLat + "&buff=" + buffer;
                        mWebView.loadUrl(url, null);
                       //https://bhuvan-app2.nrsc.gov.in/janmnrega/nrega2.php?x=77.2186069&y=28.6295675&buff=50
                   //     https://bhuvan-app2.nrsc.gov.in/janmnrega/nrega2.php?x=31.7789756&y=74.8027504&buff=5000
                        infoTV.setText(getContext().getString(R.string.your_current_location_is)+ cLong + ",  "+getContext().getString(R.string.latitude) + cLat + " " + getContext().getString(R.string.with) + " " + getContext().getString(R.string.accuracy)  + " " + dAccuracy + " Mtr");

                        mWebView.loadData(map.gpsNotEnable(), "text/html; charset=utf-8", "utf-8");
                    }
                }
                else {
                    mWebView.loadData(map.internentNotEnable(), "text/html; charset=utf-8", "utf-8");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        ivActionFeedBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionDetector.isConnectingToInternet()) {
                    if(MySharedPref.getLoginPin(getContext()).isEmpty()){
                        final Dialog dialogAlert2 = new Dialog(getActivity());
                        MyAlert.dialogForOk
                                (getContext(), R.mipmap.icon_warning, getContext().getString(R.string.warning),
                                        getContext().getString(R.string.please_register_first),
                                        dialogAlert2,
                                        getContext().getString(R.string.ok),
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogAlert2.dismiss();
                                                final Intent mainIntent = new Intent(getContext(), MainActivity.class);
                                                mainIntent.putExtra("displayPosition", "6");
                                                startActivity(mainIntent);
                                                getActivity().finish();
                                            }
                                        }, strActivityCode + "02");

                    }
                    else {
                     verifyLoginPin(getContext());
                    }




                }
                else
                    MyAlert.showAlert(getContext(), R.mipmap.icon_warning, getContext().getString(R.string.warning), getContext().getString(R.string.no_internet),strActivityCode+"01");

            }
        });
        return rootView;
    }
    protected void loadGPS() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        Boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPSEnabled || !gpsTracker.canGetLocation()) {
            dLatitude = 0.0;
            dLongitude = 0.0;
            dAccuracy = 0.0;
            gpsTracker.showSettingsAlert();
          } else
            startTimer();

    }
    private void stopTimer() {
        if (mTimer1 != null) {
            mTimer1.cancel();
            mTimer1.purge();
        }
    }

    private void startTimer() {
     Constant.startVolleyDialog(getActivity());
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run() {
                        Intent serviceIntent = new Intent(getActivity(), GlobalLocationService.class);
                        getActivity().startService(serviceIntent);
                        if (Constant.dLatitude != null) {
                            dLatitude = (Math.round(Constant.dLatitude * 1000000.0) / 1000000.0);
                            dLongitude = (Math.round(Constant.dLongitude * 1000000.0) / 1000000.0);
                            dAccuracy = (Math.round(Constant.dAccuracy * 100.0) / 100.0);
                            if ((dLatitude > 0.0) && (dLongitude > 0.0)/* && (dAccuracy < 25.0)*/) {
                                stopTimer();
                                getActivity().stopService(serviceIntent);
                                Constant.dismissVolleyDialog();

                                cLong =String.valueOf(dLongitude);
                                cLat =String.valueOf(dLatitude);
                                infoTV.setText(getContext().getString(R.string.your_current_location_is)+ cLong + ",  "+getContext().getString(R.string.latitude) + cLat + " " + getContext().getString(R.string.with) + " " + getContext().getString(R.string.accuracy)  + " " + dAccuracy + " Mtr");

                                loadNearByAssets();
                            }
                        }
                    }
                });
            }
        };
        mTimer1.schedule(mTt1, 1, 5000);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();

    }
    protected void loadNearByAssets() {
        if (gpsTracker.canGetLocation()) {
            buffer = meterArray[spinnerDistance.getSelectedItemPosition()].replace("meter", "").trim();
            if (connectionDetector.isConnectingToInternet()) {
                if (Double.parseDouble(cLat) > 0 && Double.parseDouble(cLong) > 0) {
                    mWebView.loadUrl(Api.ASSET_XY_URL + cLong + "&y=" + cLat + "&buff=" + buffer, null);
                } else 
                    mWebView.loadData(map.gpsNotEnable(), "text/html; charset=utf-8", "utf-8");
            } else 
                mWebView.loadData(map.internentNotEnable(), "text/html; charset=utf-8", "utf-8");
            infoTV.setText(getContext().getString(R.string.your_current_location_is)+ cLong + ",  "+getContext().getString(R.string.latitude) + cLat + " " + getContext().getString(R.string.with) + " " + getContext().getString(R.string.accuracy)  + " " + dAccuracy + " Mtr");
        } else {
            gpsTracker.showSettingsAlert();
        }
    }


    private void verifyLoginPin(Context context) {
        final Dialog dialog = new Dialog(getActivity());
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
                    MyAlert.showAlert(context, R.mipmap.icon_warning, context.getString(R.string.warning), context.getString(R.string.please_enter_pin), strActivityCode + "07");
                    pinview.requestFocus();
                    pinview.clearValue();
                }  else if(!inputLoginPin.isEmpty()&&inputLoginPin.length()<4) {
                    pinview.requestFocus();
                    pinview.clearValue();
                    MyAlert.showAlert(context, R.mipmap.icon_warning, context.getString(R.string.warning), context.getString(R.string.please_enter_valid_pin), strActivityCode + "08");
                }   else if(!inputLoginPin.isEmpty() && !inputLoginPin.equalsIgnoreCase(MySharedPref.getLoginPin(context))) {
                    pinview.requestFocus();
                    pinview.clearValue();
                    MyAlert.showAlert(context, R.mipmap.icon_warning, context.getString(R.string.warning), context.getString(R.string.input_pin_wrong), strActivityCode + "09");
                }  else {
                    dialog.dismiss();
                    Intent intent = new Intent(context, SendFeedBackActivity.class);
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