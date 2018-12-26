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
    private boolean settingSelected;

    // FINALS Variables
    public static final String EXTRA_ID =
            "EXTRA_ID";
    public static final String EXTRA_TITLE =
            "EXTRA_TITLE";
    public static final String EXTRA_MAX_NUMBER =
            "EXTRA_MAX_NUMBER";
    public static final String EXTRA_ITEMS_LIST =
            "EXTRA_ITEMS_LIST";
    public static final String EXTRA_HAS_ITEMS =
            "EXTRA_HAS_ITEMS";
    public static final String EXTRA_POSITION =
            "EXTRA_HAS_POSITION";



    //todo Views
    private View selectedItemView;
    private BottomNavigationView BottomNavigationView;

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
        BottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        //todo Initialize fragments
        mHomeFragment = new HomeFragment();
        mDashboardFragment = new DashboardFragment();
        mProfileFragment = new ProfileFragment();

//        //todo Display DashboardFragment:
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mDashboardFragment).commit();
        changeSelectedMenuItem(1);
        settingSelected = false;

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            if(settingSelected) {
                                selectedFragment = mHomeFragment;
                            }else{
                                Toast.makeText(MainActivity.this, "Select a Setting first", Toast.LENGTH_SHORT).show();
                                selectedFragment = mDashboardFragment;
                                changeSelectedMenuItem(1);
                            }
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


            String title = data.getStringExtra(MainActivity.EXTRA_TITLE);
            ArrayList<String> items;
            Setting setting;
            int maxDiceSum;

            boolean hasItems = data.getBooleanExtra(MainActivity.EXTRA_HAS_ITEMS, false);
            maxDiceSum = data.getIntExtra(MainActivity.EXTRA_MAX_NUMBER, 2);

            if(hasItems) {
                items = (ArrayList<String>) data.getStringArrayListExtra(MainActivity.EXTRA_ITEMS_LIST);
                setting = new Setting(title, maxDiceSum, items, hasItems);
            }
            else {
                setting = new Setting(title, maxDiceSum, hasItems);
            }

            settingsViewModel.insert(setting);

            Toast.makeText(MainActivity.this, "Setting saved", Toast.LENGTH_SHORT).show();





        } else if (requestCode == EDIT_SETTING_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(MainActivity.EXTRA_ID, -1);
            boolean hasItems = data.getBooleanExtra(MainActivity.EXTRA_HAS_ITEMS, false);

            if(id == -1){
                Toast.makeText(MainActivity.this, "Setting Can't be Updated", Toast.LENGTH_SHORT).show();
                return;
            }

            //Needed Variables
            String title = data.getStringExtra(MainActivity.EXTRA_TITLE);
            int maxDiceSum = data.getIntExtra(MainActivity.EXTRA_MAX_NUMBER, 2);
            ArrayList<String> items = new ArrayList<>();
            Setting setting;

            // Generate new Setting
            if (hasItems){
                items = data.getStringArrayListExtra(MainActivity.EXTRA_ITEMS_LIST);
                setting = new Setting(title, maxDiceSum, items, true);
            }
            else{
                setting = new Setting(title, maxDiceSum, false);
            }

            // Add Generated Setting
            setting.setId(id);
            settingsViewModel.update(setting);
            Toast.makeText(MainActivity.this, "Setting updated", Toast.LENGTH_SHORT).show();

            // Update Selected Setting
            Bundle updateSelectedSetting = mHomeFragment.getArguments();
            if(updateSelectedSetting != null) {
                int selectedSettingPosition = /*Argument set in onInputASent*/updateSelectedSetting.getInt(MainActivity.EXTRA_POSITION);
                int editedSettingPosition = data.getIntExtra(MainActivity.EXTRA_POSITION, -1);

                if (selectedSettingPosition == editedSettingPosition) {
                    Bundle args = new Bundle();
                    args.putInt(MainActivity.EXTRA_POSITION, selectedSettingPosition);
                    args.putInt(MainActivity.EXTRA_MAX_NUMBER, maxDiceSum);
                    args.putString(MainActivity.EXTRA_TITLE, title);
                    args.putStringArrayList(MainActivity.EXTRA_ITEMS_LIST, items);
                    mHomeFragment.setArguments(args);
                }
            }




         //todo  ADD WAY TO ADD A SETTING BY ID


        }else {
            Toast.makeText(MainActivity.this, "Setting Not Saved", Toast.LENGTH_SHORT).show();
        }
    }
    //todo

    public void changeSelectedMenuItem(int position){
        BottomNavigationView.getMenu().getItem(position).setChecked(true);
    }

    //todo ON CLICKING SETTING IN DASHBOARD
    //todo  Sends Info from Setting Adapter to DashboardFragment to Main to HomeFragment
    @Override
    public void onInputASent(int maxNumber, ArrayList<String> items, int position, String title, View itemView) {
        Bundle args = new Bundle();
        args.putInt(MainActivity.EXTRA_POSITION, position);
        args.putInt(MainActivity.EXTRA_MAX_NUMBER, maxNumber);
        args.putString(MainActivity.EXTRA_TITLE, title);
        args.putStringArrayList(MainActivity.EXTRA_ITEMS_LIST, items);
        settingSelected = true;
        // Change color of selected setting back to normal
        if(!(selectedItemView == null)) {
            if (selectedItem != position && selectedItemView != itemView){
                selectedItemView.setBackgroundResource(R.drawable.recycler_view_background);
            }
        }

        //todo  send setting info to Home Fragment -------------------------------------------------------------------------------------------------------------------------------------------------
        mHomeFragment.setArguments(args);

        selectedItemView = itemView;
        selectedItem = position;

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mHomeFragment).commit();
        changeSelectedMenuItem(0);
    }

    //todo Sends Setting info to AddSettingActivity
    @Override
    public void onEditInputASent(int position) {

        Intent intent = new Intent(MainActivity.this, AddSettingActivity.class);
        intent.putExtra(MainActivity.EXTRA_ID, adapter.getSettingAt(position).getId());
        intent.putExtra(MainActivity.EXTRA_TITLE, adapter.getSettingAt(position).getTitle());
        intent.putExtra(MainActivity.EXTRA_MAX_NUMBER, adapter.getSettingAt(position).getMaxDiceSum());
        intent.putExtra(MainActivity.EXTRA_ITEMS_LIST, adapter.getSettingAt(position).getItems());
        intent.putExtra(MainActivity.EXTRA_HAS_ITEMS, adapter.getSettingAt(position).hasItemList());
        intent.putExtra(MainActivity.EXTRA_POSITION, position);
        startActivityForResult(intent, EDIT_SETTING_REQUEST);

    }
}
