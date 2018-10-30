package io.github.davidwickerhf.diceroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class AddSettingActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "io.github.davidwickerhf.diceroller.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "io.github.davidwickerhf.diceroller.EXTRA_TITLE";
    public static final String EXTRA_MAX_NUMBER =
            "io.github.davidwickerhf.diceroller.EXTRA_MAX_NUMBER";
    public static final String EXTRA_PRIORITY =
            "io.github.davidwickerhf.diceroller.EXTRA_PRIORITY";
    
    
    
    private TextView maxNumberText;
    private EditText editTextTitle;
    private SeekBar seekBarMaxNumber;
    private NumberPicker numberPickerpriority;
    
    private int seekbarProgress;
    private String seekbarText;
    
    int maxNumber;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_setting);
    
        Intent intent = getIntent();
    
        editTextTitle = findViewById(R.id.edit_text_title);
        seekBarMaxNumber = findViewById(R.id.seek_bar);
        numberPickerpriority = findViewById(R.id.priority_picker);
        maxNumberText = findViewById(R.id.max_number_text);
    
        numberPickerpriority.setMinValue(1);
        numberPickerpriority.setMaxValue(5);
    
    
        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Setting");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            
            seekbarProgress = intent.getIntExtra(EXTRA_MAX_NUMBER, 2);
            seekBarMaxNumber.setProgress(seekbarProgress);
            maxNumberText.setText(String.valueOf(seekbarProgress));
            maxNumber = seekbarProgress;
            
            numberPickerpriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        }
        else {
            setTitle("Add Setting");
        }
        
        
        
        seekBarMaxNumber.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxNumberText.setText(String.valueOf(progress));
                maxNumber = progress;
            }
    
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
        
            }
    
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
    
    
    
        });
        
    }
    
    private void saveNote() {
        String title = editTextTitle.getText().toString();
        //todo Added a + 1 to the priority to leave the first priority always empty, so it can be used for the selected setting
        int priority = (numberPickerpriority.getValue());
        
        if(title.trim().isEmpty()){
            Toast.makeText(this, "Please insert title", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_MAX_NUMBER, maxNumber);
        data.putExtra(EXTRA_PRIORITY, priority);
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
