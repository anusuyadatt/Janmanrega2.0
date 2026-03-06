package nic.hp.ccmgnrega.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.common.SecondLevelExpandableListView;
import nic.hp.ccmgnrega.data.AttendanceTabData;
import nic.hp.ccmgnrega.model.AttendanceInfo;

public class AttendanceExpandableListAdapter1 extends BaseExpandableListAdapter {

    private Context context;
    private List<String> applicantIdList;
    private List<List<String>> workCodeList;
    private HashMap<String, HashMap<String,List<AttendanceInfo>>> applicantIdToWorkCodeToAttendanceMapping;

    private String languageCode;
    private int textSize = 14;
    public AttendanceExpandableListAdapter1(Context context,
                                            List<String> applicantIdList,
                                            List<List<String>> workCodeList,
                                            HashMap<String, HashMap<String,List<AttendanceInfo>>> applicantIdToWorkCodeToAttendanceMapping,
                                            String languageCode) {
        this.context = context;
        this.applicantIdList = applicantIdList;
        this.workCodeList = workCodeList;
        this.applicantIdToWorkCodeToAttendanceMapping = applicantIdToWorkCodeToAttendanceMapping;
        this.languageCode = languageCode;
    }

    @Override
    public List<String> getChild(int lstPosn, int expanded_ListPosition) {
        List<String> workCodeList = this.workCodeList.get(lstPosn);
        return workCodeList;
    }

    @Override
    public long getChildId(int listPosition, int expanded_ListPosition) {
        return expanded_ListPosition;
    }

    @Override
    public View getChildView(int lstPosn, final int expanded_ListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        List<String> workCodeList = getChild(lstPosn, expanded_ListPosition);
//        if (workCodeList == null || workCodeList.isEmpty()) {
//            workCodeList.add("No Attendance");
//        }

        List<List<AttendanceInfo>> attendance = new ArrayList<>();
        HashMap<String,List<AttendanceInfo>> workCodeToAttendanceMapping = applicantIdToWorkCodeToAttendanceMapping.get(applicantIdList.get(lstPosn));

        for (String workCode : workCodeList) {
            attendance.add(workCodeToAttendanceMapping.get(workCode));
        }

        final SecondLevelExpandableListView secondLevelExpandableListView = new SecondLevelExpandableListView(context);
        secondLevelExpandableListView.setGroupIndicator(context.getDrawable(R.drawable.grey_group_indicator));
        secondLevelExpandableListView.setAdapter(new AttendanceExpandableListAdapter2(context, workCodeList, attendance, languageCode, lstPosn));
        secondLevelExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    secondLevelExpandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
        return secondLevelExpandableListView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return 1;
    }

    @Override
    public String getGroup(int listPosition) {
        String applicantId = this.applicantIdList.get(listPosition);
        return applicantId;
    }

    @Override
    public int getGroupCount() {
        return this.applicantIdList.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group1, null);
        }

        String applicantId = getGroup(listPosition);
        Pair<String,String> listTitles = AttendanceTabData.getFirstLevelApplicantDetail(applicantId);
        String listTitle1 = listTitles.first;
        if (listTitle1 == null || listTitle1.isEmpty()) {
            listTitle1 = applicantId;
        }
        String listTitle2 = listTitles.second;
        if (listTitle2 == null || listTitle2.isEmpty()) {
            listTitle2 = "";
        }

        TextView listTitle1TextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitle1TextView.setTypeface(null, Typeface.BOLD);
        listTitle1TextView.setText(listTitle1);
        listTitle1TextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,textSize);

        TextView listTitle2TextView = (TextView) convertView.findViewById(R.id.listTitle1);
        listTitle2TextView.setText(listTitle2);
        listTitle2TextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);

        return convertView;

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

}

