package nic.hp.ccmgnrega.model;

public class WorkerModel {
    String applicantId="",applicantName="";


    public WorkerModel( String applicantName) {
        this.applicantName = applicantName;
    }

    public WorkerModel(String applicantId, String applicantName) {
        this.applicantId = applicantId;
        this.applicantName = applicantName;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }
}
