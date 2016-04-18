package com.android.chronicler.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.chronicler.R;
import com.android.chronicler.character.SheetObject;
import com.android.chronicler.character.skill.Skills;
import com.android.chronicler.character.spell.Spell;
import com.android.chronicler.character.spell.SpellSlot;
import com.android.chronicler.character.spell.SpellSlots;
import com.android.chronicler.ui.SearchActivity;
import com.android.chronicler.ui.SpellOverviewActivity;
import com.android.chronicler.util.DataLoader;
import com.android.chronicler.util.SheetAdapter;
import com.android.chronicler.util.SkillsAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

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
    private ArrayList<Boolean> spellAvailability;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.spell_fragment_layout, container, false);

        final ListView spellsView = (ListView)(rootView.findViewById(R.id.spellsView));

        final SpellFragment thisFragment = this;

        // Set add button to footer
        ImageView addButtonView = new ImageView(getContext());
        addButtonView.setPadding(20, 20, 20, 20);
        addButtonView.setImageResource(R.drawable.ic_add_circle_24dp);
        spellsView.addFooterView(addButtonView);

        spells = (SpellSlots) getArguments().getSerializable("SPELLS");
        spellAvailability = new ArrayList<>();

        for(int i = 0; i < spells.getSpellSlots().size(); i++){
            boolean availbable = spells.getSpellSlots().get(i).isAvailable();
            spellAvailability.add(availbable);
        }

        adapter = new SheetAdapter(getContext(), spells);
        spellsView.setAdapter(adapter);
        spellsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.i("Campaigns", "Position " + position + " of " + adapter.getCount());
                if (position == adapter.getCount()) {
                    // If we click the add-spell button, the SearchActivity will be
                    // started for result, which in turn will start a specific-spell overview
                    // activity for result and allow the user to select that spell.
                    Intent intent = new Intent(thisFragment.getContext(), SpellOverviewActivity.class);
                    Log.i("RESULT", "SpellFragment is starting the SpellOverviewActivity for result");
                    thisFragment.startActivityForResult(intent, 1);

                } else {
                    if (spellAvailability.get(position)) {
                        view.setBackgroundResource(R.drawable.spell_spent);
                        setAvailability(position, false);
                    } else {
                        view.setBackgroundResource(R.drawable.spell_ready);
                        setAvailability(position, true);
                    }

                    return;
                }
            }

            ;
            // --------------------------------------
        });

        spellsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Activate popup when an invite is clicked
                showPopup(view, spells.getSpellSlots().get(position));

                return false;
            }
        });

        Log.i("SPELLS","spellsView child count is "+spellsView.getChildCount());
        return rootView;
    }

    private void setAvailability(int position, boolean available){
        spellAvailability.set(position, available);
        spells.getSpellSlots().get(position).setAvailable(available);
    }

    // Pop-up for accepting or declining invites: Will later be replaced with buttons
    // nested inside the list elements for accepting and declining.
    public void showPopup(View v, final SheetObject sheetObject) {
        final SpellFragment thisFragment = this;
        PopupMenu popup = new PopupMenu(thisFragment.getContext(), v);
        popup.inflate(R.menu.menu_spell_options);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch ((String) item.getTitle()) {
                    case "Overview":
                        Log.d("SPELLS", "Should open overview for spell");
                        Intent intent = new Intent(thisFragment.getContext(), SpellOverviewActivity.class);
                        intent.putExtra("StartedForResult", false);
                        intent.putExtra(SearchActivity.SHEET_OBJECT, sheetObject);
                        startActivity(intent);
                        break;
                    case "Delete":
                        Log.d("SPELLS", "Should delete this spell");
                        break;
                    default:
                        Log.i("PopupMenu", "This is weird");
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0) return;

        //super.onActivityResult(requestCode, resultCode, data);
        Log.i("RESULT" ,"Fragment: result within the fragment");
        Log.i("RESULT", "Fragment: IS the data null? "+(data==null));

        ArrayList<SpellSlot> p = (spells.getSpellSlots());

        Log.i("RESULT", "Before result, our spells vector is "+p.toString());
        SpellSlot spellSlot = (SpellSlot)data.getSerializableExtra(SearchActivity.SHEET_OBJECT);
        Log.i("RESULT", "We got our result! it's " + spellSlot.getName());
        spellAvailability.add(true);
        spells.add(spellSlot);
        adapter.clearAndAddAll(spells);
        adapter.notifyDataSetChanged();
        ArrayList<SpellSlot> s = (spells.getSpellSlots());


        Log.i("RESULT", "After result, our spells vector is "+s.toString());
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
