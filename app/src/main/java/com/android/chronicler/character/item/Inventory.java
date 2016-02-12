package com.android.chronicler.character.item;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by leo on 28.11.2015.
 *
 * Class to keep track of a single characters inventory
 */
public class Inventory implements Serializable {
    private ArrayList<Item> items = new ArrayList<>();

    public Inventory() { /* Empty constructor for JSON */ }

    public void add(Item item){
        items.add(item);
    }

    public boolean remove(Item item){
        return items.remove(item);
    }

    @JsonIgnore
    public ArrayList<Item> getEquipped(){
        ArrayList<Item> equipped = new ArrayList<Item>();
        for(Item item : items){
            if(item.isEquipped()) equipped.add(item);
        }
        return equipped;
    }

    @JsonIgnore
    public ArrayList<Item> getNotEquipped(){
        ArrayList<Item> notEquipped = new ArrayList<Item>();
        for(Item item : items){
            if(!item.isEquipped()) notEquipped.add(item);
        }
        return notEquipped;
    }

    //<editor-fold desc="Getters and Setters">

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    // </editor-fold>
}
