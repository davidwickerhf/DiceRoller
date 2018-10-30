package io.github.davidwickerhf.diceroller;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SettingDao {
    
    @Insert
    void insert(Setting setting);
    
    @Update
    void update(Setting setting);
    
    @Delete
    void delete(Setting setting);
    
    @Query("SELECT * FROM setting_table ORDER BY priority ASC")
    LiveData<List<Setting>> getAllSettings();
    
    
    
}
