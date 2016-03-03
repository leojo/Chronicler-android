package com.android.chronicler.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.chronicler.R;

/**
 * Created by andrea on 26.2.2016.
 */
public class CombatFragment extends SheetFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.combat_fragment_layout, container, false);


        return rootView;
    }

    // newInstance is called when the CharacterActivity is started and the fragments get
    // created. Here is where we would put our arguments specific to that fragment (say, a list of spells)
    // as arguments for this function.
    public static CombatFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("ID", type);
        CombatFragment combatFrag = new CombatFragment();
        combatFrag.setArguments(args);

        return combatFrag;
    }
}
