package io.github.davidwickerhf.diceroller;


import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PreferencesFragment extends Fragment {

    // Views
    private CoordinatorLayout coordinatorLayout;
    private View fragmentView;

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_preferences, container, false);
    
        MainActivity mMainActivity = (MainActivity) getActivity();
        
        androidx.appcompat.widget.Toolbar mToolbar = fragmentView.findViewById(R.id.profile_toolbar);
        mMainActivity.setSupportActionBar(mToolbar);

        return fragmentView;
    }
    
}
