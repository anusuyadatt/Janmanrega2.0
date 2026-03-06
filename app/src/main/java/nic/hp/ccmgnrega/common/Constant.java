package nic.hp.ccmgnrega.common;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Base64;
import android.view.Window;

import java.io.ByteArrayOutputStream;

import nic.hp.ccmgnrega.R;

public class Constant {

    public static int MAX_DAYS_IN_MUSTER = 16;
    public static int MAX_LENGTH_OF_JOB_CARD_NO = 34;
    public static int MIN_LENGTH_OF_JOB_CARD_NO = 19;
    public static String NO_INTERNET_CONNECTIVITY = "No internet connectivity";
    public static String INVALID_JOB_CARD_NUMBER = "Job Card not found";
    public static String BHASHINI_PIPELINI_ID = "64392f96daac500b55c543cd";
    public static String JOB_CARD_ID = "jobCardId";
    public static String ENGLISH_LANGUAGE_CODE = "en";
    public static String ENGLISH_REGEX = "^[a-zA-Z0-9!@#$%^&*()_+{}\\[\\]:;\"'<>,.?/~`\\s-]*$";
    public static char rupeesSymbol = '\u20B9';
    public static String CENTER_SHARE_CREDITED = "Center Share Credited";
    public static String STATE_SHARE_CREDITED = "State Share Credited";
    public static Double dLatitude = 0.0, dLongitude = 0.0,dAccuracy=0.0;
    public static ProgressDialog volleyDialog;
    public static Dialog loading;
    public static void startVolleyDialog(Context context) {
        if ((volleyDialog == null) || ((volleyDialog != null) && (!volleyDialog.isShowing())))
            volleyDialog = new ProgressDialog(context);
        if (!volleyDialog.isShowing()) {
            volleyDialog.setMessage(context.getString(R.string.please_wait));
            volleyDialog.setCancelable(false);
            volleyDialog.setCanceledOnTouchOutside(false);
            volleyDialog.show();
        }
    }

    public static void dismissVolleyDialog() {
        if (volleyDialog != null && volleyDialog.isShowing()) {
            volleyDialog.dismiss();
        }
    }
    public static  boolean isNightMode(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
    /**
     * This method is invoked to display loading indicator.
     */
    public static void  showLoading(Context context) {

        if ((loading == null) || ((loading != null) && (!loading.isShowing())))
            loading = new Dialog(context);
        if (!loading.isShowing()) {
            loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loading.setContentView(R.layout.layout_loading_indicator);
            loading.setCancelable(false);
            loading.setCanceledOnTouchOutside(false);
            loading.show();
        }
    }

    /**
     * This method is invoked to hide loading indicator.
     */
    public static void hideLoading() {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
         //  loading.cancel();
           // loading.dismiss();
          loading = null;
        }
    }

    public static String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}
