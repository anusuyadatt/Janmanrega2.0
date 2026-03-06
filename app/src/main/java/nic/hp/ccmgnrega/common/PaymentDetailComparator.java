package nic.hp.ccmgnrega.common;

import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.model.PaymentInfo;

public class PaymentDetailComparator implements Comparator<PaymentInfo> {

    Context context;

    public PaymentDetailComparator(Context context) {
        this.context = context;
    }
    @Override
    public int compare(PaymentInfo o1, PaymentInfo o2) {
        if (o1.getCreditedDate().equalsIgnoreCase(context.getString(R.string.creditedDate))) {
            return -1;
        } else if (o2.getCreditedDate().equalsIgnoreCase(context.getString(R.string.creditedDate))) {
            return 1;
        }

        if (!o1.getPaymentStatus().equalsIgnoreCase(Constant.CENTER_SHARE_CREDITED)
            && !o1.getPaymentStatus().equalsIgnoreCase(Constant.STATE_SHARE_CREDITED)) {
            return -1;
        } else if (!o2.getPaymentStatus().equalsIgnoreCase(Constant.CENTER_SHARE_CREDITED)
                    && !o2.getPaymentStatus().equalsIgnoreCase(Constant.STATE_SHARE_CREDITED)) {
            return 1;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = dateFormat.parse(o1.getCreditedDate());
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date1);
            Date date2 = dateFormat.parse(o2.getCreditedDate());
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(date2);
            if (calendar1.before(calendar2)) {
                return 1;
            } else if (calendar1.after(calendar2)) {
                return -1;
            } else {
                return 0;
            }
        } catch (ParseException e) {
            Log.e("PaymentDetailComparator", "Error in parsing credited date");
            throw new RuntimeException(e);
        }
    }
}
