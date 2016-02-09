package com.android.chronicler.character;

import com.android.chronicler.character.ability.AbilityScores;
import com.android.chronicler.character.feat.FeatList;
import com.android.chronicler.character.item.Inventory;
import com.android.chronicler.character.save.Saves;
import com.android.chronicler.character.skill.Skills;
import com.android.chronicler.character.spell.SpellSlots;

public class CharacterSheet{
    //TODO: Implement this. Note! NO HASHMAPS IN THIS LAYER OF CODE! All hashmaps should be hidden inside a class! Function names are not final.

    // =====================
    // VARIABLE DECLARATIONS
    // =====================
    public SpellSlots spellSlots = new SpellSlots();
    public AbilityScores abilityScores = new AbilityScores();
    public FeatList feats = new FeatList();
    public Inventory inventory = new Inventory();
    public Skills skills = new Skills();
    public Saves saves = new Saves();

    //TODO: Add functions and variables as they become needed, don't try to forsee every possible need beforehand. Focus on the scalability of the class so that adding new functions will be easy in the future.

    // =====================
    // CHARACTER SETUP STUFF
    // =====================
    public void initAbilityScores(int[] StartingAbilityScores){
        //TODO: Implement initAbilityScores
    }

    // =================
    // DURING PLAY STUFF
    // =================

    //<editor-fold desc="HP stuff">
    public void setHP(int currentHP){
        //TODO: Implement setHP
    }

    public void healBySpell(int healthRegained){
        //TODO: Implement healBySpell
    }

    public void setNonLethalDamage(int totalNonLethalDamage){
        //TODO: Implement setNonLethalDamage
    }

    public void addNonLethalDamage(int nonLethalDamage){
        //TODO: Implement addNonLethalDamage
    }

    public void removeNonLethalDamage(){
        //TODO: Implement removeNonLethalDamage
    }
    //</editor-fold>

    //<editor-fold desc="Posessions stuff">
    public void addMoney(){
        //TODO: Implement addMoney
    }

    public void removeMoney(){
        //TODO: Implement removeMoney
    }

    public void addItem(){
        //TODO: Implement addItem
    }

    public void removeItem(){
        //TODO: Implement removeItem
    }
    //</editor-fold>

    public void rest(){
        //TODO: Implement rest
    }

    // ===============
    // SPECIAL ACTIONS
    // ===============

    public void levelUp(int ClassID){
        //TODO: Implement levelUp
    }
}