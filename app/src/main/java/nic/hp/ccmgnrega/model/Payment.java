
package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;
public class Payment {

    @SerializedName("Account_no")
    private String mAccountNo;
    @SerializedName("Applicant_ID")
    private String mApplicantId;
    @SerializedName("Bank_or_Post_Office_Name")
    private String mBankPostOfficeName;
    @SerializedName("Credited_Date")
    private String mCreditedDate;
    @SerializedName("MSR_Date_From")
    private String mMSRDateFrom;
    @SerializedName("MSR_Date_To")
    private String mMSRDateTo;
    @SerializedName("musterrollno")
    private String mMusterrollno;
    @SerializedName("No_of_days_worked")
    private String mNoOfDaysWorked;
    @SerializedName("payment_status")
    private String mPaymentStatus;
    @SerializedName("Total_wage_in_Rs")
    private String mTotalWageInRs;
    @SerializedName("workcode")
    private String mWorkcode;
    @SerializedName("workname")
    private String mWorkname;

    public String getAccountNo() {
        return mAccountNo;
    }

    public String getApplicantId() {
        return mApplicantId;
    }

    public String getBankPostOfficeName() {
        return mBankPostOfficeName;
    }

    public void setmBankPostOfficeName(String mBankPostOfficeName) {
        this.mBankPostOfficeName = mBankPostOfficeName;
    }

    public String getCreditedDate() {
        return mCreditedDate;
    }

    public String getMSRDateFrom() {
        return mMSRDateFrom;
    }

    public String getMSRDateTo() {
        return mMSRDateTo;
    }

    public String getMusterrollno() {
        return mMusterrollno;
    }

    public String getNoOfDaysWorked() {
        return mNoOfDaysWorked;
    }

    public String getPaymentStatus() {
        return mPaymentStatus;
    }

    public void setmPaymentStatus(String mPaymentStatus) {
        this.mPaymentStatus = mPaymentStatus;
    }

    public String getTotalWageInRs() {
        return mTotalWageInRs;
    }

    public String getWorkcode() {
        return mWorkcode;
    }

    public String getWorkname() {
        return mWorkname;
    }

}
