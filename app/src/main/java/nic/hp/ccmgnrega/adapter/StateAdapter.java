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
import nic.hp.ccmgnrega.model.StateModel;

public class StateAdapter extends ArrayAdapter<StateModel> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<StateModel> alStateModel;

    public StateAdapter(Context context, int textViewResourceId,
                        ArrayList<StateModel> alStateModel) {
        super(context, textViewResourceId, alStateModel);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.alStateModel = alStateModel;
    }

    public int getCount() {
        return alStateModel.size();
    }

    public StateModel getItem(int position) {
        return alStateModel.get(position);
    }
  public int getItemPosition(String stateCode){
        int position=0;
        for(int i=0;i<alStateModel.size();i++){
            if(alStateModel.get(i).getStateCode().equalsIgnoreCase(stateCode))
            {
                position=i;
                break;
            }

        }
        return position;
  }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
            label.setTextColor(Color.BLACK);
        if(position == 0)
            label.setText(alStateModel.get(position).getStateName());
        else
            label.setText(alStateModel.get(position).getStateName() /*+ " (" + alWorkerModel.get(position).getApplicantId() + ")"*/);
        return label;

    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.spinner_list_item, parent, false);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setTextColor(Color.BLACK);
        if(position == 0)
            tvTitle.setText(alStateModel.get(position).getStateName());
        else
            tvTitle.setText(alStateModel.get(position).getStateName() /*+ " (" + alWorkerModel.get(position).getBlock_code() + ")"*/);
        return convertView;

    }

}