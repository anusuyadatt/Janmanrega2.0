package nic.hp.ccmgnrega.common;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import nic.hp.ccmgnrega.BaseActivity;


public class MyLocationListener extends BaseActivity implements LocationListener  {
      Context mContext;
    public MyLocationListener(Context context) {
        this.mContext = context;

    }

    @Override
    public void onLocationChanged(Location loc) {

        cLat=String.valueOf(loc.getLatitude());
        cLong=String.valueOf(loc.getLongitude());
        Toast.makeText(mContext,"Location changed: Lat: " + loc.getLatitude() + " Lng: "+ loc.getLongitude(), Toast.LENGTH_SHORT).show();
        String longitude = "Longitude: " + loc.getLongitude();
        Log.v("MyLocationListenerlongitude", longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.v("MyLocationListenerlatitude", latitude);

        /*------- To get city name from coordinates -------- */
      /*  String cityName = null;
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                + cityName;
        */
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
