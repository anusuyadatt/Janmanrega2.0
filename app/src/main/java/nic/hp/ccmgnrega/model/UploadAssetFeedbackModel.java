package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;

public class UploadAssetFeedbackModel {
    @SerializedName("MobileNo")
    String MobileNo;
    @SerializedName("AssetID")
    String AssetID;
    @SerializedName("WorkCode")
    String WorkCode;
    @SerializedName("Longitude")
    String Longitude;

    @SerializedName("Latitude")
    String Latitude;

    @SerializedName("RatingScore")
    String RatingScore;

    @SerializedName("ISCIBExist")
    String ISCIBExist;
    @SerializedName("IsCompleted")
    String IsCompleted;

    @SerializedName("DiscriptionMatch")
    String DiscriptionMatch;

    @SerializedName("IsUseFull")
    String IsUseFull;

    @SerializedName("UploadImage")
    String UploadImage;

    public UploadAssetFeedbackModel(String MobileNo, String AssetID, String WorkCode, String Longitude, String Latitude, String RatingScore, String ISCIBExist, String IsCompleted, String DiscriptionMatch, String IsUseFull, String UploadImage) {
        this.MobileNo = MobileNo;
        this.AssetID = AssetID;
        this.WorkCode = WorkCode;
        this.Longitude = Longitude;
        this.Latitude = Latitude;
        this.RatingScore = RatingScore;
        this.ISCIBExist = ISCIBExist;
        this.IsCompleted = IsCompleted;
        this.DiscriptionMatch = DiscriptionMatch;
        this.IsUseFull = IsUseFull;
        this.UploadImage = UploadImage;
    }



}
