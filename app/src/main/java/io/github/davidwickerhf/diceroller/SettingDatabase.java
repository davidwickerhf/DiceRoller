package io.github.davidwickerhf.diceroller;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Telephony;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = Setting.class, version = 10)

@TypeConverters(ItemConverters.class)
public abstract class SettingDatabase extends RoomDatabase {
    
    private static SettingDatabase instance;
    
    public abstract SettingDao settingDao();
    
    public static synchronized SettingDatabase getInstance(Context context) {
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    SettingDatabase.class, "setting_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }
    
    //todo RoomCallback :
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
        
    };
    
    
    //TODO Remember to add AsyncTask, and Callback ( to add initial 3 settings)
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private SettingDao settingDao;

        private PopulateDbAsyncTask(SettingDatabase db){
            settingDao = db.settingDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            
            settingDao.insert(new Setting("2 Dices", 12, false));
            settingDao.insert(new Setting("5 Dices", 30, false));
            settingDao.insert(new Setting("6 Dices", 36, false));

            return null;
        }
    }




}
