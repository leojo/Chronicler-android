package com.android.chronicler.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.chronicler.R;

/**
 * Fragment for the CharacterActivity: This is the screen that includes all information about
 * the character that doesn't fall into any of the other categories.
 *
 * Created by andrea on 26.2.2016.
 */
public class AboutFragment extends SheetFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.about_fragment_layout, container, false);
        return rootView;
    }

    // newInstance is called when the CharacterActivity is started and the fragments get
    // created. Here is where we would put our arguments specific to that fragment (say, a list of spells)
    // as arguments for this function.
    public static AboutFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("ID", type);
        AboutFragment aboutFrag = new AboutFragment();
        aboutFrag.setArguments(args);

        return aboutFrag;
    }
}
