package nic.hp.ccmgnrega.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.data.AbpsStatusTabData;
import nic.hp.ccmgnrega.databinding.FragmentAbpsStatusBinding;
import nic.hp.ccmgnrega.model.JobCardData;


public class AbpsStatusFragment extends BaseFragment {

    FragmentAbpsStatusBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTheme(R.style.AppTheme1);
        binding = FragmentAbpsStatusBinding.inflate(inflater, container, false);
        JobCardData jobCardData = SearchFragment.jobCardData;
        TextView textView = binding.totalApplicants;
        textView.setText("Total Applicants: " + jobCardData.getTotalApplicantsInJobCard());
        AbpsStatusTabData.populateAbpsStatusTabData(binding, getContext(), jobCardData);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.getRoot().requestLayout();
    }
}