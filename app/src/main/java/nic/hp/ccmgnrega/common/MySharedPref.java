package nic.hp.ccmgnrega.common;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

public class MySharedPref {
    private static SharedPreferences spref;
    private static SharedPreferences.Editor editor;
    private static String strLanguage;

    public static void setOtpID(Context context, String otpID) {
        try {
            spref = context.getSharedPreferences("otpID", Context.MODE_PRIVATE);
            editor = spref.edit();
            editor.putString("ID", otpID);
            editor.apply();
        } catch (Exception e) {

        }
    }


    public static String getOtpID(Context context) {
        try {
            spref = context.getSharedPreferences("otpID", Context.MODE_PRIVATE);
            String status = spref.getString("ID", null);
            return status;
        } catch (Exception e) {
            return null;
        }
    }


    public static void setAppLangCode(Context context, String langCode) {
        try {
            spref = context.getSharedPreferences("langCode", Context.MODE_PRIVATE);
            editor = spref.edit();
            editor.putString("ID", langCode);
            editor.apply();
        } catch (Exception e) {

        }
    }


    public static String getAppLangCode(Context context) {
        try {
            spref = context.getSharedPreferences("langCode", Context.MODE_PRIVATE);
            String status = spref.getString("ID", null);
            return status;
        } catch (Exception e) {
            return null;
        }
    }


    public static void setLoginPin(Context context, String strLoginPin) {
        try {
            spref = context.getSharedPreferences("strLoginPin", Context.MODE_PRIVATE);
            editor = spref.edit();
            editor.putString("ID", strLoginPin);
            editor.apply();
        } catch (Exception e) {

        }
    }


    public static String getLoginPin(Context context) {
        try {
            spref = context.getSharedPreferences("strLoginPin", Context.MODE_PRIVATE);
            String status = spref.getString("ID", "");
            return status;
        } catch (Exception e) {
            return "";
        }
    }

    public static void setMobileNumber(Context context, String strMobileNo) {
        try {
            spref = context.getSharedPreferences("strMobileNo", Context.MODE_PRIVATE);
            editor = spref.edit();
            editor.putString("ID", strMobileNo);
            editor.apply();
        } catch (Exception e) {

        }
    }


    public static String getMobileNumber(Context context) {
        try {
            spref = context.getSharedPreferences("strMobileNo", Context.MODE_PRIVATE);
            String status = spref.getString("ID", "");
            return status;
        } catch (Exception e) {
            return "";
        }
    }

    public static void clearSharedPref(Context context) {
        setOtpID(context, null);
    }




}
