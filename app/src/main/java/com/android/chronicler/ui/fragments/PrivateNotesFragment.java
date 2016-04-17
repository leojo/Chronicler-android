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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.android.chronicler.R;
import com.android.chronicler.ui.CampaignActivity;
import com.android.chronicler.ui.CampaignNoteActivity;
import com.android.chronicler.util.DataLoader;

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
    private ArrayList<String> shortNotes;
    private ArrayAdapter<String> adapter;

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_private_notes, container, false);

        // TODO: Possibly filter out new lines here as well
        shortNotes = new ArrayList<>();
        for (String privateNote : privateNotes) {
            shortNotes.add(trimToLength(privateNote));
        }

        ListView noteListView = (ListView) rootView.findViewById(R.id.private_notes_list);
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_expandable_list_item_1,
                shortNotes);


        // Set add button to footer
        ImageView addButtonView = new ImageView(getActivity());
        addButtonView.setPadding(20, 20, 20, 20);
        addButtonView.setImageResource(R.drawable.ic_add_circle_24dp);

        noteListView.addFooterView(addButtonView);
        noteListView.setAdapter(adapter);

        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Adapter", String.valueOf(adapter.getCount()));
                if (position == adapter.getCount()) {
                    openNote("", position);
                } else {
                    openNote(privateNotes.get(position), position);
                }
            }
        });

        noteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showPopup(view, position);

                return true;
            }
        });

        return rootView;
    }

    public void showPopup(View v, final int position) {
        final PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.inflate(R.menu.menu_private_notes);
        final Activity thisActivity = getActivity();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch ((String) item.getTitle()) {
                    case "Delete":
                        privateNotes.remove(position);
                        shortNotes.remove(position);
                        adapter.notifyDataSetChanged();
                        DataLoader.deletePrivateNote(thisActivity, position, campaignName);
                        break;
                    case "Make public":
                        DataLoader.storePublicNote(thisActivity, Integer.MAX_VALUE, privateNotes.get(position), campaignName);
                        DataLoader.deletePrivateNote(thisActivity, position, campaignName);
                        ((CampaignActivity) getActivity()).setNotePublic(privateNotes.get(position));
                        privateNotes.remove(position);
                        shortNotes.remove(position);
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        Log.i("PopupMenu", "This is weird. Got "+item.getTitle());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Notes", "Received text: " + data.getStringExtra("TEXT"));
        String newNote = data.getStringExtra("TEXT");
        if (requestCode < privateNotes.size()) {
            privateNotes.set(requestCode, newNote);
            shortNotes.set(requestCode, trimToLength(newNote));
        } else {
            privateNotes.add(requestCode, newNote);
            shortNotes.add(requestCode, trimToLength(newNote));
        }
        DataLoader.storePrivateNote(getActivity(), requestCode, newNote, campaignName);
        adapter.notifyDataSetChanged();
    }

    public void addNote(String noteText) {
        privateNotes.add(noteText);
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
