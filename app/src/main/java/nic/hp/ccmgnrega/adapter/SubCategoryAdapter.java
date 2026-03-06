package nic.hp.ccmgnrega.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.model.CategoryModel;
import nic.hp.ccmgnrega.model.SubCategoryModel;

public class SubCategoryAdapter extends ArrayAdapter<SubCategoryModel> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<SubCategoryModel> alCategoryModel;

    public SubCategoryAdapter(Context context, int textViewResourceId,
                              ArrayList<SubCategoryModel> alCategoryModel) {
        super(context, textViewResourceId, alCategoryModel);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.alCategoryModel = alCategoryModel;
    }

    public int getCount() {
        return alCategoryModel.size();
    }

    public SubCategoryModel getItem(int position) {
        return alCategoryModel.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        if(position == 0)
            label.setText(alCategoryModel.get(position).getSubCategoryName());
        else
            label.setText(alCategoryModel.get(position).getSubCategoryName() /*+ " (" + alWorkerModel.get(position).getApplicantId() + ")"*/);
        return label;

    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.spinner_list_item, parent, false);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        if(position == 0)
            tvTitle.setText(alCategoryModel.get(position).getSubCategoryName());
        else
            tvTitle.setText(alCategoryModel.get(position).getSubCategoryName() /*+ " (" + alWorkerModel.get(position).getBlock_code() + ")"*/);
        return convertView;

    }

}