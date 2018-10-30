package io.github.davidwickerhf.diceroller;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class SettingsViewModel extends AndroidViewModel {
    
    private Repository repository;
    private LiveData<List<Setting>> allSettings;
    
    public SettingsViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allSettings = repository.getAllSettings();
        
    }
    
    public void insert(Setting setting) { repository.insert(setting); }
    
    public void update(Setting setting) { repository.update(setting); }
    
    public void delete(Setting setting) { repository.delete(setting); }
    
    public LiveData<List<Setting>> getAllSettings() {
        return allSettings;
    }
    
}
