package com.android.chronicler.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.chronicler.R;
import com.android.chronicler.character.feat.FeatList;
import com.android.chronicler.character.skill.Skills;
import com.android.chronicler.util.SheetAdapter;
import com.android.chronicler.util.SkillsAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Fragment for the CharacterActivity: This is the character's list of feats.
 *
 * Created by andrea on 26.2.2016.
 */
public class FeatFragment extends SheetFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.feat_fragment_layout, container, false);

        ListView featsView = (ListView)(rootView.findViewById(R.id.featListView));
        // Set add button to footer
        ImageView addButtonView = new ImageView(getContext());
        addButtonView.setPadding(20, 20, 20, 20);
        addButtonView.setImageResource(R.drawable.ic_add_circle_24dp);
        featsView.addFooterView(addButtonView);


        featsView.setAdapter(new SheetAdapter(getContext(), (FeatList)getArguments().getSerializable("FEATS")));

        return rootView;
    }

    ListView skillsView;
    private SkillsAdapter adapter;

    // newInstance is called when the CharacterActivity is started and the fragments get
    // created. Here is where we would put our arguments specific to that fragment (say, a list of spells)
    // as arguments for this function.
    public static FeatFragment newInstance(String type, FeatList feats) {
        Bundle args = new Bundle();
        args.putString("ID", type);
        args.putSerializable("FEATS", feats);
        FeatFragment featFrag = new FeatFragment();
        featFrag.setArguments(args);

        return featFrag;
    }
}
