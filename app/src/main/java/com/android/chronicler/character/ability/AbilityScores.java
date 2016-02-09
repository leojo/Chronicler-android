package com.android.chronicler.character.ability;

import com.android.chronicler.character.enums.AbilityID;
import java.util.HashMap;

/**
 * Created by leo on 8.2.2016.
 */
public class AbilityScores {

    private final HashMap<AbilityID, AbilityScore> abilityScores;
    private final AbilityID[] keys = {AbilityID.STR, AbilityID.DEX, AbilityID.CON, AbilityID.INT, AbilityID.WIS, AbilityID.CHA};

    public AbilityScores(){
        this.abilityScores = new HashMap<>();
    }

    public AbilityScores(HashMap<AbilityID, AbilityScore> abilityScoreHashMap){
        this.abilityScores = abilityScoreHashMap;
    }

    public AbilityScore get(AbilityID ID){
        return abilityScores.get(ID);
    }

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

    public void setBase(AbilityID ID, int value){
        abilityScores.get(ID).setBonus("Base Score", value);
    }

    public void incrementBase(AbilityID ID){
        abilityScores.get(ID).incrementBonus("Base Score");
    }

    public void decrementBase(AbilityID ID){
        abilityScores.get(ID).decrementBonus("Base Score");
    }

    public int[] getAbilityScoreTotals(){
        int[] abScoresTotal = new int[6];
        for(int i=0; i<keys.length; i++){
            abScoresTotal[i] = abilityScores.get(keys[i]).totalValue;
        }
        return abScoresTotal;
    }

    public int[] getAbilityScoreMods(){
        int[] mods = new int[6];
        for(int i=0; i<keys.length; i++){
            mods[i] = abilityScores.get(keys[i]).modifier;
        }
        return mods;
    }

    public HashMap<AbilityID,AbilityScore> getAbilityScores(){
        return abilityScores;
    }

    public void update(){
        for(int i=0; i<keys.length; i++){
            abilityScores.get(keys[i]).update();
        }
    }
}
