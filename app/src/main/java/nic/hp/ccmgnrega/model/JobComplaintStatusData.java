package nic.hp.ccmgnrega.model;

import com.google.gson.annotations.SerializedName;

public class JobComplaintStatusData {
    @SerializedName("status")
    private String status;
    @SerializedName("jobcardno")
    private String jobcardno;
    @SerializedName("worker_name")
    private String worker_name;
    @SerializedName("complaint_category")
    private String complaint_category;
    @SerializedName("complaint_sub_category")
    private String complaint_sub_category;
    @SerializedName("complaint_description")
    private String complaint_description;
    @SerializedName("complaint_id")
    private String complaint_id;
    @SerializedName("complaint_raised_since")
    private String complaint_raised_since;
    @SerializedName("pending_at")
    private String pending_at;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJobcardno() {
        return jobcardno;
    }

    public void setJobcardno(String jobcardno) {
        this.jobcardno = jobcardno;
    }

    public String getWorker_name() {
        return worker_name;
    }

    public void setWorker_name(String worker_name) {
        this.worker_name = worker_name;
    }

    public String getComplaint_category() {
        return complaint_category;
    }

    public void setComplaint_category(String complaint_category) {
        this.complaint_category = complaint_category;
    }

    public String getComplaint_sub_category() {
        return complaint_sub_category;
    }

    public void setComplaint_sub_category(String complaint_sub_category) {
        this.complaint_sub_category = complaint_sub_category;
    }

    public String getComplaint_description() {
        return complaint_description;
    }

    public void setComplaint_description(String complaint_description) {
        this.complaint_description = complaint_description;
    }

    public String getComplaint_id() {
        return complaint_id;
    }

    public void setComplaint_id(String complaint_id) {
        this.complaint_id = complaint_id;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("pending_since")
    private String pending_since;
    @SerializedName("message")
    private String message;


}
