package nic.hp.ccmgnrega.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;


import java.util.List;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.common.Constant;
import nic.hp.ccmgnrega.databinding.FragmentAbpsStatusBinding;
import nic.hp.ccmgnrega.databinding.FragmentPaymentBinding;
import nic.hp.ccmgnrega.model.ABPSEligibilityStatus;
import nic.hp.ccmgnrega.model.JobCardData;

public class AbpsStatusTabData {
    private static boolean errorInfoNeeded = false;
    private static boolean aadhaarNotSeededMessage = false;
    private static boolean successfullyNotAuthenticatedMessage = false;
    private static boolean abpsNotEligibleMessage = false;
    private static int textSize = 14;
    private static Context context;
    private static FragmentAbpsStatusBinding binding;
    private static JobCardData jobCardData;

    public static void populateAbpsStatusTabData (FragmentAbpsStatusBinding binding, Context context, JobCardData jobCardData) {
        AbpsStatusTabData.context = context;
        AbpsStatusTabData.binding = binding;
        AbpsStatusTabData.jobCardData = jobCardData;
        clearData();
        setEligiblityTable();
        setInfoTable();
        setPageInformation();
    }

    private static void setPageInformation() {
        if (jobCardData.getTotalApplicantsInJobCard() != null) {
            binding.totalApplicants.setText(context.getString(R.string.totalApplicants) + ": " + jobCardData.getTotalApplicantsInJobCard());
        }
    }

    private static void setEligiblityTable() {
        TableLayout tableLayout = binding.eligibilityTable;
        List<ABPSEligibilityStatus> abpsEligibilityStatusList = jobCardData.getABPSEligibilityStatus();
        if (abpsEligibilityStatusList == null) {
            return;
        }

        int greenCheckMark = R.drawable.green_check_mark;
        int redCross = R.drawable.red_cross;

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, 225, 1.0f);
        layoutParams.gravity = Gravity.CENTER;

        TableRow.LayoutParams imageLayoutParams = new TableRow.LayoutParams(0, 100, 1.0f);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(0,0,0,0);

        int rowCount = 1;
        for (int i = 0; i < abpsEligibilityStatusList.size(); i++) {
            String applicantId = abpsEligibilityStatusList.get(i).getApplicantID();
            if (applicantId == null || applicantId.isEmpty() || JobCardDataAccess.applicantIdToJobCardDetailMapping.get(applicantId) == null || JobCardDataAccess.applicantIdToJobCardDetailMapping.get(applicantId).getApplicantStatus() == null || JobCardDataAccess.applicantIdToJobCardDetailMapping.get(applicantId).getApplicantStatus().equalsIgnoreCase("Deleted")) {
                continue;
            }

            TableRow tableRow = new TableRow(context);

            TextView applicant = new TextView(context);
            String applicantName = JobCardDataAccess.applicantIdToJobCardDetailMapping.get(applicantId).getApplicantName();
            if (applicantName == null || applicantName.isEmpty()) {
                continue;
            }
            applicant.setText(applicantName);
            applicant.setLayoutParams(layoutParams);
            applicant.setTextColor(Color.parseColor("#3F51B5"));
            applicant.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
            tableRow.addView(applicant);

            ImageView aadhaarSeeded = new ImageView(context);
            if (abpsEligibilityStatusList.get(i).getAadhaarSeeded() == null
                || abpsEligibilityStatusList.get(i).getAadhaarSeeded().isEmpty()) {
                aadhaarSeeded.setImageResource(0);
            } else if (abpsEligibilityStatusList.get(i).getAadhaarSeeded().equalsIgnoreCase("Y")) {
                aadhaarSeeded.setImageResource(greenCheckMark);
            } else {
                aadhaarSeeded.setImageResource(redCross);
            }
            if (!abpsEligibilityStatusList.get(i).getAadhaarSeeded().equalsIgnoreCase("Y")) {
                errorInfoNeeded = true;
                aadhaarNotSeededMessage = true;
            }
            aadhaarSeeded.setLayoutParams(imageLayoutParams);
            tableRow.addView(aadhaarSeeded);


            ImageView successfullyAuthenticated = new ImageView(context);
            if (abpsEligibilityStatusList.get(i).getAadhaarSeeded() != null
                && abpsEligibilityStatusList.get(i).getAadhaarSeeded().equalsIgnoreCase("N")) {
                successfullyAuthenticated.setImageResource(redCross);
            } else if (abpsEligibilityStatusList.get(i).getSuccessfullyAuthenticated() == null
                        || abpsEligibilityStatusList.get(i).getSuccessfullyAuthenticated().isEmpty()) {
                successfullyAuthenticated.setImageResource(0);
            } else if (abpsEligibilityStatusList.get(i).getSuccessfullyAuthenticated().equalsIgnoreCase("Y")) {
                successfullyAuthenticated.setImageResource(greenCheckMark);
            } else {
                successfullyAuthenticated.setImageResource(redCross);
            }
            if (!abpsEligibilityStatusList.get(i).getSuccessfullyAuthenticated().equalsIgnoreCase("Y")) {
                errorInfoNeeded = true;
                successfullyNotAuthenticatedMessage = true;
            }
            successfullyAuthenticated.setLayoutParams(imageLayoutParams);
            tableRow.addView(successfullyAuthenticated);

            ImageView abpsEligible = new ImageView(context);
            if ((abpsEligibilityStatusList.get(i).getAadhaarSeeded() != null
                    && abpsEligibilityStatusList.get(i).getAadhaarSeeded().equalsIgnoreCase("N"))
                    || (abpsEligibilityStatusList.get(i).getSuccessfullyAuthenticated() != null
                    && abpsEligibilityStatusList.get(i).getSuccessfullyAuthenticated().equalsIgnoreCase("N"))) {
                abpsEligible.setImageResource(redCross);
            } else if (abpsEligibilityStatusList.get(i).getABPSEnabledInNREGASoft() == null
                        || abpsEligibilityStatusList.get(i).getABPSEnabledInNREGASoft().isEmpty()) {
                abpsEligible.setImageResource(0);
            } else if (abpsEligibilityStatusList.get(i).getABPSEnabledInNREGASoft().equalsIgnoreCase("Y")) {
                abpsEligible.setImageResource(greenCheckMark);
            } else {
                abpsEligible.setImageResource(redCross);
            }
            if (!abpsEligibilityStatusList.get(i).getABPSEnabledInNREGASoft().equalsIgnoreCase("Y")) {
                errorInfoNeeded = true;
                abpsNotEligibleMessage = true;
            }
            abpsEligible.setLayoutParams(imageLayoutParams);
            tableRow.addView(abpsEligible);

            tableLayout.addView(tableRow, rowCount);
            rowCount++;
        }
    }

    private static void setInfoTable () {
        TableLayout tableLayout = binding.infoTable;

        int redCross = R.drawable.red_cross;
        TableRow.LayoutParams redCrossParams = new TableRow.LayoutParams(0, 100, 0.085f);
        redCrossParams.gravity = Gravity.CENTER;
        TableRow.LayoutParams errorMessageParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7f);
        errorMessageParams.gravity = Gravity.CENTER;

        int errorMessageIndex = 0;
        if (errorInfoNeeded) {
            TableRow tableRow = new TableRow(context);
            tableRow.setPadding(0, 0, 0, 20);

            TextView heading = new TextView(context);
            heading.setText(context.getString(R.string.stepsToBeFollowed));
            heading.setLayoutParams(errorMessageParams);
            heading.setTypeface(null, Typeface.BOLD);
            heading.setTextColor(ContextCompat.getColor(context, R.color.all_text));
            heading.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
            tableRow.addView(heading);

            tableLayout.addView(tableRow, errorMessageIndex);
            errorMessageIndex++;
        }
        if (aadhaarNotSeededMessage) {
            TableRow tableRow = new TableRow(context);
            tableRow.setPadding(0, 0, 0, 60);

            ImageView redCrossImage = new ImageView(context);
            redCrossImage.setImageResource(redCross);
            redCrossImage.setLayoutParams(redCrossParams);
            redCrossImage.setPadding(0, 0, 10, 0);
            tableRow.addView(redCrossImage);

            TextView errorMessage = new TextView(context);
            errorMessage.setText(context.getString(R.string.aadhaarNotSeededMessage));
            errorMessage.setLayoutParams(errorMessageParams);
            errorMessage.setTextColor(ContextCompat.getColor(context, R.color.all_text));
            errorMessage.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
            tableRow.addView(errorMessage);

            tableLayout.addView(tableRow, errorMessageIndex);
            errorMessageIndex++;
        }
        if (successfullyNotAuthenticatedMessage) {
            TableRow tableRow = new TableRow(context);
            tableRow.setPadding(0, 0, 0, 60);

            ImageView redCrossImage = new ImageView(context);
            redCrossImage.setImageResource(redCross);
            redCrossImage.setLayoutParams(redCrossParams);
            redCrossImage.setPadding(0, 0, 10, 0);
            tableRow.addView(redCrossImage);

            TextView errorMessage = new TextView(context);
            errorMessage.setText(context.getString(R.string.successfullyNotAuthenticatedMessage));
            errorMessage.setLayoutParams(errorMessageParams);
            errorMessage.setTextColor(ContextCompat.getColor(context, R.color.all_text));
            errorMessage.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
            tableRow.addView(errorMessage);

            tableLayout.addView(tableRow, errorMessageIndex);
            errorMessageIndex++;
        }
        if (abpsNotEligibleMessage) {
            TableRow tableRow = new TableRow(context);
            tableRow.setPadding(0, 0, 0, 60);

            ImageView redCrossImage = new ImageView(context);
            redCrossImage.setImageResource(redCross);
            redCrossImage.setLayoutParams(redCrossParams);
            redCrossImage.setPadding(0, 0, 10, 0);
            tableRow.addView(redCrossImage);

            TextView errorMessage = new TextView(context);
            errorMessage.setText(context.getString(R.string.abpsNotEligibleMessage));
            errorMessage.setLayoutParams(errorMessageParams);
            errorMessage.setTextColor(ContextCompat.getColor(context, R.color.all_text));
            errorMessage.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
            tableRow.addView(errorMessage);

            tableLayout.addView(tableRow, errorMessageIndex);
            errorMessageIndex++;
        }

    }

    public static void clearData() {
        errorInfoNeeded = false;
        aadhaarNotSeededMessage = false;
        successfullyNotAuthenticatedMessage = false;
        abpsNotEligibleMessage = false;
    }
}
