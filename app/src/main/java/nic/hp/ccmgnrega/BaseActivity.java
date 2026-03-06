
package nic.hp.ccmgnrega;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
//import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
//import com.scottyab.rootbeer.RootBeer;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
//import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import nic.hp.ccmgnrega.adapter.NavDrawerListAdapter;
import nic.hp.ccmgnrega.common.GPSTracker;
import nic.hp.ccmgnrega.common.MySharedPref;

import nic.hp.ccmgnrega.model.NavDrawerItem;


public class BaseActivity extends AppCompatActivity {

    protected ActionBarDrawerToggle mDrawerToggle;
    protected CharSequence mTitle;
    protected String[] navMenuIDs, navMenuTitles;
    protected TypedArray navMenuIcons;
    protected ArrayList<NavDrawerItem> navDrawerItems;
    protected NavDrawerListAdapter adapter;
    protected DrawerLayout mDrawerLayout;
    protected ListView mDrawerList;
    protected String cLong = "0";
    protected String cLat = "0";
    protected String buffer = "1000";
    protected String panchayat = "";
    protected String status;
    protected String language = "en";
    protected String asset = "";
    protected String assetID = "";
    protected String assetExist = "";
    protected String block = "";
    protected String panchayatName = "";
    protected String workType = "";
    protected String workCode = "";
    protected String category = "";
    protected String finYear = "";
    int displayPosition = 0;
    int homeDisplayPosition = 0;
    GPSTracker gpsTracker;
    String langCode="en";
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context =getApplicationContext();
        if(MySharedPref.getAppLangCode(context) ==null)
            MySharedPref.setAppLangCode(context,"en");
        langCode=MySharedPref.getAppLangCode(context);
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());
        _initPermission();
        loadActionBar();
        _initMenu();
        gpsTracker = new GPSTracker(BaseActivity.this);

    }
    protected void loadGPS() {
        if (gpsTracker.canGetLocation()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    cLat = String.valueOf(gpsTracker.latitude);
                    cLong = String.valueOf(gpsTracker.longitude);
                }
            }, 1000);

        } else
            gpsTracker.showSettingsAlert();


    }

    protected void loadActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        @SuppressWarnings("deprecation")
        Drawable d = getResources().getDrawable(R.drawable.header_bg2);
        getSupportActionBar().setBackgroundDrawable(d);
        getSupportActionBar().setTitle(Html.fromHtml("<center><font color='#FFFFFF'><b>&nbsp;" + getApplicationContext().getString(R.string.app_name) + "</b></font></center>"));
        getSupportActionBar().setIcon(R.mipmap.ic_india_emblem);
    }


    protected void _initPermission() {
        try {
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.INTERNET);
            }

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            }

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.ACCESS_WIFI_STATE);
            }
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.ACCESS_NETWORK_STATE);
            }
          /*  if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.READ_CONTACTS);
            }*/


          /*  if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.RECEIVE_SMS);
            }

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.SEND_SMS);
            }

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.CALL_PHONE);
            }*/

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1001);
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error ::" + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    @SuppressLint("Range")
    private void _initMenu() {
        List<String> idsList = new ArrayList<>();
        List<String> titlesList = new ArrayList<>();
        idsList.add("1");
        titlesList.add(getApplicationContext().getString(R.string.home));
        idsList.add("2");
        titlesList.add(getApplicationContext().getString(R.string.about_mgnrega));
        idsList.add("3");
        titlesList.add(getApplicationContext().getString(R.string.ten_entitlements));
        idsList.add("4");
        titlesList.add(getApplicationContext().getString(R.string.seven_registers));
        idsList.add("5");
        titlesList.add(getApplicationContext().getString(R.string.about_janmanrega));
        idsList.add("6");
        titlesList.add(getApplicationContext().getString(R.string.video_tutorials));
        idsList.add("7");
        titlesList.add(getApplicationContext().getString(R.string.registration));

        navMenuIDs = idsList.toArray(new String[0]);
        navMenuTitles = titlesList.toArray(new String[0]);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

}

