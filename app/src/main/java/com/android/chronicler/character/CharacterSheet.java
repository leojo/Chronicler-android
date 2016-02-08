package com.android.chronicler.character;

import com.android.chronicler.character.ability.AbilityScores;
import com.android.chronicler.character.feat.FeatList;
import com.android.chronicler.character.item.Inventory;
import com.android.chronicler.character.skill.Skills;
import com.android.chronicler.character.spell.SpellSlots;

public class CharacterSheet{
    //TODO: Implement this. Note! NO HASHMAPS IN THIS LAYER OF CODE! All hashmaps should be hidden inside a class!
    public SpellSlots spellSlots = new SpellSlots();
    public AbilityScores abilityScores = new AbilityScores();
    public FeatList feats = new FeatList();
    public Inventory inventory = new Inventory();
    public Skills skills = new Skills();
}