package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;

public class DistrictModel {
    @SerializedName("DIstrict_Code")
     String districtCode;

    public DistrictModel(String districtName) {
        this.districtName = districtName;
    }

    public DistrictModel(String districtCode, String districtName, String districtNameLocal) {
        this.districtCode = districtCode;
        this.districtName = districtName;
        this.districtNameLocal = districtNameLocal;
    }

    @SerializedName("District_Name")
    String districtName;

    @SerializedName("DistrictName_Local")
    String districtNameLocal;



    @SerializedName("ErrorMessage")
    String errorMessage;

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictNameLocal() {
        return districtNameLocal;
    }

    public void setDistrictNameLocal(String districtNameLocal) {
        this.districtNameLocal = districtNameLocal;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
