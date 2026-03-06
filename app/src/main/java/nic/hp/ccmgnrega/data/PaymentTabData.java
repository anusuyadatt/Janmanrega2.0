package nic.hp.ccmgnrega.data;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.common.Constant;
import nic.hp.ccmgnrega.common.IntegerStringComparator;
import nic.hp.ccmgnrega.common.PaymentDetailComparator;
import nic.hp.ccmgnrega.databinding.FragmentPaymentBinding;
import nic.hp.ccmgnrega.model.JobCardData;
import nic.hp.ccmgnrega.model.Payment;
import nic.hp.ccmgnrega.model.PaymentInfo;

public class PaymentTabData {

    private static HashMap<String, Double> applicantToAmountCreditedMapping = new HashMap<>();
    public static HashMap<String, HashMap<String,List<PaymentInfo>>> paymentTabData = new HashMap<>();
    private static Comparator stringComparator = new IntegerStringComparator();
    private static Comparator paymentDetailComparator;
    private static Context context;
    private static FragmentPaymentBinding binding;
    private static JobCardData jobCardData;

    public static void populatePaymentTabData (FragmentPaymentBinding binding, Context context, JobCardData jobCardData) {
        PaymentTabData.context = context;
        PaymentTabData.binding = binding;
        PaymentTabData.jobCardData = jobCardData;
        clearData();
        setPageInformation();
        setPaymentTabData();
    }

    private static void setPageInformation() {
        if (jobCardData.getTotalApplicantsInJobCard() != null) {
            binding.totalApplicants.setText(context.getString(R.string.totalApplicants) + ": " + jobCardData.getTotalApplicantsInJobCard());
        }
        binding.clickInfoText.setText("*" + context.getString(R.string.toViewDetailsClickOn) + " ");
        binding.finYearInfo.setText(context.getString(R.string.finYearPaymentInfo));
    }
    private static void setPaymentTabData() {
        List<Payment> paymentList = jobCardData.getPayment();
        if (paymentList == null || paymentList.isEmpty()) {
            addNoPaymentApplicants();
            sortPayments();
            return;
        }

        for (Payment payment : paymentList) {
            if (payment.getApplicantId() == null || payment.getApplicantId().isEmpty()) {
                continue;
            }

            HashMap<String,List<PaymentInfo>> workCodeToPaymentMapping;
            List<PaymentInfo> paymentDetailList;
            processAmountCredited(payment);
            if (payment.getWorkcode() == null || payment.getWorkcode().isEmpty()) {
                workCodeToPaymentMapping = new HashMap<>();
                paymentDetailList = new ArrayList<>();
            }
            else {
                if (!paymentTabData.containsKey(payment.getApplicantId())) {
                    workCodeToPaymentMapping = new HashMap<>();
                    paymentDetailList = new ArrayList<>();
                } else {
                    workCodeToPaymentMapping = paymentTabData.get(payment.getApplicantId());
                    if (!workCodeToPaymentMapping.containsKey(payment.getWorkcode())) {
                        paymentDetailList = new ArrayList<>();
                    } else {
                        paymentDetailList = workCodeToPaymentMapping.get(payment.getWorkcode());
                    }
                }

                if (payment.getMusterrollno() != null && !payment.getMusterrollno().isEmpty()
                    && payment.getNoOfDaysWorked() != null
                    && payment.getTotalWageInRs() != null
                    && payment.getBankPostOfficeName() != null
                    && payment.getPaymentStatus() != null
                    && payment.getAccountNo() != null)
                {
                    PaymentInfo paymentInfo = new PaymentInfo();
                    paymentInfo.setMsrNo(payment.getMusterrollno());
                    paymentInfo.setDaysWorked(payment.getNoOfDaysWorked());
                    paymentInfo.setTotalWage(payment.getTotalWageInRs());
                    paymentInfo.setBankName(payment.getBankPostOfficeName());
                    paymentInfo.setAccountNumber(payment.getAccountNo());
                    if (payment.getPaymentStatus().equalsIgnoreCase(Constant.CENTER_SHARE_CREDITED)
                    || payment.getPaymentStatus().equalsIgnoreCase(Constant.STATE_SHARE_CREDITED)) {
                        paymentInfo.setCreditedDate(payment.getCreditedDate());
                    } else {
                        paymentInfo.setCreditedDate(payment.getPaymentStatus());
                    }
                    paymentInfo.setPaymentStatus(payment.getPaymentStatus());
                    paymentDetailList.add(paymentInfo);
                }
            }

            if (payment.getWorkcode() != null) {
                workCodeToPaymentMapping.put(payment.getWorkcode(), paymentDetailList);
                paymentTabData.put(payment.getApplicantId(), workCodeToPaymentMapping);
            }
        }
        addNoPaymentApplicants();
        sortPayments();
    }

    private static void sortPayments () {
        List<String> applicationIdList = new ArrayList<>(paymentTabData.keySet());
        if (applicationIdList == null) {
            return;
        }
        Collections.sort(applicationIdList, stringComparator);

        for (String applicantId : applicationIdList) {
            if (JobCardDataAccess.paymentApplicantIdToWorkCodeMapping.get(applicantId) != null) {
                List<String> workCodeList = new ArrayList<>(JobCardDataAccess.paymentApplicantIdToWorkCodeMapping.get(applicantId));

                for (String workCode : workCodeList) {
                    HashMap<String,List<PaymentInfo>> workCodeToPaymentMapping = paymentTabData.get(applicantId);
                    List<PaymentInfo> paymentInfoList = workCodeToPaymentMapping.get(workCode);
                    if (paymentInfoList != null) {
                        if (paymentInfoList.size() > 0) {
                            String msrNo = context.getString(R.string.msrNo);
                            String daysWorked = context.getString(R.string.daysWorked);
                            String totalWage = context.getString(R.string.totalWage);
                            String bankPostOffice = context.getString(R.string.bankPostOffice);
                            String creditedDate = context.getString(R.string.creditedDate);
                            String paymentStatus = context.getString(R.string.paymentStatus);
                            String accountNumber = context.getString(R.string.accountNumber);

                            paymentInfoList.add(0, new PaymentInfo(msrNo, daysWorked, totalWage, bankPostOffice, accountNumber, creditedDate, paymentStatus));
                        }
                        paymentDetailComparator = new PaymentDetailComparator(context);
                        Collections.sort(paymentInfoList, paymentDetailComparator);
                    }
                }
            }
        }
    }

    private static void addNoPaymentApplicants() {
        Set<String> applicationIdList = JobCardDataAccess.applicantIdSet;
        for (String applicantId : applicationIdList) {
            if (!paymentTabData.containsKey(applicantId)) {
                HashMap<String,List<PaymentInfo>> workCodeToPaymentMapping = new HashMap<>();
                paymentTabData.put(applicantId, workCodeToPaymentMapping);
                applicantToAmountCreditedMapping.put(applicantId, 0.0);
            }
        }
    }

    public static Pair<String, String> getFirstLevelApplicantDetail(String applicantId) {
        String applicantName = JobCardDataAccess.applicantIdToJobCardDetailMapping.get(applicantId).getApplicantName();
        Double amountCredited = applicantToAmountCreditedMapping.get(applicantId);
        DecimalFormat format = new DecimalFormat("0.#");
        String amountCreditedStatement = context.getString(R.string.creditedSince) + ": " + Constant.rupeesSymbol + format.format(amountCredited);
        Pair<String, String> applicantDetail = new Pair<>(applicantName, amountCreditedStatement);
        return applicantDetail;
    }

    private static void processAmountCredited (Payment payment) {
        Double amountCredited;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (payment.getPaymentStatus() != null && !payment.getPaymentStatus().isEmpty()
            && (payment.getPaymentStatus().equalsIgnoreCase(Constant.CENTER_SHARE_CREDITED)
                || payment.getPaymentStatus().equalsIgnoreCase(Constant.STATE_SHARE_CREDITED))
            && payment.getCreditedDate() != null && !payment.getCreditedDate().isEmpty()) {
            try {
                Date creditedDate = dateFormat.parse(payment.getCreditedDate());
                Calendar creditedCalendar = Calendar.getInstance();
                creditedCalendar.setTime(creditedDate);
                Calendar financialYearStartCalendar = JobCardDataAccess.getFinancialYearStartDate();

                if (payment.getTotalWageInRs() != null
                    && !payment.getTotalWageInRs().isEmpty()
                    && !creditedCalendar.before(financialYearStartCalendar)) {
                    try {
                        amountCredited = Double.valueOf(payment.getTotalWageInRs());
                    } catch (NumberFormatException e) {
                        Log.e("PaymentTabData", "Payment amount could not be parsed: " + e.getMessage());
                        amountCredited = 0.0;
                    }
                } else {
                    amountCredited = 0.0;
                }
            }
            catch (ParseException e) {
                Log.e("PaymentTabData", "Payment credited date could not be parsed: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            amountCredited = 0.0;
        }
        Double totalAmountCredited = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            totalAmountCredited = amountCredited + applicantToAmountCreditedMapping.getOrDefault(payment.getApplicantId(), 0.0);
        }

        applicantToAmountCreditedMapping.put(payment.getApplicantId(), totalAmountCredited);
    }

    public static void clearData() {
        applicantToAmountCreditedMapping.clear();
        paymentTabData.clear();
    }
}
