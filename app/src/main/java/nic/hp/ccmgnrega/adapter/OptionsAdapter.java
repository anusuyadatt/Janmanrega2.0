package nic.hp.ccmgnrega.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.common.MySharedPref;

public class OptionsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    ArrayList<String> itemName;
    ArrayList<Integer> itemImage;

    public OptionsAdapter(Context context, ArrayList<String> name, ArrayList<Integer> image) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.itemName = name;
        this.itemImage = image;
    }
    public int getCount() {
        return itemImage.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.layout_options_list_item, parent, false);
        TextView tvOptions_name = (TextView) convertView.findViewById(R.id.tvOptions_name);
        ImageView ivOptipons_pic=(ImageView) convertView.findViewById(R.id.ivOptions_pic);
        LinearLayout llCard=(LinearLayout) convertView.findViewById(R.id.llCard);
        tvOptions_name.setText(itemName.get(position));
        ivOptipons_pic.setImageResource(itemImage.get(position));
        if(MySharedPref.getLoginPin(context).isEmpty() && (itemName.get(position).equalsIgnoreCase(context.getString(R.string.know_worker_att_pay)) ||itemName.get(position).equalsIgnoreCase(context.getString(R.string.give_asset_feedback))) )
            llCard.setBackgroundResource(R.drawable.card_main_130_dark_grey);
        else
            llCard.setBackgroundResource(R.drawable.card_main_130);

        return convertView;
    }


}
