package nic.hp.ccmgnrega.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import nic.hp.ccmgnrega.adapter.PersonalAssetsAdapter;
import nic.hp.ccmgnrega.common.ExpandableListMediaStateManager;
import nic.hp.ccmgnrega.data.JobCardDataAccess;
import nic.hp.ccmgnrega.data.PersonalAssetsTabData;
import nic.hp.ccmgnrega.databinding.FragmentPersonalAssetsBinding;
import nic.hp.ccmgnrega.model.JobCardData;
import nic.hp.ccmgnrega.model.PersonalAsset;

public class PersonalAssetsFragment extends BaseFragment{
    FragmentPersonalAssetsBinding binding;
    ExpandableListView expandableListView;
    List<PersonalAsset> personalAssets;
    BaseExpandableListAdapter adapter;
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonalAssetsBinding.inflate(inflater, container, false);
        expandableListView = binding.expandableListViewPersonalAssets;
        JobCardData jobCardData = SearchFragment.jobCardData;
        PersonalAssetsTabData.populatePersonalAssetsTabData(binding, getContext(), jobCardData);
        personalAssets = jobCardData.getPersonalAssets();

        adapter = new PersonalAssetsAdapter(getContext(), personalAssets, JobCardDataAccess.workCodeToPersonalAssetImageMapping, JobCardDataAccess.personalAssetsWorkCodeToWorkNameMapping);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup && null != expandableListView)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

        new Thread(() ->{
            try {
                if (JobCardDataAccess.personalAssetsImageTabLatch != null) {
                    JobCardDataAccess.personalAssetsImageTabLatch.await();
                    new Handler(Looper.getMainLooper()).post(() -> {
                        adapter = new PersonalAssetsAdapter(getContext(), personalAssets, JobCardDataAccess.workCodeToPersonalAssetImageMapping, JobCardDataAccess.personalAssetsWorkCodeToWorkNameMapping);
                        expandableListView.setAdapter(adapter);
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                if (JobCardDataAccess.personalAssetsTabLatch != null) {
                    JobCardDataAccess.personalAssetsTabLatch.await();
                    new Handler(Looper.getMainLooper()).post(() -> {
                        adapter = new PersonalAssetsAdapter(getContext(), personalAssets, JobCardDataAccess.workCodeToPersonalAssetImageMapping, JobCardDataAccess.personalAssetsWorkCodeToWorkNameMapping);
                        expandableListView.setAdapter(adapter);
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        return binding.getRoot();
    }

    public void refreshExpandableList() {
        if (expandableListView != null) {
            for (int i = 0; i < personalAssets.size(); i++) {
                expandableListView.collapseGroup(i);
            }
            expandableListView.setAdapter(adapter);
        }
    }

    private void initializeMediaState() {
        List<List<Boolean>> mediaState = new ArrayList<>();
        for (int i = 0; i < personalAssets.size(); i++) {
            List<Boolean> applicantMediaState = new ArrayList<>();
            applicantMediaState.add(false);
            mediaState.add(applicantMediaState);
        }
        ExpandableListMediaStateManager.initializeState(mediaState);
    }
    @Override
    public void onResume() {
        super.onResume();
        refreshExpandableList();
        initializeMediaState();
        binding.getRoot().requestLayout();
    }
}
