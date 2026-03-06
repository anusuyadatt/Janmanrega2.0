package nic.hp.ccmgnrega.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import nic.hp.ccmgnrega.R;

public class CustomListingAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private List<String> itemText;

    public CustomListingAdapter(Activity context, List<String> itemText) {
        super(context, R.layout.listrow_details, itemText);
        this.context = context;
        this.itemText = itemText;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listrow_details, null, false);
        TextView itemTextView = rowView.findViewById(R.id.worktext);
        itemTextView.setText(itemText.get(position));
        return rowView;
    }

}