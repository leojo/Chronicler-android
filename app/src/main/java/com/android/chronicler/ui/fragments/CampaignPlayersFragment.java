package com.android.chronicler.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.chronicler.R;

import java.util.ArrayList;
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

    // TODO: Rename and change types of parameters
    private ArrayList<String> playerList;
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
    public static CampaignPlayersFragment newInstance(ArrayList<String> playerList) {
        CampaignPlayersFragment fragment = new CampaignPlayersFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(PLAYER_LIST, playerList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            playerList = getArguments().getStringArrayList(PLAYER_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_campaign_players, container, false);

        ListView playerListView = (ListView) rootView.findViewById(R.id.player_list);
        playerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, playerList);

        playerListView.setAdapter(playerAdapter);

        playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("PLAYER_FRAGMENT", "Clicked item at position " + position);
            }
        });

        return rootView;
    }
}
