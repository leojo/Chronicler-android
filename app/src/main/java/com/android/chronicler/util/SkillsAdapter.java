package com.android.chronicler.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.chronicler.R;
import com.android.chronicler.character.skill.Skill;
import com.android.chronicler.character.skill.Skills;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by leo on 12.2.2016.
 */
public class SkillsAdapter extends BaseAdapter {

    private HashMap<String,Skill> skills;
    private String[] skillNames;
    private final LayoutInflater inflater;

    public SkillsAdapter(Context context ,Skills skills){
        this.skills = skills.getSkills();
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
        String key = skillNames[position];
        int mod = skills.get(key).getTotalValue();
        String value = "Bonus: "+(mod>=0?"+":"")+mod;

        if(item == null)
            item = inflater.inflate(R.layout.skill_list_item, null);


        TextView nameView = (TextView)item.findViewById(R.id.skillName);
        nameView.setText(key+"  -  "+value);

        return item;
    }
}
