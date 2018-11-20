package io.github.davidwickerhf.diceroller;


import java.util.ArrayList;
import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "setting_table")
public class Setting {
    
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int maxDiceSum;

    private String title;

    private ArrayList<String> items = new ArrayList<>();

    @Ignore
    public Setting(String title, int maxDiceSum) {
        this.title = title;
        this.maxDiceSum = maxDiceSum;
    }


    public Setting(String title, int maxDiceSum, ArrayList<String> items) {
        this.title = title;
        this.maxDiceSum = maxDiceSum;
        this.items = items;
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

    public ArrayList<String> getItems() {return items;}

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public int getMaxDiceSum() {
        return maxDiceSum;
    }

    
    
 
}
