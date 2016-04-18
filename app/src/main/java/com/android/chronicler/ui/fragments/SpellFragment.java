package com.android.chronicler.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.android.chronicler.R;
import com.android.chronicler.character.SheetObject;
import com.android.chronicler.character.spell.SpellSlot;
import com.android.chronicler.character.spell.SpellSlots;
import com.android.chronicler.ui.SearchActivity;
import com.android.chronicler.ui.SheetObjectOverviewActivity;
import com.android.chronicler.util.SheetAdapter;

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

        //spellsView.getChildAt(spellsView.getChildCount()-1).setBackgroundColor(Color.WHITE);
        spellsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == adapter.getCount()) {
                    // If we click the add-spell button, the SearchActivity will be
                    // started for result, which in turn will start a specific-spell overview
                    // activity for result and allow the user to select that spell.
                    Intent intent = new Intent(thisFragment.getContext(), SheetObjectOverviewActivity.class);
                    intent.putExtra("TYPE","spell");
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

                showPopup(view, spells.getSpellSlots().get(position), position);


                return false;
            }
        });

        return rootView;
    }

    private void setAvailability(int position, boolean available){
        spellAvailability.set(position, available);
        spells.getSpellSlots().get(position).setAvailable(available);
    }

    // Pop-up for accepting or declining invites: Will later be replaced with buttons
    // nested inside the list elements for accepting and declining.
    public void showPopup(View v, final SheetObject sheetObject, final int position) {
        final SpellFragment thisFragment = this;
        PopupMenu popup = new PopupMenu(thisFragment.getContext(), v);
        popup.inflate(R.menu.menu_spell_options);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch ((String) item.getTitle()) {
                    case "Overview":
                        Intent intent = new Intent(thisFragment.getContext(), SheetObjectOverviewActivity.class);
                        intent.putExtra("TYPE","spell");
                        intent.putExtra("StartedForResult", false);
                        intent.putExtra(SearchActivity.SHEET_OBJECT, sheetObject);
                        startActivity(intent);
                        break;
                    case "Delete":
                        adapter.remove(position);
                        adapter.notifyDataSetChanged();
                        spells.getSpellSlots().remove(position);
                        break;
                    default:
                        Log.d("PopupMenu", "This should not happen");
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0) return;

        ArrayList<SpellSlot> p = (spells.getSpellSlots());

        SpellSlot spellSlot = (SpellSlot)data.getSerializableExtra(SearchActivity.SHEET_OBJECT);
        spellAvailability.add(true);
        SheetAdapter.searching = false;
        spells.add(spellSlot);
        adapter.clearAndAddAll(spells);
        adapter.notifyDataSetChanged();
        ArrayList<SpellSlot> s = (spells.getSpellSlots());

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
