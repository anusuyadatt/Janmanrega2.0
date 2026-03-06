package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;

public class StateModel {
    @SerializedName("State_Code")
     String stateCode;

    @SerializedName("State_Name")
    String stateName;

    @SerializedName("State_Name_Local")
    String stateNameLocal;

    public StateModel(String stateName) {
        this.stateName = stateName;
    }

    public StateModel(String stateCode, String stateName, String stateNameLocal, String shortName) {
        this.stateCode = stateCode;
        this.stateName = stateName;
        this.stateNameLocal = stateNameLocal;
        this.shortName = shortName;
    }

    @SerializedName("Short_Name")
    String shortName;

    @SerializedName("ErrorMessage")
    String errorMessage;

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateNameLocal() {
        return stateNameLocal;
    }

    public void setStateNameLocal(String stateNameLocal) {
        this.stateNameLocal = stateNameLocal;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }



}
