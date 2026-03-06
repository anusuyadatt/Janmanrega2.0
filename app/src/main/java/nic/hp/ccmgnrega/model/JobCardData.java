
package nic.hp.ccmgnrega.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class JobCardData {

    private String jobCardId;
    @SerializedName("state_code")
    private String state_code;
    @SerializedName("District_code")
    private String District_code;
    @SerializedName("Block_code")
    private String Block_code;
    @SerializedName("Panchayat_code")
    private String Panchayat_code;
    @SerializedName("Village_code")
    private String Village_code;
    @SerializedName("ABPS_Eligibility_Status")
    private List<ABPSEligibilityStatus> mABPSEligibilityStatus;
    @SerializedName("attendance_details")
    private List<Attendance> mAttendance;
    @SerializedName("Block")
    private String mBlock;
    @SerializedName("Category")
    private String mCategory;
    @SerializedName("Date_of_Registration")
    private String mDateOfRegistration;
    @SerializedName("District")
    private String mDistrict;
    @SerializedName("state")
    private String mState;
    @SerializedName("Family_ID")
    private String mFamilyID;
    @SerializedName("Job_Card_Details")
    private List<JobCardDetail> mJobCardDetails;
    @SerializedName("Name_of_Father_Husband")
    private String mNameOfFatherHusband;
    @SerializedName("Name_of_Head_of_Household")
    private String mNameOfHeadOfHousehold;
    @SerializedName("days_worked")
    private String mDaysWorked;
    @SerializedName("Panchayat")
    private String mPanchayat;
    @SerializedName("Image")
    private String mImage;
    @SerializedName("payment")
    private List<Payment> mPayment;
    @SerializedName("Total_Applicants")
    private String mTotalApplicantsInJobCard;
    @SerializedName("Village")
    private String mVillage;
    @SerializedName("BPL_Family")
    private String mBPLFamily;

    @SerializedName("status")
    private String status;

    @SerializedName("Remarks")
    private String remarks;

    @SerializedName("Deletion_Date")
    private String deletionDate;

    @SerializedName("Deletion_Reason")
    private String deletionReason;

    @SerializedName("personal_assets")
    private List<PersonalAsset> mPersonalAssets;

    public List<ABPSEligibilityStatus> getABPSEligibilityStatus() {
        return mABPSEligibilityStatus;
    }

    public List<Attendance> getAttendance() {
        return mAttendance;
    }

    public String getBlock() {
        return mBlock;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getDateOfRegistration() {
        return mDateOfRegistration;
    }

    public String getDistrict() {
        return mDistrict;
    }

    public String getFamilyID() {
        return mFamilyID;
    }

    public List<JobCardDetail> getJobCardDetails() {
        return mJobCardDetails;
    }

    public String getNameOfFatherHusband() {
        return mNameOfFatherHusband;
    }

    public String getNameOfHeadOfHousehold() {
        return mNameOfHeadOfHousehold;
    }

    public String getDaysWorked() {
        return mDaysWorked;
    }

    public String getPanchayat() {
        return mPanchayat;
    }

    public String getImage() {
        return mImage;
    }

    public List<Payment> getPayment() {
        return mPayment;
    }

    public String getTotalApplicantsInJobCard() {
        return mTotalApplicantsInJobCard;
    }

    public String getVillage() {
        return mVillage;
    }

    public String getBPLFamily() {
        return mBPLFamily;
    }

    public String getStatus() {
        return status;
    }

    public String getDeletionDate() {
        return deletionDate;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public String getDistrict_code() {
        return District_code;
    }

    public void setDistrict_code(String district_code) {
        District_code = district_code;
    }

    public String getBlock_code() {
        return Block_code;
    }

    public void setBlock_code(String block_code) {
        Block_code = block_code;
    }

    public String getPanchayat_code() {
        return Panchayat_code;
    }

    public void setPanchayat_code(String panchayat_code) {
        Panchayat_code = panchayat_code;
    }

    public String getVillage_code() {
        return Village_code;
    }

    public void setVillage_code(String village_code) {
        Village_code = village_code;
    }

    public String getDeletionReason() {
        return deletionReason;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getJobCardId() {
        return jobCardId;
    }

    public void setJobCardId(String jobCardId) {
        this.jobCardId = jobCardId;
    }

    public String getState() {
        return mState;
    }

    public List<PersonalAsset> getPersonalAssets() {
        return mPersonalAssets;
    }
}
