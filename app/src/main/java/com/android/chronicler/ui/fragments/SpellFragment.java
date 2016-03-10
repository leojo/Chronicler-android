package com.android.chronicler.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.chronicler.R;

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

        return rootView;
    }

    // newInstance is called when the CharacterActivity is started and the fragments get
    // created. Here is where we would put our arguments specific to that fragment (say, a list of spells)
    // as arguments for this function.
    public static SpellFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("ID", type);
        SpellFragment spellFrag = new SpellFragment();
        spellFrag.setArguments(args);

        return spellFrag;
    }
}
