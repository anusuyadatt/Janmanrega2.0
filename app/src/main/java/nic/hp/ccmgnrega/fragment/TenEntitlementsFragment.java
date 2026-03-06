package nic.hp.ccmgnrega.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.adapter.ResourceAdapter;
import nic.hp.ccmgnrega.common.MediaPlayerHelper;
import nic.hp.ccmgnrega.common.MediaStateManager;
import nic.hp.ccmgnrega.common.Utility;
import nic.hp.ccmgnrega.data.JobCardTabData;


public class TenEntitlementsFragment extends BaseFragment {
    protected View rootView;
    RelativeLayout linearEntitlement1, linearEntitlement2, linearEntitlement3,
            linearEntitlement4,linearEntitlement5, linearEntitlement6,
            linearEntitlement7and8, linearEntitlement9, linearEntitlement10;
    LinearLayout linearEntitlement1_Desc,linearEntitlement2_Desc,linearEntitlement3_Desc,
            linearEntitlement4_Desc,linearEntitlement5_Desc,linearEntitlement6_Desc,
            linearEntitlement7and8_Desc,linearEntitlement9_Desc,linearEntitlement10_Desc;
    ImageView imgEntitlement1,imgEntitlement2,imgEntitlement3,imgEntitlement4,imgEntitlement5,imgEntitlement6,imgEntitlement7and8,
                imgEntitlement9,imgEntitlement10;
    ImageView entitlement1AudioButton, entitlement2AudioButton, entitlement3AudioButton,
            entitlement4AudioButton, entitlement5AudioButton, entitlement6AudioButton,
            entitlement7and8AudioButton, entitlement9AudioButton, entitlement10AudioButton;
    private List<ImageView> keys;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        Bundle args = getArguments();
        if (args != null && args.getString("menu") != null) {
            menu = args.getString("menu");
        }
        rootView = inflater.inflate(R.layout.fragment_tenentitlements, container, false);
        TextView menuTextView = rootView.findViewById(R.id.heading);
        menuTextView.setText(" " + menu);
        linearEntitlement1= rootView.findViewById(R.id.linearEntitlement1);
        linearEntitlement1_Desc= rootView.findViewById(R.id.linearEntitlement1_Desc);

        imgEntitlement1= rootView.findViewById(R.id.imgEntitlement1);
        linearEntitlement2= rootView.findViewById(R.id.linearEntitlement2);
        linearEntitlement2_Desc= rootView.findViewById(R.id.linearEntitlement2_Desc);
        imgEntitlement2= rootView.findViewById(R.id.imgEntitlement2);
        linearEntitlement3= rootView.findViewById(R.id.linearEntitlement3);
        linearEntitlement3_Desc= rootView.findViewById(R.id.linearEntitlement3_Desc);
        imgEntitlement3= rootView.findViewById(R.id.imgEntitlement3);
        linearEntitlement4= rootView.findViewById(R.id.linearEntitlement4);
        linearEntitlement4_Desc= rootView.findViewById(R.id.linearEntitlement4_Desc);
        imgEntitlement4= rootView.findViewById(R.id.imgEntitlement4);
        linearEntitlement5= rootView.findViewById(R.id.linearEntitlement5);
        linearEntitlement5_Desc= rootView.findViewById(R.id.linearEntitlement5_Desc);
        imgEntitlement5= rootView.findViewById(R.id.imgEntitlement5);
        linearEntitlement6= rootView.findViewById(R.id.linearEntitlement6);
        linearEntitlement6_Desc= rootView.findViewById(R.id.linearEntitlement6_Desc);
        imgEntitlement6= rootView.findViewById(R.id.imgEntitlement6);
        linearEntitlement7and8= rootView.findViewById(R.id.linearEntitlement7and8);
        linearEntitlement7and8_Desc= rootView.findViewById(R.id.linearEntitlement7and8_Desc);
        imgEntitlement7and8= rootView.findViewById(R.id.imgEntitlement7and8);
        linearEntitlement9= rootView.findViewById(R.id.linearEntitlement9);
        linearEntitlement9_Desc= rootView.findViewById(R.id.linearEntitlement9_Desc);
        imgEntitlement9= rootView.findViewById(R.id.imgEntitlement9);
        linearEntitlement10= rootView.findViewById(R.id.linearEntitlement10);
        linearEntitlement10_Desc= rootView.findViewById(R.id.linearEntitlement10_Desc);
        imgEntitlement10= rootView.findViewById(R.id.imgEntitlement10);

        linearEntitlement1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearEntitlement1_Desc.getVisibility()==View.GONE){
                    linearEntitlement1_Desc.setVisibility(View.VISIBLE);
                    imgEntitlement1.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearEntitlement1.setBackgroundResource(R.drawable.border_up);

                    linearEntitlement2_Desc.setVisibility(View.GONE);
                    imgEntitlement2.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement2.setBackgroundResource(R.drawable.border);

                    linearEntitlement3_Desc.setVisibility(View.GONE);
                    imgEntitlement3.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement3.setBackgroundResource(R.drawable.border);


                    linearEntitlement4_Desc.setVisibility(View.GONE);
                    imgEntitlement4.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement4.setBackgroundResource(R.drawable.border);


                    linearEntitlement5_Desc.setVisibility(View.GONE);
                    imgEntitlement5.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement5.setBackgroundResource(R.drawable.border);

                    linearEntitlement6_Desc.setVisibility(View.GONE);
                    imgEntitlement6.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement6.setBackgroundResource(R.drawable.border);

                    linearEntitlement7and8_Desc.setVisibility(View.GONE);
                    imgEntitlement7and8.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement7and8.setBackgroundResource(R.drawable.border);


                    linearEntitlement9_Desc.setVisibility(View.GONE);
                    imgEntitlement9.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement9.setBackgroundResource(R.drawable.border);

                    linearEntitlement10_Desc.setVisibility(View.GONE);
                    imgEntitlement10.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement10.setBackgroundResource(R.drawable.border);

                }
                else {
                    linearEntitlement1_Desc.setVisibility(View.GONE);
                    imgEntitlement1.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement1.setBackgroundResource(R.drawable.border);
                }
            }
        });


        linearEntitlement2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearEntitlement2_Desc.getVisibility()==View.GONE){
                    linearEntitlement2_Desc.setVisibility(View.VISIBLE);
                    imgEntitlement2.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearEntitlement2.setBackgroundResource(R.drawable.border_up);

                    linearEntitlement1_Desc.setVisibility(View.GONE);
                    imgEntitlement1.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement1.setBackgroundResource(R.drawable.border);

                    linearEntitlement3_Desc.setVisibility(View.GONE);
                    imgEntitlement3.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement3.setBackgroundResource(R.drawable.border);


                    linearEntitlement4_Desc.setVisibility(View.GONE);
                    imgEntitlement4.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement4.setBackgroundResource(R.drawable.border);


                    linearEntitlement5_Desc.setVisibility(View.GONE);
                    imgEntitlement5.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement5.setBackgroundResource(R.drawable.border);

                    linearEntitlement6_Desc.setVisibility(View.GONE);
                    imgEntitlement6.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement6.setBackgroundResource(R.drawable.border);

                    linearEntitlement7and8_Desc.setVisibility(View.GONE);
                    imgEntitlement7and8.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement7and8.setBackgroundResource(R.drawable.border);


                    linearEntitlement9_Desc.setVisibility(View.GONE);
                    imgEntitlement9.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement9.setBackgroundResource(R.drawable.border);

                    linearEntitlement10_Desc.setVisibility(View.GONE);
                    imgEntitlement10.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement10.setBackgroundResource(R.drawable.border);

                }
                else {
                    linearEntitlement2_Desc.setVisibility(View.GONE);
                    imgEntitlement2.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement2.setBackgroundResource(R.drawable.border);
                }
            }
        });
        linearEntitlement3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearEntitlement3_Desc.getVisibility()==View.GONE){
                    linearEntitlement3_Desc.setVisibility(View.VISIBLE);
                    imgEntitlement3.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearEntitlement3.setBackgroundResource(R.drawable.border_up);

                    linearEntitlement1_Desc.setVisibility(View.GONE);
                    imgEntitlement1.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement1.setBackgroundResource(R.drawable.border);

                    linearEntitlement2_Desc.setVisibility(View.GONE);
                    imgEntitlement2.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement2.setBackgroundResource(R.drawable.border);

                    linearEntitlement4_Desc.setVisibility(View.GONE);
                    imgEntitlement4.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement4.setBackgroundResource(R.drawable.border);


                    linearEntitlement5_Desc.setVisibility(View.GONE);
                    imgEntitlement5.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement5.setBackgroundResource(R.drawable.border);

                    linearEntitlement6_Desc.setVisibility(View.GONE);
                    imgEntitlement6.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement6.setBackgroundResource(R.drawable.border);

                    linearEntitlement7and8_Desc.setVisibility(View.GONE);
                    imgEntitlement7and8.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement7and8.setBackgroundResource(R.drawable.border);


                    linearEntitlement9_Desc.setVisibility(View.GONE);
                    imgEntitlement9.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement9.setBackgroundResource(R.drawable.border);

                    linearEntitlement10_Desc.setVisibility(View.GONE);
                    imgEntitlement10.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement10.setBackgroundResource(R.drawable.border);

                }
                else {
                    linearEntitlement3_Desc.setVisibility(View.GONE);
                    imgEntitlement3.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement3.setBackgroundResource(R.drawable.border);
                }
            }
        });
        linearEntitlement4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearEntitlement4_Desc.getVisibility()==View.GONE){
                    linearEntitlement4_Desc.setVisibility(View.VISIBLE);
                    imgEntitlement4.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearEntitlement4.setBackgroundResource(R.drawable.border_up);

                    linearEntitlement1_Desc.setVisibility(View.GONE);
                    imgEntitlement1.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement1.setBackgroundResource(R.drawable.border);

                    linearEntitlement2_Desc.setVisibility(View.GONE);
                    imgEntitlement2.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement2.setBackgroundResource(R.drawable.border);

                    linearEntitlement3_Desc.setVisibility(View.GONE);
                    imgEntitlement3.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement3.setBackgroundResource(R.drawable.border);

                    linearEntitlement5_Desc.setVisibility(View.GONE);
                    imgEntitlement5.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement5.setBackgroundResource(R.drawable.border);

                    linearEntitlement6_Desc.setVisibility(View.GONE);
                    imgEntitlement6.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement6.setBackgroundResource(R.drawable.border);

                    linearEntitlement7and8_Desc.setVisibility(View.GONE);
                    imgEntitlement7and8.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement7and8.setBackgroundResource(R.drawable.border);


                    linearEntitlement9_Desc.setVisibility(View.GONE);
                    imgEntitlement9.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement9.setBackgroundResource(R.drawable.border);

                    linearEntitlement10_Desc.setVisibility(View.GONE);
                    imgEntitlement10.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement10.setBackgroundResource(R.drawable.border);

                }
                else {
                    linearEntitlement4_Desc.setVisibility(View.GONE);
                    imgEntitlement4.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement4.setBackgroundResource(R.drawable.border);
                }
            }
        });
        linearEntitlement5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearEntitlement5_Desc.getVisibility()==View.GONE){
                    linearEntitlement5_Desc.setVisibility(View.VISIBLE);
                    imgEntitlement5.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearEntitlement5.setBackgroundResource(R.drawable.border_up);

                    linearEntitlement1_Desc.setVisibility(View.GONE);
                    imgEntitlement1.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement1.setBackgroundResource(R.drawable.border);

                    linearEntitlement2_Desc.setVisibility(View.GONE);
                    imgEntitlement2.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement2.setBackgroundResource(R.drawable.border);

                    linearEntitlement3_Desc.setVisibility(View.GONE);
                    imgEntitlement3.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement3.setBackgroundResource(R.drawable.border);


                    linearEntitlement4_Desc.setVisibility(View.GONE);
                    imgEntitlement4.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement4.setBackgroundResource(R.drawable.border);

                    linearEntitlement6_Desc.setVisibility(View.GONE);
                    imgEntitlement6.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement6.setBackgroundResource(R.drawable.border);

                    linearEntitlement7and8_Desc.setVisibility(View.GONE);
                    imgEntitlement7and8.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement7and8.setBackgroundResource(R.drawable.border);


                    linearEntitlement9_Desc.setVisibility(View.GONE);
                    imgEntitlement9.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement9.setBackgroundResource(R.drawable.border);

                    linearEntitlement10_Desc.setVisibility(View.GONE);
                    imgEntitlement10.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement10.setBackgroundResource(R.drawable.border);
                }
                else {
                    linearEntitlement5_Desc.setVisibility(View.GONE);
                    imgEntitlement5.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement5.setBackgroundResource(R.drawable.border);
                }
            }
        });
        linearEntitlement6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearEntitlement6_Desc.getVisibility()==View.GONE){
                    linearEntitlement6_Desc.setVisibility(View.VISIBLE);
                    imgEntitlement6.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearEntitlement6.setBackgroundResource(R.drawable.border_up);


                    linearEntitlement1_Desc.setVisibility(View.GONE);
                    imgEntitlement1.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement1.setBackgroundResource(R.drawable.border);

                    linearEntitlement2_Desc.setVisibility(View.GONE);
                    imgEntitlement2.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement2.setBackgroundResource(R.drawable.border);

                    linearEntitlement3_Desc.setVisibility(View.GONE);
                    imgEntitlement3.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement3.setBackgroundResource(R.drawable.border);


                    linearEntitlement4_Desc.setVisibility(View.GONE);
                    imgEntitlement4.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement4.setBackgroundResource(R.drawable.border);


                    linearEntitlement5_Desc.setVisibility(View.GONE);
                    imgEntitlement5.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement5.setBackgroundResource(R.drawable.border);

                    linearEntitlement7and8_Desc.setVisibility(View.GONE);
                    imgEntitlement7and8.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement7and8.setBackgroundResource(R.drawable.border);


                    linearEntitlement9_Desc.setVisibility(View.GONE);
                    imgEntitlement9.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement9.setBackgroundResource(R.drawable.border);

                    linearEntitlement10_Desc.setVisibility(View.GONE);
                    imgEntitlement10.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement10.setBackgroundResource(R.drawable.border);
                }
                else {
                    linearEntitlement6_Desc.setVisibility(View.GONE);
                    imgEntitlement6.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement6.setBackgroundResource(R.drawable.border);
                }
            }
        });
        linearEntitlement7and8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearEntitlement7and8_Desc.getVisibility()==View.GONE){
                    linearEntitlement7and8_Desc.setVisibility(View.VISIBLE);
                    imgEntitlement7and8.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearEntitlement7and8.setBackgroundResource(R.drawable.border_up);

                    linearEntitlement1_Desc.setVisibility(View.GONE);
                    imgEntitlement1.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement1.setBackgroundResource(R.drawable.border);

                    linearEntitlement2_Desc.setVisibility(View.GONE);
                    imgEntitlement2.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement2.setBackgroundResource(R.drawable.border);

                    linearEntitlement3_Desc.setVisibility(View.GONE);
                    imgEntitlement3.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement3.setBackgroundResource(R.drawable.border);


                    linearEntitlement4_Desc.setVisibility(View.GONE);
                    imgEntitlement4.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement4.setBackgroundResource(R.drawable.border);


                    linearEntitlement5_Desc.setVisibility(View.GONE);
                    imgEntitlement5.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement5.setBackgroundResource(R.drawable.border);

                    linearEntitlement6_Desc.setVisibility(View.GONE);
                    imgEntitlement6.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement6.setBackgroundResource(R.drawable.border);

                    linearEntitlement9_Desc.setVisibility(View.GONE);
                    imgEntitlement9.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement9.setBackgroundResource(R.drawable.border);

                    linearEntitlement10_Desc.setVisibility(View.GONE);
                    imgEntitlement10.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement10.setBackgroundResource(R.drawable.border);
                }
                else {
                    linearEntitlement7and8_Desc.setVisibility(View.GONE);
                    imgEntitlement7and8.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement7and8.setBackgroundResource(R.drawable.border);
                }
            }
        });
        linearEntitlement9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearEntitlement9_Desc.getVisibility()==View.GONE){
                    linearEntitlement9_Desc.setVisibility(View.VISIBLE);
                    imgEntitlement9.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearEntitlement9.setBackgroundResource(R.drawable.border_up);


                    linearEntitlement1_Desc.setVisibility(View.GONE);
                    imgEntitlement1.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement1.setBackgroundResource(R.drawable.border);

                    linearEntitlement2_Desc.setVisibility(View.GONE);
                    imgEntitlement2.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement2.setBackgroundResource(R.drawable.border);

                    linearEntitlement3_Desc.setVisibility(View.GONE);
                    imgEntitlement3.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement3.setBackgroundResource(R.drawable.border);


                    linearEntitlement4_Desc.setVisibility(View.GONE);
                    imgEntitlement4.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement4.setBackgroundResource(R.drawable.border);


                    linearEntitlement5_Desc.setVisibility(View.GONE);
                    imgEntitlement5.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement5.setBackgroundResource(R.drawable.border);

                    linearEntitlement6_Desc.setVisibility(View.GONE);
                    imgEntitlement6.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement6.setBackgroundResource(R.drawable.border);

                    linearEntitlement7and8_Desc.setVisibility(View.GONE);
                    imgEntitlement7and8.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement7and8.setBackgroundResource(R.drawable.border);

                    linearEntitlement10_Desc.setVisibility(View.GONE);
                    imgEntitlement10.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement10.setBackgroundResource(R.drawable.border);
                }
                else {
                    linearEntitlement9_Desc.setVisibility(View.GONE);
                    imgEntitlement9.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement9.setBackgroundResource(R.drawable.border);
                }
            }
        });
        linearEntitlement10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearEntitlement10_Desc.getVisibility()==View.GONE){
                    linearEntitlement10_Desc.setVisibility(View.VISIBLE);
                    imgEntitlement10.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearEntitlement10.setBackgroundResource(R.drawable.border_up);

                    linearEntitlement1_Desc.setVisibility(View.GONE);
                    imgEntitlement1.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement1.setBackgroundResource(R.drawable.border);

                    linearEntitlement2_Desc.setVisibility(View.GONE);
                    imgEntitlement2.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement2.setBackgroundResource(R.drawable.border);

                    linearEntitlement3_Desc.setVisibility(View.GONE);
                    imgEntitlement3.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement3.setBackgroundResource(R.drawable.border);


                    linearEntitlement4_Desc.setVisibility(View.GONE);
                    imgEntitlement4.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement4.setBackgroundResource(R.drawable.border);


                    linearEntitlement5_Desc.setVisibility(View.GONE);
                    imgEntitlement5.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement5.setBackgroundResource(R.drawable.border);

                    linearEntitlement6_Desc.setVisibility(View.GONE);
                    imgEntitlement6.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement6.setBackgroundResource(R.drawable.border);

                    linearEntitlement7and8_Desc.setVisibility(View.GONE);
                    imgEntitlement7and8.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement7and8.setBackgroundResource(R.drawable.border);


                    linearEntitlement9_Desc.setVisibility(View.GONE);
                    imgEntitlement9.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement9.setBackgroundResource(R.drawable.border);
                }
                else {
                    linearEntitlement10_Desc.setVisibility(View.GONE);
                    imgEntitlement10.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearEntitlement10.setBackgroundResource(R.drawable.border);
                }
            }
        });
        setAudioFunctionality();

        return rootView;
    }

    private void initializeMediaState() {
        keys = new ArrayList<>();
        keys.add(entitlement1AudioButton);
        keys.add(entitlement2AudioButton);
        keys.add(entitlement3AudioButton);
        keys.add(entitlement4AudioButton);
        keys.add(entitlement5AudioButton);
        keys.add(entitlement6AudioButton);
        keys.add(entitlement7and8AudioButton);
        keys.add(entitlement9AudioButton);
        keys.add(entitlement10AudioButton);
        MediaStateManager.initializeState(keys);
//        MediaPlayerHelper.setTaskCompletionListener(this);
    }
    private void setAudioButtonStates() {
        for (ImageView key : keys) {
            if (MediaStateManager.isMediaPlaying(key)) {
                key.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.stop_button));
            } else {
                key.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.speaker));
            }
        }
    }

    private void setAudioFunctionality() {
        entitlement1AudioButton = rootView.findViewById(R.id.entitlement1AudioButton);
        entitlement2AudioButton = rootView.findViewById(R.id.entitlement2AudioButton);
        entitlement3AudioButton = rootView.findViewById(R.id.entitlement3AudioButton);
        entitlement4AudioButton = rootView.findViewById(R.id.entitlement4AudioButton);
        entitlement5AudioButton = rootView.findViewById(R.id.entitlement5AudioButton);
        entitlement6AudioButton = rootView.findViewById(R.id.entitlement6AudioButton);
        entitlement7and8AudioButton = rootView.findViewById(R.id.entitlement7and8AudioButton);
        entitlement9AudioButton = rootView.findViewById(R.id.entitlement9AudioButton);
        entitlement10AudioButton = rootView.findViewById(R.id.entitlement10AudioButton);

        String period = getContext().getString(R.string.period) + " ";
        entitlement1AudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = "";
                speech += getContext().getString(R.string.entitlement_1) + period;
                speech += getContext().getString(R.string.entitlement_1_desc) + period;
                handleClick((ImageView)v, speech);
            }
        });

        entitlement2AudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = "";
                speech += getContext().getString(R.string.entitlement_2) + period;
                speech += getContext().getString(R.string.entitlement_2_desc_1) + period;
                speech += getContext().getString(R.string.entitlement_2_desc_2) + period;
                handleClick((ImageView)v, speech);
            }
        });

        entitlement3AudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = "";
                speech += getContext().getString(R.string.entitlement_3) + period;
                speech += getContext().getString(R.string.entitlement_3_desc) + period;
                handleClick((ImageView)v, speech);
            }
        });

        entitlement4AudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = "";
                speech += getContext().getString(R.string.entitlement_4) + period;
                speech += getContext().getString(R.string.entitlement_4_desc) + period;
                handleClick((ImageView)v, speech);
            }
        });

        entitlement5AudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = "";
                speech += getContext().getString(R.string.entitlement_5) + period;
                speech += getContext().getString(R.string.entitlement_5_desc_1) + period;
                speech += getContext().getString(R.string.entitlement_5_desc_2) + period;
                handleClick((ImageView)v, speech);
            }
        });

        entitlement6AudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = "";
                speech += getContext().getString(R.string.entitlement_6) + period;
                speech += getContext().getString(R.string.entitlement_6_desc) + period;
                handleClick((ImageView)v, speech);
            }
        });

        entitlement7and8AudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = "";
                speech += getContext().getString(R.string.entitlement_7and8) + period;
                speech += getContext().getString(R.string.entitlement_7and8_desc_1) + period;
                speech += getContext().getString(R.string.entitlement_7and8_desc_2) + period;
                speech += getContext().getString(R.string.entitlement_7and8_desc_3) + period;
                speech += getContext().getString(R.string.entitlement_7and8_desc_4) + period;
                speech += getContext().getString(R.string.entitlement_7and8_desc_5) + period;
                handleClick((ImageView)v, speech);
            }
        });

        entitlement9AudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = "";
                speech += getContext().getString(R.string.entitlement_9) + period;
                speech += getContext().getString(R.string.entitlement_9_desc) + period;
                handleClick((ImageView)v, speech);
            }
        });

        entitlement10AudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = "";
                speech += getContext().getString(R.string.entitlement_10) + period;
                speech += getContext().getString(R.string.entitlement_10_desc_1) + period;
                speech += getContext().getString(R.string.entitlement_10_desc_2) + period;
                speech += getContext().getString(R.string.entitlement_10_desc_3) + period;
                speech += getContext().getString(R.string.entitlement_10_desc_4) + period;
                handleClick((ImageView)v, speech);
            }
        });
    }

    private void handleClick(ImageView speakerButton, String speech) {
        if (MediaStateManager.isMediaPlaying(speakerButton)) {
            MediaStateManager.setStateToStopped(speakerButton);
            MediaPlayerHelper.releaseMediaPlayer();
            setAudioButtonStates();
        } else {
            MediaPlayerHelper.releaseMediaPlayer();
            MediaStateManager.setStateToPlaying(speakerButton);
            setAudioButtonStates();
            speech = Utility.replaceMultiplePeriods(speech, getContext().getString(R.string.period));
            MediaPlayerHelper.playMedia(getContext(), speakerButton, speech);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeMediaState();
    }

    @Override
    public void onStop() {
        super.onStop();
        MediaPlayerHelper.releaseMediaPlayer();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
