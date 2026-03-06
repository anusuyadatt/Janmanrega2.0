package nic.hp.ccmgnrega.common;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import nic.hp.ccmgnrega.R;


public class GPSTracker extends Service implements LocationListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 1
    private static final long MIN_TIME_BW_UPDATES = 0; // 10 second
    private final Context mContext;
    public Location location;
    public double latitude;
    public double longitude;
    public boolean isGPSEnabled = false;
    public boolean isNetworkEnabled = false;
    protected LocationManager locationManager;
    boolean canGetLocation = false;
    float accuracy;
    protected DbHelper dbHelper ;
    protected SQLiteDatabase dbReader;
    protected SQLiteDatabase dbWriter;
    

    public GPSTracker(Context context) {
        this.mContext = context;
        dbHelper = new DbHelper(this.mContext);
        dbReader = dbHelper.getReadableDatabase();
        dbWriter = dbHelper.getWritableDatabase();
        getLocation();
    }


    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            accuracy = location.getAccuracy();
                        }
                        updateGPSCoordinates();
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {


                    if (locationManager != null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Location gLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (gLocation != null && accuracy > gLocation.getAccuracy()) {
                            location = gLocation;
                            updateGPSCoordinates();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Error : Location", "Impossible to connect to LocationManager", e);
        }
        return location;
    }

    private void updateGPSCoordinates() {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }


    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle(mContext.getString(R.string.switch_on_gps));
        alertDialog.setMessage(mContext.getString(R.string.enable_gps));
        alertDialog.setPositiveButton(mContext.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton(mContext.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }


    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}