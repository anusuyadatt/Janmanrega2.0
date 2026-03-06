package nic.hp.ccmgnrega.adapter;


import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import nic.hp.ccmgnrega.R;

public class WorksExpandableListAdapter extends BaseExpandableListAdapter {

    private final SparseArray<WorksGroup> groups;
    public LayoutInflater inflater;
    public Activity activity;
    private ExpandableListView expendListView;
    private int lastGroupPosition = -1;

    public WorksExpandableListAdapter(Activity act, SparseArray<WorksGroup> groups, ExpandableListView expendListView) {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
        this.expendListView = expendListView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).worktext.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String childrenworktext = (String) getChild(groupPosition, childPosition);
        TextView worktext ;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_details, null);
        }

        worktext = convertView.findViewById(R.id.worktext);
        worktext.setText(childrenworktext);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).worktext.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        if (groupPosition != lastGroupPosition) {
            expendListView.collapseGroup(lastGroupPosition);
        }
        super.onGroupExpanded(groupPosition);
        lastGroupPosition = groupPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_group, null);
        }
        WorksGroup group = (WorksGroup) getGroup(groupPosition);
        ((CheckedTextView) convertView).setText(group.string);
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}