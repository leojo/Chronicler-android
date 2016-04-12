package com.android.chronicler.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.chronicler.R;
import com.android.chronicler.character.feat.FeatList;
import com.android.chronicler.character.item.Inventory;
import com.android.chronicler.util.SheetAdapter;

/**
 * Fragment for the CharacterActivity: This is the character's inventory. It will list both
 * items in the character's backpack and equipped items.
 *
 * Created by andrea on 4.3.2016.
 */
public class InventoryFragment extends SheetFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.inventory_fragment_layout, container, false);

        ListView invtView = (ListView)(rootView.findViewById(R.id.invtView));
        // Set add button to footer
        ImageView addButtonView = new ImageView(getContext());
        addButtonView.setPadding(20, 20, 20, 20);
        addButtonView.setImageResource(R.drawable.ic_add_circle_24dp);
        invtView.addFooterView(addButtonView);
        invtView.setAdapter(new SheetAdapter(getContext(), (Inventory)getArguments().getSerializable("INVENTORY")));



        return rootView;
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
