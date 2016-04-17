package com.android.chronicler.ui.fragments;

import android.content.Context;
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
import android.widget.TextView;

import com.android.chronicler.R;
import com.android.chronicler.character.feat.Feat;
import com.android.chronicler.character.feat.FeatList;
import com.android.chronicler.character.feat.FeatSlot;
import com.android.chronicler.character.skill.Skills;
import com.android.chronicler.character.spell.Spell;
import com.android.chronicler.character.spell.SpellSlot;
import com.android.chronicler.character.spell.SpellSlots;
import com.android.chronicler.ui.CharacterActivity;
import com.android.chronicler.ui.FeatOverviewActivity;
import com.android.chronicler.ui.SearchActivity;
import com.android.chronicler.ui.SpellOverviewActivity;
import com.android.chronicler.util.SheetAdapter;
import com.android.chronicler.util.SkillsAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

/**
 * Fragment for the CharacterActivity: This is the character's list of feats.
 *
 * Created by andrea on 26.2.2016.
 */
public class FeatFragment extends SheetFragment {
    private SheetAdapter adapter;
    private FeatList feats;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.feat_fragment_layout, container, false);

        ListView featsView = (ListView)(rootView.findViewById(R.id.featListView));


        final FeatFragment thisFragment = this;

        // Set add button to footer
        ImageView addButtonView = new ImageView(getContext());
        addButtonView.setPadding(20, 20, 20, 20);
        addButtonView.setImageResource(R.drawable.ic_add_circle_24dp);
        featsView.addFooterView(addButtonView);

        feats = (FeatList)getArguments().getSerializable("FEATS");

        adapter = new SheetAdapter(getContext(), feats);
        featsView.setAdapter(adapter);

        featsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.i("Campaigns", "Position "+position+" of "+adapter.getCount());
                if (position == adapter.getCount()) {
                    Intent intent = new Intent(thisFragment.getContext(), FeatOverviewActivity.class);
                    Log.i("RESULT", "SpellFragment is starting the SpellOverviewActivity for result");
                    thisFragment.startActivityForResult(intent, 1);

                } else {
                    return;
                }
            };
            // --------------------------------------
        });

        featsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Activate popup when an invite is clicked
                showPopup(view);

                return false;
            }
        });

        return rootView;
    }



    // Pop-up for accepting or declining invites: Will later be replaced with buttons
    // nested inside the list elements for accepting and declining.
    public void showPopup(View v) {
        final FeatFragment thisFragment = this;
        PopupMenu popup = new PopupMenu(thisFragment.getContext(), v);
        popup.inflate(R.menu.menu_feat_options);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch ((String) item.getTitle()) {
                    case "Overview":
                        Log.d("FEATS", "Should open overview for spell");
                        Intent intent = new Intent(thisFragment.getContext(), FeatOverviewActivity.class);
                        intent.putExtra("StartedForResult", false);
                        startActivity(intent);
                        break;
                    case "Delete":
                        Log.d("FEATS", "Should delete this spell");
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

        String featName = data.getStringExtra("toBeAdded");

        FeatSlot newFeatSlot = new FeatSlot();
        Feat newFeat = new Feat();
        newFeat.setName(featName);
        newFeatSlot.setFeat(newFeat);
        feats.add(newFeatSlot);
        adapter.clearAndAddAll(feats);
        adapter.notifyDataSetChanged();
    }


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
