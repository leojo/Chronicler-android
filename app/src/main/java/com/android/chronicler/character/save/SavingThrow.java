package com.android.chronicler.character.save;


import com.android.chronicler.character.ability.AbilityScore;
import com.android.chronicler.character.ability.AbilityScores;
import com.android.chronicler.character.enums.AbilityID;
import com.android.chronicler.character.enums.SavingThrowID;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BjornBjarnsteins on 10/28/15.
 *
 * Contains info about saving throws used by character sheets
 *
 */

public class SavingThrow {
	private String name;
	private String shortName;
	private String abilityBonusName;
	private AbilityScore baseSkill;
	private AbilityID abilityID;
	private HashMap<String, Integer> bonuses; // Map<source, value> of bonuses to this save
	private int total;
	@JsonIgnore
	private String baseBonusName = "Class Bonus";

	public SavingThrow() { /* Empty constructor for JSON */ }

	public SavingThrow(AbilityScores abilityScores, SavingThrowID id) {
		// id is the enum value
		switch (id) {
			case FORT:
				this.name = "Fortitude";
				this.shortName = "Fort";
				this.abilityBonusName = "Constitution Modifier";
				this.abilityID = AbilityID.CON;
				break;
			case REF:
				this.name = "Reflex";
				this.shortName = "Ref";
				this.abilityBonusName = "Dexterity Modifier";
				this.abilityID = AbilityID.DEX;
				break;
			case WILL:
				this.name = "Will";
				this.shortName = "Will";
				this.abilityBonusName = "Wisdom Modifier";
				this.abilityID = AbilityID.WIS;
				break;
		}

		this.bonuses = new HashMap<>();
		this.bonuses.put(baseBonusName, 0);
		this.bonuses.put(abilityBonusName, 0);
		this.update(abilityScores);
	}


	public void update(AbilityScores abilityScores) {
		// TODO: A function that updates the ability scores of the saving thows.
		this.baseSkill = abilityScores.get(abilityID);
		setBonus(abilityBonusName,baseSkill.getModifier());
	}

	public void recalculate(){
		this.total = 0;
		for (int v : this.bonuses.values()) {
			this.total += v;
		}
	}

	@JsonIgnore
	public boolean setBonus(String key, int value){
		boolean existingBonus = this.bonuses.containsKey(key);
		this.bonuses.put(key,value);
		this.recalculate();
		return existingBonus;
	}

	public boolean incrementBonus(String key){
		boolean existingBonus = this.bonuses.containsKey(key);
		int value = 1;
		if(existingBonus) value = this.bonuses.get(key)+1;
		this.bonuses.put(key,value);
		this.recalculate();
		return existingBonus;
	}

	public boolean decrementBonus(String key){
		boolean validBonus = this.bonuses.containsKey(key);
		if(validBonus) {
			int value = this.bonuses.get(key)-1;
			this.bonuses.put(key, value);
			this.recalculate();
		}
		return validBonus;
	}

	@JsonIgnore
	public boolean setBase(int value){
		return setBonus(baseBonusName,value);
	}

	public boolean removeBonus(String key){
		if(this.bonuses.containsKey(key)){
			this.bonuses.remove(key);
			this.recalculate();
			return true;
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public AbilityScore getBaseSkill() {
		return baseSkill;
	}

	public void setBaseSkill(AbilityScore baseSkill) {
		this.baseSkill = baseSkill;
	}

	public AbilityID getAbilityID() {
		return abilityID;
	}

	public void setAbilityID(AbilityID abilityID) {
		this.abilityID = abilityID;
	}

	public HashMap<String, Integer> getBonuses() {
		return bonuses;
	}

	public void setBonuses(HashMap<String, Integer> bonuses) {
		this.bonuses = bonuses;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getAbilityBonusName() {
		return abilityBonusName;
	}

	public void setAbilityBonusName(String abilityBonusName) {
		this.abilityBonusName = abilityBonusName;
	}
}

