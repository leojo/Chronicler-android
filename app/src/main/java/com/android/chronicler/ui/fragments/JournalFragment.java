package com.android.chronicler.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.chronicler.R;
import com.android.chronicler.ui.CampaignNoteActivity;
import com.android.chronicler.ui.EditJournalActivity;
import com.android.chronicler.util.DataLoader;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JournalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JournalFragment extends SheetFragment {
    private static final String PUBLIC_NOTES = "PUBLIC_NOTES";
    private static final String CAMPAIGN_NAME = "CAMPAIGN_NAME";

    private static final int maxItemLength = 35;
    private String campaignName;
    private ArrayList<ArrayList<String>> entries;
    private ArrayList<String> titles;
    private ArrayAdapter<String> adapter;

    public JournalFragment() {
        // Required empty public constructor
    }

    // notes is a list of lists L, with the L representing a key-value pair, L[0] -> L[1]
    public static JournalFragment newInstance(String campaignName,
                                                  ArrayList<ArrayList<String>> notes) {
        JournalFragment fragment = new JournalFragment();
        Bundle args = new Bundle();
        args.putSerializable(PUBLIC_NOTES, notes);
        args.putString(CAMPAIGN_NAME, campaignName);
        Log.i("Journal", notes.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entries = (ArrayList<ArrayList<String>>) getArguments().getSerializable(PUBLIC_NOTES);
            campaignName = getArguments().getString(CAMPAIGN_NAME);
            setupTitles();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_journal, container, false);

        ListView journalView = (ListView) rootView.findViewById(R.id.journal_entry_list);
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_expandable_list_item_1,
                titles);


        // Set add button to footer
        ImageView addButtonView = new ImageView(getActivity());
        addButtonView.setPadding(20, 20, 20, 20);
        addButtonView.setImageResource(R.drawable.ic_add_circle_24dp);

        journalView.addFooterView(addButtonView);
        journalView.setAdapter(adapter);

        journalView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Adapter", String.valueOf(adapter.getCount()));
                if (position == adapter.getCount()) {
                    ArrayList<String> newEntry = new ArrayList<String>();
                    newEntry.add(0, "");
                    newEntry.add(1, "");
                    openNote(newEntry, position);
                } else {
                    openNote(entries.get(position), position);
                }
            }
        });

        return rootView;
    }

    private void openNote(ArrayList<String> entry, int position) {
        String entryTitle = entry.get(0);
        String entryText = entry.get(1);
        Intent intent = new Intent(getActivity(), EditJournalActivity.class);
        intent.putExtra("TITLE", entryTitle);
        intent.putExtra("TEXT", entryText);
        startActivityForResult(intent, position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ArrayList<String> newEntry = new ArrayList<>();
        newEntry.add(0, data.getStringExtra("TITLE"));
        newEntry.add(1, data.getStringExtra("TEXT"));
        if (requestCode < entries.size()) {
            entries.set(requestCode, newEntry);
            titles.set(requestCode, data.getStringExtra("TITLE"));
        } else {
            entries.add(requestCode, newEntry);
            titles.add(data.getStringExtra("TITLE"));
        }
        Log.i("Journal", titles.toString());
        DataLoader.storeJournalEntry(getActivity(), newEntry.get(0), newEntry.get(1), campaignName);
        adapter.notifyDataSetChanged();
    }

    private String trimToLength(String str) {
        if (str.length() >= maxItemLength) {
            return str.substring(0, maxItemLength).replace("\n", " ").concat("...");
        } else {
            return str;
        }
    }

    private void setupTitles() {
        titles = new ArrayList<>();
        for (ArrayList<String> entry : entries) {
            titles.add(entry.get(0));
        }
    }
}
