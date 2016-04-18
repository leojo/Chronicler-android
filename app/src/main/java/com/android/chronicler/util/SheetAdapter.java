package com.android.chronicler.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.chronicler.R;
import com.android.chronicler.character.SheetObject;
import com.android.chronicler.character.feat.Feat;
import com.android.chronicler.character.feat.FeatList;
import com.android.chronicler.character.feat.FeatSlot;
import com.android.chronicler.character.item.ArmorShield;
import com.android.chronicler.character.item.Inventory;
import com.android.chronicler.character.item.Item;
import com.android.chronicler.character.spell.Spell;
import com.android.chronicler.character.spell.SpellSlot;
import com.android.chronicler.character.spell.SpellSlots;
import com.android.chronicler.ui.SearchActivity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by andrea on 10.4.2016.
 */
public class SheetAdapter extends BaseAdapter {
    private ArrayList<SheetObject> sheetObjs;
    private String[] liValues;
    private final LayoutInflater inflater;

    public SheetAdapter(Context context, ArrayList<SheetObject> arrayList){
        this.sheetObjs = arrayList;
        this.setValues();
        inflater = LayoutInflater.from(context);
    }

    public SheetAdapter(Context context , FeatList feats){
        if(feats.getFeats().size() == 0) {
            FeatSlot dummySlot = new FeatSlot();
            Feat dummyFeat = new Feat();
            dummyFeat.setName("This is an awesome dummy feat");
            dummySlot.setFeat(dummyFeat);
            feats.add(dummySlot);
        }
        this.sheetObjs = new ArrayList<>();
        this.sheetObjs.addAll(feats.getFeats());
        this.setValues();
        inflater = LayoutInflater.from(context);
    }

    public void clearAndAddAll(ArrayList<SheetObject> objs) {
        SearchActivity.searchResults.clear();
        SearchActivity.searchResults.addAll(objs);
        sheetObjs = new ArrayList<>();
        this.sheetObjs.addAll(objs);
        this.setValues();
    }

    public void clearAndAddAll(FeatList feats) {
        SearchActivity.searchResults.clear();
        SearchActivity.searchResults.addAll(feats.getFeats());
        sheetObjs = new ArrayList<>();
        this.sheetObjs.addAll(feats.getFeats());
        this.setValues();
    }


    public SheetAdapter(Context context , SpellSlots spells){
        if(spells.getSpellSlots().size() == 0) {
            SpellSlot dummySlot = new SpellSlot();
            Spell dummyFeat = new Spell();
            dummyFeat.setName("This is an awesome dummy spell");
            dummySlot.setSpell(dummyFeat);
            spells.add(dummySlot);
        }
        this.sheetObjs = new ArrayList<>();
        this.sheetObjs.addAll(spells.getSpellSlots());
        this.setValues();
        inflater = LayoutInflater.from(context);
    }



    public void clearAndAddAll(SpellSlots spells) {
        SearchActivity.searchResults.clear();
        SearchActivity.searchResults.addAll(spells.getSpellSlots());
        sheetObjs.clear();
        this.sheetObjs.addAll(spells.getSpellSlots());
        this.setValues();
    }

    public SheetAdapter(Context context , Inventory invt){
        if(invt.getItems().size() == 0) {
            Item dummyItem = new ArmorShield();
            dummyItem.setName("This is an awesome dummy item!");
            invt.add(dummyItem);
        }
        this.sheetObjs = new ArrayList<>();
        this.sheetObjs.addAll(invt.getItems());
        this.setValues();
        inflater = LayoutInflater.from(context);
    }

    public void clearAndAddAll(Inventory invt) {
        SearchActivity.searchResults.clear();
        SearchActivity.searchResults.addAll(invt.getItems());
        sheetObjs = new ArrayList<>();
        this.sheetObjs.addAll(invt.getItems());
        this.setValues();
    }

    private void setValues()  {
        int size = this.sheetObjs.size();
        liValues = new String[size];
        for(int i=0; i<size; i++) {
            liValues[i] = this.sheetObjs.get(i).getName();
        }
        //Arrays.sort(liValues);
        Log.d("SHEET OBJS FROM ADAPTER",Arrays.toString(liValues));
    }

    @Override
    public int getCount() {
        return sheetObjs.size();
    }

    @Override
    public Object getItem(int position) {
        return sheetObjs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public View getView(int position, View item, ViewGroup parent) {

        if(item == null)
            item = inflater.inflate(R.layout.sheet_object_list_item, null);

        TextView nameView = (TextView)item.findViewById(R.id.name);
        nameView.setText(sheetObjs.get(position).getName());
        TextView descrView = (TextView)item.findViewById(R.id.shortDescr);
        descrView.setText(sheetObjs.get(position).shortDescr());

        return item;
    }

    public void remove(int position){
        sheetObjs.remove(position);
        setValues();
    }
}
