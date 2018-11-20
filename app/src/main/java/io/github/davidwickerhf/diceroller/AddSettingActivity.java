package io.github.davidwickerhf.diceroller;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

public class AddSettingActivity extends AppCompatActivity {

    //todo Variables
    public static final String EXTRA_ID =
            "io.github.davidwickerhf.diceroller.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "io.github.davidwickerhf.diceroller.EXTRA_TITLE";
    public static final String EXTRA_MAX_NUMBER =
            "io.github.davidwickerhf.diceroller.EXTRA_MAX_NUMBER";
    public static final String EXTRA_ITEMS_LIST =
            "io.github.davidwickerhf.diceroller.EXTRA_MAX_NUMBER";
    private int seekbarProgress;
    int maxNumber;
    ArrayList<String> items = new ArrayList<>();
    boolean hasItemList;

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

        hasItemList = false;

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

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle(R.string.edit_setting_toolbar_title);
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));

            seekbarProgress = intent.getIntExtra(EXTRA_MAX_NUMBER, 2);
            seekBarMaxNumber.setProgress(seekbarProgress);
            maxNumberText.setText(String.valueOf(seekbarProgress));
            maxNumber = seekbarProgress;


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
        //todo HAS ITEMS!!!!
        addItemListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasItemList = true;
                // Hide Views
                maxNumber = 2;
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
            }
        });

        //TODO HAS NO ITEMS!!!!
        deleteItemsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasItemList = false;
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
                    maxNumberText.setText(maxNumber);
                    seekBarMaxNumber.setProgress(maxNumber + 2);
                } else {
                    maxNumberText.setText(0);
                    seekBarMaxNumber.setProgress(2);
                }
            }
        });

        //This would be null
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemString = itemEditText.getText().toString();
                if (itemString.trim().isEmpty()) {
                    Toast.makeText(AddSettingActivity.this, getString(R.string.add_setting_toast_no_title), Toast.LENGTH_SHORT).show();
                } else if (itemString.length() > 10) {
                    Toast.makeText(AddSettingActivity.this, getString(R.string.add_setting_toast_item_title_is_too_long), Toast.LENGTH_SHORT).show();
                } else {
                    items.add(itemEditText.getText().toString());
                    itemListAdapter.setItems(items);
                    maxNumberTextRepositioned.setText(Integer.toString(items.size()));
                }
            }
        });


    }

    private void saveSetting() {
        String title = editTextTitle.getText().toString();
        if (hasItemList)
            maxNumber = itemListAdapter.items.size();
        //todo Added a + 1 to the priority to leave the first priority always empty, so it can be used for the selected setting
        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Please insert title", Toast.LENGTH_SHORT).show();
            return;
        }
        if (title.length() > 15) {
            Toast.makeText(this, "Title has to be shorter", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        if (hasItemList) {
            maxNumber = items.size();
            data.putExtra(EXTRA_MAX_NUMBER, maxNumber);
            data.putExtra(EXTRA_ITEMS_LIST, items);
        } else {
            data.putExtra(EXTRA_MAX_NUMBER, maxNumber);
        }
        int id = getIntent().getIntExtra(EXTRA_ID, -1);

        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

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
                saveSetting();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
