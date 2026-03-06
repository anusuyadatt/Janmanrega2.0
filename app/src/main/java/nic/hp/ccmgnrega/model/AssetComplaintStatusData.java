package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;

public class AssetComplaintStatusData {

    @SerializedName("status")
    private String status;
    @SerializedName("work_name")
    private String work_name;
    @SerializedName("work_code")
    private String work_code;
    @SerializedName("Rating_provided")
    private String Rating_provided;
    @SerializedName("Asset_visible")
    private String Asset_visible;
    @SerializedName("work_complete")
    private String work_complete;
    @SerializedName("cib_exists")
    private String cib_exists;
    @SerializedName("Description_match")
    private String Description_match;
    @SerializedName("Asset_useful")
    private String Asset_useful;
    @SerializedName("complaint_raised_since")
    private String complaint_raised_since;
    @SerializedName("pending_at")
    private String pending_at;
    @SerializedName("pending_since")
    private String pending_since;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWork_name() {
        return work_name;
    }

    public void setWork_name(String work_name) {
        this.work_name = work_name;
    }

    public String getWork_code() {
        return work_code;
    }

    public void setWork_code(String work_code) {
        this.work_code = work_code;
    }

    public String getRating_provided() {
        return Rating_provided;
    }

    public void setRating_provided(String rating_provided) {
        Rating_provided = rating_provided;
    }

    public String getAsset_visible() {
        return Asset_visible;
    }

    public void setAsset_visible(String asset_visible) {
        Asset_visible = asset_visible;
    }

    public String getWork_complete() {
        return work_complete;
    }

    public void setWork_complete(String work_complete) {
        this.work_complete = work_complete;
    }

    public String getCib_exists() {
        return cib_exists;
    }

    public void setCib_exists(String cib_exists) {
        this.cib_exists = cib_exists;
    }

    public String getDescription_match() {
        return Description_match;
    }

    public void setDescription_match(String description_match) {
        Description_match = description_match;
    }

    public String getAsset_useful() {
        return Asset_useful;
    }

    public void setAsset_useful(String asset_useful) {
        Asset_useful = asset_useful;
    }

    public String getComplaint_raised_since() {
        return complaint_raised_since;
    }

    public void setComplaint_raised_since(String complaint_raised_since) {
        this.complaint_raised_since = complaint_raised_since;
    }

    public String getPending_at() {
        return pending_at;
    }

    public void setPending_at(String pending_at) {
        this.pending_at = pending_at;
    }

    public String getPending_since() {
        return pending_since;
    }

    public void setPending_since(String pending_since) {
        this.pending_since = pending_since;
    }

    public String getComplaint_id() {
        return complaint_id;
    }

    public void setComplaint_id(String complaint_id) {
        this.complaint_id = complaint_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("complaint_id")
    private String complaint_id;
    @SerializedName("message")
    private String message;


}
