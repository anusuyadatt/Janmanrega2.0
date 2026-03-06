package nic.hp.ccmgnrega.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import nic.hp.ccmgnrega.R
import nic.hp.ccmgnrega.model.LgdDistrictModel
import nic.hp.ccmgnrega.model.LgdStateModel


class LgdDistrictAdapter (
    private val context: Context,
    private val items:ArrayList<LgdDistrictModel>
) : BaseAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // Inflate the custom layout
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_list_item, parent, false)
        val textView: TextView = view.findViewById(R.id.tvTitle)
        textView.setTextColor(Color.BLACK);
        val currentItem = items[position]
        textView.text =currentItem.districtName

        return view
    }

    // Optionally, override this method for dropdown view customization
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
       /* val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_list_item, parent, false)
        val textView: TextView = view.findViewById(R.id.tvTitle)
        textView.setTextColor(Color.BLACK);
        val currentItem = items[position]
        textView.text =currentItem.districtName

        return view*/
      return getView(position, convertView, parent)
    }
}