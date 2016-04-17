package com.android.chronicler.character;

import android.util.Log;

import com.android.chronicler.character.ability.AbilityScores;
import com.android.chronicler.character.enums.AbilityID;
import com.android.chronicler.character.enums.SavingThrowID;
import com.android.chronicler.character.feat.Feat;
import com.android.chronicler.character.feat.FeatList;
import com.android.chronicler.character.feat.FeatSlot;
import com.android.chronicler.character.item.ArmorShield;
import com.android.chronicler.character.item.Inventory;
import com.android.chronicler.character.item.Item;
import com.android.chronicler.character.item.Weapon;
import com.android.chronicler.character.save.Saves;
import com.android.chronicler.character.save.SavingThrow;
import com.android.chronicler.character.skill.Skills;
import com.android.chronicler.character.spell.Spell;
import com.android.chronicler.character.spell.SpellList;
import com.android.chronicler.character.spell.SpellSlots;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CharacterSheet implements Serializable{

    // =====================
    // VARIABLE DECLARATIONS
    // =====================
    private SpellSlots spellSlots = new SpellSlots();
    private SpellList availableSpells = new SpellList();
    private Inventory inventory = new Inventory();
    private FeatList feats = new FeatList();
    private Skills skills;
    private Saves saves;
    private AbilityScores abilityScores;
    private ArrayList<HashMap<String,String>> advancementTable;
    private String name, race, characterClass, alignment, gender, deity, eyes, hair, height, weight, size, skin;
    private String BaB, ac, touch, ff, speed, initiative;
    private int level, hitDie, maxHp, hp, tempHp, nonlethalDamage;

    //TODO: Add functions and variables as they become needed, don't try to foresee every possible need beforehand. Focus on the scalability of the class so that adding new functions will be easy in the future.

    // =====================
    // CHARACTER SETUP STUFF
    // =====================
    public CharacterSheet(){ /* Empty constructor for JSON conversion */}

    public CharacterSheet(String name, String race, String characterClass, String skillsJSON, String advTableJSON){
        this.characterClass = characterClass;
        this.name = name;
        this.race = race;
        this.tempHp = 0;
        this.nonlethalDamage = 0;
        ObjectMapper mapper = new ObjectMapper();
        try {
            Item sword = mapper.readValue("{\"name\":\"Sword, short\",\"cost\":\"10 gp\",\"weight\":\"2 lb.\",\"equipped\":false,\"masterwork\":false,\"slot\":\"Slotless\",\"equipAction\":\"Move Action\",\"description\":\"\",\"twoHand\":false,\"oneHand\":false,\"ranged\":false,\"thrown\":false,\"light\":true,\"damageTypes\":\"Piercing\",\"damage\":\"1d6\",\"crit\":\"19-20/x2\",\"wepCat\":\"Martial Weapons\",\"type\":null,\"rangeIncr\":\"-\"}",Weapon.class);
            inventory.add(sword);
            Item shield = mapper.readValue("{\"name\":\"Shield, light wooden\",\"cost\":\"3 gp\",\"weight\":\"5 lb.\",\"equipped\":false,\"masterwork\":false,\"slot\":\"Slotless\",\"equipAction\":\"Move Action\",\"description\":\"You strap a shield to your forearm and grip it with your hand. A light shield's weight lets you carry other items in that hand, although you cannot use weapons with it. Wooden or Steel: Wooden and steel shields offer the same basic protection, though they respond differently to special attacks. Shield Bash Attacks: You can bash an opponent with a light shield, using it as an off-hand weapon. See Table: Weapons for the damage dealt by a shield bash. Used this way, a light shield is a martial bludgeoning weapon. For the purpose of penalties on attack rolls, treat a light shield as a light weapon. If you use your shield as a weapon, you lose its AC bonus until your next action (usually until the next round). An enhancement bonus on a shield does not improve the effectiveness of a shield bash made with it, but the shield can be made into a magic weapon in its own right.\",\"maxDex\":\"-\",\"arcaneSpellFailure\":\"5%\",\"armorCheckPen\":\"-1\",\"speed20\":\"-\",\"speed30\":\"-\",\"type\":\"Shields\",\"acbonus\":\"+1\"}",ArmorShield.class);
            inventory.add(shield);
            Item armor = mapper.readValue("{\"name\":\"Banded mail\",\"cost\":\"250 gp\",\"weight\":\"35 lb.\",\"equipped\":false,\"masterwork\":false,\"slot\":\"Slotless\",\"equipAction\":\"Move Action\",\"description\":\"The suit includes gauntlets.\",\"maxDex\":\"+1\",\"arcaneSpellFailure\":\"35%\",\"armorCheckPen\":\"-6\",\"speed20\":\"15 ft.\",\"speed30\":\"20 ft.\",\"type\":\"Heavy\",\"acbonus\":\"+6\"}",ArmorShield.class);
            inventory.add(armor);
            Spell spell = mapper.readValue("{\"id\":\"422\",\"name\":\"Magic Missile\",\"shortDescription\":\"1d4+1 damage; +1 missile per two levels above 1st (max 5).\",\"fullText\":\"\\n      <div topic='Magic Missile' level='5'><p><h5>Magic Missile</h5></p><table width='100%' border='1' cellpadding='2' cellspacing='2' frame='VOID' rules='ROWS'><tr maxcol='3' curcol='3'><td width='25%'/><td>Evocation [Force]<br/></td></tr><tr maxcol='3' curcol='3'><td width='25%'><b>Level:</b><br/></td><td>Sorcerer/Wizard 1<br/></td></tr><tr maxcol='3' curcol='3'><td width='25%'><b>Components:</b><br/></td><td>V, S<br/></td></tr><tr maxcol='3' curcol='3'><td width='25%'><b>Casting Time:</b><br/></td><td>1 standard action<br/></td></tr><tr maxcol='3' curcol='3'><td width='25%'><b>Range:</b><br/></td><td>Medium (100 ft. + 10 ft./level)<br/></td></tr><tr maxcol='3' curcol='3'><td width='25%'><b>Targets:</b><br/></td><td>Up to five creatures, no two of which can be more than 15 ft. apart<br/></td></tr><tr maxcol='3' curcol='3'><td width='25%'><b>Duration:</b><br/></td><td>Instantaneous<br/></td></tr><tr maxcol='3' curcol='3'><td width='25%'><b>Saving Throw:</b><br/></td><td>None<br/></td></tr><tr maxcol='3' curcol='3'><td width='25%'><b>Spell Resistance:</b><br/></td><td>Yes<br/></td></tr></table><p>A missile of magical energy darts forth from your fingertip and strikes its target, dealing 1d4+1 points of force damage.</p><p>The missile strikes unerringly, even if the target is in melee combat or has less than total cover or total concealment. Specific parts of a creature can't be singled out. Inanimate objects are not damaged by the spell.</p><p>For every two caster levels beyond 1st, you gain an additional missile-two at 3rd level, three at 5th, four at 7th, and the maximum of five missiles at 9th level or higher. If you shoot multiple missiles, you can have them strike a single creature or several creatures. A single missile can strike only one creature. You must designate targets before you check for spell resistance or roll damage.</p><p/>\\n</div>\\n    \",\"description\":\"\\n      <p>A missile of magical energy darts forth from your fingertip and strikes its target, dealing 1d4+1 points of force damage.</p>\\n      <p>The missile strikes unerringly, even if the target is in melee combat or has less than total cover or total concealment. Specific parts of a creature can't be singled out. Inanimate objects are not damaged by the spell.</p>\\n      <p>For every two caster levels beyond 1st, you gain an additional missile-two at 3rd level, three at 5th, four at 7th, and the maximum of five missiles at 9th level or higher. If you shoot multiple missiles, you can have them strike a single creature or several creatures. A single missile can strike only one creature. You must designate targets before you check for spell resistance or roll damage.</p>\\n      <p/>\\n    \",\"school\":\"Evocation\",\"specialVerbal\":\"None\",\"druidFocus\":\"None\",\"clericFocus\":\"None\",\"bardFocus\":\"None\",\"sorcererFocus\":\"None\",\"wizardFocus\":\"None\",\"arcaneFocus\":\"None\",\"xpCost\":\"None\",\"focus\":\"None\",\"arcaneMat\":\"None\",\"material\":\"None\",\"developCost\":\"None\",\"save\":\"None\",\"duration\":\"Instantaneous\",\"effect\":\"None\",\"area\":\"None\",\"target\":\"Up to five creatures, no two of which can be more than 15 ft. apart\",\"range\":\"Medium (100 ft. + 10 ft./level)\",\"castingTime\":\"1 standard action\",\"components\":\"V, S\",\"level\":\"Sorcerer/Wizard 1\",\"spellcraftDC\":\"None\",\"subSchool\":\"None\",\"descriptor\":\"Force\",\"sr\":\"Yes\"}",Spell.class);
            Log.d("SPELL_LOAD","spell == null : "+(spell==null));
            availableSpells.add(spell);
            Spell spell2 = mapper.readValue("{\"id\":\"168\",\"name\":\"Color Spray\",\"shortDescription\":\"Knocks unconscious, blinds, and/or stuns weak creatures.\",\"fullText\":\"\\n      <div topic='Color Spray' level='5'><p><h5>Color Spray</h5></p><table width='100%' border='1' cellpadding='2' cellspacing='2' frame='VOID' rules='ROWS'><tr maxcol='3' curcol='3'><td width='25%'/><td>Illusion (Pattern) [Mind-Affecting]<br/></td></tr><tr maxcol='3' curcol='3'><td width='25%'><b>Level:</b><br/></td><td>Sorcerer/Wizard 1<br/></td></tr><tr maxcol='3' curcol='3'><td width='25%'><b>Components:</b><br/></td><td>V, S, M<br/></td></tr><tr maxcol='3' curcol='3'><td width='25%'><b>Casting Time:</b><br/></td><td>1 standard action<br/></td></tr><tr maxcol='3' curcol='3'><td width='25%'><b>Range:</b><br/></td><td>15 ft.<br/></td></tr><tr maxcol='3' curcol='3'><td width='25%'><b>Area:</b><br/></td><td>Cone-shaped burst<br/></td></tr><tr maxcol='3' curcol='3'><td width='25%'><b>Duration:</b><br/></td><td>Instantaneous; see text<br/></td></tr><tr maxcol='3' curcol='3'><td width='25%'><b>Saving Throw:</b><br/></td><td>Will negates<br/></td></tr><tr maxcol='3' curcol='3'><td width='25%'><b>Spell Resistance:</b><br/></td><td>Yes<br/></td></tr></table><p>A vivid cone of clashing colors springs forth from your hand, causing creatures to become stunned, perhaps also blinded, and possibly knocking them unconscious.</p><p>Each creature within the cone is affected according to its Hit Dice.</p><p><i>2 HD or less:</i> The creature is unconscious, blinded, and stunned for 2d4 rounds, then blinded and stunned for 1d4 rounds, and then stunned for 1 round. (Only living creatures are knocked unconscious.)</p><p><i>3 or 4 HD:</i> The creature is blinded and stunned for 1d4 rounds, then stunned for 1 round.</p><p><i>5 or more HD:</i> The creature is stunned for 1 round.</p><p>Sightless creatures are not affected by <i>color spray</i>.</p><p><i>Material Component:</i> A pinch each of powder or sand that is colored red, yellow, and blue.</p><p/>\\n</div>\\n    \",\"description\":\"\\n      <p>A vivid cone of clashing colors springs forth from your hand, causing creatures to become stunned, perhaps also blinded, and possibly knocking them unconscious.</p>\\n      <p>Each creature within the cone is affected according to its Hit Dice.</p>\\n      <p><i>2 HD or less:</i> The creature is unconscious, blinded, and stunned for 2d4 rounds, then blinded and stunned for 1d4 rounds, and then stunned for 1 round. (Only living creatures are knocked unconscious.)</p>\\n      <p><i>3 or 4 HD:</i> The creature is blinded and stunned for 1d4 rounds, then stunned for 1 round.</p>\\n      <p><i>5 or more HD:</i> The creature is stunned for 1 round.</p>\\n      <p>Sightless creatures are not affected by <i>color spray</i>.</p>\\n      <p/>\\n    \",\"school\":\"Illusion\",\"specialVerbal\":\"None\",\"druidFocus\":\"None\",\"clericFocus\":\"None\",\"bardFocus\":\"None\",\"sorcererFocus\":\"None\",\"wizardFocus\":\"None\",\"arcaneFocus\":\"None\",\"xpCost\":\"None\",\"focus\":\"None\",\"arcaneMat\":\"None\",\"material\":\"A pinch each of powder or sand that is colored red, yellow, and blue.\",\"developCost\":\"None\",\"save\":\"Will negates\",\"duration\":\"Instantaneous; see text\",\"effect\":\"None\",\"area\":\"Cone-shaped burst\",\"target\":\"None\",\"range\":\"15 ft.\",\"castingTime\":\"1 standard action\",\"components\":\"V, S, M\",\"level\":\"Sorcerer/Wizard 1\",\"spellcraftDC\":\"None\",\"subSchool\":\"Pattern\",\"descriptor\":\"Mind-Affecting\",\"sr\":\"Yes\"}",Spell.class);
            availableSpells.add(spell2);
            Feat feat = mapper.readValue("{\"id\":\"235\",\"name\":\"Improved Initiative\",\"type\":\"General\",\"multiple\":\"No\",\"stack\":\"No\",\"choice\":\"None\",\"prerequisite\":\"None\",\"benefit\":\"<div topic='Benefit' level='8'><p><b>Benefit:</b> You get a +4 bonus on initiative checks.</p>\\n</div>\\n\",\"normal\":\"None\",\"special\":\"A fighter may select Improved Initiative as one of his fighter bonus feats.\",\"fullText\":\"\\n      <div topic='Improved Initiative' level='3'><p><h3>Improved Initiative [General]</h3></p><div topic='Benefit' level='8'><p><b>Benefit:</b> You get a +4 bonus on initiative checks.</p>\\n</div><div topic='Special' level='8'><p><b>Special:</b> A fighter may select Improved Initiative as one of his fighter bonus feats.</p><p/>\\n</div>\\n</div>\\n    \",\"reference\":\"SRD 3.5 Feats\",\"selectedChoice\":\"Please select\"}",Feat.class);
            FeatSlot featSlot = new FeatSlot();
            featSlot.setType("General");
            featSlot.setFeat(feat);
            feats.add(featSlot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        abilityScores = new AbilityScores();
        saves = new Saves(abilityScores);
        skills = new Skills(abilityScores, skillsJSON);
        try {
            advancementTable = mapper.readValue(advTableJSON, new TypeReference<ArrayList<HashMap<String,String>>>() { });
            for (int i = 0; i < advancementTable.size() ; i++) {
                Log.i("ADVANCEMENT_TABLE", Arrays.toString(advancementTable.get(i).values().toArray()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            advancementTable = null;
        }
        ac = (abilityScores.get(AbilityID.DEX).getModifier()+10)+"";
        touch = ac+"";
        ff = 10+"";
        initiative = abilityScores.get(AbilityID.DEX).getModifier()+"";
        speed = "30 ft.";
        level = 0;
        maxHp = 0;
        hp = 0 ;
        this.hitDie = Integer.parseInt(advancementTable.get(0).get("hit_die"));
        levelUp();
    }

    // =================
    // DURING PLAY STUFF
    // =================

    //<editor-fold desc="HP stuff">
    public void updateHP(int newHP){
        hp = Math.min(maxHp,newHP);
    }

    public void updateHP(String newHP){
        updateHP(updateInt(hp, newHP));
    }

    public void healBySpell(int healthRegained){
        //TODO: Implement healBySpell
    }

    public void addNonLethalDamage(int nonLethalDamage){
        //TODO: Implement addNonLethalDamage
    }

    public void removeNonLethalDamage(){
        //TODO: Implement removeNonLethalDamage
    }
    //</editor-fold>

    //<editor-fold desc="AC stuff">
    public void updateAC(String newAC){
        ac = newAC;
    }

    public void updateTouch(String newTouch){
        touch = newTouch;
    }

    public void updateFF(String newFF){
        ff = newFF;
    }
    //</editor-fold>

    //<editor-fold desc="Save stuff">
    public void updateSave(SavingThrowID id, String val){
        int oldVal = saves.getSaves().get(id).getBase();
        saves.getSaves().get(id).setBase(updateInt(oldVal, val));
        saves.update(abilityScores);
    }
    //</editor-fold>

    //<editor-fold desc="Ability score stuff">

    public void updateAbility(AbilityID id, String val){
        int oldScore = abilityScores.get(id).getBase();
        int newScore = updateInt(oldScore, val);
        int modBefore = abilityScores.get(id).getModifier();
        abilityScores.get(id).setBase(newScore);
        abilityScores.update();
        int modAfter = abilityScores.get(id).getModifier();
        int modDiff = modAfter-modBefore;
        if(id==AbilityID.CON) {
            maxHp += modDiff*level;
            hp += modDiff*level;
        }
        if(id==AbilityID.DEX){
            updateAC(modDiff);
        }
        saves.update(abilityScores);
        skills.update(abilityScores);
    }

    public void updateAC(int modDiff){
        Log.i("DEX_CHANGE","DEX Changed");
        if(integerParsable(ac)){
            int oldAC = parseInt(ac);
            int newAC = oldAC+modDiff;
            Log.i("DEX_CHANGE", "Updating AC from " + oldAC + " to " + newAC);
            updateAC(newAC + "");
        }
        if(integerParsable(touch)){
            int oldTouch = parseInt(touch);
            int newTouch = oldTouch+modDiff;
            Log.i("DEX_CHANGE", "Updating Touch from " + oldTouch + " to " + newTouch);
            updateTouch(newTouch + "");
        }
        if(integerParsable(initiative)){
            int oldInit = parseInt(initiative);
            int newInit = oldInit+modDiff;
            Log.i("DEX_CHANGE", "Updating Initiative from " + oldInit + " to " + newInit);
            updateInitiative(newInit + "");
        }
    }
    //</editor-fold>

    //<editor-fold desc="Initiative stuff">

    public void updateInitiative(String newInit){
        initiative = newInit;
    }

    //</editor-fold>

    //<editor-fold desc="Posessions stuff">
    public void addMoney(){
        //TODO: Implement addMoney
    }

    public void removeMoney(){
        //TODO: Implement removeMoney
    }

    public void addItem(){
        //TODO: Implement addItem
    }

    public void removeItem(){
        //TODO: Implement removeItem
    }
    //</editor-fold>

    // ===============
    // SPECIAL ACTIONS
    // ===============

    public void rest(){
        nonlethalDamage = 0;
        updateHP(hp+level);

        spellSlots.refresh();
    }

    public void levelUp(){
        if(level >= 20) return;
        level++;
        HashMap<String,String> advTableRow = advancementTable.get(level);
        Log.i("LEVEL_UP","AdvTableRow keys: "+Arrays.toString(advTableRow.keySet().toArray()));
        BaB = advTableRow.get("base_attack_bonus");

        String fort_save = advTableRow.get("fort_save");
        String ref_save = advTableRow.get("ref_save");
        String will_save = advTableRow.get("will_save");

        SavingThrow fort = saves.getSaves().get(SavingThrowID.FORT);
        SavingThrow ref =  saves.getSaves().get(SavingThrowID.REF);
        SavingThrow will =  saves.getSaves().get(SavingThrowID.WILL);

        int newFortBase, newRefBase, newWillBase;
        DecimalFormat df = new DecimalFormat("+#;-#");
        try { newFortBase = df.parse(fort_save).intValue(); } catch (ParseException e) { newFortBase = fort.getBase(); Log.e("LEVEL_UP","Could not convert \""+fort_save+"\" to integer! (Fort)"); }
        try { newRefBase = df.parse(ref_save).intValue();   } catch (ParseException e) { newRefBase  = ref.getBase();  Log.e("LEVEL_UP","Could not convert \""+ref_save+"\" to integer! (Ref)"); }
        try { newWillBase = df.parse(will_save).intValue(); } catch (ParseException e) { newWillBase = will.getBase(); Log.e("LEVEL_UP", "Could not convert \"" + will_save + "\" to integer! (Will)"); }


        fort.setBase(newFortBase);
        ref.setBase(newRefBase);
        will.setBase(newWillBase);

        saves.update(abilityScores);

        int hdRoll = (level==1?hitDie:((int)(Math.random()*hitDie))+1);
        Log.i("LEVEL_UP","Rolled a "+hdRoll+" on a d"+hitDie+" hit die.");
        int extraHP = hdRoll+abilityScores.get(AbilityID.CON).getModifier();
        maxHp = maxHp + extraHP;
        updateHP(hp+extraHP);
    }

    public String toJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public static CharacterSheet fromJSON(String CharacterSheetJSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(CharacterSheetJSON, CharacterSheet.class);
    }

    // =================
    // UTILITY FUNCTIONS
    // =================

    private boolean integerParsable(String s){
        Log.i("PARSEINT","Checking if "+s+" can be parsed as int");
        DecimalFormat df = new DecimalFormat("+#;-#");
        try {
            int i=df.parse(s).intValue();
            Log.i("PARSEINT", "It can, and should parse to "+i);
            return true;
        } catch (ParseException pe) {
            try{
                int i = Integer.parseInt(s);
                Log.i("PARSEINT", "It can, and should parse to "+i);
                return true;
            } catch (NumberFormatException nfe) {
                Log.i("PARSEINT", "It can't");
                return false;
            }
        }
    }

    private Integer parseInt(String s){
        DecimalFormat df = new DecimalFormat("+#;-#");
        try {
            int i=df.parse(s).intValue();
            return i;
        } catch (ParseException pe) {
            try{
                int i = Integer.parseInt(s);
                return i;
            } catch (NumberFormatException nfe) {
                Log.i("PARSEINT", "Error parsing "+s,nfe);
                Log.i("PARSEINT", "Error parsing "+s,pe);
                return null;
            }
        }
    }

    // =====================
    // SYNCHRONIZATION STUFF
    // =====================

    /**
     * Functions for updating a specified field of the underlying character-sheet
     *
     */
    public void updateField(String id, String val){
        switch (id.toLowerCase()){
            case "hp":
                updateHP(val);
                return;
            case "ac":
                updateAC(val);
                return;
            case "touch":
                updateTouch(val);
                return;
            case "ff":
                updateFF(val);
                return;
            case "init":
                updateInitiative(val);
                return;
            case "fort":
                updateSave(SavingThrowID.FORT, val);
                return;
            case "ref":
                updateSave(SavingThrowID.REF, val);
                return;
            case "will":
                updateSave(SavingThrowID.WILL, val);
                return;
            case "str":
                updateAbility(AbilityID.STR, val);
                return;
            case "dex":
                updateAbility(AbilityID.DEX, val);
                return;
            case "con":
                updateAbility(AbilityID.CON, val);
                return;
            case "int":
                updateAbility(AbilityID.INT, val);
                return;
            case "wis":
                updateAbility(AbilityID.WIS, val);
                return;
            case "cha":
                updateAbility(AbilityID.CHA, val);
                return;
            case "speed":
                setSpeed(val);
                return;
            case "name":
                setName(val);
                return;
            case "race":
                setRace(val);
                return;
            case "align":
                setAlignment(val);
                return;
            case "gender":
                setGender(val);
                return;
            case "deity":
                setDeity(val);
                return;
            case "eyes":
                setEyes(val);
                return;
            case "hair":
                setHair(val);
                return;
            case "height":
                setHeight(val);
                return;
            case "weight":
                setWeight(val);
                return;
            case "size":
                setSize(val);
                return;
            case "skin":
                setSkin(val);
                return;
            default: Log.e("UPDATE_FIELD", "Unrecognized id: "+id);
        }
    }

    /**
     * Functions for updating a specified field of the underlying character-sheet
     *
     */
    @JsonIgnore
    public String getField(String id, String defaultVal){
        switch (id.toLowerCase()){
            case "hp":
                return (hp+tempHp)+"";
            case "ac":
                return ac;
            case "touch":
                return touch;
            case "ff":
                return ff;
            case "init":
                return initiative;
            case "bab":
                return BaB;
            case "fort":
                return saves.getSaves().get(SavingThrowID.FORT).getTotal()+"";
            case "ref":
                return saves.getSaves().get(SavingThrowID.REF).getTotal()+"";
            case "will":
                return saves.getSaves().get(SavingThrowID.WILL).getTotal()+"";
            case "str":
                return abilityScores.get(AbilityID.STR).getTotalValue()+"";
            case "dex":
                return abilityScores.get(AbilityID.DEX).getTotalValue()+"";
            case "con":
                return abilityScores.get(AbilityID.CON).getTotalValue()+"";
            case "int":
                return abilityScores.get(AbilityID.INT).getTotalValue()+"";
            case "wis":
                return abilityScores.get(AbilityID.WIS).getTotalValue()+"";
            case "cha":
                return abilityScores.get(AbilityID.CHA).getTotalValue()+"";
            case "strmod":
                return abilityScores.get(AbilityID.STR).getModifier()+"";
            case "dexmod":
                return abilityScores.get(AbilityID.DEX).getModifier() + "";
            case "conmod":
                return abilityScores.get(AbilityID.CON).getModifier() + "";
            case "intmod":
                return abilityScores.get(AbilityID.INT).getModifier()+"";
            case "wismod":
                return abilityScores.get(AbilityID.WIS).getModifier() + "";
            case "chamod":
                return abilityScores.get(AbilityID.CHA).getModifier() + "";
            case "speed":
                return speed;
            case "name":
                return name;
            case "race":
                return race;
            case "align":
                return alignment;
            case "gender":
                return gender;
            case "deity":
                return deity;
            case "eyes":
                return eyes;
            case "hair":
                return hair;
            case "height":
                return height;
            case "weight":
                return weight;
            case "size":
                return size;
            case "skin":
                return skin;
            default: Log.e("GET_FIELD", "Unrecognized id: "+id);
        }
        return defaultVal;
    }

    //<editor-fold desc="Getters and Setters">
    public String getBaB() {
        return BaB;
    }

    public void setBaB(String baB) {
        BaB = baB;
    }

    public int getHitDie() {
        return hitDie;
    }

    public void setHitDie(int hitDie) {
        this.hitDie = hitDie;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public ArrayList<HashMap<String, String>> getAdvancementTable() {
        return advancementTable;
    }

    public void setAdvancementTable(ArrayList<HashMap<String, String>> advancementTable) {
        this.advancementTable = advancementTable;
    }

    public SpellSlots getSpellSlots() {
        return spellSlots;
    }

    public void setSpellSlots(SpellSlots spellSlots) {
        this.spellSlots = spellSlots;
    }

    public SpellList getAvailableSpells() {
        return availableSpells;
    }

    public void setAvailableSpells(SpellList availableSpells) {
        this.availableSpells = availableSpells;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public FeatList getFeats() {
        return feats;
    }

    public void setFeats(FeatList feats) {
        this.feats = feats;
    }

    public Skills getSkills() {
        return skills;
    }

    public void setSkills(Skills skills) {
        this.skills = skills;
    }

    public Saves getSaves() {
        return saves;
    }

    public void setSaves(Saves saves) {
        this.saves = saves;
    }

    public AbilityScores getAbilityScores() {
        return abilityScores;
    }

    public void setAbilityScores(AbilityScores abilityScores) {
        this.abilityScores = abilityScores;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public String getInitiative() {
        return initiative;
    }

    public void setInitiative(String initiative) {
        this.initiative = initiative;
    }

    public int getTempHp() {
        return tempHp;
    }

    public void setTempHp(int tempHp) {
        this.tempHp = tempHp;
    }

    public int getNonlethalDamage() {
        return nonlethalDamage;
    }

    public void setNonlethalDamage(int nonlethalDamage) {
        this.nonlethalDamage = nonlethalDamage;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getTouch() {
        return touch;
    }

    public void setTouch(String touch) {
        this.touch = touch;
    }

    public String getFf() {
        return ff;
    }

    public void setFf(String ff) {
        this.ff = ff;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDeity() {
        return deity;
    }

    public void setDeity(String deity) {
        this.deity = deity;
    }

    public String getEyes() {
        return eyes;
    }

    public void setEyes(String eyes) {
        this.eyes = eyes;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    private int updateInt(int i, String s){
        try{
            i = Integer.parseInt(s);
            return i;
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        return i;
    }

    //</editor-fold>
}