package nic.hp.ccmgnrega.fragment;

import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.common.MediaPlayerHelper;
import nic.hp.ccmgnrega.common.MediaStateManager;
import nic.hp.ccmgnrega.common.TaskCompletionListener;
import nic.hp.ccmgnrega.common.Utility;
import nic.hp.ccmgnrega.data.JobCardDataAccess;
import nic.hp.ccmgnrega.data.JobCardTabData;
import nic.hp.ccmgnrega.databinding.FragmentJobCardBinding;
import nic.hp.ccmgnrega.model.JobCardData;

public class JobCardFragment extends BaseFragment implements TaskCompletionListener {

    JobCardData jobCardData;
    public FragmentJobCardBinding binding;
    private List<ImageView> keys;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTheme(R.style.AppTheme1);
        binding = FragmentJobCardBinding.inflate(inflater, container, false);
        jobCardData = SearchFragment.jobCardData;
        JobCardDataAccess.setJobCardTabTaskCompletionListener(this);
        JobCardTabData.populateJobCardTabData(binding, getContext(), jobCardData);

        new Thread(() ->{
            try {
                if (JobCardDataAccess.jobCardTabLatch != null) {
                    JobCardDataAccess.jobCardTabLatch.await();
                    new Handler(Looper.getMainLooper()).post(() -> {
                        onAllTasksCompleted();
                        setAudioFunctionality();
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        return binding.getRoot();
    }

    private void initializeMediaState() {
        keys = new ArrayList<>();
        keys.add(binding.jobCardDetailsAudioButton);
        keys.add(binding.applicantDetailsAudioButton);

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

    private void setAudioFunctionality() {
        if (!Utility.translationAllowed(getContext())) {
            return;
        }
        binding.jobCardDetailsAudioButton.setVisibility(View.VISIBLE);
        binding.applicantDetailsAudioButton.setVisibility(View.VISIBLE);
        binding.jobCardDetailsAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = JobCardTabData.getJobCardDetailsSpeech();
                handleClick((ImageView)v, speech);
            }
        });

        binding.applicantDetailsAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = JobCardTabData.getApplicantDetailsSpeech();
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
        binding.getRoot().requestLayout();
    }

    @Override
    public void onAllTasksCompleted() {
        JobCardTabData.populateJobCardTabData(binding, getContext(), jobCardData);
    }

    @Override
    public void onStop() {
        super.onStop();
        MediaPlayerHelper.releaseMediaPlayer();
    }
}