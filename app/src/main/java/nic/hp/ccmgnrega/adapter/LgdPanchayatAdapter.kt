package nic.hp.ccmgnrega.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import nic.hp.ccmgnrega.R
import nic.hp.ccmgnrega.model.LgdPanchayatModel


class LgdPanchayatAdapter (
    private val context: Context,
    private val items:ArrayList<LgdPanchayatModel>
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
        val currentItem = items[position]
        textView.text =currentItem.panchayatName

        return view
    }

    // Optionally, override this method for dropdown view customization
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent)
    }
}