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
import nic.hp.ccmgnrega.model.PanchayatModel;

public class PanchayatAdapter extends ArrayAdapter<PanchayatModel> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<PanchayatModel> alPanchayatModel;

    public PanchayatAdapter(Context context, int textViewResourceId,
                            ArrayList<PanchayatModel> alPanchayatModel) {
        super(context, textViewResourceId, alPanchayatModel);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.alPanchayatModel = alPanchayatModel;
    }

    public int getCount() {
        return alPanchayatModel.size();
    }

    public PanchayatModel getItem(int position) {
        return alPanchayatModel.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        if(position == 0)
            label.setText(alPanchayatModel.get(position).getPanchayatName());
        else
            label.setText(alPanchayatModel.get(position).getPanchayatName() /*+ " (" + alWorkerModel.get(position).getApplicantId() + ")"*/);
        return label;

    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.spinner_list_item, parent, false);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setTextColor(Color.BLACK);
        if(position == 0)
            tvTitle.setText(alPanchayatModel.get(position).getPanchayatName());
        else
            tvTitle.setText(alPanchayatModel.get(position).getPanchayatName() /*+ " (" + alWorkerModel.get(position).getBlock_code() + ")"*/);
        return convertView;

    }

}