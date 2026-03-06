package nic.hp.ccmgnrega.fragment;

import android.os.Bundle;


import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.adapter.PaymentExpandableListAdapter1;
import nic.hp.ccmgnrega.common.IntegerStringComparator;
import nic.hp.ccmgnrega.common.ExpandableListMediaStateManager;
import nic.hp.ccmgnrega.common.MediaPlayerHelper;
import nic.hp.ccmgnrega.common.Utility;
import nic.hp.ccmgnrega.common.MySharedPref;
import nic.hp.ccmgnrega.data.JobCardDataAccess;
import nic.hp.ccmgnrega.data.PaymentTabData;
import nic.hp.ccmgnrega.databinding.FragmentPaymentBinding;
import nic.hp.ccmgnrega.model.Bhashini.BhashiniConfigResponse;
import nic.hp.ccmgnrega.model. PaymentInfo;
import nic.hp.ccmgnrega.model.Bhashini.BhashiniComputeResponse;
import nic.hp.ccmgnrega.model.JobCardData;
import retrofit2.Call;

public class PaymentFragment extends BaseFragment {

    ExpandableListView complexExpandableListView;
    ExpandableListAdapter adapter;
    String languageCode;
    public static HashMap<String, HashMap<String,List<PaymentInfo>>> paymentTabData;
    private List<String> applicantIdList;
    private List<List<String>> workCodeList = new ArrayList<>();
    Comparator comparator = new IntegerStringComparator();
    FragmentPaymentBinding binding;
    static Call<BhashiniConfigResponse> configCall;
    static Call<BhashiniComputeResponse> computeCall;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTheme(R.style.AppTheme1);
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        complexExpandableListView = binding.expandableListViewPayment;
        ViewGroup.LayoutParams layoutParams = complexExpandableListView.getLayoutParams();
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        layoutParams.height = (int)(0.70*(double)displayMetrics.heightPixels);
        complexExpandableListView.setLayoutParams(layoutParams);
        complexExpandableListView.requestLayout();
        JobCardData jobCardData = SearchFragment.jobCardData;
        PaymentTabData.populatePaymentTabData(binding, getContext(), jobCardData);
        paymentTabData = PaymentTabData.paymentTabData;
        applicantIdList = new ArrayList<>(JobCardDataAccess.applicantIdSet);
        Collections.sort(applicantIdList, comparator);

        for (String applicantId : applicantIdList) {
            if (paymentTabData.get(applicantId) == null) {
                workCodeList.add(new ArrayList<>());
            } else {
                List<String> applicantWorkCodeList = new ArrayList<>(paymentTabData.get(applicantId).keySet());
                if (applicantWorkCodeList == null || applicantWorkCodeList.isEmpty()) {
                    applicantWorkCodeList = Arrays.asList(getContext().getString(R.string.noPayments));
                }
                workCodeList.add(applicantWorkCodeList);
            }
        }

        languageCode = Utility.getBhashiniLanguageCode(MySharedPref.getAppLangCode(getContext()));
        adapter = new PaymentExpandableListAdapter1(getContext(), applicantIdList, workCodeList, paymentTabData, languageCode);
        complexExpandableListView.setAdapter(adapter);
        complexExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup && complexExpandableListView!= null)
                    complexExpandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

        new Thread(() -> {
            try {
                if (JobCardDataAccess.paymentTabLatch != null) {
                    JobCardDataAccess.paymentTabLatch.await();
                    new Handler(Looper.getMainLooper()).post(() -> {
                        adapter = new PaymentExpandableListAdapter1(getContext(), applicantIdList, workCodeList, paymentTabData, languageCode);
                        complexExpandableListView.setAdapter(adapter);
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        return binding.getRoot();
    }
    public void refreshExpandableList() {
        if (complexExpandableListView != null) {
            for (int i = 0; i < applicantIdList.size(); i++) {
                complexExpandableListView.collapseGroup(i);
            }
            complexExpandableListView.setAdapter(adapter);
        }
    }

    private void initializeMediaState() {
        List<List<Boolean>> mediaState = new ArrayList<>();
        for (int i = 0; i < workCodeList.size(); i++) {
            List<Boolean> applicantMediaState = new ArrayList<>();
            for (int j = 0; j < workCodeList.get(i).size(); j++) {
                applicantMediaState.add(false);
            }
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
    @Override
    public void onStop() {
        super.onStop();
        MediaPlayerHelper.releaseMediaPlayer();
    }
}