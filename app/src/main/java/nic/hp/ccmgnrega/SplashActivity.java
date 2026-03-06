package nic.hp.ccmgnrega;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import nic.hp.ccmgnrega.common.Api;
import nic.hp.ccmgnrega.common.DbHelper;
import nic.hp.ccmgnrega.common.SQLiteAssetHelper;
import nic.hp.ccmgnrega.model.ConnectionDetector;

public class SplashActivity extends Activity {
    Context context;
    protected ConnectionDetector connectionDetector;
    protected SQLiteAssetHelper dbAssetHelper;
    protected SQLiteDatabase dbAssetReader;
    public static String strCipherTrans = "", strAesencalgo = "", strSk = "", strNregaName = "",
            strNregaPwd = "", strTName = "", strTPwd = "";


    // String strActivityCode="08-"; Max Value
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_splash);
        context = getApplicationContext();
        connectionDetector = new ConnectionDetector(SplashActivity.this);
        dbAssetHelper = new SQLiteAssetHelper(getApplicationContext());
        dbAssetReader = dbAssetHelper.getReadableDatabase();
        getBaseData(dbAssetReader);

        if (Build.getRadioVersion() == null || Build.getRadioVersion().equalsIgnoreCase("") || isEmulator()) {
            Toast.makeText(context, context.getString(R.string.app_restriction), Toast.LENGTH_LONG).show();
            finishAffinity();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkRootMethod2() || isEmulator()) {
                    Toast.makeText(context, context.getString(R.string.root_warning), Toast.LENGTH_LONG).show();
                    finishAffinity();
                } else {
                    if (connectionDetector.isConnectingToInternet()) {
                        forceUpdate(context);
                    } else
                        moveToHomePage();
                }
            }
        }, 1000);

    }

    public void forceUpdate(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentVersion = null;
        if (packageInfo != null) {
            currentVersion = packageInfo.versionName;
        }
        new ForceUpdateAsync(currentVersion, context).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public class ForceUpdateAsync extends AsyncTask<String, String, String> {
        int statusCode;
        private String currentVersion, playStoreVersionWhatsNew, playStoreAppUrl, playStoreVersionUpdateMandatory, dialogmessagetoshow, response;
        String regExp = "^\\d*\\.\\d+|\\d*$";
        @SuppressLint("StaticFieldLeak")
        private Context context;

        public ForceUpdateAsync(String currentVersion, Context context) {
            this.currentVersion = currentVersion;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String playStoreVersion = null;
            try {
                String apiUrl = Api.GET_PLAYSTORE_APP_VERSION_URL;
                apiUrl += "&Platform=" + URLEncoder.encode("A", "UTF-8");
                apiUrl += "&packageid=" + URLEncoder.encode("nic.hp.ccmgnrega", "UTF-8");

                response = getResponse(apiUrl);
                //https://mobileappshp.nic.in/MyDiary/MobileAppVersions.svc/GetAppVersion?&Platform=A&packageid=nic.hp.ccmgnrega
                //Response Format  {"appVersionDetails":[{"Mandatory":"N","Platform":"A","UpdatedOn":"3\/24\/2023 2:29:00 PM","Url":"https:\/\/play.google.com\/store\/apps\/details?id=nic.hp.ccmgnrega","VersionNumber":"16.0","WhatsNew":"Bug Fixed"}],"message":{"message":"Data Successfully Fetched","status":"200"}}
                if (statusCode == 200) {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.length() > 0) {
                        JSONArray playStoreArray = jsonObject.getJSONArray("appVersionDetails");
                        if (playStoreArray.length() > 0) {
                            for (int i = 0; i < playStoreArray.length(); i++) {
                                JSONObject playStoreObject = playStoreArray.getJSONObject(i);
                                playStoreVersion = playStoreObject.getString("VersionNumber");
                                playStoreAppUrl = playStoreObject.getString("Url");
                                playStoreVersionUpdateMandatory = playStoreObject.getString("Mandatory");
                                playStoreVersionWhatsNew = playStoreObject.getString("WhatsNew");

                            }
                        }
                        return playStoreVersion;
                    } else {
                        return playStoreVersion;
                    }
                }
            } catch (Exception e) {
                return playStoreVersion;
            }
            return playStoreVersion;
        }

        @Override
        protected void onPostExecute(String playStoreVersion) {
            super.onPostExecute(playStoreVersion);
            if (playStoreVersion != null) {
                if (playStoreVersion.matches(regExp)) {
                    double cv = Double.parseDouble(currentVersion);
                    double lv = Double.parseDouble(playStoreVersion);
                    int i2 = Double.compare(lv, cv);
                    if (i2 > 0) {
                        dialogmessagetoshow = context.getString(R.string.new_version) + context.getString(R.string.app_name) + context.getString(R.string.please_update_version) + playStoreVersion;
                      /*  if (playStoreVersionUpdateMandatory.contains("N")) {
                            dialogUpdate(dialogmessagetoshow);
                        } else {*/
                            dialogMandatoryUpdate(dialogmessagetoshow);
                     //   }
                    } else
                        moveToHomePage();
                } else
                    moveToHomePage();
            } else
                moveToHomePage();


        }


        protected void dialogUpdate(String message) {
            new AlertDialog.Builder(SplashActivity.this)
                    .setTitle(context.getString(R.string.youAreNotUpdatedTitle))
                    //.setIcon(R.mipmap.ic_wifi)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(context.getString(R.string.update),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreAppUrl)));
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                    .setNegativeButton(context.getString(R.string.skip),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    moveToHomePage();
                                }
                            })
                    .show();
        }

        protected void dialogMandatoryUpdate(String message) {
            new AlertDialog.Builder(SplashActivity.this)
                    .setTitle(context.getString(R.string.youAreNotUpdatedTitle))
                    //.setIcon(R.mipmap.ic_wifi)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(context.getString(R.string.update),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreAppUrl)));
                                    dialog.dismiss();
                                    finish();
                                }
                            }).show();
        }

        protected String getResponse(String Url) {
            try {
                URL url = new URL(Url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                statusCode = conn.getResponseCode();
                InputStream in = new BufferedInputStream(conn.getInputStream());
                return convertStreamToString(in);
            } catch (Exception e) {
                return "Error " + e.getMessage();
            }
        }

        protected String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

    }

    public void moveToHomePage() {
        final Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainIntent.putExtra("displayPosition", "0");
        startActivity(mainIntent);
        finish();

    }

    private static boolean checkRootMethod2() {
        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su",
                "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    boolean isEmulator() {
        return (Build.MANUFACTURER.contains("Genymotion")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.toLowerCase().contains("droid4x")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("sdk_gphone64_x86_64")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.HARDWARE.equals("goldfish")
                || Build.HARDWARE.equals("ranchu")
                || Build.HARDWARE.equals("vbox86")
                || Build.HARDWARE.toLowerCase().contains("nox")
                || Build.FINGERPRINT.startsWith("generic")
                || Build.PRODUCT.equals("sdk")
                || Build.PRODUCT.equals("google_sdk")
                || Build.PRODUCT.equals("sdk_x86")
                || Build.PRODUCT.equals("vbox86p")
                || Build.PRODUCT.toLowerCase().contains("nox")
                || Build.BOARD.toLowerCase().contains("nox")
                || Build.BOARD.toLowerCase().contains("goldfish_x86_64")
                || Build.DEVICE.contains("emu64x")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.BOARD.equals("QC_Reference_Phone") && !Build.MANUFACTURER.equals("Xiaomi")
                || Build.HOST.equals("Build2") //MSI App Player
        );
    }

    @SuppressLint("Range")
    public void getBaseData(SQLiteDatabase db) {
        try {
            String selectQuery = "select * from tbl_dummyinfo";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    strCipherTrans = cursor.getString(cursor.getColumnIndex("cipher_trans"));
                    strAesencalgo = cursor.getString(cursor.getColumnIndex("aesencalgo"));
                    strSk = cursor.getString(cursor.getColumnIndex("sk"));
                    strNregaName = cursor.getString(cursor.getColumnIndex("a_uname"));
                    strNregaPwd = cursor.getString(cursor.getColumnIndex("a_pwd"));
                    strTName = cursor.getString(cursor.getColumnIndex("t_uname"));
                    strTPwd = cursor.getString(cursor.getColumnIndex("t_pwd"));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {

        }
    }


}