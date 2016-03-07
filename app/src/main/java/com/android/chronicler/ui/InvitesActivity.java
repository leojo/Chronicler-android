package com.android.chronicler.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.chronicler.R;

import java.lang.reflect.Array;
import java.util.List;

public class InvitesActivity extends AppCompatActivity {
    List<String> invites;
    ListView inviteListView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invites);

        Intent intent = getIntent();
        invites = intent.getStringArrayListExtra("invites");

        Log.i("Invites", invites.toString());

        inviteListView = (ListView)findViewById(R.id.inviteListView);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, invites);

        inviteListView.setAdapter(adapter);
    }
}
