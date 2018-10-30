package io.github.davidwickerhf.diceroller;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;


public class DashboardFragment extends Fragment {
    
    private static final int ADD_NOTE_REQUEST = 1;
    private MainActivity mMainActivity;
    private SettingsViewModel settingsViewModel;
    private SettingAdapter adapter;
    private CoordinatorLayout coordinatorLayout;
    private ImageView addButton;
    private ImageView itemBackgorund;
    
    private Snackbar mSnackbar;
    private Setting recoveredSetting;
    public int selectedItemPosition;
    
    //todo Interface for sharing data ( get Settings Information from AddSettingActivity)
    private FragmentDashboardListener listener;
    public interface FragmentDashboardListener {
        void onInputASent(int maxNumber, int position, View itemView, TextView textViewTitle, TextView textViewMaxNum, ImageView editButtonView);
    }
    
    private DashboardItemPositionListener itemListener;
    public interface DashboardItemPositionListener {
        void onEditInputASent(int position);
    }
    
    
    
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View fragmentView =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        coordinatorLayout = (CoordinatorLayout) fragmentView.findViewById(R.id.coordinatorLayout);
        mMainActivity = new MainActivity();
        
        
        //todo Add Setting Button:
        addButton = fragmentView.findViewById(R.id.button_add_setting);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddSettingActivity.class);
                getActivity().startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });
        
        //todo Retrieve Selected Item from Previous session
        if (getArguments() != null){
            selectedItemPosition = getArguments().getInt("selectedItemPosition");
        }
        
        //todo Recycler View:
        final RecyclerView recyclerView = fragmentView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        // Set Adapter
        adapter = new SettingAdapter();
        recyclerView.setAdapter(adapter);
        
        //todo Interface for returning edit icon item clicked
        adapter.setOnEditItemClickLister(new SettingAdapter.OnEditItemClickListener() {
            @Override
            public void onEditItemClick(int position) {
                itemListener.onEditInputASent(position);
            }
        });
        
        //todo Interface for returning clicked item
        adapter.setOnItemClickLister(new SettingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View itemView, TextView textViewTitle, TextView textViewMaxNum, ImageView editButtonView) {

                int maxNum = adapter.getSettings().get(position).getMaxDiceSum();
                Snackbar.make(coordinatorLayout, "Setting selected", Snackbar.LENGTH_LONG).show();
                
                listener.onInputASent(maxNum, position, itemView, textViewTitle, textViewMaxNum, editButtonView);
            }
        });
        
        
        
        
        //todo View Model
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        settingsViewModel.getAllSettings().observe(this, new Observer<List<Setting>>() {
            @Override
            public void onChanged(@Nullable List<Setting> settings) {
                adapter.setSettings(settings);
            }
        });
        
        
        
        
        //todo Make Recycler Swipable
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
        
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                
                recoveredSetting = adapter.getSettingAt(viewHolder.getAdapterPosition());
                settingsViewModel.delete(adapter.getSettingAt(viewHolder.getAdapterPosition()));
    
                mSnackbar = Snackbar
                        .make(coordinatorLayout, "Setting deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                settingsViewModel.insert(recoveredSetting);
                                Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Setting restored", Snackbar.LENGTH_SHORT);
                                snackbar1.show();
                            }
                        });
    
                mSnackbar.show();
            }
        }).attachToRecyclerView(recyclerView);
        
        return fragmentView;
    }
    
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
            listener = (FragmentDashboardListener)context;
            itemListener = (DashboardItemPositionListener)context;
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        itemListener = null;
    }
}
