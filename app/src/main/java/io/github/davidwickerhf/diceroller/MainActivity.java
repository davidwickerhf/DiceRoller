//todo REMAINDER
// ADD COPY OF DASHBOARD FRAGMENT LAYOUT FOR ADDING LISTS OF ITEMS


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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DashboardFragment.FragmentDashboardListener, DashboardFragment.DashboardItemPositionListener {

    //todo Variables
    private static final int ADD_SETTING_REQUEST = 1;
    private static final int EDIT_SETTING_REQUEST = 2;
    private int selectedItem;

    //todo Views
    private View selectedItemView;

    //todo Components
    private SettingsViewModel settingsViewModel;
    private SettingAdapter adapter;

    private HomeFragment mHomeFragment;
    private DashboardFragment mDashboardFragment;
    private ProfileFragment mProfileFragment;


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


        //todo Set temporary setting when initializing activity
        int temporaryMax = 1;
        ArrayList<String> temporaryItems = new ArrayList<>(Arrays.asList("Select a Setting"));
        Bundle args = new Bundle();
        args.putInt("argMaxNumber", temporaryMax);
        args.putStringArrayList("argItems", temporaryItems);
        mHomeFragment.setArguments(args);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == ADD_SETTING_REQUEST && resultCode == RESULT_OK) {
            

            String title = data.getStringExtra(AddSettingActivity.EXTRA_TITLE);
            ArrayList<String> items;

            Setting setting;

            Log.d("AddActivity", "Has items: " + data.hasExtra(AddSettingActivity.EXTRA_ITEMS_LIST));
            if(data.hasExtra(AddSettingActivity.EXTRA_ITEMS_LIST)) {
                items = (ArrayList<String>) data.getStringArrayListExtra(AddSettingActivity.EXTRA_ITEMS_LIST);
                int maxDiceSum = items.size();
                setting = new Setting(title, maxDiceSum, items);
            }
            else {
                int maxDiceSum = data.getIntExtra(AddSettingActivity.EXTRA_MAX_NUMBER, 2);
                setting = new Setting(title, maxDiceSum);
            }

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

            Setting setting = new Setting(title, maxDiceSum);
            setting.setId(id);
            settingsViewModel.update(setting);
    
            Toast.makeText(MainActivity.this, "Setting updated", Toast.LENGTH_SHORT).show();
    
            
        } else {
            Toast.makeText(MainActivity.this, "Setting Not Saved", Toast.LENGTH_SHORT).show();
        }
    }
    //todo




    // Sends Info from DashboardFragment to HomeFragment
    @Override
    public void onInputASent(int maxNumber, ArrayList<String> items, int position, View itemView) {
        Log.d("HomeFragment", "Item List arrived in Main is:" + items.toString());
        Bundle args = new Bundle();
        args.putInt("argMaxNumber", maxNumber);
        args.putStringArrayList("argItems", items);

        // Change color of selected setting
        if(!(selectedItemView == null)) {
            if (selectedItem != position && selectedItemView != itemView){
                selectedItemView.setBackgroundResource(R.drawable.recycler_view_background);
            }
        }

        // send setting info to Home Fragment
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
        startActivityForResult(intent, EDIT_SETTING_REQUEST);
        
    }
}
