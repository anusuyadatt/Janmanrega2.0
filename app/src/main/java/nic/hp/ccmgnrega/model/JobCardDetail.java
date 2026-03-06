
package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;
public class JobCardDetail {

    @SerializedName("Aadhar_No")
    private String mAadhaarNo;
    @SerializedName("Age")
    private String mAge;
    @SerializedName("Applicant_ID")
    private String mApplicantID;
    @SerializedName("Applicant_name")
    private String mApplicantName;
    @SerializedName("Gender")
    private String mGender;
    @SerializedName("applicant_status")
    private String applicantStatus;

    public String getAadhaarNo() {
        return mAadhaarNo;
    }
    public String getAge() {
        return mAge;
    }
    public String getApplicantID() {
        return mApplicantID;
    }
    public String getApplicantName() {
        return mApplicantName;
    }
    public void setApplicantName(String mApplicantName) {
        this.mApplicantName = mApplicantName;
    }
    public String getGender() {
        return mGender;
    }
    public void setGender(String mGender) {
        this.mGender = mGender;
    }
    public String getApplicantStatus() {
        return applicantStatus;
    }
}
