package com.android.chronicler.character.save;

import com.android.chronicler.character.ability.AbilityScore;
import com.android.chronicler.character.ability.AbilityScores;
import com.android.chronicler.character.enums.SavingThrowID;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;

/**
 * Created by leo on 8.2.2016.
 */
public class Saves {

    private HashMap<SavingThrowID,SavingThrow> saves;

    public Saves() {
        saves = new HashMap<>();
        saves.put(SavingThrowID.FORT, new SavingThrow());
        saves.put(SavingThrowID.REF, new SavingThrow());
        saves.put(SavingThrowID.WILL, new SavingThrow());
    }

    public Saves(AbilityScores abilityScores) {
        saves = new HashMap<>();
        saves.put(SavingThrowID.FORT, new SavingThrow(abilityScores, SavingThrowID.FORT));
        saves.put(SavingThrowID.REF, new SavingThrow(abilityScores,SavingThrowID.REF));
        saves.put(SavingThrowID.WILL, new SavingThrow(abilityScores,SavingThrowID.WILL));
    }

    public void update(AbilityScores abilityScores){
        for(SavingThrow save : saves.values()){
            save.update(abilityScores);
        }
    }

    @JsonIgnore
    public boolean setBaseSaves(int[] baseSaves){
        if(baseSaves.length != 3) return false;
        saves.get(SavingThrowID.FORT).setBase(baseSaves[0]);
        saves.get(SavingThrowID.WILL).setBase(baseSaves[1]);
        saves.get(SavingThrowID.REF).setBase(baseSaves[2]);
        return true;
    }

    /*@JsonIgnore
    public*/

}
