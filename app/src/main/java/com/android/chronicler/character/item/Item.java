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
    private String category;
    private String subcategory;
    private String cost;
    private String weight;
    private String fullText;
    private String reference;
    private boolean special;
    private boolean equipped = false;

    public Item(String desc, boolean special){
        this.special = special;
        String[] info = desc.split(":");
        String id = info[0];

        srdDbLookup find = new srdDbLookup();
        OfflineResultSet item;

        if(special) item = find.specialItem(id + "/exact");
        else item = find.mundaneItem(id + "/exact");

        item.first();
        this.name = item.getString("name");
        this.category = item.getString("category");
        this.subcategory = item.getString("subcategory");
        this.cost = item.getString("cost");
        this.weight = item.getString("weight");
        this.fullText = item.getString("full_text");
        this.reference = item.getString("reference");
    }

    //<editor-fold desc="Getters and Setters">
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
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

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    public boolean isEquipped() {
        return equipped;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }
    //</editor-fold>
}
