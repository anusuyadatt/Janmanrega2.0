package nic.hp.ccmgnrega.fragment;

import static android.view.View.GONE;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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
import nic.hp.ccmgnrega.data.JobCardTabData;

public class AboutMGNREGAFragment extends BaseFragment {
    protected View rootView;
    protected TextView headingTv;
    private List<ImageView> keys;
    RelativeLayout linearObjective, linearKeyStakeholder;
    LinearLayout linearObjectiveDesc, linearimgKeyStakeholderDesc;
    ImageView imgObjective, imgKeyStakeholder;
    @Override
    public View onCreateView(LayoutInflater inflater2, ViewGroup container, Bundle savedInstanceState) {
       // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        Bundle args = getArguments();
        if (args != null && args.getString("menu") != null) {
            menu = args.getString("menu");
        }
        rootView = inflater2.inflate(R.layout.fragment_about_mgnerga, container, false);
        headingTv = rootView.findViewById(R.id.heading); headingTv.setText("  "+menu);
        linearObjective = rootView.findViewById(R.id.linearObjective);
        linearObjectiveDesc = rootView.findViewById(R.id.linearObjectiveDesc);
        imgObjective = rootView.findViewById(R.id.imgObjective);
        linearKeyStakeholder = rootView.findViewById(R.id.linearKeyStakeholder);
        linearimgKeyStakeholderDesc = rootView.findViewById(R.id.linearimgKeyStakeholderDesc);
        imgKeyStakeholder = rootView.findViewById(R.id.imgKeyStakeholder);
        linearObjective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearObjectiveDesc.getVisibility() == GONE) {
                    linearObjectiveDesc.setVisibility(View.VISIBLE);
                    imgObjective.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearObjective.setBackgroundResource(R.drawable.border_up);

                    linearimgKeyStakeholderDesc.setVisibility(GONE);
                    imgKeyStakeholder.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearKeyStakeholder.setBackgroundResource(R.drawable.border);

                } else {
                    linearObjectiveDesc.setVisibility(GONE);
                    imgObjective.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearObjective.setBackgroundResource(R.drawable.border);
                }
            }
        });
        linearKeyStakeholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearimgKeyStakeholderDesc.getVisibility() == GONE) {
                    linearimgKeyStakeholderDesc.setVisibility(View.VISIBLE);
                    imgKeyStakeholder.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    linearKeyStakeholder.setBackgroundResource(R.drawable.border_up);

                    linearObjectiveDesc.setVisibility(GONE);
                    imgObjective.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearObjective.setBackgroundResource(R.drawable.border);

                } else {
                    linearimgKeyStakeholderDesc.setVisibility(GONE);
                    imgKeyStakeholder.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    linearKeyStakeholder.setBackgroundResource(R.drawable.border);
                }
            }
        });

      /*  tvContent = rootView.findViewById(R.id.tvContent);
        tvContent5 = rootView.findViewById(R.id.tvContent5);
        tvContent.setText(Html.fromHtml("<u>"+getContext().getString(R.string.objective_of_scheme)+"</u>"));
        tvContent5.setText(Html.fromHtml("<u>"+getContext().getString(R.string.key_stakeholders)+"</u>"));*/
        setAudioFunctionality();
        return rootView;
    }

    private void setAudioFunctionality() {
        rootView.findViewById(R.id.aboutMgnregaAudioButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = getSpeech();
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

    private void initializeMediaState() {
        keys = new ArrayList<>();
        keys.add(rootView.findViewById(R.id.aboutMgnregaAudioButton));

        MediaStateManager.initializeState(keys);
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

    private String getSpeech() {
        String speech = "";
        String period = getContext().getString(R.string.period) + " ";

        speech += headingTv.getText() + period;
        speech += getContext().getString(R.string.about_mgnrega_content_2024) + period;
        speech += getContext().getString(R.string.objective_of_scheme) + period;
        speech += getContext().getString(R.string.objective_1) + period;
        speech += "two" + period + getContext().getString(R.string.objective_2) + period;
        speech += "three" + period + getContext().getString(R.string.objective_3) + period;
        speech += "four" + period + getContext().getString(R.string.objective_4) + period;
        speech += getContext().getString(R.string.key_stakeholders) + period;
        speech += getContext().getString(R.string.key_stakeholder_header) + period;
        speech += getContext().getString(R.string.key_stakeholder_1) + period;
        speech += getContext().getString(R.string.key_stakeholder_2) + period;
        speech += getContext().getString(R.string.key_stakeholder_3) + period;
        speech += getContext().getString(R.string.key_stakeholder_4) + period;
        speech += getContext().getString(R.string.key_stakeholder_5) + period;
        speech += getContext().getString(R.string.key_stakeholder_6) + period;
        speech += getContext().getString(R.string.key_stakeholder_7) + period;
        speech += getContext().getString(R.string.key_stakeholder_8) + period;
        speech += getContext().getString(R.string.key_stakeholder_9) + period;
        speech += getContext().getString(R.string.key_stakeholder_10) + period;
        speech += getContext().getString(R.string.key_stakeholder_11) + period;

        return speech;
    }

    @Override
    public void onStop() {
        super.onStop();
        MediaPlayerHelper.releaseMediaPlayer();
    }
    @Override
    public void onResume() {
        super.onResume();
        initializeMediaState();
    }

}