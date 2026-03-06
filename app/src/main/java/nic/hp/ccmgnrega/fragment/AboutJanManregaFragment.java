package nic.hp.ccmgnrega.fragment;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.common.MediaPlayerHelper;
import nic.hp.ccmgnrega.common.MediaStateManager;
import nic.hp.ccmgnrega.common.Utility;

public class AboutJanManregaFragment extends BaseFragment {
    protected View rootView;
    TextView tvHeading,tvContent1,tvContent2,tvContent3,tvContent4,tvContent5,tvContent6;
    private List<ImageView> keys;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        rootView = inflater.inflate(R.layout.fragment_about_janmanrega, container, false);
        Bundle args = getArguments();
        if (args != null && args.getString("menu") != null) {
            menu = args.getString("menu");
        }
        tvHeading = rootView.findViewById(R.id.heading);
        tvHeading.setText(" "+menu);
        tvContent1 = rootView.findViewById(R.id.tvContent1);
        tvContent2 = rootView.findViewById(R.id.tvContent2);
        tvContent3 = rootView.findViewById(R.id.tvContent3);
        tvContent4 = rootView.findViewById(R.id.tvContent4);
        tvContent5 = rootView.findViewById(R.id.tvContent5);
        tvContent6 = rootView.findViewById(R.id.tvContent6);
        try {
            PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            tvContent1.setText(Html.fromHtml(String.format(getResources().getString(R.string.about_janmanrega_content1), " <font color=" + ContextCompat.getColor(getContext(), R.color.green_color) + ">"+"(version: "+packageInfo.versionName+")</font>")));
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }


        tvContent2.setText(getContext().getString(R.string.about_janmanrega_content2));
        tvContent3.setText(Html.fromHtml(" <font color=" + ContextCompat.getColor(getContext(), R.color.green_color) + ">"+"<b>"+"1."+ getContext().getString(R.string.give_asset_feedback)+":" + "</b></font>"+getContext().getString(R.string.about_janmanrega_content3)));
        tvContent4.setText(Html.fromHtml(" <font color=" + ContextCompat.getColor(getContext(), R.color.green_color) + ">"+"<b>"+"2."+ getContext().getString(R.string.know_worker_att_pay)+":" + "</b></font>"+getContext().getString(R.string.about_janmanrega_content4)));
        tvContent5.setText(Html.fromHtml(" <font color=" + ContextCompat.getColor(getContext(), R.color.green_color) + ">"+"<b>"+"3."+ getContext().getString(R.string.nearby_assets)+":" + "</b></font>"+getContext().getString(R.string.about_janmanrega_content5)));
        tvContent6.setText(getContext().getString(R.string.about_janmanrega_content6));
        return rootView;
    }

    private void setAudioFunctionality() {
        rootView.findViewById(R.id.aboutJanmanregaAudioButton).setOnClickListener(new View.OnClickListener() {
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
        keys.add(rootView.findViewById(R.id.aboutJanmanregaAudioButton));

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

        speech += tvHeading.getText() + period;
        speech += getContext().getString(R.string.about_janmanrega_content1) + period;
        speech += getContext().getString(R.string.about_janmanrega_content2) + period;
        speech += getContext().getString(R.string.give_asset_feedback) + period;
        speech += getContext().getString(R.string.about_janmanrega_content3) + period;
        speech += getContext().getString(R.string.know_worker_att_pay) + period;
        speech += getContext().getString(R.string.about_janmanrega_content4) + period;
        speech += getContext().getString(R.string.nearby_assets) + period;
        speech += getContext().getString(R.string.about_janmanrega_content5) + period;
        speech += getContext().getString(R.string.about_janmanrega_content6) + period;

        return speech;
    }
    @Override
    public void onResume() {
        super.onResume();
        initializeMediaState();
        setAudioFunctionality();
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