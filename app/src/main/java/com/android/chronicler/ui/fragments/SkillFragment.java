package com.android.chronicler.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.chronicler.R;
import com.android.chronicler.character.skill.Skills;
import com.android.chronicler.util.SkillsAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Fragment for the CharacterActivity: This is the character's skill list.
 *
 *
 * Created by andrea on 26.2.2016.
 */
public class SkillFragment extends SheetFragment {

    private static Skills skills;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.skill_fragment_layout, container, false);

        ListView skillsView = (ListView)(rootView.findViewById(R.id.skillsView));
        skillsView.setAdapter(new SkillsAdapter(getContext(), skills));

        return rootView;
    }

    // newInstance is called when the CharacterActivity is started and the fragments get
    // created. Here is where we would put our arguments specific to that fragment (say, a list of spells)
    // as arguments for this function.
    public static SkillFragment newInstance(String type, Skills s) {
        Bundle args = new Bundle();
        args.putString("ID", type);
        skills = s;
        SkillFragment sheetFrag = new SkillFragment();
        sheetFrag.setArguments(args);

        return sheetFrag;
    }
}
