package com.android.chronicler.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.chronicler.character.skill.Skill;
import com.android.chronicler.character.skill.Skills;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by leo on 12.2.2016.
 *
 * An adapter for displaying a list of skills.
 */
public class SkillsAdapter extends BaseAdapter {

    private final HashMap<String,Skill> skills;
    private final String[] skillNames;
    private final LayoutInflater inflater;
    private final Context context;

    public SkillsAdapter(Context context ,Skills skills){
        this.skills = skills.getSkills();
        this.context = context;
        skillNames = this.skills.keySet().toArray(new String[this.skills.size()]);
        Arrays.sort(skillNames);
        Log.d("SKILLS_ADAPTER",Arrays.toString(skillNames));
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return skills.size();
    }

    @Override
    public Object getItem(int position) {
        return skills.get(skillNames[position]);
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public View getView(int position, View item, ViewGroup parent) {
        Log.i("SKILLS_ADAPTER","Adapter getting view...");
        if(item != null) return item;

        String name = skillNames[position];
        int mod = skills.get(name).getMod();
        int rank = skills.get(name).getRanks();
        int misc = skills.get(name).getMisc();
        int total = skills.get(name).getTotalValue();

        final SkillListItemView sliv = new SkillListItemView(context,name);
        sliv.setMod(mod);
        sliv.setRank(rank);
        sliv.setMisc(misc);
        sliv.setTotal(total);

        sliv.setRankViewOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    sliv.recalculate();
                    Log.d("SKILL_STORE", "Setting ranks of " + sliv.getName() + " to " + sliv.getRank());
                    skills.get(sliv.getName()).setRanks(sliv.getRank());
                    skills.get(sliv.getName()).update();
                }
            }
        });

        sliv.setMiscViewOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    sliv.recalculate();
                    Log.d("SKILL_STORE", "Setting misc of " + sliv.getName() + " to " + sliv.getMisc());
                    skills.get(sliv.getName()).setMisc(sliv.getMisc());
                    skills.get(sliv.getName()).update();
                }
            }
        });
        return sliv;
    }
}
