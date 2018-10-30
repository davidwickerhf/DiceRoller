package io.github.davidwickerhf.diceroller;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> fragmentListTitle = new ArrayList<>();
    
    
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
    
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentListTitle.get(position);
    }
    
    public void AddFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        fragmentListTitle.add(title);
    }
}
