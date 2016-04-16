package com.android.chronicler.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.android.chronicler.R;
import com.android.chronicler.character.CharacterSheet;
import com.android.chronicler.ui.CharacterActivity;

import java.util.Arrays;
import java.util.List;

/**
 * Created by andrea on 16.4.2016.
 */
public class MiscFragment extends SheetFragment {
    private static CharacterSheet cs;
    private static CombatFragment goToFrag;
    private static final List<String> abilityFields = Arrays.asList(new String[]{"str", "dex", "con", "int", "wis", "cha"});


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.misc_fragment_layout, container, false);

        Button restBtn = (Button)rootView.findViewById(R.id.restBtn);
        restBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cs.rest();
                ((CharacterActivity)getActivity()).getViewPager().setCurrentItem(1);
                return;
            }
        });
        Button lvlBtn = (Button)rootView.findViewById(R.id.lvlBtn);
        lvlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cs.levelUp();
                ((CharacterActivity)getActivity()).getViewPager().setCurrentItem(1);
                return;
            }
        });

        Button classTblBtn = (Button)rootView.findViewById(R.id.classTblBtn);
        classTblBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Should start an overview activity for the class table on click.
                // Need to create the class table activity first!
                // Intent intent = new Intent(getContext(), ClassTableActivity.class);
                // getContext().startActivity(intent);
            }
        });


        return rootView;
    }

    // newInstance is called when the CharacterActivity is started and the fragments get
    // created. Here is where we would put our arguments specific to that fragment (say, a list of spells)
    // as arguments for this function.
    public static MiscFragment newInstance(String type, CharacterSheet characterSheet, CombatFragment combatFrag) {
        cs = characterSheet;
        goToFrag = combatFrag;
        Bundle args = new Bundle();
        args.putString("ID", type);
        MiscFragment miscFrag = new MiscFragment();
        miscFrag.setArguments(args);
        return miscFrag;
    }
}
