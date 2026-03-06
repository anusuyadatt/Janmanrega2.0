package nic.hp.ccmgnrega.model;

public class AttendanceInfo {

    private String msrNo;
    private String date;
    private String attendance;

    public AttendanceInfo() {}

    public AttendanceInfo(String msrNo, String date, String attendance) {
        this.msrNo = msrNo;
        this.date = date;
        this.attendance = attendance;
    }

    public String getMsrNo() {
        return msrNo;
    }

    public void setMsrNo(String msrNo) {
        this.msrNo = msrNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }
}
