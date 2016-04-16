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
 *
 * An adapter for displaying a list of skills.
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
        //if(skills.get(key).get)
        int mod = skills.get(key).getMod();
        int rank = skills.get(key).getRanks();
        int total = skills.get(key).getTotalValue();
        int misc = total-mod-rank;

        if(item == null) item = inflater.inflate(R.layout.skill_list_item, null);


        TextView nameView = (TextView)item.findViewById(R.id.skillName);
        TextView modView = (TextView)item.findViewById(R.id.skillMod);
        TextView rankView = (TextView)item.findViewById(R.id.skillRank);
        TextView miscView = (TextView)item.findViewById(R.id.skillMisc);
        TextView totalView = (TextView)item.findViewById(R.id.skillTotal);
        nameView.setText(key);
        modView.setText(mod+"");
        rankView.setText(rank+"");
        miscView.setText(misc+"");
        totalView.setText(total+"");

        return item;
    }
}
