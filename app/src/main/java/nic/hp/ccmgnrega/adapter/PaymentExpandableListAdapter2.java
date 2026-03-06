package nic.hp.ccmgnrega.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.common.Constant;
import nic.hp.ccmgnrega.common.IntegerStringComparator;
import nic.hp.ccmgnrega.common.ExpandableListMediaStateManager;
import nic.hp.ccmgnrega.common.MediaPlayerHelper;
import nic.hp.ccmgnrega.common.TaskCompletionListener;
import nic.hp.ccmgnrega.common.Utility;
import nic.hp.ccmgnrega.data.JobCardDataAccess;
import nic.hp.ccmgnrega.model.PaymentInfo;

public class PaymentExpandableListAdapter2 extends BaseExpandableListAdapter implements TaskCompletionListener {

    private Context context;
    List<List<PaymentInfo>> payments;
    List<String> workCodeList;
    private int textSize = 14;
    String languageCode;
    int parentIndex;

    public PaymentExpandableListAdapter2(Context context,
                                         List<String> workCodeList,
                                         List<List<PaymentInfo>> payments,
                                         String languageCode,
                                         int parentIndex) {
        this.context = context;
        this.workCodeList = workCodeList;
        this.payments = payments;
        this.languageCode = languageCode;
        this.parentIndex = parentIndex;
        MediaPlayerHelper.setTaskCompletionListener(this);
    }

    @Override
    public PaymentInfo getChild(int lstPosn, int expanded_ListPosition) {
        PaymentInfo paymentInfo = this.payments.get(lstPosn).get(expanded_ListPosition);
        return  paymentInfo;
    }

    @Override
    public long getChildId(int listPosition, int expanded_ListPosition) {
        return expanded_ListPosition;
    }

    @Override
    public View getChildView(int lstPosn, final int expanded_ListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.payment_list_item, null);
        }
        final PaymentInfo expandedListText = getChild(lstPosn, expanded_ListPosition);

        String msrNo = context.getString(R.string.msrNo);
        String daysWorked = context.getString(R.string.daysWorked);
        String totalWage = context.getString(R.string.totalWage);
        String bankPostOffice = context.getString(R.string.bankPostOffice);
        String creditedDate = context.getString(R.string.creditedDate);

        if (expandedListText.getMsrNo().equalsIgnoreCase(msrNo)) {
            convertView.findViewById(R.id.tableRow).setBackgroundColor(Color.rgb(146, 214, 236));
        } else {
            convertView.findViewById(R.id.tableRow).setBackgroundColor(Color.rgb(255, 255, 255));
        }

        TextView cell1 = (TextView) convertView.findViewById(R.id.cell1);
        if (expandedListText.getMsrNo().equalsIgnoreCase(msrNo)) {
            cell1.setTypeface(null, Typeface.BOLD);
        } else {
            cell1.setTypeface(null, Typeface.NORMAL);
        }
        cell1.setText(expandedListText.getMsrNo());
        cell1.setTextColor(ContextCompat.getColor(context, R.color.all_text));
        cell1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        cell1.setPadding(2, 0, 2, 0);

        TextView cell2 = (TextView) convertView.findViewById(R.id.cell2);
        if (expandedListText.getDaysWorked().equalsIgnoreCase(daysWorked)) {
            cell2.setTypeface(null, Typeface.BOLD);
        } else {
            cell2.setTypeface(null, Typeface.NORMAL);
        }
        cell2.setText(expandedListText.getDaysWorked());
        cell2.setTextColor(ContextCompat.getColor(context, R.color.all_text));
        cell2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        cell2.setPadding(2, 0, 2, 0);

        TextView cell3 = (TextView) convertView.findViewById(R.id.cell3);
        String applicantTotalWage = expandedListText.getTotalWage();
        if (applicantTotalWage.equalsIgnoreCase(totalWage)) {
            cell3.setTypeface(null, Typeface.BOLD);
        } else {
            cell3.setTypeface(null, Typeface.NORMAL);
            if (!applicantTotalWage.isEmpty()) {
                applicantTotalWage = Constant.rupeesSymbol + applicantTotalWage;
            } else {
                applicantTotalWage = "-";
            }
        }
        cell3.setText(applicantTotalWage);
        cell3.setTextColor(ContextCompat.getColor(context, R.color.all_text));
        cell3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        cell3.setPadding(2, 0, 2, 0);

        TextView cell4 = (TextView) convertView.findViewById(R.id.cell4);
        String bankName = expandedListText.getBankName();
        if (bankName.equalsIgnoreCase(bankPostOffice)) {
            cell4.setTypeface(null, Typeface.BOLD);
        } else {
            cell4.setTypeface(null, Typeface.NORMAL);
            if (bankName.isEmpty()) {
                bankName = "-";
            } else if (!expandedListText.getAccountNumber().isEmpty())
            {
                bankName += " (A/c " + expandedListText.getAccountNumber() + ")";
            }
        }

        cell4.setText(bankName);
        cell4.setTextColor(ContextCompat.getColor(context, R.color.all_text));
        cell4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        cell4.setPadding(2, 0, 2, 0);

        TextView cell5 = (TextView) convertView.findViewById(R.id.cell5);
        if (expandedListText.getCreditedDate().equalsIgnoreCase(creditedDate)) {
            cell5.setTypeface(null, Typeface.BOLD);
            cell5.setTextColor(context.getColor(R.color.all_text));
        } else {
            cell5.setTypeface(null, Typeface.NORMAL);
            if (expandedListText.getPaymentStatus().equalsIgnoreCase(Constant.CENTER_SHARE_CREDITED)
                || expandedListText.getPaymentStatus().equalsIgnoreCase(Constant.STATE_SHARE_CREDITED)) {
                cell5.setTextColor(ContextCompat.getColor(context, R.color.accepted));
            } else {
                cell5.setTextColor(context.getColor(R.color.orange));
            }
        }
        cell5.setText(expandedListText.getCreditedDate());
        cell5.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        cell5.setPadding(2, 0, 2, 0);

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        if (this.payments.get(listPosition) == null) {
            return 0;
        }
        return this.payments.get(listPosition).size();
    }

    @Override
    public String getGroup(int listPosition) {
        String workCode = this.workCodeList.get(listPosition);
        return workCode;
    }

    @Override
    public int getGroupCount() {
        return this.workCodeList.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group2, null);
        }

        String workCode = getGroup(listPosition);
        String workName = JobCardDataAccess.paymentWorkCodeToWorkNameMapping.get(workCode);

        String workNameDisplay = workCode;
//        if (workCode == null || workCode.isEmpty()) {
//            workNameDisplay = "No Payments";
//        }

        if (workName != null && !workName.isEmpty()) {
            workNameDisplay = workName + " (" + workCode + ")";
        }


        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.rowSecondText);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(workNameDisplay);
        listTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);

        addAudioFunctionality(convertView, listPosition);

        return convertView;
    }

    private void addAudioFunctionality(View convertView, int listPosition) {
        if (!Utility.translationAllowed(context)) {
            return;
        }
        ImageView speakerButton = (ImageView) convertView.findViewById(R.id.playPauseButton);
        speakerButton.setVisibility(View.VISIBLE);
        if (ExpandableListMediaStateManager.isMediaPlaying(parentIndex, listPosition)) {
            speakerButton.setBackground(ContextCompat.getDrawable(context,R.drawable.stop_button));
        } else {
            speakerButton.setBackground(ContextCompat.getDrawable(context,R.drawable.speaker));
        }
        speakerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });


        speakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View speakerButton) {
                if (ExpandableListMediaStateManager.isMediaPlaying(parentIndex, listPosition)) {
                    speakerButton.setBackground(ContextCompat.getDrawable(context,R.drawable.speaker));
                    ExpandableListMediaStateManager.setStateToStopped(parentIndex, listPosition);
                    MediaPlayerHelper.releaseMediaPlayer();
                    notifyDataSetChanged();
                } else {
                    MediaPlayerHelper.releaseMediaPlayer();
                    ExpandableListMediaStateManager.setStateToPlaying(parentIndex, listPosition);
                    speakerButton.setBackground(ContextCompat.getDrawable(context,R.drawable.stop_button));
                    notifyDataSetChanged();
                    onButtonClicked(parentIndex, listPosition, (ImageView) speakerButton);
                }
            }
        });
    }

    public void onButtonClicked(int parentIndex, int listPosition, ImageView speakerButton) {

        String speech = getSpeech(parentIndex, listPosition);
        speech = Utility.replaceMultiplePeriods(speech, context.getString(R.string.period));
        MediaPlayerHelper.playMedia(context, speakerButton, speech);
    }

    private String getSpeech(int parentIndex, int listPosition) {

        String speech = "";
        String period = context.getString(R.string.period) + " ";

        List<String> applicantIdList = new ArrayList<>(JobCardDataAccess.applicantIdSet);
        Collections.sort(applicantIdList, new IntegerStringComparator());

        String applicantId = applicantIdList.get(parentIndex);
        String applicantName = JobCardDataAccess.applicantIdToJobCardDetailMapping.get(applicantId).getApplicantName();
        if (applicantName != null && !applicantName.isEmpty()) {
            speech += context.getString(R.string.applicantName) + period;
            speech += applicantName + period;
        } else {
            speech += applicantId + period;
        }

        String workCode = workCodeList.get(listPosition);
        if (!workCode.equalsIgnoreCase(context.getString(R.string.noPayments))) {
            String workName = JobCardDataAccess.paymentWorkCodeToWorkNameMapping.get(workCode);
            if (workName != null && !workName.isEmpty()) {
                speech += context.getString(R.string.workName) + period;
                speech += workName + period;
            } else {
                speech += context.getString(R.string.workCode) + period;
                speech += workCode + period;
            }
        }

        String msrNo = context.getString(R.string.msrNo);
        String daysWorked = context.getString(R.string.daysWorked);
        String totalWage = context.getString(R.string.totalWage);
        String bankPostOffice = context.getString(R.string.bankPostOffice);
        String creditedDate = context.getString(R.string.creditedDate);
        String paymentStatus = context.getString(R.string.paymentStatus);
        String accountNumberString = context.getString(R.string.accountNumber);
        String rupees = context.getString(R.string.rupees);

        if (payments == null || payments.isEmpty() || payments.get(listPosition) == null || payments.get(listPosition).isEmpty()) {
            speech += context.getString(R.string.noPayments) + period;
        } else {
            SimpleDateFormat indianDateFormat = new SimpleDateFormat("dd/MM/yyyy");

            for (PaymentInfo paymentInfo: payments.get(listPosition)) {
                if (paymentInfo.getMsrNo().equalsIgnoreCase(context.getString(R.string.msrNo))) {
                    continue;
                }

                if (!paymentInfo.getMsrNo().isEmpty()) {
                    speech += msrNo + period + paymentInfo.getMsrNo() + period;
                }
                if (!paymentInfo.getDaysWorked().isEmpty()) {
                    speech += daysWorked + period + paymentInfo.getDaysWorked() + period;
                }
                if (!paymentInfo.getTotalWage().isEmpty()) {
                    speech += totalWage + period + rupees + " " + paymentInfo.getTotalWage() + period;
                }
                if (!paymentInfo.getBankName().isEmpty()) {
                    speech += bankPostOffice + period + paymentInfo.getBankName() + period;
                }
                if (!paymentInfo.getAccountNumber().isEmpty()) {
                    String accountNumber = paymentInfo.getAccountNumber();
                    if (accountNumber.length() >= 4) {
                        String lastFourDigits = accountNumber.substring(accountNumber.length() - 4);
                        lastFourDigits = lastFourDigits.replace("", " ").trim();
                        speech += accountNumberString + period + lastFourDigits + period;
                    }
                }

                if ((paymentInfo.getPaymentStatus().equalsIgnoreCase(Constant.CENTER_SHARE_CREDITED)
                        || paymentInfo.getPaymentStatus().equalsIgnoreCase(Constant.STATE_SHARE_CREDITED))
                        && !paymentInfo.getCreditedDate().isEmpty()) {
                    try {
                        Date date = indianDateFormat.parse(paymentInfo.getCreditedDate());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        speech += creditedDate + period + calendar.get(Calendar.DATE) + " " + new SimpleDateFormat("MMMM").format(calendar.getTime()) + " " + calendar.get(Calendar.YEAR) + period;
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                } else if (!paymentInfo.getPaymentStatus().isEmpty()) {
                    speech += paymentStatus + period + paymentInfo.getPaymentStatus() + period;
                }
            }
        }

        return speech;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    @Override
    public void onAllTasksCompleted() {
        notifyDataSetChanged();
    }
}