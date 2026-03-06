package nic.hp.ccmgnrega.data;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;

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
import java.util.stream.Collectors;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.common.AttendanceDetailComparator;
import nic.hp.ccmgnrega.common.Constant;
import nic.hp.ccmgnrega.common.IntegerStringComparator;
import nic.hp.ccmgnrega.databinding.FragmentAttendanceBinding;
import nic.hp.ccmgnrega.model.Attendance;
import nic.hp.ccmgnrega.model.AttendanceInfo;
import nic.hp.ccmgnrega.model.JobCardData;

public class AttendanceTabData {

    //in current financial year
    private static HashMap<String, Integer> applicantToDaysWorkedMapping = new HashMap<>();
    public static HashMap<String, HashMap<String,List<AttendanceInfo>>> attendanceTabData = new HashMap<>();
    private static Comparator stringComparator = new IntegerStringComparator();
    private static Comparator attendanceDetailComparator;
    private static Context context;
    private static FragmentAttendanceBinding binding;
    private static JobCardData jobCardData;

    public static void populateAttendanceTabData (FragmentAttendanceBinding binding, Context context, JobCardData jobCardData) {
        AttendanceTabData.context = context;
        AttendanceTabData.binding = binding;
        AttendanceTabData.jobCardData = jobCardData;
        clearData();
        setPageInformation();
        setAttendanceTabData();
    }
    private static void setPageInformation() {
        if (jobCardData.getTotalApplicantsInJobCard() != null) {
            binding.totalApplicants.setText(context.getString(R.string.totalApplicants) + ": " + jobCardData.getTotalApplicantsInJobCard());
        }
        binding.clickInfoText.setText("*" + context.getString(R.string.toViewDetailsClickOn) + " ");
        binding.finYearInfo.setText(context.getString(R.string.finYearAttendanceInfo));
    }

    private static void setAttendanceTabData() {
        List<Attendance> attendanceList = jobCardData.getAttendance();
        if (attendanceList == null || attendanceList.isEmpty()) {
            addNoAttendanceApplicants();
            sortAttendance();
            return;
        }
        attendanceList = removeDuplicates(attendanceList);
        for (Attendance attendance : attendanceList) {
            if (attendance.getApplicantId() == null || attendance.getApplicantId().isEmpty()) {
                   continue;
            }
            //dayWiseAttendance = {"P","A",null}
            List<String> dayWiseAttendance = getAttendanceArray(attendance);
            //dateList = {"01/04/23","02/04/23",""}
            List<String> dateList = getDateList(attendance);

            HashMap<String,List<AttendanceInfo>> workCodeToAttendanceMapping;
            List<AttendanceInfo> attendanceDetailList;
            if (dayWiseAttendance ==  null || dayWiseAttendance.isEmpty() || dateList == null || dateList.isEmpty()) {
                workCodeToAttendanceMapping = new HashMap<>();
                attendanceDetailList = new ArrayList<>();
            } else {
                if (!attendanceTabData.containsKey(attendance.getApplicantId())) {
                    workCodeToAttendanceMapping = new HashMap<>();
                    attendanceDetailList = new ArrayList<>();
                } else {
                    workCodeToAttendanceMapping = attendanceTabData.get(attendance.getApplicantId());
                    if (!workCodeToAttendanceMapping.containsKey(attendance.getWorkcode())) {
                        attendanceDetailList = new ArrayList<>();
                    } else {
                        attendanceDetailList = workCodeToAttendanceMapping.get(attendance.getWorkcode());
                    }
                }
            }
            for (int i = 0; i < Constant.MAX_DAYS_IN_MUSTER; i++) {
                if (attendance.getMusterrollno() != null && !attendance.getMusterrollno().isEmpty()
                        && dateList.get(i) != null && !dateList.get(i).isEmpty()
                        && dayWiseAttendance.get(i) != null && !dayWiseAttendance.get(i).isEmpty()) {
                    AttendanceInfo attendanceDetail = new AttendanceInfo();
                    attendanceDetail.setMsrNo(attendance.getMusterrollno());
                    attendanceDetail.setDate(dateList.get(i));
                    attendanceDetail.setAttendance(dayWiseAttendance.get(i));
                    attendanceDetailList.add(attendanceDetail);
                }
            }
            if (attendance.getWorkcode() != null) {
                 workCodeToAttendanceMapping.put(attendance.getWorkcode(),attendanceDetailList);
                 attendanceTabData.put(attendance.getApplicantId(), workCodeToAttendanceMapping);
            }
        }
        addNoAttendanceApplicants();
        sortAttendance();
    }

    public static List<Attendance> removeDuplicates (List<Attendance> attendanceList) {
        List<Attendance> distinctAttendanceList = attendanceList;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            distinctAttendanceList = attendanceList.stream().distinct().collect(Collectors.toList());
        }
        return distinctAttendanceList;
    }

    private static void sortAttendance () {
        List<String> applicationIdList = new ArrayList<>(attendanceTabData.keySet());
        if (applicationIdList == null)
        {
            return;
        }
        Collections.sort(applicationIdList, stringComparator);

        for (String applicantId : applicationIdList) {
            if (JobCardDataAccess.attendanceApplicantIdToWorkCodeMapping.get(applicantId) != null) {
                List<String> workCodeList = new ArrayList<>(JobCardDataAccess.attendanceApplicantIdToWorkCodeMapping.get(applicantId));

                for (String workCode : workCodeList) {
                    HashMap<String,List<AttendanceInfo>> workCodeToAttendanceMapping = attendanceTabData.get(applicantId);
                    List<AttendanceInfo> attendanceInfoList = workCodeToAttendanceMapping.get(workCode);
                    if (attendanceInfoList != null) {
                        if (attendanceInfoList.size() > 0) {
                            String msrNo = context.getString(R.string.msrNo);
                            String date = context.getString(R.string.date);
                            String attendance = context.getString(R.string.attendanceTranslated);
                            attendanceInfoList.add(0, new AttendanceInfo(msrNo, date, attendance));
                        }
                        attendanceDetailComparator = new AttendanceDetailComparator(context);
                        Collections.sort(attendanceInfoList, attendanceDetailComparator);
                    }
                }
            }
        }
    }

    private static void addNoAttendanceApplicants() {
        Set<String> applicationIdList = JobCardDataAccess.applicantIdSet;
        for (String applicantId : applicationIdList) {
            if (!attendanceTabData.containsKey(applicantId)) {
                HashMap<String,List<AttendanceInfo>> workCodeToAttendanceMapping = new HashMap<>();
                attendanceTabData.put(applicantId, workCodeToAttendanceMapping);
                applicantToDaysWorkedMapping.put(applicantId, 0);
            }
        }
    }

    public static Pair<String, String> getFirstLevelApplicantDetail(String applicantId) {
        String applicantName = JobCardDataAccess.applicantIdToJobCardDetailMapping.get(applicantId).getApplicantName();
        Integer daysWorked = applicantToDaysWorkedMapping.get(applicantId);
//        String daysWorkedStatement = daysWorked + " days attendance since 1 April " + JobCardDataAccess.getFinancialYearStart();
        String daysWorkedStatement = context.getString(R.string.daysAttendanceSince) + ": " + daysWorked;
        Pair<String, String> applicantDetail = new Pair<>(applicantName, daysWorkedStatement);
        return applicantDetail;
    }

    private static List<String> getAttendanceArray(Attendance attendance) {
        List<String> dayWiseAttendance = JobCardDataAccess.getDayWiseAttendance(attendance);
        Integer daysWorked = 0;

        if (attendance.getmMsrDateFrom() == null || attendance.getmMsrDateFrom().isEmpty()) {
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date msrStartDate = dateFormat.parse(attendance.getmMsrDateFrom());
            Calendar msrStartCalendar = Calendar.getInstance();
            msrStartCalendar.setTime(msrStartDate);
            Calendar financialYearStartCalendar = JobCardDataAccess.getFinancialYearStartDate();
            for (int i = 0; i < dayWiseAttendance.size(); i++) {
                if (dayWiseAttendance.get(i) != null) {
                    if (dayWiseAttendance.get(i).equalsIgnoreCase("P")) {
                        dayWiseAttendance.set(i, context.getString(R.string.present));
                        if (!msrStartCalendar.before(financialYearStartCalendar)) {
                            daysWorked++;
                        }
                    } else if (dayWiseAttendance.get(i).equalsIgnoreCase("A")){
                        dayWiseAttendance.set(i, context.getString(R.string.absent));
                    } else if (dayWiseAttendance.get(i).equalsIgnoreCase("N")) {
                        dayWiseAttendance.set(i, context.getString(R.string.notTaken));
                    } else {
                        dayWiseAttendance.set(i, null);
                    }
                }
            }
        } catch (ParseException e) {
            Log.e("AttendanceTabData", "Error in parsing MSR start date");
            throw new RuntimeException(e);
        }

        Integer totalDaysWorked = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            totalDaysWorked = applicantToDaysWorkedMapping.getOrDefault(attendance.getApplicantId(), 0) + daysWorked;
        }
        applicantToDaysWorkedMapping.put(attendance.getApplicantId(), totalDaysWorked);

        return dayWiseAttendance;
    }

    private static List<String> getDateList (Attendance attendance) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        List<String> dateList = new ArrayList<>();
        try {
            if (attendance.getmMsrDateFrom() != null
                    && !attendance.getmMsrDateFrom().isEmpty()
                    && attendance.getmMsrDateTo() != null
                    && !attendance.getmMsrDateTo().isEmpty()) {
                Date startDate = dateFormat.parse(attendance.getmMsrDateFrom());
                Date toDate = dateFormat.parse(attendance.getmMsrDateTo());
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.setTime(startDate);
                Calendar toCalendar = Calendar.getInstance();
                toCalendar.setTime(toDate);
                Calendar currentCalendar = startCalendar;
                int index = 0;
                while (currentCalendar.before(toCalendar) || currentCalendar.equals(toCalendar)) {
                    dateList.add(dateFormat.format(currentCalendar.getTime()));
                    currentCalendar.add(Calendar.DATE, 1);
                    index++;
                }
                while (index < Constant.MAX_DAYS_IN_MUSTER) {
                    dateList.add("");
                    index++;
                }
            }
        } catch (ParseException e) {
            Log.e("AttendanceTabData", "Error in parsing MSR start/to date");
            e.printStackTrace();
        }
        return dateList;
    }

    public static void clearData() {
        applicantToDaysWorkedMapping.clear();
        attendanceTabData.clear();
    }
}
