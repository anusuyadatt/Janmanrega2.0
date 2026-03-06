package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;

public class UploadWorkerQueryModel {
    @SerializedName("state_code")
    String stateCode;
    @SerializedName("district_code")
    String districtCode;
    @SerializedName("block_code")
    String blockCode;
    @SerializedName("panchayat_code")
    String panchayatCode;

    @SerializedName("applicant_no")
    String applicantNumber;

    @SerializedName("jobcardno")
    String jobCardNumber;

    @SerializedName("Complaint_Category")
    String complaintCat;
    @SerializedName("Complaint_Sub_Category")
    String complaintSubCat;

    @SerializedName("Complaint_Description")
    String complaintDesc;

    @SerializedName("Name")
    String name;

    @SerializedName("mob")
    String mobileNumber;

    public UploadWorkerQueryModel(String stateCode,String districtCode, String blockCode,String panchayatCode, String applicantNumber, String jobCardNumber, String complaintCat, String complaintSubCat, String complaintDesc, String name, String mobileNumber) {
        this.stateCode = stateCode;
        this.districtCode = districtCode;
        this.blockCode = blockCode;
        this.panchayatCode = panchayatCode;
        this.applicantNumber = applicantNumber;
        this.jobCardNumber = jobCardNumber;
        this.complaintCat = complaintCat;
        this.complaintSubCat = complaintSubCat;
        this.complaintDesc = complaintDesc;
        this.name = name;
        this.mobileNumber = mobileNumber;
    }



}
