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
import nic.hp.ccmgnrega.common.IntegerStringComparator;
import nic.hp.ccmgnrega.common.ExpandableListMediaStateManager;
import nic.hp.ccmgnrega.common.MediaPlayerHelper;
import nic.hp.ccmgnrega.common.TaskCompletionListener;
import nic.hp.ccmgnrega.common.Utility;
import nic.hp.ccmgnrega.data.JobCardDataAccess;
import nic.hp.ccmgnrega.model.AttendanceInfo;

public class AttendanceExpandableListAdapter2 extends BaseExpandableListAdapter implements TaskCompletionListener {

    private Context context;
    List<String> workCodeList;
    int textSize = 14;
    List<List<AttendanceInfo>> attendance;
    String languageCode;
    int parentIndex;

    public AttendanceExpandableListAdapter2(Context context,
                                            List<String> workCodeList,
                                            List<List<AttendanceInfo>> attendance,
                                            String languageCode,
                                            int parentIndex) {
        this.context = context;
        this.workCodeList = workCodeList;
        this.attendance = attendance;
        this.languageCode = languageCode;
        this.parentIndex = parentIndex;
        MediaPlayerHelper.setTaskCompletionListener(this);
    }

    @Override
    public AttendanceInfo getChild(int lstPosn, int expanded_ListPosition) {
        AttendanceInfo attendanceInfo = this.attendance.get(lstPosn).get(expanded_ListPosition);
        return attendanceInfo;
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
            convertView = layoutInflater.inflate(R.layout.attendance_list_item, null);
        }
        final AttendanceInfo expandedListText = getChild(lstPosn, expanded_ListPosition);

        String msrNo = context.getString(R.string.msrNo);
        String date = context.getString(R.string.date);
        String attendance = context.getString(R.string.attendanceTranslated);

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
        if (expandedListText.getDate().equalsIgnoreCase(date)) {
            cell2.setTypeface(null, Typeface.BOLD);
        } else {
            cell2.setTypeface(null, Typeface.NORMAL);
        }
        cell2.setText(expandedListText.getDate());
        cell2.setTextColor(ContextCompat.getColor(context, R.color.all_text));
        cell2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        cell2.setPadding(2, 0, 2, 0);

        TextView cell3 = (TextView) convertView.findViewById(R.id.cell3);
        if (expandedListText.getAttendance().equalsIgnoreCase(attendance)) {
            cell3.setTypeface(null, Typeface.BOLD);
            cell3.setTextColor(ContextCompat.getColor(context, R.color.all_text));
        } else if (expandedListText.getAttendance().equalsIgnoreCase(context.getString(R.string.present))) {
            cell3.setTypeface(null, Typeface.NORMAL);
            cell3.setTextColor(ContextCompat.getColor(context, R.color.accepted));
        } else if (expandedListText.getAttendance().equalsIgnoreCase(context.getString(R.string.absent))) {
            cell3.setTypeface(null, Typeface.NORMAL);
            cell3.setTextColor(Color.RED);
        } else {
            cell3.setTypeface(null, Typeface.NORMAL);
            cell3.setTextColor(ContextCompat.getColor(context, R.color.all_text));
        }
        cell3.setText(expandedListText.getAttendance());
        cell3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        cell3.setPadding(2, 0, 2, 0);

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        if (this.attendance.get(listPosition) == null) {
            return 0;
        }
        return this.attendance.get(listPosition).size();
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
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group2, null);
        }

        String workCode = getGroup(listPosition);
        String workName = JobCardDataAccess.attendanceWorkCodeToWorkNameMapping.get(workCode);

        String workNameDisplay = workCode;
//        if (workCode == null || workCode.isEmpty()) {
//            workNameDisplay = "No Attendance";
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
        if (!workCode.equalsIgnoreCase(context.getString(R.string.noAttendance))) {
            String workName = JobCardDataAccess.attendanceWorkCodeToWorkNameMapping.get(workCode);
            if (workName != null && !workName.isEmpty()) {
                speech += context.getString(R.string.workName) + period;
                speech += workName + period;
            } else {
                speech += context.getString(R.string.workCode) + period;
                speech += workCode + period;
            }
        }

        String msrNoString = context.getString(R.string.msrNo);
        String dateString = context.getString(R.string.date);
        String attendanceString = context.getString(R.string.attendanceTranslated);

        if (attendance == null || attendance.isEmpty() || attendance.get(listPosition) == null || attendance.get(listPosition).isEmpty()) {
            speech += context.getString(R.string.noAttendance) + period;
        } else {
            SimpleDateFormat indianDateFormat = new SimpleDateFormat("dd/MM/yyyy");

            for (AttendanceInfo attendanceInfo: attendance.get(listPosition)) {
                if (attendanceInfo.getMsrNo().equalsIgnoreCase(msrNoString)) {
                    continue;
                }
                if (!attendanceInfo.getMsrNo().isEmpty()) {
                    speech += msrNoString + period + attendanceInfo.getMsrNo() + period;
                }
                try {
                    Date date = indianDateFormat.parse(attendanceInfo.getDate());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    if (!attendanceInfo.getDate().isEmpty()) {
                        speech += dateString + period + calendar.get(Calendar.DATE) + " " + new SimpleDateFormat("MMMM").format(calendar.getTime()) + " " + calendar.get(Calendar.YEAR) + period;
                    }
                } catch (ParseException e) {
                    speech += dateString + period + attendanceInfo.getDate() + period;
                }
                if (!attendanceInfo.getAttendance().isEmpty()) {
                    speech += attendanceString + period + attendanceInfo.getAttendance() + period;
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