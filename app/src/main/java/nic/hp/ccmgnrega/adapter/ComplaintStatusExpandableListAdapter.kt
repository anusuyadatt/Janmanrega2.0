package nic.hp.ccmgnrega.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.Pair
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.RelativeLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import nic.hp.ccmgnrega.R

class ComplaintStatusExpandableListAdapter internal constructor(
    private val context: Context,
    private val alComplaintIdList: List<String>,
    private val complaintDetailMap: HashMap<String, ArrayList<Pair<String, String>>>

) : BaseExpandableListAdapter() {
    override fun getChild(listPosition: Int, expandedListPosition: Int): Pair<String, String> {
        return this.complaintDetailMap.get(this.alComplaintIdList.get(listPosition))!!
            .get(expandedListPosition);
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.complaint_list_child_item, null)
        }
        val tableLayout: TableLayout? =
            convertView!!.findViewById<TableLayout>(R.id.tabComplaintDetail)
        val tableLayoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        tableLayout!!.removeAllViews()
        val childItemObject = getChild(listPosition, expandedListPosition)
        val tableRow = TableRow(context)
        tableRow.setPadding(0, 0, 0, 0)
        tableRow.layoutParams = TableRow.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            2.0f
        )
        val tvLabel = TextView(context)
        tvLabel.text = childItemObject.first
        tvLabel.setTextColor(context.resources.getColor(R.color.blue))
        tvLabel.setTextColor(Color.parseColor("#3F51B5"))
        tvLabel.setTypeface(null, Typeface.BOLD)
        tvLabel.layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)
        tvLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
        tvLabel.gravity = Gravity.LEFT
        // tvLabel.background = ContextCompat.getDrawable(context, R.drawable.tab_row_style)
        tableRow.addView(tvLabel)

        val tvValue = TextView(context)
        tvValue.text = childItemObject.second
        if (childItemObject.second == context.getString(R.string.pending))
            tvValue.setTextColor(ContextCompat.getColor(context, R.color.red))
        else if (childItemObject.second == context.getString(R.string.closed))
            tvValue.setTextColor(ContextCompat.getColor(context, R.color.green_color))
        else if (childItemObject.second == context.getString(R.string.forwarded))
            tvValue.setTextColor(ContextCompat.getColor(context, R.color.red))
        else
            tvValue.setTextColor(ContextCompat.getColor(context, R.color.black))

        tvValue.layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)
        tvValue.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
        tvValue.gravity = Gravity.LEFT
        //  tvValue.background = ContextCompat.getDrawable(context, R.drawable.tab_row_style)
        tableRow.addView(tvValue)
        /* if (childItemObject.first == "Name") {
             tableLayoutParams.setMargins(0, 20, 0, 0);
             tableRow.layoutParams = tableLayoutParams
         }
         if (childItemObject.first == "Gender"){
             tableLayoutParams.setMargins(0, 0, 0, 20);
             tableRow.layoutParams = tableLayoutParams
         }*/
        tableRow.background = ContextCompat.getDrawable(context, R.drawable.tab_row_style)
        tableLayout!!.addView(tableRow)

        return convertView!!
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.complaintDetailMap[this.alComplaintIdList[listPosition]]!!.size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.alComplaintIdList[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.alComplaintIdList.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.complaint_list_head_item, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.tv)
        val rlComplaint = convertView!!.findViewById<RelativeLayout>(R.id.rlComplaint)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = context.getString(R.string.complaint_id) + ": " + listTitle
        if (isExpanded)
            rlComplaint.setBackgroundResource(R.drawable.border_up)
        else
            rlComplaint.setBackgroundResource(R.drawable.border)
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}
