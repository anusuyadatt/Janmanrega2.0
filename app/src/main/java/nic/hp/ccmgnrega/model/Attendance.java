
package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Attendance {

    @SerializedName("Applicant_ID")
    private String mApplicantId;
    @SerializedName("MSR_Date_From")
    private String mMsrDateFrom;
    @SerializedName("MSR_Date_To")
    private String mMsrDateTo;
    @SerializedName("musterrollno")
    private String mMusterrollno;
    @SerializedName("workcode")
    private String mWorkcode;
    @SerializedName("workname")
    private String mWorkname;
    @SerializedName("A1")
    private String day1;
    @SerializedName("A2")
    private String day2;
    @SerializedName("A3")
    private String day3;
    @SerializedName("A4")
    private String day4;
    @SerializedName("A5")
    private String day5;
    @SerializedName("A6")
    private String day6;
    @SerializedName("A7")
    private String day7;
    @SerializedName("A8")
    private String day8;
    @SerializedName("A9")
    private String day9;
    @SerializedName("A10")
    private String day10;
    @SerializedName("A11")
    private String day11;
    @SerializedName("A12")
    private String day12;
    @SerializedName("A13")
    private String day13;
    @SerializedName("A14")
    private String day14;
    @SerializedName("A15")
    private String day15;
    @SerializedName("A16")
    private String day16;

    public String getApplicantId() {
        return mApplicantId;
    }

    public String getMusterrollno() {
        return mMusterrollno;
    }
    public String getWorkcode() {
        return mWorkcode;
    }

    public String getWorkname() {
        return mWorkname;
    }

    public String getmMsrDateFrom() {
        return mMsrDateFrom;
    }

    public String getmMsrDateTo() {
        return mMsrDateTo;
    }

    public String getDay1() {
        return day1;
    }

    public String getDay2() {
        return day2;
    }

    public String getDay3() {
        return day3;
    }

    public String getDay4() {
        return day4;
    }

    public String getDay5() {
        return day5;
    }

    public String getDay6() {
        return day6;
    }

    public String getDay7() {
        return day7;
    }

    public String getDay8() {
        return day8;
    }

    public String getDay9() {
        return day9;
    }

    public String getDay10() {
        return day10;
    }

    public String getDay11() {
        return day11;
    }

    public String getDay12() {
        return day12;
    }

    public String getDay13() {
        return day13;
    }

    public String getDay14() {
        return day14;
    }

    public String getDay15() {
        return day15;
    }

    public String getDay16() {
        return day16;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return mApplicantId.equals(that.mApplicantId) && mMsrDateFrom.equals(that.mMsrDateFrom) && mMsrDateTo.equals(that.mMsrDateTo) && mMusterrollno.equals(that.mMusterrollno) && mWorkcode.equals(that.mWorkcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mApplicantId, mMsrDateFrom, mMsrDateTo, mMusterrollno, mWorkcode);
    }
}
