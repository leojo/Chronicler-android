package com.android.chronicler.character.item;


import com.android.chronicler.util.OfflineResultSet;
import com.android.chronicler.util.srdDbLookup;

import java.io.Serializable;

/**
 * Created by leo on 28.11.2015.
 *
 * A single item
 */
public class Item implements Serializable {
    private String name = "";
    private String cost;
    private String weight;

    //<editor-fold desc="Getters and Setters">
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
    //</editor-fold>
}
