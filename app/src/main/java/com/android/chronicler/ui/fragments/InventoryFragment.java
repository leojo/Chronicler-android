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
import com.android.chronicler.character.feat.FeatList;
import com.android.chronicler.character.item.ArmorShield;
import com.android.chronicler.character.item.Inventory;
import com.android.chronicler.character.item.Item;
import com.android.chronicler.character.spell.Spell;
import com.android.chronicler.character.spell.SpellSlot;
import com.android.chronicler.character.spell.SpellSlots;
import com.android.chronicler.ui.FeatOverviewActivity;
import com.android.chronicler.ui.ItemOverviewActivity;
import com.android.chronicler.ui.SearchActivity;
import com.android.chronicler.ui.SpellOverviewActivity;
import com.android.chronicler.util.SheetAdapter;

import java.util.ArrayList;

/**
 * Fragment for the CharacterActivity: This is the character's inventory. It will list both
 * items in the character's backpack and equipped items.
 *
 * Created by andrea on 4.3.2016.
 */
public class InventoryFragment extends SheetFragment {
    private SheetAdapter adapter;
    private Inventory items;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.inventory_fragment_layout, container, false);

        ListView invtView = (ListView)(rootView.findViewById(R.id.invtView));


        final InventoryFragment thisFragment = this;


        // Set add button to footer
        ImageView addButtonView = new ImageView(getContext());
        addButtonView.setPadding(20, 20, 20, 20);
        addButtonView.setImageResource(R.drawable.ic_add_circle_24dp);
        invtView.addFooterView(addButtonView);

        items =  (Inventory)getArguments().getSerializable("INVENTORY");

        adapter = new SheetAdapter(getContext(),items);
        invtView.setAdapter(adapter);

        invtView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.i("Campaigns", "Position "+position+" of "+adapter.getCount());
                if (position == adapter.getCount()) {
                    Intent intent = new Intent(thisFragment.getContext(), ItemOverviewActivity.class);
                    Log.i("RESULT", "SpellFragment is starting the SpellOverviewActivity for result");
                    thisFragment.startActivityForResult(intent, 1);

                } else {
                    return;
                }
            };
            // --------------------------------------
        });


        invtView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
        final InventoryFragment thisFragment = this;
        PopupMenu popup = new PopupMenu(thisFragment.getContext(), v);
        popup.inflate(R.menu.menu_feat_options);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch ((String) item.getTitle()) {
                    case "Overview":
                        Log.d("ITEMS", "Should open overview for spell");
                        Intent intent = new Intent(thisFragment.getContext(), ItemOverviewActivity.class);
                        intent.putExtra("StartedForResult", false);
                        startActivity(intent);
                        break;
                    case "Delete":
                        Log.d("ITEMS", "Should delete this spell");
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

        ArrayList<Item> p = (items.getItems());

        String itemName = data.getStringExtra("toBeAdded");
        Item newItem = new ArmorShield();
        newItem.setName(itemName);
        items.add(newItem);
        adapter.clearAndAddAll(items);
        adapter.notifyDataSetChanged();
    }

    // newInstance is called when the CharacterActivity is started and the fragments get
    // created. Here is where we would put our arguments specific to that fragment (say, a list of spells)
    // as arguments for this function.
    public static InventoryFragment newInstance(String type, Inventory invt) {
        Bundle args = new Bundle();
        args.putString("ID", type);
        args.putSerializable("INVENTORY", invt);
        InventoryFragment invFrag = new InventoryFragment();
        invFrag.setArguments(args);

        return invFrag;
    }
}
