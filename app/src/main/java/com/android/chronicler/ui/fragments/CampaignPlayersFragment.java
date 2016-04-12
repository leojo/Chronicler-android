package com.android.chronicler.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.chronicler.R;
import com.android.chronicler.ui.CharacterActivity;
import com.android.chronicler.util.DataLoader;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CampaignPlayersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CampaignPlayersFragment extends SheetFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PLAYER_LIST = "PLAYER_LIST";
    private static final String PLAYER_IDS = "PLAYER_IDS";
    private static final String CAMPAIGN_NAME = "CAMPAIGN_NAME";

    // TODO: Rename and change types of parameters
    private ArrayList<String> playerList;
    private ArrayList<String> playerIDs;
    private String campaignName;
    private ListAdapter playerAdapter;

    public CampaignPlayersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param playerList List of players to associate with this campaign.
     * @return A new instance of fragment CampaignPlayersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CampaignPlayersFragment newInstance(String campaignName,
                                                      ArrayList<String> playerList,
                                                      ArrayList<String> characterIDs) {
        CampaignPlayersFragment fragment = new CampaignPlayersFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(PLAYER_LIST, playerList);
        args.putStringArrayList(PLAYER_IDS, characterIDs);
        args.putString(CAMPAIGN_NAME, campaignName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            playerList = getArguments().getStringArrayList(PLAYER_LIST);
            playerIDs = getArguments().getStringArrayList(PLAYER_IDS);
            campaignName = getArguments().getString(CAMPAIGN_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_campaign_players, container, false);

        ListView playerListView = (ListView) rootView.findViewById(R.id.player_list);
        playerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_expandable_list_item_1,
                playerList);


        // Set add button to footer
        ImageView addButtonView = new ImageView(getActivity());
        addButtonView.setPadding(20, 20, 20, 20);
        addButtonView.setImageResource(R.drawable.ic_add_circle_24dp);

        playerListView.addFooterView(addButtonView);
        playerListView.setAdapter(playerAdapter);

        playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == playerAdapter.getCount()) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    dialogBuilder.setTitle("Invite User");
                    dialogBuilder.setMessage("Username:");
                    final EditText input = new EditText(getActivity());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    dialogBuilder.setView(input);
                    dialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("Invite", "Inviting player " + input.getText() + " to campaign " + campaignName);
                            DataLoader.inviteToCampaign(getActivity(), campaignName, input.getText().toString());
                        }
                    });
                    dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialogBuilder.show();
                } else {
                    Log.i("CampaignPlayer", "Inviting character with id "+playerIDs.get(position));
                    DataLoader.readySheetThenStart(getActivity(),
                            new Intent(getActivity(), CharacterActivity.class),
                            Integer.parseInt(playerIDs.get(position)));
                }
            }
        });

        return rootView;
    }
}
