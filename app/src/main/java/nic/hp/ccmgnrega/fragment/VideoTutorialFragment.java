package nic.hp.ccmgnrega.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.common.MediaPlayerHelper;
import nic.hp.ccmgnrega.common.MediaStateManager;
import nic.hp.ccmgnrega.common.Utility;


public class VideoTutorialFragment extends BaseFragment {
    protected View rootView;
    TextView tvHeading,tvContent,tvContent2,tvContent3,tvContent4, tvContent8;
    private List<ImageView> keys;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        rootView = inflater.inflate(R.layout.fragment_video_turorials, container, false);
        Bundle args = getArguments();
        if (args != null && args.getString("menu") != null) {
            menu = args.getString("menu");
        }
        tvHeading = rootView.findViewById(R.id.heading);
        tvHeading.setText(" "+menu);
        tvContent = rootView.findViewById(R.id.tvContent);
        tvContent.setClickable(true);
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvContent2 = rootView.findViewById(R.id.tvContent2);
        tvContent2.setClickable(true);
        tvContent2.setMovementMethod(LinkMovementMethod.getInstance());
        tvContent3 = rootView.findViewById(R.id.tvContent3);
        tvContent3.setClickable(true);
        tvContent3.setMovementMethod(LinkMovementMethod.getInstance());
        tvContent8 = rootView.findViewById(R.id.tvContent8);
        tvContent8.setClickable(true);
        tvContent8.setMovementMethod(LinkMovementMethod.getInstance());

        tvContent4 = rootView.findViewById(R.id.tvContent4);
        tvContent4.setClickable(true);
        tvContent4.setMovementMethod(LinkMovementMethod.getInstance());

        String strContent=getContext().getString(R.string.video_content1);
        String strContent2=getContext().getString(R.string.in_english) + "<a href='https://youtu.be/L2q2HjNG-Hk'> Discover Janmanrega: Know Worker's Payment, Attendance, Job-Card Details & APBS Status </a>";
        String strContent3=getContext().getString(R.string.in_hindi) + "<a href='https://youtu.be/d3IGiWk4flM'> जनमनरेगा (Janmanrega) मोबाइल App में जानें कर्मचारी की भुगतान/उपस्थिति </a>";
        String strContent8="In Telugu: " + "<a href='https://youtu.be/D1M5DanjmsM'> తెలుగులో జన్మరేగ: Know Worker's Payment, Attendance, Job-Card Details & APBS Status </a>";
        String strContent4=getContext().getString(R.string.video_content2)+" "+"<a href=''>"+getContext().getString(R.string.link) +"</a>";
        tvContent.setText(Html.fromHtml(strContent));
        tvContent2.setText(Html.fromHtml(strContent2));
        tvContent3.setText(Html.fromHtml(strContent3));
        tvContent8.setText(Html.fromHtml(strContent8));
        tvContent4.setText(Html.fromHtml(strContent4));
        tvContent4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_SUBJECT,getContext().getString(R.string.link));
                i.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=nic.hp.ccmgnrega&pcampaignid=web_share");
                startActivity(Intent.createChooser(i,"Share via"));
            }
        });
        setAudioFunctionality();

        WebView webViewEnglish = rootView.findViewById(R.id.webviewEnglish);
        String videoEnglish = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/L2q2HjNG-Hk?si=DAPigiNeOfZPizsY\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webViewEnglish.loadData(videoEnglish, "text/html", "utf-8");
        webViewEnglish.getSettings().setJavaScriptEnabled(true);
        webViewEnglish.setWebChromeClient(new WebChromeClient());

        WebView webviewHindi = rootView.findViewById(R.id.webviewHindi);
        String videoHindi = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/d3IGiWk4flM?si=Rw9x3sfblqUjrIje\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webviewHindi.loadData(videoHindi, " text/html", "utf-8");
        webviewHindi.getSettings().setJavaScriptEnabled(true);
        webviewHindi.setWebChromeClient(new WebChromeClient());

        WebView webviewTelugu = rootView.findViewById(R.id.webviewTelugu);
        String videoTelugu = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/D1M5DanjmsM?si=a_pNdYQIoSkZea9X\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        webviewTelugu.loadData(videoTelugu, "text/html", "utf-8");
        webviewTelugu.getSettings().setJavaScriptEnabled(true);
        webviewTelugu.setWebChromeClient(new WebChromeClient());

        return rootView;
    }

    private void setAudioFunctionality() {
        rootView.findViewById(R.id.videoTutorialAudioButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = "";
                speech += getContext().getString(R.string.video_content1);
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
        keys.add(rootView.findViewById(R.id.videoTutorialAudioButton));

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