package io.github.davidwickerhf.diceroller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class AddSettingActivity extends AppCompatActivity {

    //todo Variables
    private int seekbarProgress;
    int maxNumber;
    ArrayList<String> items = new ArrayList<>();
    boolean hasItemList;
    // for editing an item
    boolean isEditing;
    int itemPosition;

    //todo Views
    private ConstraintLayout addSettingConstraint;
    private TextView maxNumberText;
    private EditText editTextTitle;
    private SeekBar seekBarMaxNumber;
    androidx.appcompat.widget.Toolbar addSettingsToolbar;
    private Button addItemListButton;
    private TextView maxNumberTextRepositioned;
    private ImageButton addItemButton;
    private EditText itemEditText;
    private ImageButton deleteItemsList;
    private View itemListView;

    //todo Components
    ItemListAdapter itemListAdapter;
    RecyclerView itemListRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_setting);

        final Intent intent = getIntent();

        //todo Views Initialized
        addSettingConstraint = findViewById(R.id.add_setting_constraint_layout);
        editTextTitle = findViewById(R.id.edit_text_title);
        seekBarMaxNumber = findViewById(R.id.seek_bar);
        seekBarMaxNumber.setMax(34 /*is 36 - 2, to correctly set minimum value to 2*/);
        maxNumberText = findViewById(R.id.max_number_text);
        addItemListButton = findViewById(R.id.add_items_btn);
        // With Item List
        maxNumberTextRepositioned = findViewById(R.id.max_number_text_with_items);
        addItemButton = findViewById(R.id.add_item);
        itemEditText = findViewById(R.id.edit_text_item_string);
        deleteItemsList = findViewById(R.id.delete_item_list);

        hasItemList = false; //default Value for adding a setting. If true, It means the setting has Items (strings entered by users, attached to the number)
        isEditing = false; //Default. This is used to edit an item in the list
        maxNumber = 2; // Lowest number should be 2
        seekBarMaxNumber.setProgress(0);
        maxNumberText.setText(String.valueOf(2));

        //todo Toolbar
        addSettingsToolbar = findViewById(R.id.add_setting_toolbar);
        setSupportActionBar(addSettingsToolbar);

        //todo  Get a support ActionBar corresponding to this toolbar
        final ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_close);

        //todo RecyclerView
        itemListRecyclerView = findViewById(R.id.item_list_recycler_view);
        itemListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        itemListRecyclerView.setHasFixedSize(true);

        //todo Adapter
        itemListAdapter = new ItemListAdapter();
        itemListRecyclerView.setAdapter(itemListAdapter);


        //todo SET LAYOUT IF IS EDIT SETTING
        if (intent.hasExtra(MainActivity.EXTRA_ID)) {
            setTitle(R.string.edit_setting_toolbar_title);
            editTextTitle.setText(intent.getStringExtra(MainActivity.EXTRA_TITLE));

            seekbarProgress = intent.getIntExtra(MainActivity.EXTRA_MAX_NUMBER, 2);
            seekBarMaxNumber.setProgress(seekbarProgress);
            maxNumberText.setText(String.valueOf(seekbarProgress));
            maxNumber = seekbarProgress;

            hasItemList = intent.getBooleanExtra(MainActivity.EXTRA_HAS_ITEMS, false);
            if (hasItemList) {
                //hide views
                seekBarMaxNumber.setVisibility(View.INVISIBLE);
                maxNumberText.setVisibility(View.INVISIBLE);
                addItemListButton.setVisibility(View.INVISIBLE);
                // Show Views for Item List
                itemListRecyclerView.setVisibility(View.VISIBLE);
                addSettingConstraint.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_item_list_background));
                maxNumberTextRepositioned.setVisibility(View.VISIBLE);
                addItemButton.setVisibility(View.VISIBLE);
                itemEditText.setVisibility(View.VISIBLE);
                deleteItemsList.setVisibility(View.VISIBLE);
                //variables
                items = intent.getStringArrayListExtra(MainActivity.EXTRA_ITEMS_LIST);
                itemListAdapter.setItems(items);
                maxNumberTextRepositioned.setText(String.format("%s", items.size()));
            }

        } else {
            setTitle(R.string.add_setting_toolbar_title);
        }

        seekBarMaxNumber.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxNumberText.setText(String.valueOf(progress + 2));
                maxNumber = progress + 2;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //todo Add Items Button
        //todo ADD ITEM LIST
        addItemListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasItemList = true;
                changeStateHasItems(hasItemList);
            }
        });

        //TODO DELETE ITEM LIST
        deleteItemsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasItemList = false;
                changeStateHasItems(hasItemList);
                itemListAdapter.setItems(items); // This clears the items in the ItemListAdapter (item list is in fact null)
            }
        });

        //todo ADD ITEM
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isEditing) {
                    String itemString = itemEditText.getText().toString();
                    if (itemString.trim().isEmpty()) {
                        Toast.makeText(AddSettingActivity.this, getString(R.string.add_setting_toast_no_title), Toast.LENGTH_SHORT).show();
                    } else if (itemString.length() > 23) {
                        Toast.makeText(AddSettingActivity.this, getString(R.string.add_setting_toast_item_title_is_too_long), Toast.LENGTH_SHORT).show();
                    } else {
                        items.add(itemEditText.getText().toString());
                        itemListAdapter.setItems(items);
                        maxNumberTextRepositioned.setText(String.format("%s", items.size()));
                        itemEditText.setText(""); // resets edit text input every time an item is added
                    }
                } else {
                    String itemString = itemEditText.getText().toString();
                    if (itemString.trim().isEmpty()) {
                        Toast.makeText(AddSettingActivity.this, getString(R.string.add_setting_toast_no_title), Toast.LENGTH_SHORT).show();
                    } else if (itemString.length() > 23) {
                        Toast.makeText(AddSettingActivity.this, getString(R.string.add_setting_toast_item_title_is_too_long), Toast.LENGTH_SHORT).show();
                    } else {
                        // Save Item String
                        items.set(itemPosition, itemString);
                        itemListAdapter.setItems(items);

                        // Reset View and Variables
                        itemEditText.setText(""); // resets edit text input every time an item is added
                        //itemListView.setBackgroundResource(R.drawable.list_item);
                        addItemButton.setImageResource(R.drawable.ic_add_dark);
                        itemPosition = 0;
                        itemListView = null;
                        isEditing = false;
                    }

                }

            }
        });

        //todo Edit Item when Clicking it
        itemListAdapter.setOnItemClickLister(new ItemListAdapter.OnListItemClickListener() {
            @Override
            public void onItemClick(int position, View itemView) {
                String title = items.get(position);
                itemEditText.setText(title);
                itemListView = itemView;
                itemPosition = position;
                addItemButton.setImageResource(R.drawable.ic_save);
                isEditing = true;
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
                items.remove(viewHolder.getAdapterPosition());
                Toast.makeText(AddSettingActivity.this, "Item Removed", Toast.LENGTH_SHORT).show();
                maxNumberTextRepositioned.setText(String.valueOf(items.size()));
                itemListAdapter.setItems(items);
            }
        }).attachToRecyclerView(itemListRecyclerView);


    }

    private void changeStateHasItems(boolean hasItemList) {
        if (hasItemList) {
            // Hide Views
            maxNumber = 0;
            maxNumberTextRepositioned.setText(String.valueOf(0));
            seekBarMaxNumber.setVisibility(View.INVISIBLE);
            maxNumberText.setVisibility(View.INVISIBLE);
            addItemListButton.setVisibility(View.INVISIBLE);
            // Show Views for Item List
            itemListRecyclerView.setVisibility(View.VISIBLE);
            addSettingConstraint.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_item_list_background));
            maxNumberTextRepositioned.setVisibility(View.VISIBLE);
            addItemButton.setVisibility(View.VISIBLE);
            itemEditText.setVisibility(View.VISIBLE);
            deleteItemsList.setVisibility(View.VISIBLE);
        } else {
            //Show Items List Views
            itemListRecyclerView.setVisibility(View.INVISIBLE);
            addSettingConstraint.setBackgroundColor(getResources().getColor(R.color.white));
            seekBarMaxNumber.setVisibility(View.VISIBLE);
            maxNumberText.setVisibility(View.VISIBLE);
            addItemListButton.setVisibility(View.VISIBLE);
            //Hide No Item List Views
            maxNumberTextRepositioned.setVisibility(View.INVISIBLE);
            addItemButton.setVisibility(View.INVISIBLE);
            itemEditText.setVisibility(View.INVISIBLE);
            deleteItemsList.setVisibility(View.INVISIBLE);
            //Transition Max Num from Number of Items to SeekBar
            maxNumber = items.size();
            items.clear();
            if (maxNumber > 1) {
                maxNumberText.setText(String.valueOf(maxNumber));
                seekBarMaxNumber.setProgress(maxNumber - 2);
            } else {
                maxNumberText.setText("2");
                seekBarMaxNumber.setProgress(0);
            }
        }
    }

    private void saveSetting(boolean hasItemList) {
        String title = editTextTitle.getText().toString();

        //todo Check Title isn't empty
        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Please insert title", Toast.LENGTH_SHORT).show();
            return;
        }
        if (title.length() > 15) {
            Toast.makeText(this, "Title has to be shorter", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(MainActivity.EXTRA_TITLE, title);


        //todo Insert Extras
        if (hasItemList) {

            if (items.size() < 2) {
                Toast.makeText(this, "Setting should have at least 2 items", Toast.LENGTH_SHORT).show();
                return;
            }

            maxNumber = items.size();
            data.putExtra(MainActivity.EXTRA_MAX_NUMBER, maxNumber);
            data.putExtra(MainActivity.EXTRA_ITEMS_LIST, items);
            data.putExtra(MainActivity.EXTRA_HAS_ITEMS, true);
        } else {
            Log.d("AddActivity", "Has Item List is false, max number is = " + maxNumber);
            data.putExtra(MainActivity.EXTRA_MAX_NUMBER, maxNumber);
        }
        int id = getIntent().getIntExtra(MainActivity.EXTRA_ID, -1);

        if (id != -1) {
            data.putExtra(MainActivity.EXTRA_ID, id);
        }

        // Pass Position (From Main Edit Interface to onResult in Main)
        int position = getIntent().getIntExtra(MainActivity.EXTRA_POSITION, -1);
        data.putExtra(MainActivity.EXTRA_POSITION, position);

        // send results
        setResult(RESULT_OK, data);
        finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_setting:
                saveSetting(hasItemList);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
