package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;

public class UploadEkycExemptionModel {

    @SerializedName("muster_rollno")
    String muster_rollno;
    @SerializedName("entry_by")
    String entry_by;
    @SerializedName("state_code")
    String state_code;
    @SerializedName("panchayat_code")
    String panchayat_code;

    @SerializedName("exemption_reason")
    String exemption_reason;

    @SerializedName("work_code")
    String work_code;

    @SerializedName("DT_from")
    String DT_from;
    @SerializedName("DT_To")
    String DT_To;

    @SerializedName("latitude")
    String latitude;

    @SerializedName("longitude")
    String longitude;

    @SerializedName("work_site_photo")
    String work_site_photo;
    @SerializedName("exemption_date")
    String exemption_date;
    @SerializedName("exempt_raise_by")
    String exempt_raise_by;
    @SerializedName("exemption_reason_detail")
    String exemption_reason_detail;

    @SerializedName("reg_no")
    String reg_no;
    @SerializedName("applicant_no")
    String applicant_no;

    public UploadEkycExemptionModel(String muster_rollno, String entry_by, String state_code, String panchayat_code, String exemption_reason, String work_code, String DT_from, String DT_To, String latitude, String longitude, String work_site_photo, String exemption_date, String exempt_raise_by, String exemption_reason_detail, String reg_no, String applicant_no) {
        this.muster_rollno = muster_rollno;
        this.entry_by = entry_by;
        this.state_code = state_code;
        this.panchayat_code = panchayat_code;
        this.exemption_reason = exemption_reason;
        this.work_code = work_code;
        this.DT_from = DT_from;
        this.DT_To = DT_To;
        this.latitude = latitude;
        this.longitude = longitude;
        this.work_site_photo = work_site_photo;
        this.exemption_date = exemption_date;
        this.exempt_raise_by = exempt_raise_by;
        this.exemption_reason_detail = exemption_reason_detail;
        this.reg_no = reg_no;
        this.applicant_no = applicant_no;
    }
}
