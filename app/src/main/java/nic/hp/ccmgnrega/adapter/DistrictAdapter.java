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
import nic.hp.ccmgnrega.model.DistrictModel;

public class DistrictAdapter extends ArrayAdapter<DistrictModel> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<DistrictModel> alDistrictModel;

    public DistrictAdapter(Context context, int textViewResourceId,
                           ArrayList<DistrictModel> alDistrictModel) {
        super(context, textViewResourceId, alDistrictModel);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.alDistrictModel = alDistrictModel;
    }

    public int getCount() {
        return alDistrictModel.size();
    }

    public DistrictModel getItem(int position) {
        return alDistrictModel.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    public int getItemPosition(String distCode){
        int position=0;
        for(int i=0;i<alDistrictModel.size();i++){
            if(alDistrictModel.get(i).getDistrictCode().equalsIgnoreCase(distCode)) {
                position=i;
                break;
            }

        }
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        if(position == 0)
            label.setText(alDistrictModel.get(position).getDistrictName());
        else
            label.setText(alDistrictModel.get(position).getDistrictName() /*+ " (" + alWorkerModel.get(position).getApplicantId() + ")"*/);
        return label;

    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.spinner_list_item, parent, false);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
      tvTitle.setTextColor(Color.BLACK);
        if(position == 0)
            tvTitle.setText(alDistrictModel.get(position).getDistrictName());
        else
            tvTitle.setText(alDistrictModel.get(position).getDistrictName() /*+ " (" + alWorkerModel.get(position).getBlock_code() + ")"*/);
        return convertView;

    }

}