package nic.hp.ccmgnrega.common;

import java.util.Calendar;
import java.util.Date;

public class CurrentFinancialYear {
    private Date actual;
    private int month;
    private int year;
    private int cyear;

    public CurrentFinancialYear() {
        this.actual = new Date();
        this.init();
    }

    private void init() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.actual);
        this.month = cal.get(Calendar.MONTH);
        int advance = (this.month <= 3) ? -1 : 0;
        this.year = cal.get(Calendar.YEAR) + advance;
        this.cyear = year + 1;
    }

    public String getFinacialYear() {
        return year + "-" + cyear;
    }
}
