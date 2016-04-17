package com.android.chronicler.character.skill;

import android.util.Log;

import com.android.chronicler.character.ability.AbilityScores;
import com.android.chronicler.character.enums.AbilityID;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 10.2.2016.
 *
 * A data wrapper for a skill object, along with some character specific stuff such as skill ranks and update functions.
 */

public class Skill implements Serializable {
	private String name;
    private String description;
    private String action;
    private String try_again;
    private String special;
    private String synergy;
    private AbilityID abilityID;
	private boolean trainedOnly;
	private boolean armorPenalty;

    private int ranks;
	private Map<String, Integer> bonuses; // Map<source, value> of bonuses to this skill
	private int totalValue;

    @JsonIgnore
    private final static String modBonus = "Ability Modifier";
    @JsonIgnore
    private final static String miscBonus = "Misc";

    // UPDATE FUNCTIONS ///////////////////////////////////////////////
    // Call this to make the skill recalculate stuff i.e. when ranks increase
    public void update(){
        update(this.getBonuses().get(modBonus));
    }

    // Call this when the ability scores change
    public void update(AbilityScores abilityScores){
        update(abilityScores.get(abilityID).getModifier());
    }

    // The actual update function, using the apropriate ability modifier.
	public void update(int abilityModifier) {
		this.bonuses.put(modBonus, abilityModifier);
        this.totalValue = 0;
        for(Integer value : bonuses.values()) totalValue += value;
	}
    // UPDATE END //////////////////////////////////////////////////

    // Does what it says on the tin.
	public void incrementRanks(){
		this.ranks++;
	}

    // Get's the total modifier of the skill, ranks and bonuses added together.
    @JsonIgnore
    public int getMod(){
        if(bonuses.containsKey(modBonus)) return bonuses.get(modBonus);
        return 0;
    }

    @JsonIgnore
    public int getMisc(){
        if(bonuses.containsKey(miscBonus)) return bonuses.get(miscBonus);
        return 0;
    }

    @JsonIgnore
    public void setMisc(int misc){
        bonuses.put(miscBonus, misc);
    }

    // Does what you'd think, creates bonus if it didn't exist. returns true if bonus existed,
    // false if it had to be created.
    @JsonIgnore
	public boolean setBonus(String key, int value){
		boolean existingBonus = this.bonuses.containsKey(key);
		this.bonuses.put(key,value);
		this.update();
		return existingBonus;
	}

    // Increments the designated bonus if it exists, creates it if it doesn't.
    // Returns true if bonus existed, false if it was created.
	public boolean incrementBonus(String key){
		boolean existingBonus = this.bonuses.containsKey(key);
		int value = 1;
		if(existingBonus) value = this.bonuses.get(key)+1;
		this.bonuses.put(key,value);
		this.update();
		return existingBonus;
	}

    // Decrements the designated bonus and returns true, returns false if bonus doesn't exist.
	public boolean decrementBonus(String key){
		boolean validBonus = this.bonuses.containsKey(key);
		if(validBonus) {
			int value = this.bonuses.get(key)-1;
			this.bonuses.put(key, value);
			this.update();
		}
		return validBonus;
	}

    // Returns false if bonus didn't exist.
	public boolean removeBonus(String key){
		if(this.bonuses.containsKey(key)){
			this.bonuses.remove(key);
			this.update();
			return true;
		}
		return false;
	}

    //<editor-fold desc="Getters and Setters">
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AbilityID getAbilityID() {
        return abilityID;
    }

    public void setAbilityID(AbilityID abilityID) {
        this.abilityID = abilityID;
    }

    public boolean isTrainedOnly() {
        return trainedOnly;
    }

    public void setTrainedOnly(boolean trainedOnly) {
        this.trainedOnly = trainedOnly;
    }

    public boolean isArmorPenalty() {
        return armorPenalty;
    }

    public void setArmorPenalty(boolean armorPenalty) {
        this.armorPenalty = armorPenalty;
    }

    public int getRanks() {
        return ranks;
    }

    public void setRanks(int ranks) {
        this.ranks = ranks;
    }

    public Map<String, Integer> getBonuses() {
        return bonuses;
    }

    public void setBonuses(Map<String, Integer> bonuses) {
        this.bonuses = bonuses;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSynergy() {
        return synergy;
    }

    public void setSynergy(String synergy) {
        this.synergy = synergy;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTry_again() {
        return try_again;
    }

    public void setTry_again(String try_again) {
        this.try_again = try_again;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }
    //</editor-fold>

    // Creates a new skill object using the same static skill data as the baseSkill.
    public static Skill copy(Skill baseSkill) {
        Skill newSkill =  new Skill();

        newSkill.setName(baseSkill.getName());
        newSkill.setDescription(baseSkill.getDescription());
        newSkill.setAction(baseSkill.getAction());
        newSkill.setTry_again(baseSkill.getTry_again());
        newSkill.setSpecial(baseSkill.getSpecial());
        newSkill.setSynergy(baseSkill.getSynergy());
        newSkill.setAbilityID(baseSkill.getAbilityID());
        newSkill.setTrainedOnly(baseSkill.isTrainedOnly());
        newSkill.setArmorPenalty(baseSkill.isArmorPenalty());

        newSkill.setBonuses(new HashMap<String, Integer>());
        newSkill.setBonus(modBonus, baseSkill.getBonuses().get(modBonus));
        newSkill.setRanks(0);

        return newSkill;
    }
}
