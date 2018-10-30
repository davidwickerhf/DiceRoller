package io.github.davidwickerhf.diceroller;


import android.app.Activity;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class ProfileFragment extends Fragment {
    
    private SettingsViewModel settingsViewModel;
    private SettingAdapter adapter;
    private CoordinatorLayout coordinatorLayout;
    private View fragmentView;
    
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
    
        MainActivity mMainActivity = (MainActivity) getActivity();
        
        androidx.appcompat.widget.Toolbar mToolbar = fragmentView.findViewById(R.id.profile_toolbar);
        mMainActivity.setSupportActionBar(mToolbar);
    
        mViewPager = (ViewPager) fragmentView.findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(mMainActivity.getSupportFragmentManager());
        adapter.AddFragment(new ProfileInfoFragment(), "Profile");
        adapter.AddFragment(new ProfileMessagingFragment(), "Messages");
        mViewPager.setAdapter(adapter);
        mTabLayout = (TabLayout) fragmentView.findViewById(R.id.tab_layout_profile);
        mTabLayout.setupWithViewPager(mViewPager);
    
        return fragmentView;
    }
    
}
