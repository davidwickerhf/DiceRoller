package io.github.davidwickerhf.diceroller.settingDatabase;


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

    private boolean hasItemList;

    private ArrayList<String> items = new ArrayList<>();

    @Ignore
    public Setting(String title, int maxDiceSum, boolean hasItemList) {
        this.title = title;
        this.maxDiceSum = maxDiceSum;
        this.hasItemList = hasItemList;
    }


    public Setting(String title, int maxDiceSum, ArrayList<String> items, boolean hasItemList) {
        this.title = title;
        this.maxDiceSum = maxDiceSum;
        this.hasItemList = hasItemList;
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

    public boolean hasItemList(){ return hasItemList; }
    
    
 
}
