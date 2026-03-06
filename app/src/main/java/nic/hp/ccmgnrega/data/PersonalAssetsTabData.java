package nic.hp.ccmgnrega.data;

import android.content.Context;
import android.view.View;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.databinding.FragmentPersonalAssetsBinding;
import nic.hp.ccmgnrega.model.JobCardData;
import nic.hp.ccmgnrega.model.PersonalAsset;

public class PersonalAssetsTabData {

    private static FragmentPersonalAssetsBinding binding;
    private static Context context;
    private static JobCardData jobCardData;
    private static HashMap<String,String> workStatusMapping = new HashMap<>();

    public static void populatePersonalAssetsTabData(FragmentPersonalAssetsBinding binding, Context context, JobCardData jobCardData) {
        PersonalAssetsTabData.binding = binding;
        PersonalAssetsTabData.context = context;
        PersonalAssetsTabData.jobCardData = jobCardData;
        clearData();
        setPageInformation();
        setPersonalAssetList();
        setWorkStatusMapping();
    }

    private static void setWorkStatusMapping() {
        workStatusMapping.put("01",context.getString(R.string.newWork));
        workStatusMapping.put("02",context.getString(R.string.approved));
        workStatusMapping.put("03",context.getString(R.string.ongoing));
        workStatusMapping.put("04",context.getString(R.string.suspended));
        workStatusMapping.put("05",context.getString(R.string.completed));
        workStatusMapping.put("06","Physically complete");
        workStatusMapping.put("91","Sleep-Admin Sanction");
        workStatusMapping.put("92","Sleep-Tech Sanction");
        workStatusMapping.put("93","Sleep-Fin Sanction");
    }

    private static void setPageInformation() {
        binding.clickInfoText.setText("*" + context.getString(R.string.toViewDetailsClickOn) + " ");

        if (jobCardData.getPersonalAssets() == null || jobCardData.getPersonalAssets().size() == 0) {
            binding.noPersonalAssetInfo.setVisibility(View.VISIBLE);
        } else {
            binding.noPersonalAssetInfo.setVisibility(View.GONE);
        }
    }

    private static void setPersonalAssetList() {
        for (PersonalAsset personalAsset : jobCardData.getPersonalAssets()) {
            if (personalAsset == null) {
                jobCardData.getPersonalAssets().remove(personalAsset);
                continue;
            }
            if (personalAsset.getWorkCode() == null || personalAsset.getWorkCode().isEmpty()) {
                jobCardData.getPersonalAssets().remove(personalAsset);
                continue;
            }
            if (personalAsset.getWorkStatus() != null && !personalAsset.getWorkStatus().isEmpty()) {
                String status = workStatusMapping.get(personalAsset.getWorkStatus().replaceAll("\\s+",""));
                personalAsset.setWorkStatus(status);
            }

            SimpleDateFormat receivedDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            if (personalAsset.getStartDate() != null && !personalAsset.getStartDate().isEmpty()) {
                try {
                    Date date = receivedDateFormat.parse(personalAsset.getStartDate());
                    String displayDate = displayDateFormat.format(date);
                    personalAsset.setStartDate(displayDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (personalAsset.getCompletionDate() != null && !personalAsset.getCompletionDate().isEmpty()) {
                try {
                    Date date = receivedDateFormat.parse(personalAsset.getCompletionDate());
                    String displayDate = displayDateFormat.format(date);
                    personalAsset.setCompletionDate(displayDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (personalAsset.getVillageName() != null && !personalAsset.getVillageName().isEmpty()) {
                personalAsset.setVillageName(personalAsset.getVillageName().trim());
            }
            if (personalAsset.getGramPanchayatName() != null && !personalAsset.getGramPanchayatName().isEmpty()) {
                personalAsset.setGramPanchayatName(personalAsset.getGramPanchayatName().trim());
            }
            if (personalAsset.getSanctionedAmount() != null && !personalAsset.getSanctionedAmount().isEmpty()) {
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                double amount = Double.parseDouble(personalAsset.getSanctionedAmount());
                personalAsset.setSanctionedAmount(formatter.format(amount * 100000).replaceAll(".[0-9]+$",""));
            }
            if (personalAsset.getPermissibleWork() != null && !personalAsset.getPermissibleWork().isEmpty()) {
                personalAsset.setPermissibleWork(personalAsset.getPermissibleWork().trim());
            }
        }
    }

    public static void clearData() {
        workStatusMapping.clear();
    }
}
