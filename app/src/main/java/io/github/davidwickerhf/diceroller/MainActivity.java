package io.github.davidwickerhf.diceroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DashboardFragment.FragmentDashboardListener, DashboardFragment.DashboardItemPositionListener {
    
    private static final int ADD_SETTING_REQUEST = 1;
    private static final int EDIT_SETTING_REQUEST = 2;
    
    private SettingsViewModel settingsViewModel;
    private SettingAdapter adapter;
    
    private HomeFragment mHomeFragment;
    private DashboardFragment mDashboardFragment;
    private ProfileFragment mProfileFragment;
    
    private int selectedItem;
    private View selectedItemView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //todo Initilize SettingsAdapter
        adapter = new SettingAdapter();
        
        //todo Initialize ViewModel
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        settingsViewModel.getAllSettings().observe(this, new Observer<List<Setting>>() {
            @Override
            public void onChanged(@Nullable List<Setting> settings) {
                adapter.setSettings(settings);
            }
        });
        
        //todo navigation view
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        
        //todo Initialize fragments
        mHomeFragment = new HomeFragment();
        mDashboardFragment = new DashboardFragment();
        mProfileFragment = new ProfileFragment();
        
        //todo Display HomeFragment:
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                mHomeFragment).commit();
        
    }
    
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = mHomeFragment;
                            break;
                        case R.id.nav_dashboard:
                            selectedFragment = mDashboardFragment;
                            break;
                        case R.id.nav_profile:
                            selectedFragment = mProfileFragment;
                            break;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    return true;
                }
            };
    
    
    //todo NOT WORKING:
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == ADD_SETTING_REQUEST && resultCode == RESULT_OK) {
            
            
            String title = data.getStringExtra(AddSettingActivity.EXTRA_TITLE);
            int maxDiceSum = data.getIntExtra(AddSettingActivity.EXTRA_MAX_NUMBER, 30);
            int priority = data.getIntExtra(AddSettingActivity.EXTRA_PRIORITY, 1);
            
            Setting setting = new Setting(title, maxDiceSum, priority);
            settingsViewModel.insert(setting);
            
            Toast.makeText(MainActivity.this, "Setting saved", Toast.LENGTH_SHORT).show();
            
            
        } else if (requestCode == EDIT_SETTING_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddSettingActivity.EXTRA_ID, -1);
            
            if(id == -1){
                Toast.makeText(MainActivity.this, "Setting Can't be Updated", Toast.LENGTH_SHORT).show();
                return;
            }
    
            String title = data.getStringExtra(AddSettingActivity.EXTRA_TITLE);
            int maxDiceSum = data.getIntExtra(AddSettingActivity.EXTRA_MAX_NUMBER, 30);
            int priority = data.getIntExtra(AddSettingActivity.EXTRA_PRIORITY, 1);
            
            Setting setting = new Setting(title, maxDiceSum, priority);
            setting.setId(id);
            settingsViewModel.update(setting);
    
            Toast.makeText(MainActivity.this, "Setting updated", Toast.LENGTH_SHORT).show();
    
            
        } else {
            Toast.makeText(MainActivity.this, "Setting Not Saved", Toast.LENGTH_SHORT).show();
        }
    }
    //todo
    
    @Override
    public void onInputASent(int maxNumber, int position, View itemView) {
        Bundle args = new Bundle();
        args.putInt("argMaxNumber", maxNumber);
        
        if(!(selectedItemView == null)) {
            if (selectedItem != position && selectedItemView != itemView){
                selectedItemView.setBackgroundResource(R.drawable.recycler_view_background);
            }
        }
        
        mHomeFragment.setArguments(args);
        
        selectedItemView = itemView;
        selectedItem = position;
    }
    
    @Override
    public void onEditInputASent(int position) {
        
        Intent intent = new Intent(MainActivity.this, AddSettingActivity.class);
        intent.putExtra(AddSettingActivity.EXTRA_ID, adapter.getSettingAt(position).getId());
        intent.putExtra(AddSettingActivity.EXTRA_TITLE, adapter.getSettingAt(position).getTitle());
        intent.putExtra(AddSettingActivity.EXTRA_MAX_NUMBER, adapter.getSettingAt(position).getMaxDiceSum());
        intent.putExtra(AddSettingActivity.EXTRA_PRIORITY, adapter.getSettingAt(position).getPriority());
        startActivityForResult(intent, EDIT_SETTING_REQUEST);
        
    }
}
