package com.android.chronicler.character.ability;

import com.android.chronicler.character.enums.AbilityID;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by leo on 8.2.2016.
 *
 * A class to represent and handle all the things you would use the ability score section of a
 * classic paper character sheet to do.
 */
public class AbilityScores implements Serializable{

    private HashMap<AbilityID, AbilityScore> abilityScores;

    public AbilityScores(){
        // Create the ability scores:
        abilityScores = new HashMap<>();
        abilityScores.put(AbilityID.STR,new AbilityScore(AbilityID.STR));
        abilityScores.put(AbilityID.DEX, new AbilityScore(AbilityID.DEX));
        abilityScores.put(AbilityID.CON, new AbilityScore(AbilityID.CON));
        abilityScores.put(AbilityID.INT,new AbilityScore(AbilityID.INT));
        abilityScores.put(AbilityID.WIS,new AbilityScore(AbilityID.WIS));
        abilityScores.put(AbilityID.CHA, new AbilityScore(AbilityID.CHA));
        // Assign a randomly rolled ability score (using the standard 4d6 drop lowest method)
        setBase(AbilityID.STR,roll4d6dropLowest());
        setBase(AbilityID.DEX,roll4d6dropLowest());
        setBase(AbilityID.CON,roll4d6dropLowest());
        setBase(AbilityID.WIS,roll4d6dropLowest());
        setBase(AbilityID.INT,roll4d6dropLowest());
        setBase(AbilityID.CHA,roll4d6dropLowest());
        // update the stuff
        update();
    }

    @JsonIgnore
    public AbilityScore get(AbilityID ID){
        return abilityScores.get(ID);
    }

    @JsonIgnore
    public void setBonus(AbilityID ID, String bonusName, int bonus){
        AbilityScore a = abilityScores.get(ID);
        a.setBonus(bonusName, bonus);
    }

    public void removeBonus(AbilityID ID, String bonusName){
        AbilityScore a = abilityScores.get(ID);
        a.removeBonus(bonusName);
    }

    public void incrementBonus(AbilityID ID, String bonusName){
        AbilityScore a = abilityScores.get(ID);
        a.incrementBonus(bonusName);
    }

    public void decrementBonus(AbilityID ID, String bonusName){
        AbilityScore a = abilityScores.get(ID);
        a.decrementBonus(bonusName);
    }

    @JsonIgnore
    public void setBase(AbilityID ID, int value){
        abilityScores.get(ID).setBonus(AbilityScore.baseBonusName, value);
    }

    public void incrementBase(AbilityID ID){
        abilityScores.get(ID).incrementBonus(AbilityScore.baseBonusName);
    }

    public void decrementBase(AbilityID ID){
        abilityScores.get(ID).decrementBonus(AbilityScore.baseBonusName);
    }

    @JsonIgnore
    public int[] getAbilityScoreTotals(){
        int[] abScoresTotal = new int[6];
        AbilityID[] AbilityIDs = new AbilityID[]{AbilityID.STR,AbilityID.DEX,AbilityID.CON,AbilityID.INT,AbilityID.WIS,AbilityID.CHA};
        for(int i=0; i<6; i++){
            AbilityID id = AbilityIDs[i];
            abScoresTotal[i]=(abilityScores.get(id).getTotalValue());
        }
        return abScoresTotal;
    }

    @JsonIgnore
    public int[] getAbilityScoreMods() {
        int[] mods = new int[6];
        AbilityID[] AbilityIDs = new AbilityID[]{AbilityID.STR,AbilityID.DEX,AbilityID.CON,AbilityID.INT,AbilityID.WIS,AbilityID.CHA};
        for(int i=0; i<6; i++){
            AbilityID id = AbilityIDs[i];
            mods[i]=(abilityScores.get(id).getModifier());
        }
        return mods;
    }

    public int rolld6(){
        return ((int)(Math.random()*6+1));
    }

    public int roll4d6dropLowest(){
        int rolls[] = new int[4];
        for (int i = 0; i < rolls.length; i++) {
            rolls[i] = rolld6();
        }
        int total = 0;
        int lowest = rolls[0];
        for(int i=1; i<rolls.length; i++){
            if(rolls[i]<lowest){
                total += lowest;
                lowest = rolls[i];
            } else {
                total += rolls[i];
            }
        }
        return total;
    }

    public void update(){
        for(AbilityID id : AbilityID.values()){
            abilityScores.get(id).update();
        }
    }

    public void update(AbilityID id){
        abilityScores.get(id).update();
    }

    public HashMap<AbilityID, AbilityScore> getAbilityScores() {
        return abilityScores;
    }

    public void setAbilityScores(HashMap<AbilityID, AbilityScore> abilityScores) {
        this.abilityScores = abilityScores;
    }


}
