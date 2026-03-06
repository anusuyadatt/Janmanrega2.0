package nic.hp.ccmgnrega.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import nic.hp.ccmgnrega.R;


public class ResourceAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String> alResource;

    public ResourceAdapter(Context context, ArrayList<String> alResource) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.alResource = sortListAlphabatically(alResource);
    }

    public int getCount(){
        return alResource.size();
    }

    public String getItem(int position){
        return alResource.get(position);
    }

    public long getItemId(int position){
        return position;
    }
    

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.resource_list_item, parent, false);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(alResource.get(position));
        return convertView;
    }

    private ArrayList<String> sortListAlphabatically(ArrayList<String> list ){
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        return  list;
    }
}
