package com.android.chronicler.character;

import com.android.chronicler.character.ability.AbilityScores;
import com.android.chronicler.character.feat.FeatList;
import com.android.chronicler.character.item.Inventory;
import com.android.chronicler.character.save.Saves;
import com.android.chronicler.character.skill.Skills;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CharacterSheet implements Serializable{
    private static final Logger logger = Logger.getLogger(CharacterSheet.class.getName());
    //TODO: Implement this. Note! NO HASHMAPS IN THIS LAYER OF CODE! All hashmaps should be hidden inside a class! Function names are not final.

    // =====================
    // VARIABLE DECLARATIONS
    // =====================
    /*private SpellSlots spellSlots = new SpellSlots();
    private Inventory inventory = new Inventory();*/
    private FeatList feats = new FeatList();
    private Skills skills;
    private Saves saves;
    private AbilityScores abilityScores;
    private String name;
    private String race;
    private String characterClass;
    private int level;

    //TODO: Add functions and variables as they become needed, don't try to foresee every possible need beforehand. Focus on the scalability of the class so that adding new functions will be easy in the future.

    // =====================
    // CHARACTER SETUP STUFF
    // =====================
    public CharacterSheet(){}

    public CharacterSheet(String name, String race, String characterClass, String skillsJSON){
        level = 1;
        this.characterClass = characterClass;
        this.name = name;
        this.race = race;
        this.skills = skills;
        abilityScores = new AbilityScores();
        saves = new Saves(abilityScores);
        skills = new Skills(abilityScores, skillsJSON);
    }

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

    public String toJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public static CharacterSheet fromJSON(String CharacterSheetJSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(CharacterSheetJSON, CharacterSheet.class);
    }

    //<editor-fold desc="Getters and Setters">
    /*public SpellSlots getSpellSlots() {
            return spellSlots;
        }

        public void setSpellSlots(SpellSlots spellSlots) {
            this.spellSlots = spellSlots;
        }

        public FeatList getFeats() {
            return feats;
        }

        public void setFeats(FeatList feats) {
            this.feats = feats;
        }

        public Inventory getInventory() {
            return inventory;
        }

        public void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }*/

    public Skills getSkills() {
        return skills;
    }

    public void setSkills(Skills skills) {
        this.skills = skills;
    }

    public Saves getSaves() {
        return saves;
    }

    public void setSaves(Saves saves) {
        this.saves = saves;
    }

    public AbilityScores getAbilityScores() {
        return abilityScores;
    }

    public void setAbilityScores(AbilityScores abilityScores) {
        this.abilityScores = abilityScores;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    //</editor-fold>
}