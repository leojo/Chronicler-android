package com.android.chronicler.character.save;

import com.android.chronicler.character.ability.AbilityScores;
import com.android.chronicler.character.enums.SavingThrowID;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by leo on 8.2.2016.
 *
 * This class represents the trio of skills on a character sheet. Everything you would do in that
 * section of the character sheet this class should handle.
 */
public class Saves implements Serializable {

    private HashMap<SavingThrowID,SavingThrow> saves;

    public Saves() { // For JSON
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

    // updates all the saves with the given ability scores. Should be called whenever ability scores change.
    public void update(AbilityScores abilityScores){
        for(SavingThrow save : saves.values()){
            save.update(abilityScores);
        }
    }

    // Before: baseSaves should be an array containing the new base saves for [Fort, Ref, Will] in that order.
    // After: the saves have the correct base bonus value.
    @JsonIgnore
    public boolean setBaseSaves(int[] baseSaves){
        if(baseSaves.length != 3) return false;
        saves.get(SavingThrowID.FORT).setBase(baseSaves[0]);
        saves.get(SavingThrowID.WILL).setBase(baseSaves[1]);
        saves.get(SavingThrowID.REF).setBase(baseSaves[2]);
        return true;
    }

    @JsonIgnore
    public HashMap<String, Integer> getBonuses(SavingThrowID saveID){
        return saves.get(saveID).getBonuses();
    }

    @JsonIgnore
    public void setBonus(String bonusName, int value, SavingThrowID saveID){
        saves.get(saveID).setBonus(bonusName, value);
    }

    //<editor-fold desc="Getters and Setters">
    public HashMap<SavingThrowID, SavingThrow> getSaves() {
        return saves;
    }

    public void setSaves(HashMap<SavingThrowID, SavingThrow> saves) {
        this.saves = saves;
    }
    //</editor-fold>
}
