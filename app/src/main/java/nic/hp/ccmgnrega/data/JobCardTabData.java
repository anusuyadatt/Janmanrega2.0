package nic.hp.ccmgnrega.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.databinding.FragmentJobCardBinding;
import nic.hp.ccmgnrega.model.JobCardData;
import nic.hp.ccmgnrega.model.JobCardDetail;

public class JobCardTabData {
    private static int allTextColor = R.color.all_text;
    private static int headerColor = R.color.blue_text;
    private static int textSize = 14;
    private static boolean applicantDeletedTextNeeded = false;
    static String jobCardDetailsSpeech = "";
    static String applicantDetailsSpeech = "";
    private static JobCardData jobCardData;
    private static FragmentJobCardBinding binding;
    private static Context context;
    public static void populateJobCardTabData(FragmentJobCardBinding binding, Context context, JobCardData jobCardData) {
        clearData();
        JobCardTabData.jobCardData = jobCardData;
        JobCardTabData.binding = binding;
        JobCardTabData.context = context;
        if (context != null) {
            setJobCardTabTable();
            setDetailsOfApplicantsTable();
            setApplicantDeletedText();
        }
    }

    private static void setJobCardTabTable() {
        TableLayout tableLayout = binding.jobCardDetailTable;
        tableLayout.removeAllViews();
        List<Pair<String,String>> jobCardTabLabelValueList = JobCardDataAccess.jobCardTabLabelValueList;

        for (int i = 0; i < jobCardTabLabelValueList.size(); i++) {
            TableRow tableRow = new TableRow(context);
            tableRow.setPadding(0, 0, 0, 5);

            TextView labelView = new TextView(context);
            labelView.setText(jobCardTabLabelValueList.get(i).first + ":");
            labelView.setTextColor(context.getResources().getColor(R.color.blue_text));
            labelView.setTextColor(Color.parseColor("#3F51B5"));
            labelView.setTypeface(null, Typeface.BOLD);
            labelView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
            tableRow.addView(labelView);

            TextView valueView = new TextView(context);
            valueView.setText(jobCardTabLabelValueList.get(i).second);
            valueView.setTextColor(ContextCompat.getColor(context, allTextColor));
            valueView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            valueView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
            tableRow.addView(valueView);

            tableLayout.addView(tableRow, i);
        }
    }

    public static String getJobCardDetailsSpeech() {
        String period = context.getString(R.string.period) + " ";
        jobCardDetailsSpeech = context.getString(R.string.jobCardDetails) + period;

        List<Pair<String,String>> jobCardTabLabelValueList = JobCardDataAccess.jobCardTabLabelValueList;
        for (int i = 0; i < jobCardTabLabelValueList.size(); i++) {
            jobCardDetailsSpeech += jobCardTabLabelValueList.get(i).first + period;
            if (jobCardTabLabelValueList.get(i).first.equalsIgnoreCase(context.getString(R.string.dateOfRegistration))) {
                SimpleDateFormat indianDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = indianDateFormat.parse(jobCardTabLabelValueList.get(i).second);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    jobCardDetailsSpeech +=  calendar.get(Calendar.DATE) + " " + new SimpleDateFormat("MMMM").format(calendar.getTime()) + " " + calendar.get(Calendar.YEAR) + period;
                } catch (ParseException e) {
                    jobCardDetailsSpeech += jobCardTabLabelValueList.get(i).second + period;
                }
            } else {
                jobCardDetailsSpeech += jobCardTabLabelValueList.get(i).second + period;
            }
        }
        return jobCardDetailsSpeech;
    }

    private static void setDetailsOfApplicantsTable () {
        List<JobCardDetail> jobCardDetailList = jobCardData.getJobCardDetails();
        TableLayout tableLayout = binding.applicantDetailTable;
        tableLayout.removeAllViews();

        addHeadersToDetailsOfApplicationsTable(context, tableLayout);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT, .10f);

        for (int i = 0; i < jobCardDetailList.size(); i++) {
            if (jobCardDetailList.get(i).getApplicantName() == null || jobCardDetailList.get(i).getApplicantName().isEmpty()) {
                continue;
            }

            TableRow tableRow = new TableRow(context);

            TextView serialNo = getRowTextView(context);
            serialNo.setText("" + (i+1));
            serialNo.setLayoutParams(layoutParams);
            tableRow.addView(serialNo);

            TextView nameOfApplicant = getRowTextView(context);
            String nameOfApplicantText = "<font color='#635F5F'>" + jobCardDetailList.get(i).getApplicantName() + "</font>";
            if (jobCardDetailList.get(i).getApplicantStatus() != null
                && !jobCardDetailList.get(i).getApplicantStatus().isEmpty()
                && jobCardDetailList.get(i).getApplicantStatus().equalsIgnoreCase("Deleted")) {
                nameOfApplicantText += "<font color='#FF0000'>*</font>";
                applicantDeletedTextNeeded = true;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                nameOfApplicant.setText(Html.fromHtml(nameOfApplicantText,0));
            }
            TableRow.LayoutParams namelayoutParams = new TableRow.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT, .30f);
            nameOfApplicant.setLayoutParams(namelayoutParams);
            tableRow.addView(nameOfApplicant);

            TextView gender = getRowTextView(context);
            if (jobCardDetailList.get(i).getGender() == null
                || jobCardDetailList.get(i).getGender().isEmpty()) {
                gender.setText("");
            } else {
                gender.setText(jobCardDetailList.get(i).getGender());
            }
            TableRow.LayoutParams genderlayoutParams = new TableRow.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT, .12f);
            gender.setLayoutParams(genderlayoutParams);
            tableRow.addView(gender);

            TextView age = getRowTextView(context);
            if (jobCardDetailList.get(i).getAge() == null
                    || jobCardDetailList.get(i).getAge().isEmpty()) {
                age.setText("");
            } else {
                age.setText(jobCardDetailList.get(i).getAge());
            }
            age.setLayoutParams(layoutParams);
            tableRow.addView(age);

            tableLayout.addView(tableRow, i+1);
        }
    }

    private static TextView getRowTextView(Context context) {
        TextView textView = new TextView(context);
        textView.setTextColor(ContextCompat.getColor(context, allTextColor));
        textView.setGravity(Gravity.LEFT);
        textView.setPadding(2, 0, 2, 20);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        return textView;
    }

    private static TextView getHeaderTextView(Context context) {
        TextView textView = new TextView(context);
        textView.setTextColor(ContextCompat.getColor(context, headerColor));
        textView.setGravity(Gravity.LEFT);
        textView.setPadding(2, 0, 2, 20);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        textView.setTypeface(null, Typeface.BOLD);
        return textView;
    }
    private static void addHeadersToDetailsOfApplicationsTable(Context context, TableLayout tableLayout) {
        TableRow tableRow = new TableRow(context);

        TextView serialNo = getHeaderTextView(context);
        serialNo.setText(context.getString(R.string.sNo));
        TableRow.LayoutParams serialNoLayoutParams = new TableRow.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT, .10f);
        serialNo.setLayoutParams(serialNoLayoutParams);
        tableRow.addView(serialNo);

        TextView nameOfApplicant = getHeaderTextView(context);
        nameOfApplicant.setText(context.getString(R.string.nameOfApplicant));
        TableRow.LayoutParams nameOfApplicantLayoutParams = new TableRow.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT, .30f);
        nameOfApplicant.setLayoutParams(nameOfApplicantLayoutParams);
        tableRow.addView(nameOfApplicant);

        TextView gender = getHeaderTextView(context);
        gender.setText(context.getString(R.string.gender));
        TableRow.LayoutParams genderLayoutParams = new TableRow.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT, .12f);
        gender.setLayoutParams(genderLayoutParams);
        tableRow.addView(gender);

        TextView age = getHeaderTextView(context);
        age.setText(context.getString(R.string.age));
        TableRow.LayoutParams ageLayoutParams = new TableRow.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT, .10f);
        age.setLayoutParams(ageLayoutParams);
        tableRow.addView(age);

        tableLayout.addView(tableRow, 0);
    }

    public static String getApplicantDetailsSpeech() {
        String period = context.getString(R.string.period) + " ";
        applicantDetailsSpeech = context.getString(R.string.applicantDetailsTableHeading) + period;

        List<JobCardDetail> jobCardDetailList = jobCardData.getJobCardDetails();
        for (int i = 0; i < jobCardDetailList.size(); i++) {
            if (jobCardDetailList.get(i).getApplicantName() == null || jobCardDetailList.get(i).getApplicantName().isEmpty()) {
                continue;
            }

            applicantDetailsSpeech += context.getString(R.string.nameOfApplicant) + period;
            applicantDetailsSpeech += jobCardDetailList.get(i).getApplicantName() + period;

            applicantDetailsSpeech += context.getString(R.string.gender) + period;
            applicantDetailsSpeech += jobCardDetailList.get(i).getGender().toLowerCase() + period;

            applicantDetailsSpeech += context.getString(R.string.age).toLowerCase() + period;
            applicantDetailsSpeech += jobCardDetailList.get(i).getAge() + period;
        }

        return applicantDetailsSpeech;
    }
    private static void setApplicantDeletedText() {
        if (applicantDeletedTextNeeded) {
            TextView applicantDeleted = binding.applicantDeletedText;
            applicantDeleted.setText("*" + context.getString(R.string.applicantDeleted));
            applicantDeleted.setVisibility(View.VISIBLE);
        }
    }
    public static void clearData() {
        applicantDeletedTextNeeded = false;
        applicantDetailsSpeech = "";
        jobCardDetailsSpeech = "";
    }
}
