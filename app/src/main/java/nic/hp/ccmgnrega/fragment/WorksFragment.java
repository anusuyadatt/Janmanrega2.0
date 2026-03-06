package nic.hp.ccmgnrega.fragment;


import android.content.Context;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;


import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.adapter.ResourceAdapter;


public class WorksFragment extends BaseFragment {
    protected View rootView;
    ListView lvCatAResource,lvCatBResource,lvCatCResource,lvCatDResource;
    TextView headingTv,workParaTV,tvCatAHeader,tvCatBHeader,tvCatCHeader,tvCatDHeader;
    LinearLayout linearCatAHeader,linearCatAContent,linearCatBHeader,linearCatBContent,linearCatCHeader,linearCatCContent,linearCatDHeader,linearCatDContent;
    ImageView imgCatAHeader,imgCatBHeader,imgCatCHeader,imgCatDHeader;
    RadioGroup rgCatAContent,rgCatBContent,rgCatCContent,rgCatDContent;
    RadioButton rbNrCatAContent,rbNonNrCatAContent,rbAllCatAContent,rbNrCatBContent,rbNonNrCatBContent,rbAllCatBContent,
            rbNrCatCContent,rbNonNrCatCContent,rbAllCatCContent,rbNrCatDContent,rbNonNrCatDContent,rbAllCatDContent;
    Context context;
    ResourceAdapter resourceAdapter;
    ArrayList<String> alNrCatAResource=new ArrayList<>();
    ArrayList<String> alNonNrCatAResource=new ArrayList<>();
    ArrayList<String> alAllCatAResource=new ArrayList<>();

    ArrayList<String> alNrCatBResource=new ArrayList<>();
    ArrayList<String> alNonNrCatBResource=new ArrayList<>();
    ArrayList<String> alAllCatBResource=new ArrayList<>();
    ArrayList<String> alNrCatCResource=new ArrayList<>();
    ArrayList<String> alNonNrCatCResource=new ArrayList<>();
    ArrayList<String> alAllCatCResource=new ArrayList<>();
    ArrayList<String> alNrCatDResource=new ArrayList<>();
    ArrayList<String> alNonNrCatDResource=new ArrayList<>();
    ArrayList<String> alAllCatDResource=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        context =getContext();
        rootView = inflater.inflate(R.layout.fragment_works, container, false);
        headingTv = rootView.findViewById(R.id.heading);
        workParaTV = rootView.findViewById(R.id.workParaTV);
        headingTv.setText(" "+context.getString(R.string.permissible_work_list));
        workParaTV.setText(context.getString(R.string.work_para));
        /*Category A Section Start*/
        tvCatAHeader = rootView.findViewById(R.id.tvCatAHeader);
        linearCatAHeader = rootView.findViewById(R.id.linearCatAHeader);
        linearCatAContent = rootView.findViewById(R.id.linearCatAContent);
        imgCatAHeader = rootView.findViewById(R.id.imgCatAHeader);
        rgCatAContent = rootView.findViewById(R.id.rgCatAContent);
        rbNrCatAContent = rootView.findViewById(R.id.rbNrCatAContent);
        rbNonNrCatAContent = rootView.findViewById(R.id.rbNonNrCatAContent);
        rbAllCatAContent = rootView.findViewById(R.id.rbAllCatAContent);
        lvCatAResource = rootView.findViewById(R.id.lvCatAResource);
        rbNrCatAContent.setButtonDrawable(R.color.transparent);
        rbNonNrCatAContent.setButtonDrawable(R.color.transparent);
        rbAllCatAContent.setButtonDrawable(R.color.transparent);
        setCategoryANrList(alNrCatAResource);//Category A Natural Resource
        setCategoryANonNrList(alNonNrCatAResource);//Category A Non Natural Resource

        alAllCatAResource.addAll(alNrCatAResource);
     //   alAllCatAResource.addAll(alNonNrCatAResource);
        resourceAdapter = new ResourceAdapter(getContext(),alAllCatAResource);
        lvCatAResource.setAdapter(resourceAdapter);
        linearCatAHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearCatAContent.getVisibility()==View.GONE){
                    linearCatAContent.setVisibility(View.VISIBLE);
                    imgCatAHeader.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearCatAHeader.setBackgroundResource(R.drawable.border_up);
                    rbAllCatAContent.setChecked(true);
                    resourceAdapter = new ResourceAdapter(getContext(),alAllCatAResource);
                    lvCatAResource.setAdapter(resourceAdapter);

                    linearCatBContent.setVisibility(View.GONE);
                    imgCatBHeader.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearCatBHeader.setBackgroundResource(R.drawable.border);

                    linearCatCContent.setVisibility(View.GONE);
                    imgCatCHeader.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearCatCHeader.setBackgroundResource(R.drawable.border);

                    linearCatDContent.setVisibility(View.GONE);
                    imgCatDHeader.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearCatDHeader.setBackgroundResource(R.drawable.border);

                }
                else {
                    linearCatAContent.setVisibility(View.GONE);
                    imgCatAHeader.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearCatAHeader.setBackgroundResource(R.drawable.border);
                }
            }
        });
        rgCatAContent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.rbNrCatAContent){
                    resourceAdapter.notifyDataSetChanged();
                    resourceAdapter = new ResourceAdapter(getContext(),alNrCatAResource);
                    lvCatAResource.setAdapter(resourceAdapter);

                }
                else if(checkedId==R.id.rbNonNrCatAContent){
                    resourceAdapter.notifyDataSetChanged();
                    resourceAdapter = new ResourceAdapter(getContext(),alNonNrCatAResource);
                    lvCatAResource.setAdapter(resourceAdapter);
                }
                else {
                    resourceAdapter.notifyDataSetChanged();
                    resourceAdapter = new ResourceAdapter(getContext(),alAllCatAResource);
                    lvCatAResource.setAdapter(resourceAdapter);
                }
            }
        });
        /*Category A Section End*/
        /*Category B Section Start*/
        tvCatBHeader = rootView.findViewById(R.id.tvCatBHeader);
        linearCatBHeader = rootView.findViewById(R.id.linearCatBHeader);
        linearCatBContent = rootView.findViewById(R.id.linearCatBContent);
        imgCatBHeader = rootView.findViewById(R.id.imgCatBHeader);
        rgCatBContent = rootView.findViewById(R.id.rgCatBContent);
        rbNrCatBContent = rootView.findViewById(R.id.rbNrCatBContent);
        rbNonNrCatBContent = rootView.findViewById(R.id.rbNonNrCatBContent);
        rbAllCatBContent = rootView.findViewById(R.id.rbAllCatBContent);
        lvCatBResource = rootView.findViewById(R.id.lvCatBResource);
        rbNrCatBContent.setButtonDrawable(R.color.transparent);
        rbNonNrCatBContent.setButtonDrawable(R.color.transparent);
        rbAllCatBContent.setButtonDrawable(R.color.transparent);
        setCategoryBNrList(alNrCatBResource);// Category B Natural Resource
        setCategoryBNonNrList(alNonNrCatBResource);// Category B Non Natural Resource
        alAllCatBResource.addAll(alNrCatBResource);
        alAllCatBResource.addAll(alNonNrCatBResource);

        linearCatBHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearCatBContent.getVisibility()==View.GONE){
                    linearCatBContent.setVisibility(View.VISIBLE);
                    imgCatBHeader.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearCatBHeader.setBackgroundResource(R.drawable.border_up);
                    rbAllCatBContent.setChecked(true);
                    resourceAdapter.notifyDataSetChanged();
                    resourceAdapter = new ResourceAdapter(getContext(),alAllCatBResource);
                    lvCatBResource.setAdapter(resourceAdapter);

                    linearCatAContent.setVisibility(View.GONE);
                    imgCatAHeader.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearCatAHeader.setBackgroundResource(R.drawable.border);

                    linearCatCContent.setVisibility(View.GONE);
                    imgCatCHeader.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearCatCHeader.setBackgroundResource(R.drawable.border);

                    linearCatDContent.setVisibility(View.GONE);
                    imgCatDHeader.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearCatDHeader.setBackgroundResource(R.drawable.border);

                }
                else {
                    linearCatBContent.setVisibility(View.GONE);
                    imgCatBHeader.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearCatBHeader.setBackgroundResource(R.drawable.border);
                }
            }
        });
        rgCatBContent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.rbNrCatBContent){
                    resourceAdapter.notifyDataSetChanged();
                    resourceAdapter = new ResourceAdapter(getContext(),alNrCatBResource);
                    lvCatBResource.setAdapter(resourceAdapter);

                }
                else if(checkedId==R.id.rbNonNrCatBContent){
                    resourceAdapter.notifyDataSetChanged();
                    resourceAdapter = new ResourceAdapter(getContext(),alNonNrCatBResource);
                    lvCatBResource.setAdapter(resourceAdapter);
                }
                else {
                    resourceAdapter.notifyDataSetChanged();
                    resourceAdapter = new ResourceAdapter(getContext(),alAllCatBResource);
                    lvCatBResource.setAdapter(resourceAdapter);
                }
            }
        });
        /*Category B Section End*/
        /*Category C Section Start*/
        tvCatCHeader = rootView.findViewById(R.id.tvCatCHeader);
        linearCatCHeader = rootView.findViewById(R.id.linearCatCHeader);
        linearCatCContent = rootView.findViewById(R.id.linearCatCContent);
        imgCatCHeader = rootView.findViewById(R.id.imgCatCHeader);
        rgCatCContent = rootView.findViewById(R.id.rgCatCContent);
        rbNrCatCContent = rootView.findViewById(R.id.rbNrCatCContent);
        rbNonNrCatCContent = rootView.findViewById(R.id.rbNonNrCatCContent);
        rbAllCatCContent = rootView.findViewById(R.id.rbAllCatCContent);
        lvCatCResource = rootView.findViewById(R.id.lvCatCResource);
        rbNrCatCContent.setButtonDrawable(R.color.transparent);
        rbNonNrCatCContent.setButtonDrawable(R.color.transparent);
        rbAllCatCContent.setButtonDrawable(R.color.transparent);
        setCategoryCNrList(alNrCatCResource);// Category C Natural Resource
        setCategoryCNonNrList(alNonNrCatCResource);// Category C Non Natural Resource
        alAllCatCResource.addAll(alNrCatCResource);
        alAllCatCResource.addAll(alNonNrCatCResource);

        linearCatCHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearCatCContent.getVisibility()==View.GONE){
                    linearCatCContent.setVisibility(View.VISIBLE);
                    imgCatCHeader.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearCatCHeader.setBackgroundResource(R.drawable.border_up);
                    rbAllCatCContent.setChecked(true);
                    resourceAdapter.notifyDataSetChanged();
                    resourceAdapter = new ResourceAdapter(getContext(),alAllCatCResource);
                    lvCatCResource.setAdapter(resourceAdapter);

                    linearCatAContent.setVisibility(View.GONE);
                    imgCatAHeader.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearCatAHeader.setBackgroundResource(R.drawable.border);

                    linearCatBContent.setVisibility(View.GONE);
                    imgCatBHeader.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearCatBHeader.setBackgroundResource(R.drawable.border);

                    linearCatDContent.setVisibility(View.GONE);
                    imgCatDHeader.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearCatDHeader.setBackgroundResource(R.drawable.border);

                }
                else {
                    linearCatCContent.setVisibility(View.GONE);
                    imgCatCHeader.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearCatCHeader.setBackgroundResource(R.drawable.border);
                }
            }
        });
        rgCatCContent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.rbNrCatCContent){
                    resourceAdapter.notifyDataSetChanged();
                    resourceAdapter = new ResourceAdapter(getContext(),alNrCatCResource);
                    lvCatCResource.setAdapter(resourceAdapter);

                }
                else if(checkedId==R.id.rbNonNrCatCContent){
                    resourceAdapter.notifyDataSetChanged();
                    resourceAdapter = new ResourceAdapter(getContext(),alNonNrCatCResource);
                    lvCatCResource.setAdapter(resourceAdapter);
                }
                else {
                    resourceAdapter.notifyDataSetChanged();
                    resourceAdapter = new ResourceAdapter(getContext(),alAllCatCResource);
                    lvCatCResource.setAdapter(resourceAdapter);
                }
            }
        });
        /*Category C Section End*/

        /*Category D Section Start*/
        tvCatDHeader = rootView.findViewById(R.id.tvCatDHeader);
        linearCatDHeader = rootView.findViewById(R.id.linearCatDHeader);
        linearCatDContent = rootView.findViewById(R.id.linearCatDContent);
        imgCatDHeader = rootView.findViewById(R.id.imgCatDHeader);
        rgCatDContent = rootView.findViewById(R.id.rgCatDContent);
        rbNrCatDContent = rootView.findViewById(R.id.rbNrCatDContent);
        rbNonNrCatDContent = rootView.findViewById(R.id.rbNonNrCatDContent);
        rbAllCatDContent = rootView.findViewById(R.id.rbAllCatDContent);
        lvCatDResource = rootView.findViewById(R.id.lvCatDResource);
        rbNrCatDContent.setButtonDrawable(R.color.transparent);
        rbNonNrCatDContent.setButtonDrawable(R.color.transparent);
        rbAllCatDContent.setButtonDrawable(R.color.transparent);
        setCategoryDNrList(alNrCatDResource);// Category D Natural Resource
        setCategoryDNonNrList(alNonNrCatDResource);// Category D Non Natural Resource
        alAllCatDResource.addAll(alNrCatDResource);
        alAllCatDResource.addAll(alNonNrCatDResource);

        linearCatDHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearCatDContent.getVisibility()==View.GONE){
                    linearCatDContent.setVisibility(View.VISIBLE);
                    imgCatDHeader.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearCatDHeader.setBackgroundResource(R.drawable.border_up);
                    rbAllCatDContent.setChecked(true);
                    resourceAdapter.notifyDataSetChanged();
                    resourceAdapter = new ResourceAdapter(getContext(),alAllCatDResource);
                    lvCatDResource.setAdapter(resourceAdapter);

                    linearCatAContent.setVisibility(View.GONE);
                    imgCatAHeader.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearCatAHeader.setBackgroundResource(R.drawable.border);

                    linearCatBContent.setVisibility(View.GONE);
                    imgCatBHeader.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearCatBHeader.setBackgroundResource(R.drawable.border);

                    linearCatCContent.setVisibility(View.GONE);
                    imgCatCHeader.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearCatCHeader.setBackgroundResource(R.drawable.border);

                }
                else {
                    linearCatDContent.setVisibility(View.GONE);
                    imgCatDHeader.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearCatDHeader.setBackgroundResource(R.drawable.border);
                }
            }
        });
        rgCatDContent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.rbNrCatDContent){
                    resourceAdapter.notifyDataSetChanged();
                    resourceAdapter = new ResourceAdapter(getContext(),alNrCatDResource);
                    lvCatDResource.setAdapter(resourceAdapter);

                }
                else if(checkedId==R.id.rbNonNrCatDContent){
                    resourceAdapter.notifyDataSetChanged();
                    resourceAdapter = new ResourceAdapter(getContext(),alNonNrCatDResource);
                    lvCatDResource.setAdapter(resourceAdapter);
                }
                else {
                    resourceAdapter.notifyDataSetChanged();
                    resourceAdapter = new ResourceAdapter(getContext(),alAllCatDResource);
                    lvCatDResource.setAdapter(resourceAdapter);
                }
            }
        });
        /*Category D Section End*/
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void setCategoryANrList(ArrayList<String> alNrCatAResource){
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_1));alNrCatAResource.add(context.getString(R.string.cat_a_nr_2));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_3));alNrCatAResource.add(context.getString(R.string.cat_a_nr_4));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_5));alNrCatAResource.add(context.getString(R.string.cat_a_nr_6));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_7));alNrCatAResource.add(context.getString(R.string.cat_a_nr_8));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_9));alNrCatAResource.add(context.getString(R.string.cat_a_nr_10));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_11));alNrCatAResource.add(context.getString(R.string.cat_a_nr_12));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_13));alNrCatAResource.add(context.getString(R.string.cat_a_nr_14));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_15));alNrCatAResource.add(context.getString(R.string.cat_a_nr_16));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_17));alNrCatAResource.add(context.getString(R.string.cat_a_nr_18));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_19));alNrCatAResource.add(context.getString(R.string.cat_a_nr_20));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_21));alNrCatAResource.add(context.getString(R.string.cat_a_nr_22));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_23));alNrCatAResource.add(context.getString(R.string.cat_a_nr_24));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_25));alNrCatAResource.add(context.getString(R.string.cat_a_nr_26));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_27));alNrCatAResource.add(context.getString(R.string.cat_a_nr_28));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_29));alNrCatAResource.add(context.getString(R.string.cat_a_nr_30));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_31));alNrCatAResource.add(context.getString(R.string.cat_a_nr_32));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_33));alNrCatAResource.add(context.getString(R.string.cat_a_nr_34));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_35));alNrCatAResource.add(context.getString(R.string.cat_a_nr_36));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_37));alNrCatAResource.add(context.getString(R.string.cat_a_nr_38));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_39));alNrCatAResource.add(context.getString(R.string.cat_a_nr_40));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_41));alNrCatAResource.add(context.getString(R.string.cat_a_nr_42));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_43));alNrCatAResource.add(context.getString(R.string.cat_a_nr_44));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_45));alNrCatAResource.add(context.getString(R.string.cat_a_nr_46));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_47));alNrCatAResource.add(context.getString(R.string.cat_a_nr_48));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_49));alNrCatAResource.add(context.getString(R.string.cat_a_nr_50));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_51));alNrCatAResource.add(context.getString(R.string.cat_a_nr_52));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_53));alNrCatAResource.add(context.getString(R.string.cat_a_nr_54));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_55));alNrCatAResource.add(context.getString(R.string.cat_a_nr_56));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_57));alNrCatAResource.add(context.getString(R.string.cat_a_nr_58));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_59));alNrCatAResource.add(context.getString(R.string.cat_a_nr_60));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_61));alNrCatAResource.add(context.getString(R.string.cat_a_nr_62));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_63));alNrCatAResource.add(context.getString(R.string.cat_a_nr_64));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_65));alNrCatAResource.add(context.getString(R.string.cat_a_nr_66));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_67));alNrCatAResource.add(context.getString(R.string.cat_a_nr_68));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_69));alNrCatAResource.add(context.getString(R.string.cat_a_nr_70));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_71));alNrCatAResource.add(context.getString(R.string.cat_a_nr_72));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_73));alNrCatAResource.add(context.getString(R.string.cat_a_nr_74));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_75));alNrCatAResource.add(context.getString(R.string.cat_a_nr_76));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_77));alNrCatAResource.add(context.getString(R.string.cat_a_nr_78));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_79));alNrCatAResource.add(context.getString(R.string.cat_a_nr_80));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_81));alNrCatAResource.add(context.getString(R.string.cat_a_nr_82));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_83));alNrCatAResource.add(context.getString(R.string.cat_a_nr_84));
        alNrCatAResource.add(context.getString(R.string.cat_a_nr_85));
    }
    private void setCategoryANonNrList(ArrayList<String> alNonNrCatAResource){
        alNonNrCatAResource.add(context.getString(R.string.works_not_found));

    }
    private void setCategoryBNrList(ArrayList<String> alNrCatBResource){
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_1));alNrCatBResource.add(context.getString(R.string.cat_b_nr_2));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_3));alNrCatBResource.add(context.getString(R.string.cat_b_nr_4));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_5));alNrCatBResource.add(context.getString(R.string.cat_b_nr_6));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_7));alNrCatBResource.add(context.getString(R.string.cat_b_nr_8));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_9));alNrCatBResource.add(context.getString(R.string.cat_b_nr_10));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_11));alNrCatBResource.add(context.getString(R.string.cat_b_nr_12));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_13));alNrCatBResource.add(context.getString(R.string.cat_b_nr_14));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_15));alNrCatBResource.add(context.getString(R.string.cat_b_nr_16));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_17));alNrCatBResource.add(context.getString(R.string.cat_b_nr_18));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_19));alNrCatBResource.add(context.getString(R.string.cat_b_nr_20));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_21));alNrCatBResource.add(context.getString(R.string.cat_b_nr_22));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_23));alNrCatBResource.add(context.getString(R.string.cat_b_nr_24));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_25));alNrCatBResource.add(context.getString(R.string.cat_b_nr_26));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_27));alNrCatBResource.add(context.getString(R.string.cat_b_nr_28));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_29));alNrCatBResource.add(context.getString(R.string.cat_b_nr_30));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_31));alNrCatBResource.add(context.getString(R.string.cat_b_nr_32));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_33));alNrCatBResource.add(context.getString(R.string.cat_b_nr_34));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_35));alNrCatBResource.add(context.getString(R.string.cat_b_nr_36));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_37));alNrCatBResource.add(context.getString(R.string.cat_b_nr_38));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_39));alNrCatBResource.add(context.getString(R.string.cat_b_nr_40));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_41));alNrCatBResource.add(context.getString(R.string.cat_b_nr_42));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_43));alNrCatBResource.add(context.getString(R.string.cat_b_nr_44));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_45));alNrCatBResource.add(context.getString(R.string.cat_b_nr_46));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_47));alNrCatBResource.add(context.getString(R.string.cat_b_nr_48));
        alNrCatBResource.add(context.getString(R.string.cat_b_nr_49));
    }
    private void setCategoryBNonNrList(ArrayList<String> alNonNrCatBResource){
        alNonNrCatBResource.add(context.getString(R.string.cat_b_non_nr_1));alNonNrCatBResource.add(context.getString(R.string.cat_b_non_nr_2));
        alNonNrCatBResource.add(context.getString(R.string.cat_b_non_nr_3));alNonNrCatBResource.add(context.getString(R.string.cat_b_non_nr_4));
        alNonNrCatBResource.add(context.getString(R.string.cat_b_non_nr_5));alNonNrCatBResource.add(context.getString(R.string.cat_b_non_nr_6));
        alNonNrCatBResource.add(context.getString(R.string.cat_b_non_nr_7));alNonNrCatBResource.add(context.getString(R.string.cat_b_non_nr_8));
        alNonNrCatBResource.add(context.getString(R.string.cat_b_non_nr_9));alNonNrCatBResource.add(context.getString(R.string.cat_b_non_nr_10));
        alNonNrCatBResource.add(context.getString(R.string.cat_b_non_nr_11));alNonNrCatBResource.add(context.getString(R.string.cat_b_non_nr_12));
        alNonNrCatBResource.add(context.getString(R.string.cat_b_non_nr_13));alNonNrCatBResource.add(context.getString(R.string.cat_b_non_nr_14));
        alNonNrCatBResource.add(context.getString(R.string.cat_b_non_nr_15));alNonNrCatBResource.add(context.getString(R.string.cat_b_non_nr_16));

    }

    private void setCategoryCNrList(ArrayList<String> alNrCatCResource){
        alNrCatCResource.add(context.getString(R.string.cat_c_nr_1));alNrCatCResource.add(context.getString(R.string.cat_c_nr_2));
        alNrCatCResource.add(context.getString(R.string.cat_c_nr_3));alNrCatCResource.add(context.getString(R.string.cat_c_nr_4));
    }
    private void setCategoryCNonNrList(ArrayList<String> alNonNrCatCResource){
        alNonNrCatCResource.add(context.getString(R.string.cat_c_non_nr_1));alNonNrCatCResource.add(context.getString(R.string.cat_c_non_nr_2));
        alNonNrCatCResource.add(context.getString(R.string.cat_c_non_nr_3));alNonNrCatCResource.add(context.getString(R.string.cat_c_non_nr_4));
    }
    private void setCategoryDNrList(ArrayList<String> alNrCatDResource){
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_1));alNrCatDResource.add(context.getString(R.string.cat_d_nr_2));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_3));alNrCatDResource.add(context.getString(R.string.cat_d_nr_4));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_5));alNrCatDResource.add(context.getString(R.string.cat_d_nr_6));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_7));alNrCatDResource.add(context.getString(R.string.cat_d_nr_8));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_9));alNrCatDResource.add(context.getString(R.string.cat_d_nr_10));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_11));alNrCatDResource.add(context.getString(R.string.cat_d_nr_12));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_13));alNrCatDResource.add(context.getString(R.string.cat_d_nr_14));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_15));alNrCatDResource.add(context.getString(R.string.cat_d_nr_16));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_17));alNrCatDResource.add(context.getString(R.string.cat_d_nr_18));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_19));alNrCatDResource.add(context.getString(R.string.cat_d_nr_20));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_21));alNrCatDResource.add(context.getString(R.string.cat_d_nr_22));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_23));alNrCatDResource.add(context.getString(R.string.cat_d_nr_24));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_25));alNrCatDResource.add(context.getString(R.string.cat_d_nr_26));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_27));alNrCatDResource.add(context.getString(R.string.cat_d_nr_28));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_29));alNrCatDResource.add(context.getString(R.string.cat_d_nr_30));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_31));alNrCatDResource.add(context.getString(R.string.cat_d_nr_32));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_33));alNrCatDResource.add(context.getString(R.string.cat_d_nr_34));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_35));alNrCatDResource.add(context.getString(R.string.cat_d_nr_36));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_37));alNrCatDResource.add(context.getString(R.string.cat_d_nr_38));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_39));alNrCatDResource.add(context.getString(R.string.cat_d_nr_40));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_41));  alNrCatDResource.add(context.getString(R.string.cat_d_nr_42));
        alNrCatDResource.add(context.getString(R.string.cat_d_nr_43));

    }
    private void setCategoryDNonNrList(ArrayList<String> alNonNrCatDResource){
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_1));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_2));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_3));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_4));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_5));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_6));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_7));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_8));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_9));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_10));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_11));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_12));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_13));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_14));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_15));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_16));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_17));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_18));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_19));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_20));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_21));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_22));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_23));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_24));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_25));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_26));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_27));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_28));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_29));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_30));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_31));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_32));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_33));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_34));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_35));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_36));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_37));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_38));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_39));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_40));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_41));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_42));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_43));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_44));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_45));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_46));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_47));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_48));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_49));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_50));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_51));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_52));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_53));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_54));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_55));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_56));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_57));alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_58));
        alNonNrCatDResource.add(context.getString(R.string.cat_d_non_nr_59));
    }


}