package nic.hp.ccmgnrega.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.common.MySharedPref;
import nic.hp.ccmgnrega.common.ResponseCallback;
import nic.hp.ccmgnrega.common.Constant;
import nic.hp.ccmgnrega.common.TaskCompletionListener;
import nic.hp.ccmgnrega.common.Utility;
import nic.hp.ccmgnrega.model.Attendance;
import nic.hp.ccmgnrega.model.JobCardData;
import nic.hp.ccmgnrega.model.JobCardDetail;
import nic.hp.ccmgnrega.model.Payment;
import nic.hp.ccmgnrega.model.PersonalAsset;

public class JobCardDataAccess {
    public static HashMap<String, JobCardDetail> applicantIdToJobCardDetailMapping = new HashMap<>();
    public static Set<String> applicantIdSet = new HashSet<>();
    public static Set<String> attendanceApplicantIdSet = new HashSet<>();
    public static Set<String> paymentApplicantIdSet = new HashSet<>();
    public static HashMap<String, Set<String>> attendanceApplicantIdToWorkCodeMapping = new HashMap<>();
    public static HashMap<String, Set<String>> paymentApplicantIdToWorkCodeMapping = new HashMap<>();
    public static List<Pair<String, String>> jobCardTabLabelValueList = new ArrayList<>(9);
    public static HashMap<String, String> attendanceWorkCodeToWorkNameMapping = new HashMap<>();
    public static HashMap<String, String> paymentWorkCodeToWorkNameMapping = new HashMap<>();
    public static HashMap<String, String> personalAssetsWorkCodeToWorkNameMapping = new HashMap<>();
    private static HashMap<String, String> bankNameTranslations = new HashMap<>();
    private static HashMap<String, String> paymentStatusTranslations = new HashMap<>();
    public static int totalDaysWorked = 0;

    public static HashMap<String, Bitmap> workCodeToPersonalAssetImageMapping = new HashMap<>();
    private static String regionalLanguageCode = null;
    private static String targetLanguage = null;
    private static String userSelectedLanguageCode = null;
    private static int noOfJobCardTabAsyncCalls = 0;
    private static int noOfAttendanceTabAsyncCalls = 0;
    private static int noOfPaymentTabAsyncCalls = 0;
    private static int noOfPerosnalAssetsTabAsyncCalls = 0;
    public static CountDownLatch jobCardTabLatch;
    public static CountDownLatch attendanceTabLatch;
    public static CountDownLatch paymentTabLatch;
    public static CountDownLatch personalAssetsTabLatch;
    public static CountDownLatch personalAssetsImageTabLatch;

    private static boolean latchAborted = false;
    private static TaskCompletionListener jobCardTabTaskCompletionListener;
    private static Context context;
    private static JobCardData jobCardData;

    public static void setJobCardTabTaskCompletionListener(TaskCompletionListener jobCardTabTaskCompletionListener) {
        JobCardDataAccess.jobCardTabTaskCompletionListener = jobCardTabTaskCompletionListener;
    }

    public static void extractData(JobCardData jobCardData, Context context) {
        clearData();
        JobCardDataAccess.context = context;
        JobCardDataAccess.jobCardData = jobCardData;
        extractDataWithoutTranslating();

        String userSelectedLanguage = MySharedPref.getAppLangCode(context);
        userSelectedLanguageCode = Utility.getBhashiniLanguageCode(userSelectedLanguage);
        String stateCode = JobCardDataAccess.getJobCardId().substring(0,2).toUpperCase();
        regionalLanguageCode = Utility.getLanguageFromState(stateCode);

        if (Utility.translationAllowed(context)) {
            String sourceLanguage;
            if (userSelectedLanguageCode.equalsIgnoreCase(Constant.ENGLISH_LANGUAGE_CODE)) {
                sourceLanguage = regionalLanguageCode;
            } else {
                sourceLanguage = Constant.ENGLISH_LANGUAGE_CODE;
            }
            targetLanguage = userSelectedLanguageCode;

            noOfJobCardTabAsyncCalls = jobCardData.getJobCardDetails().size() + 7;
            jobCardTabLatch = new CountDownLatch(noOfJobCardTabAsyncCalls);

            if (attendanceWorkCodeToWorkNameMapping != null && attendanceWorkCodeToWorkNameMapping.keySet() != null) {
                noOfAttendanceTabAsyncCalls += attendanceWorkCodeToWorkNameMapping.keySet().size();
            }
            attendanceTabLatch = new CountDownLatch(noOfAttendanceTabAsyncCalls);

            if (paymentWorkCodeToWorkNameMapping != null && paymentWorkCodeToWorkNameMapping.keySet() != null) {
                noOfPaymentTabAsyncCalls += paymentWorkCodeToWorkNameMapping.keySet().size();
            }
            if (bankNameTranslations != null && bankNameTranslations.keySet() != null) {
                noOfPaymentTabAsyncCalls += bankNameTranslations.keySet().size();
            }
            if (paymentStatusTranslations != null && paymentStatusTranslations.keySet() != null) {
                noOfPaymentTabAsyncCalls += paymentStatusTranslations.keySet().size();
            }
            paymentTabLatch = new CountDownLatch(noOfPaymentTabAsyncCalls);

            if (personalAssetsWorkCodeToWorkNameMapping != null && personalAssetsWorkCodeToWorkNameMapping.keySet() != null) {
                noOfPaymentTabAsyncCalls += personalAssetsWorkCodeToWorkNameMapping.keySet().size();
            }
            noOfPerosnalAssetsTabAsyncCalls += jobCardData.getPersonalAssets().size()*3;
            personalAssetsTabLatch = new CountDownLatch(noOfPerosnalAssetsTabAsyncCalls);

            extractAndTranslate(sourceLanguage);
        }
    }

    public static String getJobCardId() {
        if (jobCardData != null && jobCardData.getJobCardId() != null && !jobCardData.getJobCardId().isEmpty()) {
            return jobCardData.getJobCardId();
        }
        return null;
    }
    private static void extractDataWithoutTranslating () {
        setApplicantIdToJobCardDetailMapping();
        setWorkMappingsViaAttendance();
        setWorkMappingsViaPayment();
        setJobCardTabLabelValueList();
        setWorkCodeToPersonalAssetImageMapping();
        setWorkCodeToWorkNameMappingForPersonalAssets();
    }

    private static void setWorkCodeToWorkNameMappingForPersonalAssets() {
        for (PersonalAsset personalAsset : jobCardData.getPersonalAssets()) {
            if (personalAsset.getWorkCode() != null && !personalAsset.getWorkCode().isEmpty()) {
                personalAssetsWorkCodeToWorkNameMapping.put(personalAsset.getWorkCode(), personalAsset.getWorkname());
            }
        }
    }
    private static void extractAndTranslate(String sourceLanguage) {
        Utility.makeConfigCall(sourceLanguage, targetLanguage, new ResponseCallback() {
            @Override
            public void onResponse(String translationServiceId) {
                setApplicantIdToJobCardDetailMappingWithTranslation(translationServiceId);
                setJobCardTabLabelValueListWithTranslation(translationServiceId);
                translateWorkCodeToWorkNameMapping(translationServiceId);
                translateBankNameAndPaymentStatus(translationServiceId);
                translatePersonalAsset(translationServiceId);
            }

            @Override
            public void onFailure() {
                latchAborted = true;
                while(jobCardTabLatch.getCount() != 0) {
                    jobCardTabLatch.countDown();
                }
                while(attendanceTabLatch.getCount() != 0) {
                    attendanceTabLatch.countDown();
                }
                while(paymentTabLatch.getCount() != 0) {
                    paymentTabLatch.countDown();
                }
            }
        });
    }
    private static void setApplicantIdToJobCardDetailMapping() {
        List<JobCardDetail> jobCardDetailList = jobCardData.getJobCardDetails();
        for (JobCardDetail jobCardDetail : jobCardDetailList) {
            if (jobCardDetail.getApplicantID() != null
                    && !jobCardDetail.getApplicantID().isEmpty()) {
                applicantIdToJobCardDetailMapping.put(jobCardDetail.getApplicantID(), jobCardDetail);
            }
        }
        applicantIdSet = applicantIdToJobCardDetailMapping.keySet();
    }

    private static void setApplicantIdToJobCardDetailMappingWithTranslation(String translationServiceId) {
        List<JobCardDetail> jobCardDetailList = jobCardData.getJobCardDetails();
        for (JobCardDetail jobCardDetail : jobCardDetailList) {
            if (jobCardDetail.getApplicantID() != null && !jobCardDetail.getApplicantID().isEmpty()) {
                translateAndSetMapping(jobCardDetail, translationServiceId);
            } else {
                jobCardTabLatch.countDown();
            }
        }
        applicantIdSet = applicantIdToJobCardDetailMapping.keySet();
    }

    private static void translateAndSetMapping(JobCardDetail jobCardDetail, String translationServiceId) {
        String applicantName = jobCardDetail.getApplicantName();
        String sourceLanguage = getSourceLanguageIfTranslationNeeded(applicantName);
        String gender = jobCardDetail.getGender();
        if (gender != null && gender.equalsIgnoreCase("M")) {
            jobCardDetail.setGender(context.getString(R.string.male));
        } else if (gender != null && gender.equalsIgnoreCase("F")) {
            jobCardDetail.setGender(context.getString(R.string.female));
        }
        if (sourceLanguage != null) {
            applicantName = "'" + applicantName + "'";
            Utility.makeComputeCall(sourceLanguage, targetLanguage, translationServiceId, applicantName, new ResponseCallback() {
                @Override
                public void onResponse(String translatedString) {
                    jobCardDetail.setApplicantName(Utility.removeQuotations(translatedString));
                    applicantIdToJobCardDetailMapping.put(jobCardDetail.getApplicantID(), jobCardDetail);
                    jobCardTabLatch.countDown();
                    if (jobCardTabTaskCompletionListener != null && !latchAborted) {
                        new Handler(Looper.getMainLooper()).post(() -> jobCardTabTaskCompletionListener.onAllTasksCompleted());
                    }
                }

                @Override
                public void onFailure() {
                    applicantIdToJobCardDetailMapping.put(jobCardDetail.getApplicantID(), jobCardDetail);
                    jobCardTabLatch.countDown();
                }
            });
        } else {
            applicantIdToJobCardDetailMapping.put(jobCardDetail.getApplicantID(), jobCardDetail);
            jobCardTabLatch.countDown();
        }
    }

    private static String getSourceLanguageIfTranslationNeeded (String speech) {
        String sourceLanguage = null;
        if (speech != null) {
            if (isTextInEnglish(speech) && !targetLanguage.equalsIgnoreCase(Constant.ENGLISH_LANGUAGE_CODE)) {
                sourceLanguage = Constant.ENGLISH_LANGUAGE_CODE;
            }
            if (!isTextInEnglish(speech) && targetLanguage.equalsIgnoreCase(Constant.ENGLISH_LANGUAGE_CODE)) {
                sourceLanguage = regionalLanguageCode;
            }
        }
        return sourceLanguage;
    }
    private static boolean isTextInEnglish(String text) {
        boolean isEnglish = text.matches(Constant.ENGLISH_REGEX);
        return isEnglish;
    }

    private static void setWorkMappingsViaAttendance() {
        List<Attendance> attendanceList = AttendanceTabData.removeDuplicates(jobCardData.getAttendance());
        for (Attendance attendance : attendanceList) {
            //calculate totalDaysWorked
            totalDaysWorked += getDaysWorked(attendance);
            if (attendance.getApplicantId() != null && !attendance.getApplicantId().isEmpty()) {
                //updating applicantIdList
                attendanceApplicantIdSet.add(attendance.getApplicantId());
                if (attendance.getWorkcode() != null && !attendance.getWorkcode().isEmpty()) {
                    //updating applicantIdToWorkCodeMapping
                    Set<String> workList;
                    if (!attendanceApplicantIdToWorkCodeMapping.containsKey(attendance.getApplicantId())) {
                        workList = new HashSet<>();
                    } else {
                        workList = attendanceApplicantIdToWorkCodeMapping.get(attendance.getApplicantId());
                    }
                    workList.add(attendance.getWorkcode());
                    attendanceApplicantIdToWorkCodeMapping.put(attendance.getApplicantId(), workList);
                    if (attendance.getWorkname() != null && !attendance.getWorkname().isEmpty()) {
                        //updating workCodeToWorkNameMapping
                        if (!attendanceWorkCodeToWorkNameMapping.containsKey(attendance.getWorkcode())) {
                            attendanceWorkCodeToWorkNameMapping.put(attendance.getWorkcode(), attendance.getWorkname());
                        }
                    }
                }
            }
        }
    }

    private static void translateWorkCodeToWorkNameMapping(String translationServiceId) {
        Set<String> attendanceWorkCodeSet = attendanceWorkCodeToWorkNameMapping.keySet();
        for (String workCode : attendanceWorkCodeSet) {
            translateAndSetWorkNameMapping(attendanceWorkCodeToWorkNameMapping, workCode, translationServiceId, attendanceTabLatch);
        }

        Set<String> paymentWorkCodeSet = paymentWorkCodeToWorkNameMapping.keySet();
        for (String workCode : paymentWorkCodeSet) {
            translateAndSetWorkNameMapping(paymentWorkCodeToWorkNameMapping, workCode, translationServiceId, paymentTabLatch);
        }

        Set<String> personalAssetsWorkCodeSet = personalAssetsWorkCodeToWorkNameMapping.keySet();
        for (String workCode : personalAssetsWorkCodeSet) {
            translateAndSetWorkNameMapping(personalAssetsWorkCodeToWorkNameMapping, workCode, translationServiceId, personalAssetsTabLatch);
        }
    }

    private static void translateAndSetWorkNameMapping (HashMap<String, String> workCodeToWorkNameMapping, String workCode, String translationServiceId, CountDownLatch latch) {
        String workName = workCodeToWorkNameMapping.get(workCode);
        String sourceLanguage = getSourceLanguageIfTranslationNeeded(workName);
        if (sourceLanguage != null) {
            Utility.makeComputeCall(sourceLanguage, targetLanguage, translationServiceId, workName, new ResponseCallback() {
                @Override
                public void onResponse(String translatedString) {
                    workCodeToWorkNameMapping.put(workCode, translatedString);
                    latch.countDown();
                }

                @Override
                public void onFailure() {
                    workCodeToWorkNameMapping.put(workCode, workName);
                    latch.countDown();
                }
            });
        } else {
            workCodeToWorkNameMapping.put(workCode, workName);
            latch.countDown();
        }
    }

    private static void setWorkMappingsViaPayment() {
        List<Payment> paymentList = jobCardData.getPayment();
        for (Payment payment : paymentList) {
            if (payment.getApplicantId() != null && !payment.getApplicantId().isEmpty()) {
                //updating applicantIdList
                paymentApplicantIdSet.add(payment.getApplicantId());

                if (payment.getWorkcode() != null && !payment.getWorkcode().isEmpty()) {
                    //updating applicantIdToWorkCodeMapping
                    Set<String> workList;
                    if (!paymentApplicantIdToWorkCodeMapping.containsKey(payment.getApplicantId())) {
                        workList = new HashSet<>();
                    } else {
                        workList = paymentApplicantIdToWorkCodeMapping.get(payment.getApplicantId());
                    }
                    workList.add(payment.getWorkcode());
                    paymentApplicantIdToWorkCodeMapping.put(payment.getApplicantId(), workList);

                    if (payment.getWorkname() != null && !payment.getWorkname().isEmpty()) {
                        //updating workCodeToWorkNameMapping
                        if (!paymentWorkCodeToWorkNameMapping.containsKey(payment.getWorkcode())) {
                            paymentWorkCodeToWorkNameMapping.put(payment.getWorkcode(), payment.getWorkname());
                        }
                    }
                }
                if (payment.getBankPostOfficeName() != null && !payment.getBankPostOfficeName().isEmpty()) {
                    bankNameTranslations.put(payment.getBankPostOfficeName(),null);
                }
                if (payment.getPaymentStatus() != null && !payment.getPaymentStatus().isEmpty()
                        && !payment.getPaymentStatus().equalsIgnoreCase(Constant.CENTER_SHARE_CREDITED)
                        && !payment.getPaymentStatus().equalsIgnoreCase(Constant.STATE_SHARE_CREDITED)) {
                    paymentStatusTranslations.put(payment.getPaymentStatus(), null);
                }
            }
        }
    }

    private static void translateBankNameAndPaymentStatus(String translationServiceId) {
        List<Payment> paymentList = jobCardData.getPayment();
        for (Payment payment : paymentList) {
            if (payment.getBankPostOfficeName() != null && !payment.getBankPostOfficeName().isEmpty()) {
                translateBankNameAndSaveTranslation(payment, translationServiceId);
            }
            if (payment.getPaymentStatus() != null && !payment.getPaymentStatus().isEmpty()
                    && !payment.getPaymentStatus().equalsIgnoreCase(Constant.CENTER_SHARE_CREDITED)
                    && !payment.getPaymentStatus().equalsIgnoreCase(Constant.STATE_SHARE_CREDITED)) {
                translatePaymentStatusAndSaveTranslation(payment, translationServiceId);
            }
        }
    }

    private static void translatePersonalAsset(String translationServiceId) {
        List<PersonalAsset> personalAssetList = jobCardData.getPersonalAssets();
        for (PersonalAsset personalAsset : personalAssetList) {
            translateVillageNameOfPersonalAsset(personalAsset, translationServiceId);
            translateGramPanchayatNameOfPersonalAsset(personalAsset, translationServiceId);
            translatePermissbleWorkOfPersonalAsset(personalAsset, translationServiceId);
        }

    }

    private static void translateVillageNameOfPersonalAsset(PersonalAsset personalAsset, String translationServiceId) {
        String villageName = personalAsset.getVillageName();
        String sourceLanguage = getSourceLanguageIfTranslationNeeded(villageName);

        if (sourceLanguage != null) {
            Utility.makeComputeCall(sourceLanguage, targetLanguage, translationServiceId, villageName, new ResponseCallback() {
                @Override
                public void onResponse(String translatedString) {
                    personalAsset.setVillageName(translatedString);
                    personalAssetsTabLatch.countDown();
                }

                @Override
                public void onFailure() {
                    personalAssetsTabLatch.countDown();
                }
            });
        } else {
            personalAssetsTabLatch.countDown();
        }
    }

    private static void translateGramPanchayatNameOfPersonalAsset(PersonalAsset personalAsset, String translationServiceId) {
        String gramPanchayatName = personalAsset.getGramPanchayatName();
        String sourceLanguage = getSourceLanguageIfTranslationNeeded(gramPanchayatName);

        if (sourceLanguage != null) {
            Utility.makeComputeCall(sourceLanguage, targetLanguage, translationServiceId, gramPanchayatName, new ResponseCallback() {
                @Override
                public void onResponse(String translatedString) {
                    personalAsset.setGramPanchayatName(translatedString);
                    personalAssetsTabLatch.countDown();
                }

                @Override
                public void onFailure() {
                    personalAssetsTabLatch.countDown();
                }
            });
        } else {
            personalAssetsTabLatch.countDown();
        }
    }

    private static void translatePermissbleWorkOfPersonalAsset(PersonalAsset personalAsset, String translationServiceId) {
        String permissibleWork = personalAsset.getPermissibleWork();
        String sourceLanguage = getSourceLanguageIfTranslationNeeded(permissibleWork);

        if (sourceLanguage != null) {
            Utility.makeComputeCall(sourceLanguage, targetLanguage, translationServiceId, permissibleWork, new ResponseCallback() {
                @Override
                public void onResponse(String translatedString) {
                    personalAsset.setPermissibleWork(translatedString);
                    personalAssetsTabLatch.countDown();
                }

                @Override
                public void onFailure() {
                    personalAssetsTabLatch.countDown();
                }
            });
        } else {
            personalAssetsTabLatch.countDown();
        }
    }
    private static void translateBankNameAndSaveTranslation(Payment payment, String translationServiceId) {
        String bankName = payment.getBankPostOfficeName();
        String sourceLanguage = getSourceLanguageIfTranslationNeeded(bankName);
        if (bankNameTranslations.get(bankName) != null) {
            payment.setmBankPostOfficeName(bankNameTranslations.get(bankName));
        } else {
            if (sourceLanguage != null) {
                Utility.makeComputeCall(sourceLanguage, targetLanguage, translationServiceId, bankName, new ResponseCallback() {
                    @Override
                    public void onResponse(String translatedString) {
                        bankNameTranslations.put(bankName, translatedString);
                        payment.setmBankPostOfficeName(translatedString);
                        paymentTabLatch.countDown();
                    }

                    @Override
                    public void onFailure() {
                        bankNameTranslations.put(bankName, bankName);
                        paymentTabLatch.countDown();
                    }
                });
            } else {
                bankNameTranslations.put(bankName, bankName);
                paymentTabLatch.countDown();
            }
        }
    }

    private static void translatePaymentStatusAndSaveTranslation(Payment payment, String translationServiceId) {
        String paymentStatus = payment.getPaymentStatus();
        String sourceLanguage = getSourceLanguageIfTranslationNeeded(paymentStatus);
        if (paymentStatusTranslations.get(paymentStatus) != null) {
            payment.setmPaymentStatus(paymentStatusTranslations.get(paymentStatus));
        } else {
            if (sourceLanguage != null) {
                Utility.makeComputeCall(sourceLanguage, targetLanguage, translationServiceId, paymentStatus, new ResponseCallback() {
                    @Override
                    public void onResponse(String translatedString) {
                        paymentStatusTranslations.put(paymentStatus, translatedString);
                        payment.setmPaymentStatus(translatedString);
                        paymentTabLatch.countDown();
                    }

                    @Override
                    public void onFailure() {
                        paymentStatusTranslations.put(paymentStatus, paymentStatus);
                        paymentTabLatch.countDown();
                    }
                });
            } else {
                paymentStatusTranslations.put(paymentStatus, paymentStatus);
                paymentTabLatch.countDown();
            }
        }
    }
    private static void setJobCardTabLabelValueList() {
        jobCardTabLabelValueList = new ArrayList<>();
        if (jobCardData.getJobCardId() != null && !jobCardData.getJobCardId().isEmpty()) {
            String jobCardNumber = context.getString(R.string.jobCardNumber);
            jobCardTabLabelValueList.add(new Pair<>(jobCardNumber, jobCardData.getJobCardId().toUpperCase()));
        }
        if (jobCardData.getNameOfHeadOfHousehold() != null && !jobCardData.getNameOfHeadOfHousehold().isEmpty()) {
            String headOfHousehold = context.getString(R.string.headOfHousehold);
            jobCardTabLabelValueList.add(new Pair<>(headOfHousehold, jobCardData.getNameOfHeadOfHousehold()));
        }
        if (jobCardData.getNameOfFatherHusband() != null && !jobCardData.getNameOfFatherHusband().isEmpty()) {
            String fatherHusbandName = context.getString(R.string.fatherHusbandName);
            jobCardTabLabelValueList.add(new Pair<>(fatherHusbandName, jobCardData.getNameOfFatherHusband()));
        }
       /* if (jobCardData.getCategory() != null && !jobCardData.getCategory().isEmpty()) {
            jobCardTabLabelValueList.add(new Pair<>("Social Category", jobCardData.getCategory()));
        }*/
        if (jobCardData.getDateOfRegistration() != null && !jobCardData.getDateOfRegistration().isEmpty()) {
            String dateOfRegistration = context.getString(R.string.dateOfRegistration);
            jobCardTabLabelValueList.add(new Pair<>(dateOfRegistration, jobCardData.getDateOfRegistration()));
        }
        if (jobCardData.getState() != null && !jobCardData.getState().isEmpty()) {
            String stateTranslated = context.getString(R.string.stateTranslated);
            jobCardTabLabelValueList.add(new Pair<>(stateTranslated, jobCardData.getState()));
        }
        if (jobCardData.getDistrict() != null && !jobCardData.getDistrict().isEmpty()) {
            String districtTranslated = context.getString(R.string.districtTranslated);
            jobCardTabLabelValueList.add(new Pair<>(districtTranslated, jobCardData.getDistrict()));
        }
        if (jobCardData.getBlock() != null && !jobCardData.getBlock().isEmpty()) {
            String blockTranslated = context.getString(R.string.blockTranslated);
            jobCardTabLabelValueList.add(new Pair<>(blockTranslated, jobCardData.getBlock()));
        }
        if (jobCardData.getPanchayat() != null && !jobCardData.getPanchayat().isEmpty()) {
            String panchayatTranslated = context.getString(R.string.panchayatTranslated);
            jobCardTabLabelValueList.add(new Pair<>(panchayatTranslated, jobCardData.getPanchayat()));
        }
        if (jobCardData.getVillage() != null && !jobCardData.getVillage().isEmpty()) {
            String villageTranslated = context.getString(R.string.villageTranslated);
            jobCardTabLabelValueList.add(new Pair<>(villageTranslated, jobCardData.getVillage()));
        }
        /*if (jobCardData.getBPLFamily() != null && !jobCardData.getBPLFamily().isEmpty()) {
            jobCardTabLabelValueList.add(new Pair<>("Whether BPL Family?", jobCardData.getBPLFamily().equalsIgnoreCase("Y")?"Yes":"No"));
        }*/
    }

    private static void setJobCardTabLabelValueListWithTranslation (String translationServiceId) {
        int index = 0;
        String first;
        if (jobCardData.getJobCardId() != null && !jobCardData.getJobCardId().isEmpty()) {
            String jobCardNumber = context.getString(R.string.jobCardNumber);
            jobCardTabLabelValueList.set(index++, new Pair<>(jobCardNumber, jobCardData.getJobCardId().toUpperCase()));
        }
        if (jobCardData.getNameOfHeadOfHousehold() != null && !jobCardData.getNameOfHeadOfHousehold().isEmpty()) {
            String headOfHousehold = context.getString(R.string.headOfHousehold);
            translateAndAddToList(index++, headOfHousehold, jobCardData.getNameOfHeadOfHousehold(), translationServiceId);
        }
        if (jobCardData.getNameOfFatherHusband() != null && !jobCardData.getNameOfFatherHusband().isEmpty()) {
            String fatherHusbandName = context.getString(R.string.fatherHusbandName);
            translateAndAddToList(index++, fatherHusbandName, jobCardData.getNameOfFatherHusband(), translationServiceId);
        }
       /* if (jobCardData.getCategory() != null && !jobCardData.getCategory().isEmpty()) {
            jobCardTabLabelValueList.add(new Pair<>("Social Category", jobCardData.getCategory()));
        }*/
        if (jobCardData.getDateOfRegistration() != null && !jobCardData.getDateOfRegistration().isEmpty()) {
            String dateOfRegistration = context.getString(R.string.dateOfRegistration);
            jobCardTabLabelValueList.set(index++, new Pair<>(dateOfRegistration, jobCardData.getDateOfRegistration()));
        }
        if (jobCardData.getState() != null && !jobCardData.getState().isEmpty()) {
            String stateTranslated = context.getString(R.string.stateTranslated);
            translateAndAddToList(index++, stateTranslated, jobCardData.getState(), translationServiceId);
        }
        if (jobCardData.getDistrict() != null && !jobCardData.getDistrict().isEmpty()) {
            String districtTranslated = context.getString(R.string.districtTranslated);
            translateAndAddToList(index++, districtTranslated, jobCardData.getDistrict(), translationServiceId);
        }
        if (jobCardData.getBlock() != null && !jobCardData.getBlock().isEmpty()) {
            String blockTranslated = context.getString(R.string.blockTranslated);
            translateAndAddToList(index++, blockTranslated, jobCardData.getBlock(), translationServiceId);
        }
        if (jobCardData.getPanchayat() != null && !jobCardData.getPanchayat().isEmpty()) {
            String panchayatTranslated = context.getString(R.string.panchayatTranslated);
            translateAndAddToList(index++, panchayatTranslated, jobCardData.getPanchayat(), translationServiceId);
        }
        if (jobCardData.getVillage() != null && !jobCardData.getVillage().isEmpty()) {
            String villageTranslated = context.getString(R.string.villageTranslated);
            translateAndAddToList(index++, villageTranslated, jobCardData.getVillage(), translationServiceId);
        }
        /*if (jobCardData.getBPLFamily() != null && !jobCardData.getBPLFamily().isEmpty()) {
            jobCardTabLabelValueList.add(new Pair<>("Whether BPL Family?", jobCardData.getBPLFamily().equalsIgnoreCase("Y")?"Yes":"No"));
        }*/
    }

    private static void translateAndAddToList(int index, String key, String value, String translationServiceId) {
        String speech = value;
        String sourceLanguage = getSourceLanguageIfTranslationNeeded(speech);
        if (sourceLanguage != null) {
            speech = "'" + speech + "'";
            Utility.makeComputeCall(sourceLanguage, targetLanguage, translationServiceId, speech, new ResponseCallback() {
                @Override
                public void onResponse(String translatedString) {
                    jobCardTabLabelValueList.set(index, new Pair<>(key, Utility.removeQuotations(translatedString)));
                    jobCardTabLatch.countDown();
                    if (jobCardTabTaskCompletionListener != null && !latchAborted) {
                        new Handler(Looper.getMainLooper()).post(() -> jobCardTabTaskCompletionListener.onAllTasksCompleted());
                    }
                }

                @Override
                public void onFailure() {
                    jobCardTabLabelValueList.set(index, new Pair<>(key, value));
                    jobCardTabLatch.countDown();
                }
            });
        } else {
            jobCardTabLabelValueList.set(index, new Pair<>(key, value));
            jobCardTabLatch.countDown();
        }
    }

    public static List<String> getDayWiseAttendance(Attendance attendance) {
        List<String> dayWiseAttendance = new ArrayList<>();
        dayWiseAttendance.add(attendance.getDay1());
        dayWiseAttendance.add(attendance.getDay2());
        dayWiseAttendance.add(attendance.getDay3());
        dayWiseAttendance.add(attendance.getDay4());
        dayWiseAttendance.add(attendance.getDay5());
        dayWiseAttendance.add(attendance.getDay6());
        dayWiseAttendance.add(attendance.getDay7());
        dayWiseAttendance.add(attendance.getDay8());
        dayWiseAttendance.add(attendance.getDay9());
        dayWiseAttendance.add(attendance.getDay10());
        dayWiseAttendance.add(attendance.getDay11());
        dayWiseAttendance.add(attendance.getDay12());
        dayWiseAttendance.add(attendance.getDay13());
        dayWiseAttendance.add(attendance.getDay14());
        dayWiseAttendance.add(attendance.getDay15());
        dayWiseAttendance.add(attendance.getDay16());
        return dayWiseAttendance;
    }

    public static int getFinancialYearStart() {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        if (currentMonth >= 3) {
            return currentYear;
        } else {
            return currentYear - 1;
        }
    }

    public static Calendar getFinancialYearStartDate() {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        Calendar resultCalendar = Calendar.getInstance();
        resultCalendar.set(Calendar.HOUR_OF_DAY, 0);
        resultCalendar.set(Calendar.MINUTE, 0);
        resultCalendar.set(Calendar.SECOND, 0);
        resultCalendar.set(Calendar.MILLISECOND, 0);
        resultCalendar.set(Calendar.DATE, 1);
        resultCalendar.set(Calendar.MONTH, 3);
        if (currentMonth >= 3) {
            resultCalendar.set(Calendar.YEAR, currentYear);
        } else {
            resultCalendar.set(Calendar.YEAR, currentYear - 1);
        }
        return resultCalendar;
    }

    private static int getDaysWorked(Attendance attendance) {
        int daysWorked = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (attendance != null) {
            List<String> dayWiseAttendance = JobCardDataAccess.getDayWiseAttendance(attendance);

            try {
                Date startDate = dateFormat.parse(attendance.getmMsrDateFrom());
                Calendar msrStartCalendar = Calendar.getInstance();
                msrStartCalendar.setTime(startDate);
                Calendar financialYearStartCalendar = JobCardDataAccess.getFinancialYearStartDate();
                for (int i = 0; i < dayWiseAttendance.size(); i++) {
                    if (null != dayWiseAttendance.get(i)
                        && dayWiseAttendance.get(i).equalsIgnoreCase("P")
                        && !msrStartCalendar.before(financialYearStartCalendar)) {
                                daysWorked++;
                    }
                }
            } catch (ParseException e) {
                Log.e("JobCardDataAccess", "Error in parsing MSR start date");
                throw new RuntimeException(e);
            }
        }

        return daysWorked;
    }

    private static void setWorkCodeToPersonalAssetImageMapping() {
        personalAssetsImageTabLatch = new CountDownLatch(jobCardData.getPersonalAssets().size());
        List<PersonalAsset> personalAssetList = jobCardData.getPersonalAssets();
        new Thread(() ->{
            for (PersonalAsset personalAsset : personalAssetList) {
                PersonalAsset pa  = personalAsset;
                String image = personalAsset.getImageUrl();
                InputStream inputStream = null;
                URL url = null;
                try {
                    String urlString = image;
                    url = new URL(urlString);
                    inputStream = (InputStream) url.getContent();
                    personalAssetsImageTabLatch.countDown();
                } catch (IOException e) {
                    try {
                        String urlString = image.replaceAll("_comp2", "");
                        url = new URL(urlString);
                        inputStream = (InputStream) url.getContent();
                        personalAssetsImageTabLatch.countDown();
                    } catch (IOException ex) {
                        try {
                            String urlString = image.replaceAll("photos2", "photos/2");
                            url = new URL(urlString);
                            inputStream = (InputStream) url.getContent();
                            personalAssetsImageTabLatch.countDown();
                        } catch (IOException exe) {
                            exe.printStackTrace();
                            personalAssetsImageTabLatch.countDown();
                        }
                    }
                }
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    workCodeToPersonalAssetImageMapping.put(personalAsset.getWorkCode(),bitmap);
                }
            }
        }).start();
    }
    public static void clearData() {
        applicantIdToJobCardDetailMapping.clear();
        attendanceApplicantIdSet.clear();
        paymentApplicantIdSet.clear();
        attendanceApplicantIdToWorkCodeMapping.clear();
        paymentApplicantIdToWorkCodeMapping.clear();
        jobCardTabLabelValueList.clear();
        attendanceWorkCodeToWorkNameMapping.clear();
        paymentWorkCodeToWorkNameMapping.clear();
        workCodeToPersonalAssetImageMapping.clear();
        personalAssetsWorkCodeToWorkNameMapping.clear();
        totalDaysWorked = 0;
        noOfPaymentTabAsyncCalls = 0;
        noOfAttendanceTabAsyncCalls = 0;
        noOfJobCardTabAsyncCalls = 0;

        JobCardTabData.clearData();
        AttendanceTabData.clearData();
        PaymentTabData.clearData();
        AbpsStatusTabData.clearData();
        PersonalAssetsTabData.clearData();
    }

}
