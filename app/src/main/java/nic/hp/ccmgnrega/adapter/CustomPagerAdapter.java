//package nic.hp.ccmgnrega.adapter;
//
//import android.app.Fragment;
////import android.app.FragmentManager;
//import android.view.View;
//import android.view.ViewGroup;
//
////import androidx.fragment.app.Fragment;
////import androidx.fragment.app.FragmentManager;
//import android.app.FragmentManager;
//import android.support.v13.app.FragmentStatePagerAdapter;
//
//
//
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.FragmentPagerAdapter;
//import androidx.fragment.app.FragmentStatePagerAdapter;
//import androidx.viewpager.widget.PagerAdapter;
//import androidx.viewpager2.adapter.FragmentStateAdapter;
//
//import java.util.ArrayList;
//
//import nic.hp.ccmgnrega.fragment.UserFragment;
//
//public class CustomPagerAdapter extends FragmentStatePagerAdapter {
//
//    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
//    private final ArrayList<String> fragmentTitle = new ArrayList<>();
//    private FragmentManager fragmentManager;
//
//    public CustomPagerAdapter(FragmentManager fragmentManager) {
//        super(fragmentManager);;
//    }
//
//    @NonNull
//    @Override
//    public Fragment getItem(int position) {
//        return fragmentArrayList.get(position);
//    }
//
//    @Override
//    public int getCount() {
//        return fragmentArrayList.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//        return false;
//    }
//
//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        Fragment fragment = fragmentArrayList.get(position);
//        return fragment;
//    }
//
//    public void addFragment(Fragment fragment, String title) {
//        fragmentArrayList.add(fragment);
//        fragmentTitle.add(title);
//    }
//
//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return fragmentTitle.get(position);
//    }
//
//    public android.app.Fragment getLegacyFragment(int position) {
//        Fragment fragment = fragmentArrayList.get(position);
//        android.app.Fragment legacyFragment = android.app.Fragment.instantiate(fragment.getContext(), fragmentTitle.get(position);
//         return legacyFragment;
//    }
//}
