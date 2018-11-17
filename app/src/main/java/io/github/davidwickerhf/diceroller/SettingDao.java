package io.github.davidwickerhf.diceroller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverter;
import androidx.room.Update;

@Dao
public interface SettingDao {
    
    @Insert
    void insert(Setting setting);
    
    @Update
    void update(Setting setting);
    
    @Delete
    void delete(Setting setting);
    
    @Query("SELECT * FROM setting_table ORDER BY id ASC")
    LiveData<List<Setting>> getAllSettings();


    
}
