package com.android.chronicler.character.ability;

import com.android.chronicler.character.enums.AbilityID;

import java.util.HashMap;
import java.util.Map;

import static com.android.chronicler.util.alUtils.sum;

/**
 * Created by BjornBjarnsteins on 10/28/15.
 *
 * Contains information about ability scores used by character sheets
 *
 */

public class AbilityScore {
	String name;
	String shortName;

	public Map<String, Integer> bonuses; // Map<source, value> of bonuses to this skill
	public int totalValue;
	public int modifier;

	public AbilityScore(AbilityID id) {
		// Setup name and shortname based on AbilityID
		switch (id) {
			case STR:
				this.name = "Strength";
				this.shortName = "STR";
				break;
			case DEX:
				this.name = "Dexterity";
				this.shortName = "DEX";
				break;
			case CON:
				this.name = "Constitution";
				this.shortName = "CON";
				break;
			case INT:
				this.name = "Intelligence";
				this.shortName = "INT";
				break;
			case WIS:
				this.name = "Wisdom";
				this.shortName = "WIS";
				break;
			case CHA:
				this.name = "Charisma";
				this.shortName = "CHA";
				break;
		}
		this.totalValue = 10;
		this.modifier = 0;
		this.bonuses = new HashMap<String, Integer>();
		this.bonuses.put("Base Score", 10);
	}

	public void update() {
		this.totalValue = sum(this.bonuses.values());
		this.modifier = (this.totalValue / 2) - 5;
	}

	public boolean setBonus(String key, int value){
		boolean existingBonus = this.bonuses.containsKey(key);
		this.bonuses.put(key,value);
		this.update();
		return existingBonus;
	}

	public boolean incrementBonus(String key){
		boolean existingBonus = this.bonuses.containsKey(key);
		int value = 1;
		if(existingBonus) value = this.bonuses.get(key)+1;
		this.bonuses.put(key,value);
		this.update();
		return existingBonus;
	}

	public boolean decrementBonus(String key){
		boolean validBonus = this.bonuses.containsKey(key);
		if(validBonus) {
			int value = this.bonuses.get(key)-1;
			this.bonuses.put(key, value);
			this.update();
		}
		return validBonus;
	}

	public boolean removeBonus(String key){
		if(this.bonuses.containsKey(key)){
			this.bonuses.remove(key);
			this.update();
			return true;
		}
		return false;
	}
}
