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
import nic.hp.ccmgnrega.common.Constant;
import nic.hp.ccmgnrega.model.VillageModel;

public class VillageAdapter extends ArrayAdapter<VillageModel> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<VillageModel> alVillageModel;

    public VillageAdapter(Context context, int textViewResourceId,
                          ArrayList<VillageModel> alVillageModel) {
        super(context, textViewResourceId, alVillageModel);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.alVillageModel = alVillageModel;
    }

    public int getCount() {
        return alVillageModel.size();
    }

    public VillageModel getItem(int position) {
        return alVillageModel.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        if(position == 0)
            label.setText(alVillageModel.get(position).getVillageName());
        else
            label.setText(alVillageModel.get(position).getVillageName() /*+ " (" + alWorkerModel.get(position).getApplicantId() + ")"*/);
        return label;

    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.spinner_list_item, parent, false);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setTextColor(Color.BLACK);
        if(position == 0)
            tvTitle.setText(alVillageModel.get(position).getVillageName());
        else
            tvTitle.setText(alVillageModel.get(position).getVillageName() /*+ " (" + alWorkerModel.get(position).getBlock_code() + ")"*/);
        return convertView;

    }

}