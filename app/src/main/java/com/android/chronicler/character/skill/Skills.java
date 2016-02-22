package com.android.chronicler.character.skill;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import com.android.chronicler.character.ability.AbilityScores;
import com.android.chronicler.util.ChroniclerRestClient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;

/**
 * Created by leo on 8.2.2016.
 */
public class Skills  implements Serializable {
    private HashMap<String, Skill> skills;
    private boolean loaded = false;
    private boolean usable = false;

    public Skills(){ /* Empty constructor for JSON */ }

    public Skills(AbilityScores abilityScores, String skillsJSON){
        skills = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
        JSONObject responseObject = new JSONObject(skillsJSON);
        Iterator<String> keys = responseObject.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                String skillString = responseObject.getString(key);
                Log.d("SKILL_JSON", skillString);
                Skill s = mapper.readValue(skillString, Skill.class);
                skills.put(key, s);
            }
        } catch(Exception e) {e.printStackTrace();}
        loaded = true;

        final AbilityScores finalAbilityScores = new AbilityScores();
        finalAbilityScores.setAbilityScores(abilityScores.getAbilityScores());

        try {
            update(finalAbilityScores);
        } catch(Exception e) {e.printStackTrace();}
    }

    public void update(AbilityScores abilityScores) throws SkillsException{
        if(!loaded)throw new SkillsException();
        for(Skill s : skills.values()){
            if(!s.getName().equalsIgnoreCase("speak language")) s.update(abilityScores);
        }
        usable = true;
    }

    @JsonIgnore
    public Skill get(String skillName) throws SkillsException{
        if(!usable) throw new SkillsException();
        if(!skills.containsKey(skillName)) throw new IllegalArgumentException("Skill \""+skillName+"\" not found. Check spelling.");
        return skills.get(skillName);
    }

    //Returns false if skillName is not valid.
    public boolean train(String skillName) throws SkillsException{
        if(!usable) throw new SkillsException();
        if(!skills.containsKey(skillName)){
            String[] parametrizableSkills = {"Craft","Knowledge","Perform","Profession","Speak Language"};
            for(String s : parametrizableSkills){
                if(skillName.startsWith(s)){
                    Skill baseSkill = skills.get(s);
                    Skill newSkill = Skill.copy(baseSkill);
                    skills.put(skillName,newSkill);
                    break;
                }
            }
        }
        skills.get(skillName).incrementRanks();
        return true;
    }

    //<editor-fold desc="Getters and Setters">

    public HashMap<String, Skill> getSkills() {
        return skills;
    }

    public void setSkills(HashMap<String, Skill> skills) {
        this.skills = skills;
    }

    //</editor-fold>

    public class SkillsException extends Exception{
        public SkillsException(){
            super((loaded?"Skills need to be updated with abilityScores before use.":"Skills were not loaded succesfully."));
        }

        public SkillsException(String message){
            super(message);
        }
    }
}
