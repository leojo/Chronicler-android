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
import com.android.chronicler.character.skill.Skills;
import com.android.chronicler.character.spell.SpellSlots;
import com.android.chronicler.util.SheetAdapter;
import com.android.chronicler.util.SkillsAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Fragment for the CharacterActivity: This is the character's spell list. The character should
 * be able to populate spell slots with spells he knows and then cast them so the spell slot
 * will show as 'spent'. He can then prep a spell again if he rests (or otherwise has permission
 * to prep a spell)
 *
 * Created by andrea on 26.2.2016.
 */
public class SpellFragment extends SheetFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.spell_fragment_layout, container, false);

        ListView spellsView = (ListView)(rootView.findViewById(R.id.spellsView));
        // Set add button to footer
        ImageView addButtonView = new ImageView(getContext());
        addButtonView.setPadding(20, 20, 20, 20);
        addButtonView.setImageResource(R.drawable.ic_add_circle_24dp);
        spellsView.addFooterView(addButtonView);
        spellsView.setAdapter(new SheetAdapter(getContext(), (SpellSlots) getArguments().getSerializable("SPELLS")));

        return rootView;
    }

    // newInstance is called when the CharacterActivity is started and the fragments get
    // created. Here is where we would put our arguments specific to that fragment (say, a list of spells)
    // as arguments for this function.
    public static SpellFragment newInstance(String type, SpellSlots spellslots) {
        Bundle args = new Bundle();
        args.putString("ID", type);
        args.putSerializable("SPELLS", spellslots);
        SpellFragment spellFrag = new SpellFragment();
        spellFrag.setArguments(args);

        return spellFrag;
    }
}
