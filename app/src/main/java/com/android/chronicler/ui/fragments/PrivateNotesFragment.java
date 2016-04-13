package com.android.chronicler.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.chronicler.R;

import java.util.ArrayList;

/**
 * A simple {@link SheetFragment} subclass.
 */
public class PrivateNotesFragment extends SheetFragment {
    private static final String PRIVATE_NOTES = "PRIVATE_NOTES";
    private static final String CAMPAIGN_NAME = "CAMPAIGN_NAME";

    private static final int maxItemLength = 35;
    private String campaignName;
    private ArrayList<String> privateNotes;
    private ListAdapter adapter;

    public PrivateNotesFragment() {
        // Required empty public constructor
    }

    public static PrivateNotesFragment newInstance(String campaignName,
                                                      ArrayList<String> notes) {
        PrivateNotesFragment fragment = new PrivateNotesFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(PRIVATE_NOTES, notes);
        args.putString(CAMPAIGN_NAME, campaignName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            privateNotes = getArguments().getStringArrayList(PRIVATE_NOTES);
            campaignName = getArguments().getString(CAMPAIGN_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_campaign_players, container, false);

        ArrayList<String> shortNotes = new ArrayList<>();
        for (String privateNote : privateNotes) {
            shortNotes.add(privateNote.substring(0, maxItemLength).concat("..."));
        }

        ListView playerListView = (ListView) rootView.findViewById(R.id.player_list);
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_expandable_list_item_1,
                shortNotes);


        // Set add button to footer
        ImageView addButtonView = new ImageView(getActivity());
        addButtonView.setPadding(20, 20, 20, 20);
        addButtonView.setImageResource(R.drawable.ic_add_circle_24dp);

        playerListView.addFooterView(addButtonView);
        playerListView.setAdapter(adapter);

        return rootView;
    }

}
