package io.github.davidwickerhf.diceroller;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class ProfileInfoFragment extends Fragment{
    View fragmentView;
    private SettingAdapter adapter;
    
    public ProfileInfoFragment() {
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_profile_info, container, false);
    
        //todo Recycler View:
        final RecyclerView recyclerView = fragmentView.findViewById(R.id.info_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        // Set Adapter
        adapter = new SettingAdapter();
        recyclerView.setAdapter(adapter);
        
        
        return fragmentView;
    }
}
