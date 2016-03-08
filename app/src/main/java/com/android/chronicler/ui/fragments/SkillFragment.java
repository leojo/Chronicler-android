package com.android.chronicler.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.chronicler.R;
import com.android.chronicler.character.skill.Skills;
import com.android.chronicler.util.SkillsAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by andrea on 26.2.2016.
 */
public class SkillFragment extends SheetFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("SKILLFRAG","Created");
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.skill_fragment_layout, container, false);
        //if(rootView.findViewById(R.id.tabID) == null) Log.d("SKILLFRAG", "tabID view is null");
        //if(getArguments().getString("ID") == null) Log.d("SKILLFRAG","ID is null");
        // This is where we should input stuff, by using getArguments().get....
        //((TextView)rootView.findViewById(R.id.tabID)).setText(getArguments().getString("ID"));

        skillsView = (ListView)(rootView.findViewById(R.id.skillsView));
        skillsView.setAdapter(new SkillsAdapter(getContext(), (Skills)getArguments().getSerializable("SKILLS")));

        try {
            Log.d("SKILLFRAG", new ObjectMapper().writeValueAsString((Skills)getArguments().getSerializable("SKILLS")));
        } catch (JsonProcessingException e) {
            Log.d("SKILLFRAG","Failed to write skills");
        }

        return rootView;
    }


    ListView skillsView;
    private SkillsAdapter adapter;

    // newInstance is called when the CharacterActivity is started and the fragments get
    // created. Here is where we would put our arguments specific to that fragment (say, a list of spells)
    // as arguments for this function.
    public static SkillFragment newInstance(String type, Skills skills) {
        Bundle args = new Bundle();
        args.putString("ID", type);
        args.putSerializable("SKILLS", skills);
        SkillFragment sheetFrag = new SkillFragment();
        sheetFrag.setArguments(args);

        return sheetFrag;
    }
}