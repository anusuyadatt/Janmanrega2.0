package nic.hp.ccmgnrega.common;

import android.content.Context;
import android.util.Log;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.model.AttendanceInfo;

public class AttendanceDetailComparator implements Comparator<AttendanceInfo> {

    Context context;

    public AttendanceDetailComparator(Context context) {
        this.context = context;
    }
    @Override
    public int compare(AttendanceInfo o1, AttendanceInfo o2) {
        if (o1.getDate().equalsIgnoreCase(context.getString(R.string.date))) {
            return -1;
        } else if (o2.getDate().equalsIgnoreCase(context.getString(R.string.date))) {
            return 1;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = dateFormat.parse(o1.getDate());
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date1);
            Date date2 = dateFormat.parse(o2.getDate());
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
            Log.e("AttendanceDetailComparator", "Error in parsing date");
            throw new RuntimeException(e);
        }
    }
}
