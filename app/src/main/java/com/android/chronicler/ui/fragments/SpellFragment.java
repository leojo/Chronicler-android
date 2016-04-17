package com.android.chronicler.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.chronicler.R;
import com.android.chronicler.character.skill.Skills;
import com.android.chronicler.character.spell.Spell;
import com.android.chronicler.character.spell.SpellSlot;
import com.android.chronicler.character.spell.SpellSlots;
import com.android.chronicler.ui.SearchActivity;
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

    private SheetAdapter adapter;
    private SpellSlots spells;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.spell_fragment_layout, container, false);

        ListView spellsView = (ListView)(rootView.findViewById(R.id.spellsView));

        final SpellFragment thisFragment = this;

        // Set add button to footer
        ImageView addButtonView = new ImageView(getContext());
        addButtonView.setPadding(20, 20, 20, 20);
        addButtonView.setImageResource(R.drawable.ic_add_circle_24dp);
        spellsView.addFooterView(addButtonView);
        spells = (SpellSlots) getArguments().getSerializable("SPELLS");
        adapter = new SheetAdapter(getContext(), spells);
        spellsView.setAdapter(adapter);

        spellsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.i("Campaigns", "Position "+position+" of "+adapter.getCount());
                if (position == adapter.getCount()) {
                    // If we click the add-spell button, the SearchActivity will be
                    // started for result, which in turn will start a specific-spell overview
                    // activity for result and allow the user to select that spell.
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    intent.putExtra("TYPE", "spell");
                    thisFragment.startActivityForResult(intent, 1);

                } else {
                    return;
                }
            };
            // --------------------------------------
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Log.i("RESULT" ,"Fragment: result within the fragment");
        Log.i("RESULT", "Fragment: IS the data null? "+(data==null));
       /* String spellName = data.getStringExtra("toBeAdded");
        Log.i("RESULT", "We got our result! it's "+spellName);
        SpellSlot newSpellSlot = new SpellSlot();
        Spell newSpell = new Spell();
        newSpell.setName(spellName);
        newSpellSlot.setSpell(newSpell);
        spells.add(newSpellSlot);
        adapter.notifyDataSetChanged();*/
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
