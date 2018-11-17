package io.github.davidwickerhf.diceroller;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Objects;

public class AddSettingActivity extends AppCompatActivity {

    //todo Variables
    public static final String EXTRA_ID =
            "io.github.davidwickerhf.diceroller.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "io.github.davidwickerhf.diceroller.EXTRA_TITLE";
    public static final String EXTRA_MAX_NUMBER =
            "io.github.davidwickerhf.diceroller.EXTRA_MAX_NUMBER";
    private int seekbarProgress;
    int maxNumber;

    //todo Views
    private TextView maxNumberText;
    private EditText editTextTitle;
    private SeekBar seekBarMaxNumber;
    androidx.appcompat.widget.Toolbar addSettingsToolbar;
    private Button addItemsButton;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_setting);

        Intent intent = getIntent();
    
        editTextTitle = findViewById(R.id.edit_text_title);
        seekBarMaxNumber = findViewById(R.id.seek_bar);
        maxNumberText = findViewById(R.id.max_number_text);
        addItemsButton = findViewById(R.id.add_items_btn);

        //todo Toolbar
        addSettingsToolbar = findViewById(R.id.add_setting_toolbar);
        setSupportActionBar(addSettingsToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_close);

        if (intent.hasExtra(EXTRA_ID)){
            setTitle(R.string.edit_setting_toolbar_title);
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            
            seekbarProgress = intent.getIntExtra(EXTRA_MAX_NUMBER, 2);
            seekBarMaxNumber.setProgress(seekbarProgress);
            maxNumberText.setText(String.valueOf(seekbarProgress));
            maxNumber = seekbarProgress;
            
        }
        else {
            setTitle(R.string.add_setting_toolbar_title);
        }
        
        
        
        seekBarMaxNumber.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxNumberText.setText(String.valueOf(progress));
                maxNumber = progress;
            }
    
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        //todo Add Items Button
        addItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_add_setting_with_items);
            }
        });
    }


    private void saveNote() {
        String title = editTextTitle.getText().toString();
        //todo Added a + 1 to the priority to leave the first priority always empty, so it can be used for the selected setting
        if(title.trim().isEmpty()){
            Toast.makeText(this, "Please insert title", Toast.LENGTH_SHORT).show();
            return;
        }
        if(title.length() > 15){
            Toast.makeText(this, "Title has to be shorter", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_MAX_NUMBER, maxNumber);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1){
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
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
