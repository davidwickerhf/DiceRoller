package io.github.davidwickerhf.diceroller.settingDatabase;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.github.davidwickerhf.diceroller.Setting;

public class Repository {
    
    private SettingDao settingDao;
    private LiveData<List<Setting>> allSettings;
    
    public Repository(Application application) {
        SettingDatabase database = SettingDatabase.getInstance(application);
        settingDao = database.settingDao();
        allSettings = settingDao.getAllSettings();
    }
    
    
    public void insert(Setting setting) {
        new InsertSettingAsyncTask(settingDao).execute(setting);
    }
    
    public void update(Setting setting) {
        new UpdateSettingAsyncTask(settingDao).execute(setting);
    }
    
    public void delete(Setting setting) {
        new DeleteSettingAsyncTask(settingDao).execute(setting);
    }

    public LiveData<List<Setting>> getAllSettings() {
        return allSettings;
    }
    
    
    //Todo Add AsynkTasks and methods for insert, update, delete:
    
    //todo AsyncTask Insert:
    public static class InsertSettingAsyncTask extends AsyncTask<Setting, Void, Void>{
        private SettingDao settingDao;
    
        private InsertSettingAsyncTask(SettingDao settingDao){
            this.settingDao = settingDao;
        }
        
        @Override
        protected Void doInBackground(Setting... settings) {
            settingDao.insert(settings[0]);
            return null;
        }
    }
    
    //todo AsyncTask Update:
    public static class UpdateSettingAsyncTask extends AsyncTask<Setting, Void, Void>{
        private SettingDao settingDao;
        
        private UpdateSettingAsyncTask(SettingDao settingDao){
            this.settingDao = settingDao;
        }
        
        @Override
        protected Void doInBackground(Setting... settings) {
            settingDao.update(settings[0]);
            return null;
        }
    }
    
    //todo AsyncTask Delete:
    public static class DeleteSettingAsyncTask extends AsyncTask<Setting, Void, Void>{
        private SettingDao settingDao;
        
        private DeleteSettingAsyncTask(SettingDao settingDao){
            this.settingDao = settingDao;
        }
        
        @Override
        protected Void doInBackground(Setting... settings) {
            settingDao.delete(settings[0]);
            return null;
        }
    }
}
