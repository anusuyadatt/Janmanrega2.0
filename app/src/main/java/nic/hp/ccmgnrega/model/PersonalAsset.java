package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;

public class PersonalAsset {

    @SerializedName("work_code")
    private String workCode;

    @SerializedName("workname")
    private String workname;

    @SerializedName("work_status")
    private String workStatus;

    @SerializedName("start_date")
    private String startDate;

    @SerializedName("completion_date")
    private String completionDate;

    @SerializedName("village_name")
    private String villageName;

    @SerializedName("gram_panchayat_name")
    private String gramPanchayatName;

    @SerializedName("sanctioned_amount")
    private String sanctionedAmount;

    @SerializedName("permissible_work")
    private String permissibleWork;

    @SerializedName("image_url")
    private String imageUrl;

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public String getWorkname() {
        return workname;
    }

    public void setWorkname(String workname) {
        this.workname = workname;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getGramPanchayatName() {
        return gramPanchayatName;
    }

    public void setGramPanchayatName(String gramPanchayatName) {
        this.gramPanchayatName = gramPanchayatName;
    }

    public String getSanctionedAmount() {
        return sanctionedAmount;
    }

    public void setSanctionedAmount(String sanctionedAmount) {
        this.sanctionedAmount = sanctionedAmount;
    }

    public String getPermissibleWork() {
        return permissibleWork;
    }

    public void setPermissibleWork(String permissibleWork) {
        this.permissibleWork = permissibleWork;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
