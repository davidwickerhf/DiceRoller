package io.github.davidwickerhf.diceroller;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class AddSettingByID extends AppCompatActivity {

    //Views
    private ConstraintLayout addSettingByIdConstraint;
    private EditText idInputEditText;
    private Button checkIDButton;
    private TextView correctOrWrong;
    private androidx.appcompat.widget.Toolbar addSettingByIdToolbar;
    //Variables
    private String title;
    private int maxNum;
    private ArrayList items;
    private boolean hasItems;
    private boolean isInitiated;

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

        addSettingByIdConstraint = findViewById(R.id.add_setting_by_id_constraint);
        idInputEditText = findViewById(R.id.add_setting_by_id_edit_text);
        checkIDButton = findViewById(R.id.add_setting_by_id_check_button);
        correctOrWrong = findViewById(R.id.add_setting_by_id_text_view);

        addSettingByIdConstraint.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

        //todo Methods
        // Check input on ENTER button press
        idInputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String inputID = idInputEditText.getText().toString();
                    initiate(inputID);
                    // Return true (tell system that right key is pressed)
                    return true;
                }
                return false;
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

    private void initiate(String inputID){
        switch (inputID) {
            case "!1EPascal2018!":
                correctOrWrong.setVisibility(View.VISIBLE);
                correctOrWrong.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                correctOrWrong.setText(getString(R.string.add_setting_by_id_correct));
                addSettingByIdConstraint.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_setting_by_id_correct_background));
                Toast.makeText(AddSettingByID.this, getString(R.string.add_setting_by_id_correct_toast_text), Toast.LENGTH_SHORT).show();

                // Hide Soft Keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(idInputEditText.getWindowToken(), 0);

                //Set Variables
                title = "Classe 1E";
                maxNum = 28;
                items = new ArrayList<String>() {{
                    add("Alpe Stefano"); //1
                    add("Bergo Beatrice");//2
                    add("Bonavero Arianna");//3
                    add("Bravi Gianluca");//4
                    add("Brodesco Lorenzio");
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
                break;
            default: // No Setting Showed or Saved
                correctOrWrong.setVisibility(View.VISIBLE);
                correctOrWrong.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                correctOrWrong.setText(getString(R.string.add_setting_by_id_wrong));
                addSettingByIdConstraint.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                Toast.makeText(AddSettingByID.this, getString(R.string.add_setting_by_id_wrong_toast_text), Toast.LENGTH_SHORT).show();
                isInitiated = false;

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
                return super.onOptionsItemSelected(item);
        }
    }
}
