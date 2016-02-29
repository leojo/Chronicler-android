package com.android.chronicler.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.chronicler.R;

/**
 * Created by andrea on 23.2.2016.
 * A fragment class for character sheet fragments.
 * The character sheet will have (at least) 6 fragments to shuffle through:
 *      Combat
 *      Spells
 *      Feats
 *      Skills
 *      Inventory
 *      About
 */
public class SheetFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sheet_fragment_layout, container, false);

        ((TextView)rootView.findViewById(R.id.tabID)).setText(getArguments().getString("ID"));

        return rootView;
    }

    // newInstance is called when the CharacterActivity is started and the fragments get
    // created. Here is where we would put our arguments specific to that fragment (say, a list of spells)
    // as arguments for this function.
    public static SheetFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("ID", type);
        SheetFragment sheetFrag = new SheetFragment();
        sheetFrag.setArguments(args);

        return sheetFrag;
    }

    public String getName() {
        return getArguments().getString("ID");
    }
}
