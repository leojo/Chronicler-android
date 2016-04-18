package com.android.chronicler.character.item;

import com.android.chronicler.character.enums.ArmorType;
import com.android.chronicler.character.item.Equipment;

import java.io.Serializable;

/**
 * Created by leo on 23.2.2016.
 *
 * A single piece of armor or shield.
 */
public class ArmorShield extends Equipment {
    private String ACbonus, maxDex, arcaneSpellFailure, armorCheckPen;
    private String speed20, speed30;
    private String type;

    @Override
    public String longDescr() {
        String customDetails = "<p><h3>"+getName()+"</h3></p>";
        String fullText = "";
        if(getDescription() != null && !getDescription().trim().equalsIgnoreCase("")) fullText = getDescription();
        if(ACbonus != null && !ACbonus.trim().equalsIgnoreCase("")) customDetails += "Armor/Shield Bonus: "+ACbonus+"<br>Maximum Dex Bonus: "+maxDex+"<br>Armor Check Penalty: "+armorCheckPen+"<br>Arcane Spell Failure Chance: "+arcaneSpellFailure+"<br>Speed: "+speed30+" (30ft.), "+speed20+" (20ft.)<br>Weight: "+getWeight()+"<br>Cost: "+getCost();
        else return super.longDescr();
        return customDetails+"<br><div><p>"+fullText+"</p></div>";
    }

    //<editor-fold desc="Getters and Setters">
    public String getACbonus() {
        return ACbonus;
    }

    public void setACbonus(String ACbonus) {
        this.ACbonus = ACbonus;
    }

    public String getMaxDex() {
        return maxDex;
    }

    public void setMaxDex(String maxDex) {
        this.maxDex = maxDex;
    }

    public String getArcaneSpellFailure() {
        return arcaneSpellFailure;
    }

    public void setArcaneSpellFailure(String arcaneSpellFailure) {
        this.arcaneSpellFailure = arcaneSpellFailure;
    }

    public String getArmorCheckPen() {
        return armorCheckPen;
    }

    public void setArmorCheckPen(String armorCheckPen) {
        this.armorCheckPen = armorCheckPen;
    }

    public String getSpeed20() {
        return speed20;
    }

    public void setSpeed20(String speed20) {
        this.speed20 = speed20;
    }

    public String getSpeed30() {
        return speed30;
    }

    public void setSpeed30(String speed30) {
        this.speed30 = speed30;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    //</editor-fold>
}