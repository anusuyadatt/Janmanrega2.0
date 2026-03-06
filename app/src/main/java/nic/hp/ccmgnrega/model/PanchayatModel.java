package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;

public class PanchayatModel {
    @SerializedName("state_code")
     String stateCode;

    @SerializedName("district_code")
     String districtCode;

    @SerializedName("block_code")
     String blockCode;

    @SerializedName("Panchayat_Code")
     String panchayatCode;

    @SerializedName("Panchayat_Name")
    String panchayatName;

    @SerializedName("PanchaytName_Local")
    String panchayatLocal;

    @SerializedName("ErrorMessage")
    String errorMessage;

    public PanchayatModel(String panchayatName) {
        this.panchayatName = panchayatName; }

    public String getStateCode() {
        return stateCode;
    }
    public PanchayatModel(String stateCode, String districtCode, String blockCode, String panchayatCode, String panchayatName, String panchayatLocal, String errorMessage) {
        this.stateCode = stateCode;
        this.districtCode = districtCode;
        this.blockCode = blockCode;
        this.panchayatCode = panchayatCode;
        this.panchayatName = panchayatName;
        this.panchayatLocal = panchayatLocal;
        this.errorMessage = errorMessage;
    }  public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getPanchayatCode() {
        return panchayatCode;
    }

    public void setPanchayatCode(String panchayatCode) {
        this.panchayatCode = panchayatCode;
    }

    public String getPanchayatName() {
        return panchayatName;
    }

    public void setPanchayatName(String panchayatName) {
        this.panchayatName = panchayatName;
    }

    public String getPanchayatLocal() {
        return panchayatLocal;
    }

    public void setPanchayatLocal(String panchayatLocal) {
        this.panchayatLocal = panchayatLocal;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
