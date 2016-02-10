package com.android.chronicler.character.ability;

import com.android.chronicler.character.enums.AbilityID;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;

/**
 * Created by leo on 8.2.2016.
 */
public class AbilityScores {

    private HashMap<AbilityID, AbilityScore> abilityScores;

    public AbilityScores(){
        abilityScores = new HashMap<>();
        abilityScores.put(AbilityID.STR,new AbilityScore(AbilityID.STR));
        abilityScores.put(AbilityID.DEX,new AbilityScore(AbilityID.DEX));
        abilityScores.put(AbilityID.CON,new AbilityScore(AbilityID.CON));
        abilityScores.put(AbilityID.INT,new AbilityScore(AbilityID.INT));
        abilityScores.put(AbilityID.WIS,new AbilityScore(AbilityID.WIS));
        abilityScores.put(AbilityID.CHA,new AbilityScore(AbilityID.CHA));
    }

    public AbilityScores(HashMap<AbilityID, AbilityScore> abilityScoreHashMap){
        this.abilityScores = abilityScoreHashMap;
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
        abilityScores.get(ID).setBonus("Base Score", value);
    }

    public void incrementBase(AbilityID ID){
        abilityScores.get(ID).incrementBonus("Base Score");
    }

    public void decrementBase(AbilityID ID){
        abilityScores.get(ID).decrementBonus("Base Score");
    }

    @JsonIgnore
    public int[] getAbilityScoreTotals(){
        int[] abScoresTotal = new int[6];
        int i = 0;
        for(AbilityID id : AbilityID.values()){
            abScoresTotal[i] = abilityScores.get(id).getTotalValue();
            i++;
        }
        return abScoresTotal;
    }

    @JsonIgnore
    public int[] getAbilityScoreMods() {
        int[] mods = new int[6];
        int i=0;
        for (AbilityID id : AbilityID.values()) {
            mods[i] = abilityScores.get(id).getModifier();
            i++;
        }
        return mods;
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
