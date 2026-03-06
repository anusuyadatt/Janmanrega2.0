package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;

public class BlockModel {
    @SerializedName("state_code")
     String stateCode;

    @SerializedName("district_code")
     String districtCode;

    @SerializedName("block_code")
     String blockCode;

    @SerializedName("block_name")
    String blockName;

    @SerializedName("BlockName_Local")
    String blockNameLocal;

    public BlockModel(String blockName) {
        this.blockName = blockName;
    }

    @SerializedName("fin_year")
    String finYear;

    public BlockModel(String stateCode, String districtCode, String blockCode, String blockName, String blockNameLocal, String finYear, String errorMessage) {
        this.stateCode = stateCode;
        this.districtCode = districtCode;
        this.blockCode = blockCode;
        this.blockName = blockName;
        this.blockNameLocal = blockNameLocal;
        this.finYear = finYear;
        this.errorMessage = errorMessage;
    }

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

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getBlockNameLocal() {
        return blockNameLocal;
    }

    public void setBlockNameLocal(String blockNameLocal) {
        this.blockNameLocal = blockNameLocal;
    }

    public String getFinYear() {
        return finYear;
    }

    public void setFinYear(String finYear) {
        this.finYear = finYear;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
