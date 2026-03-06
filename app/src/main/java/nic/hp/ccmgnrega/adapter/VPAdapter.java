package nic.hp.ccmgnrega.adapter;

import androidx.annotation.NonNull;
import androidx.viewpager2.adapter.FragmentStateAdapter;

//import android.app.Fragment;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import nic.hp.ccmgnrega.JobCardHolderActivity;
import nic.hp.ccmgnrega.fragment.BaseFragment;

public class VPAdapter extends FragmentStateAdapter {
    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();
    public VPAdapter(@NonNull JobCardHolderActivity jobCardHolderActivity) {
        super(jobCardHolderActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = fragmentArrayList.get(position);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }

    public void addFragment(BaseFragment fragment, String title) {
        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
    }

    public String getFragmentTitle(int position) {
        return fragmentTitle.get(position);
    }
}
