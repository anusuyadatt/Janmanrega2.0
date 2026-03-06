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
import nic.hp.ccmgnrega.model.UserTypeModel;

public class UserTypeAdapter extends ArrayAdapter<UserTypeModel> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<UserTypeModel> alUserTypeModel;

    public UserTypeAdapter(Context context, int textViewResourceId,
                           ArrayList<UserTypeModel> alUserTypeModel) {
        super(context, textViewResourceId, alUserTypeModel);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.alUserTypeModel = alUserTypeModel;
    }

    public int getCount() {
        return alUserTypeModel.size();
    }

    public UserTypeModel getItem(int position) {
        return alUserTypeModel.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    public int getItemPosition(String userTypeCode){
        int position=0;
        for(int i=0;i<alUserTypeModel.size();i++){
            if(alUserTypeModel.get(i).getUserTypeCode().equalsIgnoreCase(userTypeCode)) {
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
            label.setText(alUserTypeModel.get(position).getUserTypeName());
        else
            label.setText(alUserTypeModel.get(position).getUserTypeName() /*+ " (" + alWorkerModel.get(position).getApplicantId() + ")"*/);
        return label;

    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.spinner_list_item, parent, false);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
       tvTitle.setTextColor(Color.BLACK);
        if(position == 0)
            tvTitle.setText(alUserTypeModel.get(position).getUserTypeName());
        else
            tvTitle.setText(alUserTypeModel.get(position).getUserTypeName() /*+ " (" + alWorkerModel.get(position).getBlock_code() + ")"*/);
        return convertView;

    }

}