package com.android.chronicler.character.skill;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import com.android.chronicler.character.ability.AbilityScores;
import com.android.chronicler.util.ChroniclerRestClient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by leo on 8.2.2016.
 */
public class Skills {
    private HashMap<String, Skill> skills;
    private boolean loaded = false;
    private boolean usable = false;

    public Skills(){ /* Empty constructor for JSON */ }

    public Skills(AbilityScores abilityScores){
        skills = new HashMap<>();
        final AbilityScores finalAbilityScores = new AbilityScores();
        finalAbilityScores.setAbilityScores(abilityScores.getAbilityScores());
        ChroniclerRestClient cli = new ChroniclerRestClient();
        cli.get("/skillData", null, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ObjectMapper mapper = new ObjectMapper();
                String skillData = new String(responseBody);

                String[] responseArray = skillData.split(":");
                for(String s : responseArray) Log.i("JSON", s);
                /*Log.d("SKILLS_JSON","response size: "+responseBody.length);
                try {
                    TypeReference<HashMap<String,Skill>> typeRef = new TypeReference<HashMap<String, Skill>>() {};
                    Log.d("SKILLS_JSON",skillData);
                    skills = mapper.readValue(skillData, typeRef);
                    loaded = true;
                    update(finalAbilityScores);
                } catch (IOException e) {
                    Log.e("SKILLS", e.getMessage());
                } catch (SkillsException e) {
                    Log.e("SKILLS", e.getMessage());
                }
*/
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("SKILLS", "Failure fetching skill data: " + new String(responseBody));
            }
        });
    }

    public void update(AbilityScores abilityScores) throws SkillsException{
        if(!loaded)throw new SkillsException();
        for(Skill s : skills.values()){
            s.update(abilityScores);
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
