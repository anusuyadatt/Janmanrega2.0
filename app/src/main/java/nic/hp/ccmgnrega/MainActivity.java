package nic.hp.ccmgnrega;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import nic.hp.ccmgnrega.adapter.NavDrawerListAdapter;
import nic.hp.ccmgnrega.common.DbHelper;
import nic.hp.ccmgnrega.fragment.AboutJanManregaFragment;
import nic.hp.ccmgnrega.fragment.AboutMGNREGAFragment;
import nic.hp.ccmgnrega.fragment.AssetsFragment;
import nic.hp.ccmgnrega.fragment.HomeFragment;
import nic.hp.ccmgnrega.fragment.SearchFragment;
import nic.hp.ccmgnrega.fragment.SearchAssetFragment;
import nic.hp.ccmgnrega.fragment.SettingFragment;
import nic.hp.ccmgnrega.fragment.SevenRegistersFragment;
import nic.hp.ccmgnrega.fragment.TenEntitlementsFragment;
import nic.hp.ccmgnrega.fragment.UserRegistrationFragment;
import nic.hp.ccmgnrega.fragment.VideoTutorialFragment;
import nic.hp.ccmgnrega.fragment.WorksFragment;
import nic.hp.ccmgnrega.model.ConnectionDetector;
import nic.hp.ccmgnrega.model.NavDrawerItem;

public class MainActivity extends BaseActivity {
    protected ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("displayPosition") != null)
              displayPosition = Integer.parseInt(bundle.getString("displayPosition"));
            if (bundle.getString("homeDisplayPosition") != null)
              homeDisplayPosition = Integer.parseInt(bundle.getString("homeDisplayPosition"));
            if (bundle.getString("buffer") != null)
                buffer = bundle.getString("buffer");
            if (bundle.getString("asset") != null)
                asset = bundle.getString("asset");
            if (bundle.getString("assetID") != null)
                assetID = bundle.getString("assetID");
            if (bundle.getString("workType") != null)
                workType = bundle.getString("workType");
            if (bundle.getString("workCode") != null)
                workCode = bundle.getString("workCode");
            if (bundle.getString("category") != null)
                category = bundle.getString("category");
            if (bundle.getString("finYear") != null)
                finYear = bundle.getString("finYear");
            if (bundle.getString("assetExist") != null)
                assetExist = bundle.getString("assetExist");
            if (bundle.getString("buffer") != null)
                buffer = bundle.getString("buffer");
            if (bundle.getString("panchayat") != null)
                panchayat = bundle.getString("panchayat");
            if (bundle.getString("panchayatName") != null)
                panchayatName = bundle.getString("panchayatName");
            if (bundle.getString("block") != null)
                block = bundle.getString("block");
            if (bundle.getString("cLong") != null)
                cLong = bundle.getString("cLong");
            if (bundle.getString("cLat") != null)
                cLat = bundle.getString("cLat");
        }
        connectionDetector = new ConnectionDetector(MainActivity.this);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.list_slidermenu);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            ViewGroup.MarginLayoutParams drawerParams = (ViewGroup.MarginLayoutParams) mDrawerLayout.getLayoutParams();
            drawerParams.setMargins(0, 80, 0, 0);
            mDrawerLayout.setLayoutParams(drawerParams);

        }
        navDrawerItems = new ArrayList<>();
        loadMenus();
        navMenuIcons.recycle();
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        loadGPS();
        if (gpsTracker.canGetLocation()) {
            cLong = gpsTracker.getLongitude() + "";
            cLat = gpsTracker.getLatitude() + "";
        }
        if (savedInstanceState == null) {
            if(displayPosition==-1 && homeDisplayPosition>0)
                homeDisplayView(homeDisplayPosition);
            else
               displayView(displayPosition);

        }

    }

    private void loadMenus() {
        for (int i = 0; i < navMenuTitles.length; i++) {
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
        }
    }

    protected void displayView(int position) {
        Fragment fragment = null;
        boolean isFragment = true;
        Bundle args = new Bundle();
        displayPosition = position;
        switch (navMenuIDs[position]) {
            case "1"://0 Home
                fragment = new HomeFragment();
                args.putString("menu", navMenuTitles[position]);
                args.putString("cLat", cLat);
                args.putString("cLong", cLong);
                break;

            case "2"://1  About MGNREGA
                fragment = new AboutMGNREGAFragment();
                args.putString("menu", navMenuTitles[position]);
                break;

            case "3":///2 Ten Entitlements
                fragment = new TenEntitlementsFragment();
                args.putString("menu", navMenuTitles[position]);
                break;

            case "4"://3 Seven Registers
                fragment = new SevenRegistersFragment();
                args.putString("menu", navMenuTitles[position]);
                break;

            case "5"://4 About Jan MANREGA
                fragment = new AboutJanManregaFragment();
                args.putString("menu", navMenuTitles[position]);
                break;

            case "6"://5 Video Tutorials
                fragment = new VideoTutorialFragment();
                args.putString("menu", navMenuTitles[position]);
                break;
            case "7"://6 Registration
                if (connectionDetector.isConnectingToInternet()) {
                    fragment = new UserRegistrationFragment();
                    args.putString("menu", navMenuTitles[position]);
                    args.putString("cLat", cLat);
                    args.putString("cLong", cLong);
                }
                else {
                    fragment = new HomeFragment();
                    args.putString("menu", navMenuTitles[0]);
                    args.putString("cLat", cLat);
                    args.putString("cLong", cLong);
                    Toast.makeText(MainActivity.this, getApplicationContext().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }

                break;
        }
        if (isFragment) {
            mDrawerList.setAdapter(adapter);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    protected void homeDisplayView(int position) {
        Fragment fragment = null;
        boolean isFragment = true;
        Bundle args = new Bundle();

        switch (position) {
            case  1:// Search Assets Part Of Home Screen
                fragment = new SearchAssetFragment();
                    args.putString("category", category);
                    args.putString("finYear", finYear);
                    args.putString("asset", asset);
                    args.putString("workType", workType);
                    args.putString("workCode", workCode);
                    args.putString("cLat", cLat);
                    args.putString("cLong", cLong);
                    args.putString("menu", getApplicationContext().getString(R.string.search_assets));

                break;

            case 2://  Nearby Assets  Part Of Home Screen

                    fragment = new AssetsFragment();
                    args.putString("cLat", cLat);
                    args.putString("cLong", cLong);
                    args.putString("buffer", buffer);
                    args.putString("menu", getApplicationContext().getString(R.string.nearby_assets));
                break;

            case 3:// Search By Job Card Number  Part Of Home Screen
                fragment =  new SearchFragment();
                args.putString("menu",getApplicationContext().getString(R.string.know_worker_att_pay));
                break;
            case 4://Preferences  Part Of Home Screen
                fragment = new SettingFragment();
                args.putString("menu",getApplicationContext().getString(R.string.preferences));
                break;
            case 7://Permissible Works Part Of Home Screen
                fragment = new WorksFragment();
                break;

        }
        if (isFragment) {
            mDrawerList.setAdapter(adapter);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
      //  menu.getItem(0).setTitle(getApplicationContext().getString(R.string.about_app));
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_home).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(getApplicationContext().getString(R.string.exit))
                .setMessage(getApplicationContext().getString(R.string.are_you_sure))
                .setIcon(android.R.drawable.ic_menu_close_clear_cancel)
                .setPositiveButton(getApplicationContext().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton(getApplicationContext().getString(R.string.cancel), null).show();
    }



    protected class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            displayView(position);
        }
    }
}