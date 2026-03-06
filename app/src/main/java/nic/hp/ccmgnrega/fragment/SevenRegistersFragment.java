package nic.hp.ccmgnrega.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.common.MediaPlayerHelper;
import nic.hp.ccmgnrega.common.MediaStateManager;
import nic.hp.ccmgnrega.common.Utility;


public class SevenRegistersFragment extends BaseFragment {
    protected View rootView;
    RelativeLayout linearRegister1, linearRegister2, linearRegister3,
            linearRegister4, linearRegister5, linearRegister6, linearRegister7;
    LinearLayout linearRegister1_Desc, linearRegister2_Desc, linearRegister3_Desc,
            linearRegister4_Desc, linearRegister5_Desc, linearRegister6_Desc,
            linearRegister7_Desc;
    ImageView imgRegister1, imgRegister2, imgRegister3, imgRegister4, imgRegister5, imgRegister6, imgRegister7;
    ImageView sevenRegistersAudioButton, register1AudioButton, register2AudioButton, register3AudioButton,
            register4AudioButton, register5AudioButton, register6AudioButton,
            register7AudioButton;
    private List<ImageView> keys;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        Bundle args = getArguments();
        if (args != null && args.getString("menu") != null)
            menu = args.getString("menu");
        rootView = inflater.inflate(R.layout.fragment_sevenregisters, container, false);
        TextView menuTextView = rootView.findViewById(R.id.heading);
        menuTextView.setText(" " + menu);
        linearRegister1 = rootView.findViewById(R.id.linearRegister1);
        linearRegister1_Desc = rootView.findViewById(R.id.linearRegister1_Desc);
        imgRegister1 = rootView.findViewById(R.id.imgRegister1);
        linearRegister2 = rootView.findViewById(R.id.linearRegister2);
        linearRegister2_Desc = rootView.findViewById(R.id.linearRegister2_Desc);
        imgRegister2 = rootView.findViewById(R.id.imgRegister2);
        linearRegister3 = rootView.findViewById(R.id.linearRegister3);
        linearRegister3_Desc = rootView.findViewById(R.id.linearRegister3_Desc);
        imgRegister3 = rootView.findViewById(R.id.imgRegister3);
        linearRegister4 = rootView.findViewById(R.id.linearRegister4);
        linearRegister4_Desc = rootView.findViewById(R.id.linearRegister4_Desc);
        imgRegister4 = rootView.findViewById(R.id.imgRegister4);
        linearRegister5 = rootView.findViewById(R.id.linearRegister5);
        linearRegister5_Desc = rootView.findViewById(R.id.linearRegister5_Desc);
        imgRegister5 = rootView.findViewById(R.id.imgRegister5);
        linearRegister6 = rootView.findViewById(R.id.linearRegister6);
        linearRegister6_Desc = rootView.findViewById(R.id.linearRegister6_Desc);
        imgRegister6 = rootView.findViewById(R.id.imgRegister6);
        linearRegister7 = rootView.findViewById(R.id.linearRegister7);
        linearRegister7_Desc = rootView.findViewById(R.id.linearRegister7_Desc);
        imgRegister7 = rootView.findViewById(R.id.imgRegister7);

        linearRegister1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearRegister1_Desc.getVisibility() == View.GONE) {
                    linearRegister1_Desc.setVisibility(View.VISIBLE);
                    imgRegister1.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearRegister1.setBackgroundResource(R.drawable.border_up);

                    linearRegister2_Desc.setVisibility(View.GONE);
                    imgRegister2.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister2.setBackgroundResource(R.drawable.border);

                    linearRegister3_Desc.setVisibility(View.GONE);
                    imgRegister3.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister3.setBackgroundResource(R.drawable.border);


                    linearRegister4_Desc.setVisibility(View.GONE);
                    imgRegister4.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister4.setBackgroundResource(R.drawable.border);


                    linearRegister5_Desc.setVisibility(View.GONE);
                    imgRegister5.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister5.setBackgroundResource(R.drawable.border);

                    linearRegister6_Desc.setVisibility(View.GONE);
                    imgRegister6.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister6.setBackgroundResource(R.drawable.border);

                    linearRegister7_Desc.setVisibility(View.GONE);
                    imgRegister7.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister7.setBackgroundResource(R.drawable.border);



                } else {
                    linearRegister1_Desc.setVisibility(View.GONE);
                    imgRegister1.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister1.setBackgroundResource(R.drawable.border);
                }
            }
        });
        linearRegister2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearRegister2_Desc.getVisibility() == View.GONE) {
                    linearRegister2_Desc.setVisibility(View.VISIBLE);
                    imgRegister2.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearRegister2.setBackgroundResource(R.drawable.border_up);

                    linearRegister1_Desc.setVisibility(View.GONE);
                    imgRegister1.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister1.setBackgroundResource(R.drawable.border);

                    linearRegister3_Desc.setVisibility(View.GONE);
                    imgRegister3.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister3.setBackgroundResource(R.drawable.border);


                    linearRegister4_Desc.setVisibility(View.GONE);
                    imgRegister4.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister4.setBackgroundResource(R.drawable.border);


                    linearRegister5_Desc.setVisibility(View.GONE);
                    imgRegister5.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister5.setBackgroundResource(R.drawable.border);

                    linearRegister6_Desc.setVisibility(View.GONE);
                    imgRegister6.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister6.setBackgroundResource(R.drawable.border);

                    linearRegister7_Desc.setVisibility(View.GONE);
                    imgRegister7.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister7.setBackgroundResource(R.drawable.border);


                } else {
                    linearRegister2_Desc.setVisibility(View.GONE);
                    imgRegister2.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister2.setBackgroundResource(R.drawable.border);
                }
            }
        });

        linearRegister3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearRegister3_Desc.getVisibility() == View.GONE) {
                    linearRegister3_Desc.setVisibility(View.VISIBLE);
                    imgRegister3.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearRegister3.setBackgroundResource(R.drawable.border_up);

                    linearRegister1_Desc.setVisibility(View.GONE);
                    imgRegister1.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister1.setBackgroundResource(R.drawable.border);

                    linearRegister2_Desc.setVisibility(View.GONE);
                    imgRegister2.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister2.setBackgroundResource(R.drawable.border);

                    linearRegister4_Desc.setVisibility(View.GONE);
                    imgRegister4.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister4.setBackgroundResource(R.drawable.border);


                    linearRegister5_Desc.setVisibility(View.GONE);
                    imgRegister5.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister5.setBackgroundResource(R.drawable.border);

                    linearRegister6_Desc.setVisibility(View.GONE);
                    imgRegister6.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister6.setBackgroundResource(R.drawable.border);

                    linearRegister7_Desc.setVisibility(View.GONE);
                    imgRegister7.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister7.setBackgroundResource(R.drawable.border);


                } else {
                    linearRegister3_Desc.setVisibility(View.GONE);
                    imgRegister3.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister3.setBackgroundResource(R.drawable.border);
                }
            }
        });

        linearRegister4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearRegister4_Desc.getVisibility() == View.GONE) {
                    linearRegister4_Desc.setVisibility(View.VISIBLE);
                    imgRegister4.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearRegister4.setBackgroundResource(R.drawable.border_up);

                    linearRegister1_Desc.setVisibility(View.GONE);
                    imgRegister1.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister1.setBackgroundResource(R.drawable.border);

                    linearRegister2_Desc.setVisibility(View.GONE);
                    imgRegister2.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister2.setBackgroundResource(R.drawable.border);

                    linearRegister3_Desc.setVisibility(View.GONE);
                    imgRegister3.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister3.setBackgroundResource(R.drawable.border);

                    linearRegister5_Desc.setVisibility(View.GONE);
                    imgRegister5.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister5.setBackgroundResource(R.drawable.border);

                    linearRegister6_Desc.setVisibility(View.GONE);
                    imgRegister6.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister6.setBackgroundResource(R.drawable.border);

                    linearRegister7_Desc.setVisibility(View.GONE);
                    imgRegister7.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister7.setBackgroundResource(R.drawable.border);


                } else {
                    linearRegister4_Desc.setVisibility(View.GONE);
                    imgRegister4.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister4.setBackgroundResource(R.drawable.border);
                }
            }
        });

        linearRegister5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearRegister5_Desc.getVisibility() == View.GONE) {
                    linearRegister5_Desc.setVisibility(View.VISIBLE);
                    imgRegister5.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearRegister5.setBackgroundResource(R.drawable.border_up);

                    linearRegister1_Desc.setVisibility(View.GONE);
                    imgRegister1.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister1.setBackgroundResource(R.drawable.border);

                    linearRegister2_Desc.setVisibility(View.GONE);
                    imgRegister2.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister2.setBackgroundResource(R.drawable.border);

                    linearRegister3_Desc.setVisibility(View.GONE);
                    imgRegister3.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister3.setBackgroundResource(R.drawable.border);


                    linearRegister4_Desc.setVisibility(View.GONE);
                    imgRegister4.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister4.setBackgroundResource(R.drawable.border);

                    linearRegister6_Desc.setVisibility(View.GONE);
                    imgRegister6.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister6.setBackgroundResource(R.drawable.border);

                    linearRegister7_Desc.setVisibility(View.GONE);
                    imgRegister7.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister7.setBackgroundResource(R.drawable.border);


                } else {
                    linearRegister5_Desc.setVisibility(View.GONE);
                    imgRegister5.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister5.setBackgroundResource(R.drawable.border);
                }
            }
        });

        linearRegister6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearRegister6_Desc.getVisibility() == View.GONE) {
                    linearRegister6_Desc.setVisibility(View.VISIBLE);
                    imgRegister6.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearRegister6.setBackgroundResource(R.drawable.border_up);


                    linearRegister1_Desc.setVisibility(View.GONE);
                    imgRegister1.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister1.setBackgroundResource(R.drawable.border);

                    linearRegister2_Desc.setVisibility(View.GONE);
                    imgRegister2.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister2.setBackgroundResource(R.drawable.border);

                    linearRegister3_Desc.setVisibility(View.GONE);
                    imgRegister3.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister3.setBackgroundResource(R.drawable.border);


                    linearRegister4_Desc.setVisibility(View.GONE);
                    imgRegister4.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister4.setBackgroundResource(R.drawable.border);


                    linearRegister5_Desc.setVisibility(View.GONE);
                    imgRegister5.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister5.setBackgroundResource(R.drawable.border);

                    linearRegister7_Desc.setVisibility(View.GONE);
                    imgRegister7.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister7.setBackgroundResource(R.drawable.border);


                } else {
                    linearRegister6_Desc.setVisibility(View.GONE);
                    imgRegister6.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister6.setBackgroundResource(R.drawable.border);
                }
            }
        });

        linearRegister7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearRegister7_Desc.getVisibility() == View.GONE) {
                    linearRegister7_Desc.setVisibility(View.VISIBLE);
                    imgRegister7.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearRegister7.setBackgroundResource(R.drawable.border_up);

                    linearRegister1_Desc.setVisibility(View.GONE);
                    imgRegister1.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister1.setBackgroundResource(R.drawable.border);

                    linearRegister2_Desc.setVisibility(View.GONE);
                    imgRegister2.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister2.setBackgroundResource(R.drawable.border);

                    linearRegister3_Desc.setVisibility(View.GONE);
                    imgRegister3.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister3.setBackgroundResource(R.drawable.border);


                    linearRegister4_Desc.setVisibility(View.GONE);
                    imgRegister4.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister4.setBackgroundResource(R.drawable.border);


                    linearRegister5_Desc.setVisibility(View.GONE);
                    imgRegister5.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister5.setBackgroundResource(R.drawable.border);

                    linearRegister6_Desc.setVisibility(View.GONE);
                    imgRegister6.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister6.setBackgroundResource(R.drawable.border);

                } else {
                    linearRegister7_Desc.setVisibility(View.GONE);
                    imgRegister7.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearRegister7.setBackgroundResource(R.drawable.border);
                }
            }
        });
        setAudioFunctionality();

        return rootView;
    }

    private void initializeMediaState() {
        keys = new ArrayList<>();
        keys.add(sevenRegistersAudioButton);
        keys.add(register1AudioButton);
        keys.add(register2AudioButton);
        keys.add(register3AudioButton);
        keys.add(register4AudioButton);
        keys.add(register5AudioButton);
        keys.add(register6AudioButton);
        keys.add(register7AudioButton);
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
        sevenRegistersAudioButton = rootView.findViewById(R.id.sevenRegistersAudioButton);
        register1AudioButton = rootView.findViewById(R.id.register1AudioButton);
        register2AudioButton = rootView.findViewById(R.id.register2AudioButton);
        register3AudioButton = rootView.findViewById(R.id.register3AudioButton);
        register4AudioButton = rootView.findViewById(R.id.register4AudioButton);
        register5AudioButton = rootView.findViewById(R.id.register5AudioButton);
        register6AudioButton = rootView.findViewById(R.id.register6AudioButton);
        register7AudioButton = rootView.findViewById(R.id.register7AudioButton);

        String period = getContext().getString(R.string.period) + " ";

        sevenRegistersAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = "";
                speech += getContext().getString(R.string.sevenregister);
                handleClick((ImageView) v, speech);
            }
        });
        register1AudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = "";
                speech += getContext().getString(R.string.sevenregister_1) + period;
                speech += getContext().getString(R.string.sevenregister_1_desc_1) + period;
                speech += getContext().getString(R.string.sevenregister_1_desc_2) + period;
                speech += getContext().getString(R.string.sevenregister_1_desc_3) + period;
                speech += getContext().getString(R.string.sevenregister_1_desc_4) + period;
                handleClick((ImageView) v, speech);
            }
        });

        register2AudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = "";
                speech += getContext().getString(R.string.sevenregister_2) + period;
                speech += getContext().getString(R.string.sevenregister_2_desc_1) + period;
                speech += getContext().getString(R.string.sevenregister_2_desc_2) + period;
                handleClick((ImageView) v, speech);
            }
        });

        register3AudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = "";
                speech += getContext().getString(R.string.sevenregister_3) + period;
                speech += getContext().getString(R.string.sevenregister_3_desc_1) + period;
                speech += getContext().getString(R.string.sevenregister_3_desc_2) + period;
                speech += getContext().getString(R.string.sevenregister_3_desc_3) + period;
                handleClick((ImageView) v, speech);
            }
        });

        register4AudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = "";
                speech += getContext().getString(R.string.sevenregister_4) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_1) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_2) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_3) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_4) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_5) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_6) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_7) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_8) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_9) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_10) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_11) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_12) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_13) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_14) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_14a) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_14b) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_14c) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_14d) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_14e) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_15) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_15a) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_15b) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_15c) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_15d) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_16) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_16a) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_16b) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_16c) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_16d) + period;
                speech += getContext().getString(R.string.sevenregister_4_desc_16e) + period;
                handleClick((ImageView) v, speech);
            }
        });

        register5AudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = "";
                speech += getContext().getString(R.string.sevenregister_5) + period;
                speech += getContext().getString(R.string.sevenregister_5_desc) + period;
                handleClick((ImageView) v, speech);
            }
        });

        register6AudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = "";
                speech += getContext().getString(R.string.sevenregister_6) + period;
                speech += getContext().getString(R.string.sevenregister_6_desc) + period;
                speech += getContext().getString(R.string.sevenregister_6_desc_1) + period;
                speech += getContext().getString(R.string.sevenregister_6_desc_2) + period;
                speech += getContext().getString(R.string.sevenregister_6_desc_3) + period;
                speech += getContext().getString(R.string.sevenregister_6_desc_4) + period;
                speech += getContext().getString(R.string.sevenregister_6_desc_5) + period;
                speech += getContext().getString(R.string.sevenregister_6_desc_6) + period;
                speech += getContext().getString(R.string.sevenregister_6_desc_7) + period;
                speech += getContext().getString(R.string.sevenregister_6_desc_8) + period;
                speech += getContext().getString(R.string.sevenregister_6_desc_9) + period;
                handleClick((ImageView) v, speech);
            }
        });

        register7AudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = "";
                speech += getContext().getString(R.string.sevenregister_7) + period;
                speech += getContext().getString(R.string.sevenregister_7_desc_1) + period;
                speech += getContext().getString(R.string.sevenregister_7_desc_2) + period;
                speech += getContext().getString(R.string.sevenregister_7_desc_3) + period;
                speech += getContext().getString(R.string.sevenregister_7_desc_4) + period;
                speech += getContext().getString(R.string.sevenregister_7_desc_5) + period;
                speech += getContext().getString(R.string.sevenregister_7_desc_6) + period;
                speech += getContext().getString(R.string.sevenregister_7_desc_7) + period;
                speech += getContext().getString(R.string.sevenregister_7_desc_8) + period;
                speech += getContext().getString(R.string.sevenregister_7_desc_9) + period;
                speech += getContext().getString(R.string.sevenregister_7_desc_10) + period;
                speech += getContext().getString(R.string.sevenregister_7_desc_11) + period;
                speech += getContext().getString(R.string.sevenregister_7_desc_12) + period;
                speech += getContext().getString(R.string.sevenregister_7_desc_13) + period;
                speech += getContext().getString(R.string.sevenregister_7_desc_14) + period;
                speech += getContext().getString(R.string.sevenregister_7_desc_15) + period;
                handleClick((ImageView) v, speech);
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
