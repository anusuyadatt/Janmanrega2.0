
package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;

public class ABPSEligibilityStatus {

    @SerializedName("ABPS_Enabled_in_NREGASoft")
    private String mABPSEnabledInNREGASoft;
    @SerializedName("Aadhar_Seeded")
    private String mAadhaarSeeded;
    @SerializedName("Applicant_ID")
    private String mApplicantID;
    @SerializedName("Successfully_Authenticated")
    private String mSuccessfullyAuthenticated;

    public String getABPSEnabledInNREGASoft() {
        return mABPSEnabledInNREGASoft;
    }

    public String getAadhaarSeeded() {
        return mAadhaarSeeded;
    }

    public String getApplicantID() {
        return mApplicantID;
    }

    public String getSuccessfullyAuthenticated() {
        return mSuccessfullyAuthenticated;
    }

    public void setmABPSEnabledInNREGASoft(String mABPSEnabledInNREGASoft) {
        this.mABPSEnabledInNREGASoft = mABPSEnabledInNREGASoft;
    }

    public void setmAadhaarSeeded(String mAadhaarSeeded) {
        this.mAadhaarSeeded = mAadhaarSeeded;
    }

    public void setmSuccessfullyAuthenticated(String mSuccessfullyAuthenticated) {
        this.mSuccessfullyAuthenticated = mSuccessfullyAuthenticated;
    }
}
