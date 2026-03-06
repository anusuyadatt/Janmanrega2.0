package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;

public class VillageModel {
    @SerializedName("state_code")
     String stateCode;

    @SerializedName("district_code")
     String districtCode;

    @SerializedName("block_code")
     String blockCode;

    @SerializedName("Panchayat_Code")
     String panchayatCode;


    @SerializedName("Village_Code")
     String villageCode;

    @SerializedName("Village_Name")
    String villageName;

    @SerializedName("VillageName_Local")
    String villageNameLocal;

    @SerializedName("ErrorMessage")
    String errorMessage;

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
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

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getVillageNameLocal() {
        return villageNameLocal;
    }

    public void setVillageNameLocal(String villageNameLocal) {
        this.villageNameLocal = villageNameLocal;
    }

    public VillageModel(String villageName) {
        this.villageName = villageName;
    }

    public VillageModel(String stateCode, String districtCode, String blockCode, String panchayatCode, String villageCode, String villageName, String villageNameLocal, String errorMessage) {
        this.stateCode = stateCode;
        this.districtCode = districtCode;
        this.blockCode = blockCode;
        this.panchayatCode = panchayatCode;
        this.villageCode = villageCode;
        this.villageName = villageName;
        this.villageNameLocal = villageNameLocal;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
