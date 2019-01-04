package io.github.davidwickerhf.diceroller;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.davidwickerhf.diceroller.adapters.ShowItemListAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddSettingByID extends AppCompatActivity {

    //Views
    private ConstraintLayout addSettingByIdConstraint;
    private EditText idInputEditText;
    private Button checkIDButton;
    private TextView correctOrWrong;
    private androidx.appcompat.widget.Toolbar addSettingByIdToolbar;
    //Hidden Views
    private TextView settingTitleTextView;
    private TextView maxNumTextView;
    private TextView maxNumDescription;
    private ImageView maxNumBackground;
    private RecyclerView showItemListRecyclerView;
    //Variables
    private String title;
    private int maxNum;
    private ArrayList<String> items;
    private boolean hasItems;
    private boolean isInitiated;
    //Components
    ShowItemListAdapter showItemListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_setting_by_id);


        //todo Initialize Variables and Views
        // Toolbar
        addSettingByIdToolbar = findViewById(R.id.add_setting_by_id_toolbar);
        setSupportActionBar(addSettingByIdToolbar);
        setTitle(R.string.add_setting_by_id_activity_title);

        // Get a support ActionBar corresponding to this toolbar
        final ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_close);

        //Initialize views
        addSettingByIdConstraint = findViewById(R.id.add_setting_by_id_constraint);
        idInputEditText = findViewById(R.id.add_setting_by_id_edit_text);
        checkIDButton = findViewById(R.id.add_setting_by_id_check_button);
        correctOrWrong = findViewById(R.id.add_setting_by_id_text_view);

        settingTitleTextView = findViewById(R.id.setting_title_text_view);
        maxNumTextView = findViewById(R.id.max_num_text_view);
        maxNumDescription = findViewById(R.id.max_num_description);
        maxNumBackground = findViewById(R.id.max_num_background);
        addSettingByIdConstraint.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

        // Recycler View
        showItemListRecyclerView = findViewById(R.id.show_item_list_recycler_view);
        showItemListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        showItemListRecyclerView.setHasFixedSize(true);
        // Set Adapter
        showItemListAdapter = new ShowItemListAdapter();
        showItemListRecyclerView.setAdapter(showItemListAdapter);
        // Show Keyboard
        showKeyboard(idInputEditText);

        //todo Methods
        // Check input on ENTER button press
        idInputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String inputID = idInputEditText.getText().toString();
                    if (initiate(inputID)) {
                        hideKeyboard(idInputEditText);
                    }
                    // Return true (tell system that right key is pressed)
                    return true;
                }
                return false;
            }
        });

        idInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(idInputEditText);
                    initiate(idInputEditText.getText().toString());
                }
            }
        });
        // Check input on button press
        checkIDButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputID = idInputEditText.getText().toString();
                initiate(inputID);
            }
        });


    }

    private boolean initiate(String inputID) {
        switch (inputID) {
            case "!1EPascal2018!":
                changeViewsVisibility(true);
                Toast.makeText(AddSettingByID.this, getString(R.string.add_setting_by_id_correct_toast_text), Toast.LENGTH_SHORT).show();
                //Set Variables
                title = "Classe 1E";
                maxNum = 28;
                items = new ArrayList<String>() {{
                    add("Alpe Stefano"); //1
                    add("Bergo Beatrice");//2
                    add("Bonavero Arianna");//3
                    add("Bravi Gianluca");//4
                    add("Brodesco Lorentio");
                    add("Cerato Giulio");
                    add("Coluccia Giorgia");
                    add("Danoune Nizar");
                    add("Ferrante Andrea");
                    add("Fiore Federico");
                    add("Izzo Samuele");
                    add("Maio Elisa");//12
                    add("Mozzone Marco");
                    add("Oliva Matteo");
                    add("Perucca Alberto");
                    add("Petruzzi Alessandro");
                    add("Piani Arthur");
                    add("Pirulli Tommaso");
                    add("Princivalle Aurora");//
                    add("Rabihi Jasmine");
                    add("Riggio Sergio");
                    add("Sammaruca Alessio");
                    add("Sanfilippo Martina");
                    add("Semeraro Lorenzo");
                    add("Seren Rosso Martina");
                    add("Veca Alessandro");
                    add("Venuti Filippo");
                    add("Wicker David");
                }};
                hasItems = true;
                isInitiated = true;
                setVariablesToViews(maxNum, title, items, isInitiated);
                return isInitiated;


            default: // No Setting Showed or Saved
                changeViewsVisibility(false);
                Toast.makeText(AddSettingByID.this, getString(R.string.add_setting_by_id_wrong_toast_text), Toast.LENGTH_SHORT).show();
                isInitiated = false;
                return isInitiated;
        }
    }

    private void saveSetting() {

        Intent data = new Intent();
        data.putExtra(MainActivity.EXTRA_TITLE, title);
        data.putExtra(MainActivity.EXTRA_MAX_NUMBER, maxNum);
        data.putExtra(MainActivity.EXTRA_ITEMS_LIST, items);
        data.putExtra(MainActivity.EXTRA_HAS_ITEMS, hasItems);

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

    private void changeViewsVisibility(boolean show) {
        if (show) {
            correctOrWrong.setVisibility(View.VISIBLE);
            correctOrWrong.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
            correctOrWrong.setText(getString(R.string.add_setting_by_id_correct));
            addSettingByIdConstraint.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_setting_by_id_correct_background));

            settingTitleTextView.setVisibility(View.VISIBLE);
            maxNumTextView.setVisibility(View.VISIBLE);
            maxNumDescription.setVisibility(View.VISIBLE);
            maxNumBackground.setVisibility(View.VISIBLE);
            showItemListRecyclerView.setVisibility(View.VISIBLE);

        } else {
            correctOrWrong.setVisibility(View.VISIBLE);
            correctOrWrong.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
            correctOrWrong.setText(getString(R.string.add_setting_by_id_wrong));
            addSettingByIdConstraint.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

            settingTitleTextView.setVisibility(View.INVISIBLE);
            maxNumTextView.setVisibility(View.INVISIBLE);
            maxNumDescription.setVisibility(View.INVISIBLE);
            maxNumBackground.setVisibility(View.INVISIBLE);
            showItemListRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void setVariablesToViews(int maxNum, String title, ArrayList<String> items, boolean isInitiated) {
        if (isInitiated) {
            maxNumTextView.setText(String.valueOf(maxNum));
            settingTitleTextView.setText(title);
            showItemListAdapter.setItems(items);
        }
    }

    private void setVariablesToViews(int maxNum, String title, boolean isInitiated) {
        if (isInitiated) {
            maxNumTextView.setText(String.valueOf(maxNum));
            settingTitleTextView.setText(title);
        }
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
                if (isInitiated)
                    saveSetting();
                else
                    Toast.makeText(this, getString(R.string.add_setting_by_id_save_setting_no_id_toast), Toast.LENGTH_SHORT).show();
                return true;
            default:
                hideKeyboard();
                return super.onOptionsItemSelected(item);
        }
    }
}
