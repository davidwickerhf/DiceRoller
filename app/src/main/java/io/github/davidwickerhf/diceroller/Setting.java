package io.github.davidwickerhf.diceroller;


import java.util.List;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "setting_table")
public class Setting {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    
    private int maxDiceSum;
    
    private int priority;
    
    private String title;
    
    
    public Setting(String title, int maxDiceSum, int priority) {
        this.title = title;
        this.maxDiceSum = maxDiceSum;
        this.priority = priority;
    }
    
    
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public int getMaxDiceSum() {
        return maxDiceSum;
    }
    
    public int getPriority() {
        return priority;
    }
    
    
    
 
}
