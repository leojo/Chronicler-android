package com.android.chronicler.character.spell;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by leo on 25.11.2015.
 *
 * Class that represents the collection of spell slots for a single character.
 */
public class SpellSlots  implements Serializable {
    private ArrayList<SpellSlot> spellSlots;

    public void add(SpellSlot ss){
        this.spellSlots.add(ss);
    }

    // Removes a single spellslot of the given className and level. Returns true if a deletion was made.
    public boolean remove(String className, int level){
        for(SpellSlot slot : this.spellSlots){
            if(slot.getClassName() == className && slot.getLevel() == level){
                this.spellSlots.remove(slot);
                return true;
            }
        }
        return false;
    }

    // Removes all spellslots matching the className,level. Returns true if any deletion was made.
    public boolean removeAll(String className, int level){
        boolean retVal = false;
        for(SpellSlot slot : this.spellSlots){
            if(slot.getClassName() == className && slot.getLevel() == level){
                this.spellSlots.remove(slot);
                retVal = true;
            }
        }
        return retVal;
    }

    // get a list of the unique spell slot types.
    @JsonIgnore
    public ArrayList<SpellSlot> getSpellSlotTypes() {
        ArrayList<String> typeNames = new ArrayList<String>();
        ArrayList<SpellSlot> types = new ArrayList<SpellSlot>();
        for(SpellSlot slot : this.spellSlots){
            String typeName = slot.getType();
            if(!typeNames.contains(typeName)){
                typeNames.add(typeName);
                types.add(slot);
            }
        }
        return types;
    }

    // Sets the number of spell-slots for the className and level to numSpells
    public void update(String className, int level, int numSpells){
        int oldCount = this.count(className, level);
        if(numSpells >= oldCount){
            for (int i = 0; i < numSpells - oldCount; i++) {
                SpellSlot ss = new SpellSlot();
                ss.setClassName(className);
                ss.setLevel(level);
                this.add(ss);
            }
        } else {
            // This should never happen, except by manual override from user
            for (int i = 0; i < oldCount - numSpells; i++) {
                this.remove(className,level);
            }
        }
    }

    // Returns the number of spell-slots that match the criteria
    public int count(String className, int level){
        int count = 0;
        for(SpellSlot slot : this.spellSlots){
            if(slot.getLevel() == level && slot.getClassName() == className) count++;
        }
        return count;
    }

    //<editor-fold desc="Getters and Setters">
    public ArrayList<SpellSlot> getSpellSlots() {
        return spellSlots;
    }

    public void setSpellSlots(ArrayList<SpellSlot> spellSlots) {
        this.spellSlots = spellSlots;
    }
    //</editor-fold>
}
