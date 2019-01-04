package io.github.davidwickerhf.diceroller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.davidwickerhf.diceroller.adapters.ItemListAdapter;
import io.github.davidwickerhf.diceroller.itemTouchHelper.SimpleItemTouchHelperCallback;

import android.app.Activity;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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
    private TextView itemsNumberTitleTextView;
    private ImageView addItemEditTextBackground;
    private ImageView itemListBackground;

    //todo Components
    private ItemListAdapter itemListAdapter;
    private RecyclerView itemListRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private View itemListView;

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

        itemsNumberTitleTextView = findViewById(R.id.items_max_num_title_text_view);
        addItemEditTextBackground = findViewById(R.id.item_edit_text_background);
        itemListBackground = findViewById(R.id.item_list_background);

        hasItemList = false; //default Value for adding a setting. If true, It means the setting has Items (strings entered by users, attached to the number)
        isEditing = false; //Default. This is used to edit an item in the list
        maxNumber = 2; // Lowest number should be 2
        seekBarMaxNumber.setProgress(0);
        maxNumberText.setText(String.valueOf(2));
        addSettingConstraint.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

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

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(itemListAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(itemListRecyclerView);


        //todo SET LAYOUT IF IS EDIT SETTING
        if (intent.hasExtra(MainActivity.EXTRA_ID)) {
            setTitle(R.string.edit_setting_toolbar_title);
            editTextTitle.setText(intent.getStringExtra(MainActivity.EXTRA_TITLE));
            editTextTitle.setSelection(editTextTitle.getText().length()); //this moves the cursor to the end of the String

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
                maxNumberTextRepositioned.setVisibility(View.VISIBLE);
                addItemButton.setVisibility(View.VISIBLE);
                itemEditText.setVisibility(View.VISIBLE);
                deleteItemsList.setVisibility(View.VISIBLE);

                itemsNumberTitleTextView.setVisibility(View.VISIBLE);
                itemListBackground.setVisibility(View.VISIBLE);
                addItemEditTextBackground.setVisibility(View.VISIBLE);
                //variables
                items = intent.getStringArrayListExtra(MainActivity.EXTRA_ITEMS_LIST);
                itemListAdapter.setItems(items);
                maxNumberTextRepositioned.setText(String.format("%s", itemListAdapter.getItems().size()));
            }

        } else {
            setTitle(R.string.add_setting_toolbar_title);
        }

        showKeyboard(editTextTitle);

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

        //todo Add and Delete Items Button
        // ADD ITEM LIST
        addItemListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasItemList = true;
                changeStateHasItems(hasItemList);
            }
        });

        //DELETE ITEM LIST
        deleteItemsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemListAdapter.getItems().size() > 0) {
                    AlertDialog dialog = new AlertDialog.Builder(AddSettingActivity.this, R.style.DeleteItemListAlertDialog)
                            .setTitle("Delete Item List?")
                            .setMessage("If you delete the item list of this Setting, you won't be able to recover it.")
                            .setPositiveButton("Yes, delete it!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    hideKeyboard();

                                    hasItemList = false;
                                    changeStateHasItems(hasItemList);
                                    itemListAdapter.setItems(items); // This clears the items in the ItemListAdapter (item list is in fact null)
                                }
                            })
                            .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                } else{
                    //This part of code is copied from the method above!
                    hideKeyboard();

                    hasItemList = false;
                    changeStateHasItems(hasItemList);
                    itemListAdapter.setItems(items); // This clears the items in the ItemListAdapter (item list is in fact null)
                }
            }
        });

        //todo HAS ITEMS Methods:
        // Add an Item on ENTER button press
        itemEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    saveItem(isEditing);
                    // Return true to tell system the right key has been pressed
                    return true;
                }

                return false;
            }
        });

        editTextTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    editTextTitle.clearFocus();
                    return true;
                }
                return false;
            }
        });

        // Add an Item on ADD button press
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem(isEditing);
            }
        });

        // Edit Item when Clicking it
        itemListAdapter.setOnItemClickLister(new ItemListAdapter.OnListItemClickListener() {
            @Override
            public void onItemClick(int position, View itemView) {
                String title = itemListAdapter.getItems().get(position);
                itemEditText.setText(title);
                itemListView = itemView;
                itemPosition = position;
                addItemButton.setImageResource(R.drawable.ic_save_green);
                isEditing = true;
                itemEditText.setSelection(itemEditText.getText().length()); //This moves the cursor to the end of the string

                //Show Soft Keyboard
                showKeyboard(itemEditText);
            }
        });

        itemEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(isEditing) {
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
                                itemEditText.setText("");
                                //itemListView.setBackgroundResource(R.drawable.list_item);
                                addItemButton.setImageResource(R.drawable.ic_add_dark);
                                itemPosition = 0;
                                itemListView = null;
                                isEditing = false;
                            }

                    }
                    hideKeyboard(itemEditText);
                }
            }
        });

        editTextTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    addItemEditTextBackground.setVisibility(View.INVISIBLE);
                    addItemButton.setVisibility(View.INVISIBLE);
                    itemEditText.setVisibility(View.INVISIBLE);

                    ConstraintSet set = new ConstraintSet();

                    set.clone(addSettingConstraint);
                    // The following breaks the connection.
                    set.clear(R.id.item_list_recycler_view, ConstraintSet.BOTTOM);
                    // This is the new connection
                    set.connect(itemListRecyclerView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
                    // Save changes
                    set.applyTo(addSettingConstraint);


                } else{

                    hideKeyboard(editTextTitle);

                    addItemEditTextBackground.setVisibility(View.VISIBLE);
                    addItemButton.setVisibility(View.VISIBLE);
                    itemEditText.setVisibility(View.VISIBLE);

                    ConstraintSet set = new ConstraintSet();

                    set.clone(addSettingConstraint);
                    // The following breaks the connection.
                    set.clear(R.id.item_list_recycler_view, ConstraintSet.BOTTOM);
                    // This is the new connection
                    set.connect(itemListRecyclerView.getId(), ConstraintSet.BOTTOM, R.id.edit_text_item_string, ConstraintSet.TOP, 0);
                    // Save changes
                    set.applyTo(addSettingConstraint);


                }
            }
        });


        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(receiver, new IntentFilter("sendItemListData"));

    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                items = intent.getStringArrayListExtra(MainActivity.EXTRA_ITEMS_LIST);
                maxNumber = items.size();
                itemListAdapter.setItems(items);
                maxNumberTextRepositioned.setText(String.valueOf(maxNumber));
            }
        }
    };




    //todo CHANGE STATE
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
            maxNumberTextRepositioned.setVisibility(View.VISIBLE);
            addItemButton.setVisibility(View.VISIBLE);
            itemEditText.setVisibility(View.VISIBLE);
            deleteItemsList.setVisibility(View.VISIBLE);
            itemsNumberTitleTextView.setVisibility(View.VISIBLE);
            itemListBackground.setVisibility(View.VISIBLE);
            addItemEditTextBackground.setVisibility(View.VISIBLE);
        } else {
            //Show Items List Views
            itemListRecyclerView.setVisibility(View.INVISIBLE);
            seekBarMaxNumber.setVisibility(View.VISIBLE);
            maxNumberText.setVisibility(View.VISIBLE);
            addItemListButton.setVisibility(View.VISIBLE);
            //Hide No Item List Views
            maxNumberTextRepositioned.setVisibility(View.INVISIBLE);
            addItemButton.setVisibility(View.INVISIBLE);
            itemEditText.setVisibility(View.INVISIBLE);
            deleteItemsList.setVisibility(View.INVISIBLE);
            itemsNumberTitleTextView.setVisibility(View.INVISIBLE);
            itemListBackground.setVisibility(View.INVISIBLE);
            addItemEditTextBackground.setVisibility(View.INVISIBLE);
            //Transition Max Num from Number of Items to SeekBar
            maxNumber = itemListAdapter.getItems().size();
            items.clear();
            itemListAdapter.setItems(items);
            if (maxNumber > 1) {
                maxNumberText.setText(String.valueOf(maxNumber));
                seekBarMaxNumber.setProgress(maxNumber - 2);
            } else {
                maxNumberText.setText("2");
                seekBarMaxNumber.setProgress(0);
            }
        }
    }

    private void saveItem(boolean isEditing){
        if (!isEditing) {
            String itemString = itemEditText.getText().toString();
            if (itemString.trim().isEmpty()) {
                Toast.makeText(AddSettingActivity.this, getString(R.string.add_setting_toast_no_title), Toast.LENGTH_SHORT).show();
            } else if (itemString.length() > 30) {
                Toast.makeText(AddSettingActivity.this, getString(R.string.add_setting_toast_item_title_is_too_long), Toast.LENGTH_SHORT).show();
            } else {
                items.add(itemEditText.getText().toString());
                itemListAdapter.setItems(items);
                maxNumberTextRepositioned.setText(String.format("%s", itemListAdapter.getItems().size()));
                itemEditText.setText(""); // resets edit text input every time an item is added

                //todo Change RecyclerView Position when adding and removing item !
            }
        } else {
            String itemString = itemEditText.getText().toString();
            if (itemString.trim().isEmpty()) {
                Toast.makeText(AddSettingActivity.this, getString(R.string.add_setting_toast_no_title), Toast.LENGTH_SHORT).show();
            } else if (itemString.length() > 30) {
                Toast.makeText(AddSettingActivity.this, getString(R.string.add_setting_toast_item_title_is_too_long), Toast.LENGTH_SHORT).show();
            } else {
                // Save Item String
                items.set(itemPosition, itemString);
                itemListAdapter.setItems(items);

                // Reset View and Variables
                itemEditText.setText("");
                //itemListView.setBackgroundResource(R.drawable.list_item);
                addItemButton.setImageResource(R.drawable.ic_add_dark);
                itemPosition = 0;
                itemListView = null;
                this.isEditing = false;

                // Hide Soft Keyboard
                hideKeyboard();
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

            if (itemListAdapter.getItems().size() < 2) {
                Toast.makeText(this, "Setting should have at least 2 items", Toast.LENGTH_SHORT).show();
                return;
            }

            items = itemListAdapter.getItems();
            maxNumber = itemListAdapter.getItems().size();
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

    public void showKeyboard(View view){
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void hideKeyboard(View view /*edit text*/) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        hideKeyboard();
        super.onDestroy();
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
