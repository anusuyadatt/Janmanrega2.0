package nic.hp.ccmgnrega.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.common.ExpandableListMediaStateManager;
import nic.hp.ccmgnrega.common.MediaPlayerHelper;
import nic.hp.ccmgnrega.common.Utility;
import nic.hp.ccmgnrega.model.PersonalAsset;

public class PersonalAssetsAdapter extends BaseExpandableListAdapter {

    Context context;
    List<PersonalAsset> personalAssets;
    HashMap<String,Bitmap> workCodeToPersonalAssetImageMapping;
    HashMap<String, String> personalAssetsWorkCodeToWorkNameMapping;

    private int textSize = 14;


    public PersonalAssetsAdapter(Context context,
                                 List<PersonalAsset> personalAssets,
                                 HashMap<String,Bitmap> workCodeToPersonalAssetImageMapping,
                                 HashMap<String, String> personalAssetsWorkCodeToWorkNameMapping) {
        this.context = context;
        this.personalAssets = personalAssets;
        this.workCodeToPersonalAssetImageMapping = workCodeToPersonalAssetImageMapping;
        this.personalAssetsWorkCodeToWorkNameMapping = personalAssetsWorkCodeToWorkNameMapping;
    }


    @Override
    public int getGroupCount() {
        return this.personalAssets.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public PersonalAsset getGroup(int groupPosition) {
        return this.personalAssets.get(groupPosition);
    }

    @Override
    public PersonalAsset getChild(int groupPosition, int childPosition) {
        return this.personalAssets.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group2, null);
        }

        String workcode = getGroup(groupPosition).getWorkCode();
        String workname = personalAssetsWorkCodeToWorkNameMapping.get(workcode);

        String workNameDisplay = workcode;

        if (workname != null && !workname.isEmpty()) {
            workNameDisplay = workname + " (" + workcode + ")";
        }

        convertView.setBackgroundColor(ContextCompat.getColor(context,R.color.tab_layout_background));
        TextView listTitle2TextView = (TextView) convertView.findViewById(R.id.rowSecondText);
        listTitle2TextView.setText(workNameDisplay);
        listTitle2TextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);

        int[] attrs = new int[]{android.R.attr.expandableListPreferredItemPaddingLeft};
        TypedArray a = context.obtainStyledAttributes(attrs);
        int paddingLeft = a.getDimensionPixelSize(0, 0);
        listTitle2TextView.setPadding(paddingLeft,0,10,0);

        addAudioFunctionality(convertView, groupPosition);
        return convertView;
    }

    private void addAudioFunctionality(View convertView, int groupPosition) {
        if (!Utility.translationAllowed(context)) {
            return;
        }
        ImageView speakerButton = (ImageView) convertView.findViewById(R.id.playPauseButton);
        speakerButton.setVisibility(View.VISIBLE);
        if (ExpandableListMediaStateManager.isMediaPlaying(groupPosition)) {
            speakerButton.setBackground(ContextCompat.getDrawable(context,R.drawable.stop_button));
        } else {
            speakerButton.setBackground(ContextCompat.getDrawable(context,R.drawable.speaker));
        }
        speakerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });


        speakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View speakerButton) {
                if (ExpandableListMediaStateManager.isMediaPlaying(groupPosition)) {
                    speakerButton.setBackground(ContextCompat.getDrawable(context,R.drawable.speaker));
                    ExpandableListMediaStateManager.setStateToStopped(groupPosition);
                    MediaPlayerHelper.releaseMediaPlayer();
                    notifyDataSetChanged();
                } else {
                    MediaPlayerHelper.releaseMediaPlayer();
                    ExpandableListMediaStateManager.setStateToPlaying(groupPosition);
                    speakerButton.setBackground(ContextCompat.getDrawable(context,R.drawable.stop_button));
                    notifyDataSetChanged();
                    onButtonClicked(groupPosition, (ImageView) speakerButton);
                }
            }
        });
    }

    public void onButtonClicked(int groupPosition, ImageView speakerButton) {

        String speech = getSpeech(groupPosition);
        speech = Utility.replaceMultiplePeriods(speech, context.getString(R.string.period));
        MediaPlayerHelper.playMedia(context, speakerButton, speech);
    }

    private String getSpeech(int groupPosition) {

        String speech = "";
        String period = context.getString(R.string.period) + " ";

        PersonalAsset personalAsset = getGroup(groupPosition);

        String workname = personalAssetsWorkCodeToWorkNameMapping.get(personalAsset.getWorkCode());
        if (workname != null && !workname.isEmpty()) {
            speech += context.getString(R.string.workName) + period + workname + period;
        } else if (personalAsset.getWorkCode() != null && !personalAsset.getWorkCode().isEmpty()) {
            speech += context.getString(R.string.workCode) + period + personalAsset.getWorkCode() + period;
        }

        if (personalAsset.getPermissibleWork() != null && !personalAsset.getPermissibleWork().isEmpty()) {
            speech += context.getString(R.string.permissibleCategory) + period + personalAsset.getPermissibleWork() + period;
        }

        SimpleDateFormat indianDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (personalAsset.getStartDate() != null && !personalAsset.getStartDate().isEmpty()) {
            try {
                Date date = indianDateFormat.parse(personalAsset.getStartDate());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                speech += context.getString(R.string.startDate) + period + calendar.get(Calendar.DATE) + " " + new SimpleDateFormat("MMMM").format(calendar.getTime()) + " " + calendar.get(Calendar.YEAR) + period;
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        if (personalAsset.getCompletionDate() != null && !personalAsset.getCompletionDate().isEmpty()) {
            try {
                Date date = indianDateFormat.parse(personalAsset.getCompletionDate());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                speech += context.getString(R.string.completionDate) + period + calendar.get(Calendar.DATE) + " " + new SimpleDateFormat("MMMM").format(calendar.getTime()) + " " + calendar.get(Calendar.YEAR) + period;
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        if (personalAsset.getGramPanchayatName() != null && !personalAsset.getGramPanchayatName().isEmpty()) {
            speech += context.getString(R.string.gramPanchayat) + period + personalAsset.getGramPanchayatName() + period;
        }

        if (personalAsset.getVillageName() != null && !personalAsset.getVillageName().isEmpty()) {
            speech += context.getString(R.string.village) + period + personalAsset.getVillageName() + period;
        }

        if (personalAsset.getSanctionedAmount() != null && !personalAsset.getSanctionedAmount().isEmpty()) {
            speech += context.getString(R.string.sanctionedAmount) + period + context.getString(R.string.rupees) + " " + personalAsset.getSanctionedAmount() + period;
        }
        return speech;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.table_layout, null);
        }

        convertView.findViewById(R.id.personal_asset_detail_table).setBackgroundColor(Color.rgb(255, 255, 255));

        PersonalAsset personalAsset = getChild(groupPosition, childPosition);
        TableLayout tableLayout = convertView.findViewById(R.id.personal_asset_detail_table);
        tableLayout.removeAllViews();

        ImageView imageView = convertView.findViewById(R.id.personal_asset_image);
        if (workCodeToPersonalAssetImageMapping.containsKey(personalAsset.getWorkCode())) {
            imageView.setVisibility(View.VISIBLE);
            Bitmap bitmap = workCodeToPersonalAssetImageMapping.get(personalAsset.getWorkCode());
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setVisibility(View.GONE);
        }

        if (personalAsset.getWorkStatus() != null && !personalAsset.getWorkStatus().isEmpty()) {
            tableLayout.addView(createRow(this.context.getString(R.string.workStatus), personalAsset.getWorkStatus()));
        }
        if (personalAsset.getPermissibleWork() != null && !personalAsset.getPermissibleWork().isEmpty()) {
            tableLayout.addView(createRow(this.context.getString(R.string.permissibleCategory), personalAsset.getPermissibleWork()));
        }
        if (personalAsset.getStartDate() != null && !personalAsset.getStartDate().isEmpty()) {
            tableLayout.addView(createRow(this.context.getString(R.string.startDate), personalAsset.getStartDate()));
        }
        if (personalAsset.getCompletionDate() != null && !personalAsset.getCompletionDate().isEmpty()) {
            tableLayout.addView(createRow(this.context.getString(R.string.completionDate), personalAsset.getCompletionDate()));
        }
        if (personalAsset.getGramPanchayatName() != null && !personalAsset.getGramPanchayatName().isEmpty()) {
            tableLayout.addView(createRow(this.context.getString(R.string.gramPanchayat), personalAsset.getGramPanchayatName()));
        }
        if (personalAsset.getVillageName() != null && !personalAsset.getVillageName().isEmpty()) {
            tableLayout.addView(createRow(this.context.getString(R.string.village), personalAsset.getVillageName()));
        }
        if (personalAsset.getSanctionedAmount() != null && !personalAsset.getSanctionedAmount().isEmpty()) {
            tableLayout.addView(createRow(this.context.getString(R.string.sanctionedAmount), personalAsset.getSanctionedAmount()));
        }

        return convertView;
    }

    private TableRow createRow (String label, String value) {

        TableRow tableRow = new TableRow(context);
        tableRow.setPadding(0, 0, 0, 5);

        TextView labelView = new TextView(context);
        labelView.setText(label + ":");
        labelView.setTextColor(context.getResources().getColor(R.color.blue_text));
        labelView.setTextColor(Color.parseColor("#3F51B5"));
        labelView.setTypeface(null, Typeface.BOLD);
        labelView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        tableRow.addView(labelView);

        TextView valueView = new TextView(context);
        valueView.setText(value);
        valueView.setTextColor(ContextCompat.getColor(context, R.color.all_text));
        valueView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        valueView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        tableRow.addView(valueView);

        return tableRow;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
