package com.android.chronicler.character.spell;

import com.android.chronicler.character.SheetObject;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by leo on 25.11.2015.
 *
 * This class represents a single spell slot
 */
public class SpellSlot extends SheetObject implements Serializable {
    private Spell spell;
    private boolean available = true;
    private int level;
    private String className;


    // Prepares a spell to the spell slot, returns true if successful, false if not.
    // A spell can be prepared to the slot at any time without altering it's availability.
    // However this action is supposed to take about an hour in-game and should not be done
    // Without DM approval or knowledge
    public boolean prepare(Spell s){
        if(this.level != s.getLevelFor(this.className)) return false;
        this.spell = s;
        return true;
    }

    public boolean containsSpell(){
        return (this.spell != null);
    }

    // uses the spell slot in this slot, returns the spell for the option of showing details (may not be necessary)
    public Spell cast(){
        this.available = false;
        return this.spell;
    }

    public void refresh() {
        this.available = true;
    }

    // This may be redundant
    @JsonIgnore
    public String getStatus(){ return (this.available ? "available" : "spent");}

    // This may be redundant
    @JsonIgnore
    public String getSpellID() {
        return spell.getId();
    }

    // Returns a human readable string describing the class and lvl of the spell-slot.
    @JsonIgnore
    public String getType() {return ""+this.className+this.level;}

    @JsonIgnore
    public String getName() {
        if(this.spell == null) return "Empty";
        return this.spell.getName();
    }

    // Given a spell-list returns a list of all spells this spell-slot can hold
    @JsonIgnore
    public ArrayList<Spell> getPossibleSpells(SpellList spellList){
        return spellList.getSpellsFor(this.className,this.level);
    }

    //<editor-fold desc="Getters and Setters">

    public Spell getSpell() {
        return spell;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    //</editor-fold>
}
