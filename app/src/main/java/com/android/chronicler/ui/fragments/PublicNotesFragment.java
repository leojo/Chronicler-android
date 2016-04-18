package com.android.chronicler.ui.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.android.chronicler.R;
import com.android.chronicler.ui.CampaignActivity;
import com.android.chronicler.ui.CampaignNoteActivity;
import com.android.chronicler.util.DataLoader;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PublicNotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicNotesFragment extends SheetFragment {
    private static final String PUBLIC_NOTES = "PUBLIC_NOTES";
    private static final String CAMPAIGN_NAME = "CAMPAIGN_NAME";
    private static final String READ_ONLY = "READ_ONLY";

    private static final int maxItemLength = 35;
    private String campaignName;
    private ArrayList<String> publicNotes;
    private ArrayList<String> shortNotes;
    private ArrayAdapter<String> adapter;
    private boolean readOnly;

    public PublicNotesFragment() {
        // Required empty public constructor
    }

    public static PublicNotesFragment newInstance(String campaignName,
                                                  ArrayList<String> notes,
                                                  boolean readOnly) {
        PublicNotesFragment fragment = new PublicNotesFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(PUBLIC_NOTES, notes);
        args.putString(CAMPAIGN_NAME, campaignName);
        args.putBoolean(READ_ONLY, readOnly);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            publicNotes = getArguments().getStringArrayList(PUBLIC_NOTES);
            campaignName = getArguments().getString(CAMPAIGN_NAME);
            readOnly = getArguments().getBoolean(READ_ONLY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_public_notes, container, false);

        // TODO: Possibly filter out new lines here as well
        shortNotes = new ArrayList<>();
        for (String privateNote : publicNotes) {
            shortNotes.add(trimToLength(privateNote));
        }

        ListView noteListView = (ListView) rootView.findViewById(R.id.public_notes_list);
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_expandable_list_item_1,
                shortNotes);

        if (!readOnly) {
            // Set add button to footer
            ImageView addButtonView = new ImageView(getActivity());
            addButtonView.setPadding(20, 20, 20, 20);
            addButtonView.setImageResource(R.drawable.ic_add_circle_24dp);

            noteListView.addFooterView(addButtonView);
        }

        noteListView.setAdapter(adapter);

        if (!readOnly) {
            noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == adapter.getCount()) {
                        openNote("", position);
                    } else {
                        openNote(publicNotes.get(position), position);
                    }
                }
            });

            noteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == adapter.getCount()) return false;
                    showPopup(view, position);

                    return true;
                }
            });
        }

        return rootView;
    }

    public void showPopup(View v, final int position) {
        final PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.inflate(R.menu.menu_public_notes);
        final Activity thisActivity = getActivity();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch ((String) item.getTitle()) {
                    case "Delete":
                        publicNotes.remove(position);
                        shortNotes.remove(position);
                        adapter.notifyDataSetChanged();
                        DataLoader.deletePublicNote(thisActivity, position, campaignName);
                        break;
                    case "Make private":
                        DataLoader.storePrivateNote(thisActivity, Integer.MAX_VALUE, publicNotes.get(position), campaignName);
                        DataLoader.deletePublicNote(thisActivity, position, campaignName);
                        ((CampaignActivity) getActivity()).setNotePrivate(publicNotes.get(position));
                        publicNotes.remove(position);
                        shortNotes.remove(position);
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        Log.d("PopupMenu", "This is weird");
                }
                return false;
            }
        });
        popup.show();
    }

    private void openNote(String noteText, int position) {
        Intent intent = new Intent(getActivity(), CampaignNoteActivity.class);
        intent.putExtra("TEXT", noteText);
        startActivityForResult(intent, position);
    }

    // requestCode doubles as index
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String newNote = data.getStringExtra("TEXT");
        if (newNote.isEmpty()) return;
        if (requestCode < publicNotes.size()) {
            publicNotes.set(requestCode, newNote);
            shortNotes.set(requestCode, trimToLength(newNote));
        } else {
            publicNotes.add(requestCode, newNote);
            shortNotes.add(requestCode, trimToLength(newNote));
        }
        DataLoader.storePublicNote(getActivity(), requestCode, newNote, campaignName);
        adapter.notifyDataSetChanged();
    }

    public void addNote(String noteText) {
        publicNotes.add(noteText);
        shortNotes.add(trimToLength(noteText));
        adapter.notifyDataSetChanged();
    }

    private String trimToLength(String str) {
        if (str.length() >= maxItemLength) {
            return str.substring(0, maxItemLength).replace("\n", " ").concat("...");
        } else {
            return str;
        }
    }
}
