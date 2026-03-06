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
import nic.hp.ccmgnrega.model.BlockModel;

public class BlockAdapter extends ArrayAdapter<BlockModel> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<BlockModel> alBlockModel;

    public BlockAdapter(Context context, int textViewResourceId,
                        ArrayList<BlockModel> alBlockModel) {
        super(context, textViewResourceId, alBlockModel);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.alBlockModel = alBlockModel;
    }

    public int getCount() {
        return alBlockModel.size();
    }

    public BlockModel getItem(int position) {
        return alBlockModel.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    public int getItemPosition(String blockCode){
        int position=0;
        for(int i=0;i<alBlockModel.size();i++){
            if(alBlockModel.get(i).getBlockCode().equalsIgnoreCase(blockCode)) {
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
            label.setText(alBlockModel.get(position).getBlockName());
        else
            label.setText(alBlockModel.get(position).getBlockName() /*+ " (" + alWorkerModel.get(position).getApplicantId() + ")"*/);
        return label;

    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.spinner_list_item, parent, false);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
       tvTitle.setTextColor(Color.BLACK);
        if(position == 0)
            tvTitle.setText(alBlockModel.get(position).getBlockName());
        else
            tvTitle.setText(alBlockModel.get(position).getBlockName() /*+ " (" + alWorkerModel.get(position).getBlock_code() + ")"*/);
        return convertView;

    }

}