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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;

/**
 * Created by leo on 8.2.2016.
 *
 * A class representing a list of skills. Unique to a character.
 */
public class Skills  implements Serializable {
    private HashMap<String, Skill> skills;

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
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        update(abilityScores);
    }

    public void update(AbilityScores abilityScores){
        for(Skill s : skills.values()){
            // speak language isn't really a skill, but it costs skill points to learn new languages so there...
            if(!s.getName().equalsIgnoreCase("speak language")) s.update(abilityScores);
        }
    }

    @JsonIgnore
    public Skill get(String skillName){
        if(!skills.containsKey(skillName)) throw new IllegalArgumentException("Skill \""+skillName+"\" not found. Check spelling.");
        return skills.get(skillName);
    }

    //Returns false if skillName is not valid.
    public boolean train(String skillName){
        // These skills require some further descriptor f.x. Knowledge(Arcana) is a valid skill, whereas Knowledge is not actually a skill.
        String[] parametrizableSkills = {"Craft","Knowledge","Perform","Profession","Speak Language"};
        for(String s: parametrizableSkills){
            // if the skill should be parametrized but isn't, then it's not valid.
            if(skillName.equalsIgnoreCase(s)) return false;
        }
        if(!skills.containsKey(skillName)){
            for(String s : parametrizableSkills){
                if(skillName.startsWith(s)){
                    // The skill is a parametrized version of a parametrizable skill.
                    Skill baseSkill = skills.get(s);
                    Skill newSkill = Skill.copy(baseSkill);
                    skills.put(skillName,newSkill);
                    return true;
                }
            }
            // The skill was not a valid skill
            return false;
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
}
