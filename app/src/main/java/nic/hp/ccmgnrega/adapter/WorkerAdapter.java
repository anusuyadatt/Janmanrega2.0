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
import nic.hp.ccmgnrega.model.WorkerModel;

public class WorkerAdapter extends ArrayAdapter<WorkerModel> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<WorkerModel> alWorkerModel;

    public WorkerAdapter(Context context, int textViewResourceId,
                                   ArrayList<WorkerModel> alWorkerModel) {
        super(context, textViewResourceId, alWorkerModel);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.alWorkerModel = alWorkerModel;
    }

    public int getCount() {
        return alWorkerModel.size();
    }

    public WorkerModel getItem(int position) {
        return alWorkerModel.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        if(position == 0)
            label.setText(alWorkerModel.get(position).getApplicantName());
        else
            label.setText(alWorkerModel.get(position).getApplicantName() /*+ " (" + alWorkerModel.get(position).getApplicantId() + ")"*/);
        return label;

    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.spinner_list_item, parent, false);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        if(position == 0)
            tvTitle.setText(alWorkerModel.get(position).getApplicantName());
        else
            tvTitle.setText(alWorkerModel.get(position).getApplicantName() /*+ " (" + alWorkerModel.get(position).getBlock_code() + ")"*/);
        return convertView;

    }

}