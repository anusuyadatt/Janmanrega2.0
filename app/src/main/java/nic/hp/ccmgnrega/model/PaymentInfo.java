package nic.hp.ccmgnrega.model;

public class PaymentInfo {

    private String msrNo;
    private String daysWorked;
    private String totalWage;
    private String bankName;
    private String accountNumber;
    private String creditedDate;
    private String paymentStatus;

    public PaymentInfo() {}

    public PaymentInfo(String msrNo, String daysWorked, String totalWage, String bankName, String accountNumber, String creditedDate, String paymentStatus) {
        this.msrNo = msrNo;
        this.daysWorked = daysWorked;
        this.totalWage = totalWage;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.creditedDate = creditedDate;
        this.paymentStatus = paymentStatus;
    }

    public String getMsrNo() {
        return msrNo;
    }

    public void setMsrNo(String msrNo) {
        this.msrNo = msrNo;
    }

    public String getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(String daysWorked) {
        this.daysWorked = daysWorked;
    }

    public String getTotalWage() {
        return totalWage;
    }

    public void setTotalWage(String totalWage) {
        this.totalWage = totalWage;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCreditedDate() {
        return creditedDate;
    }

    public void setCreditedDate(String creditedDate) {
        this.creditedDate = creditedDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
