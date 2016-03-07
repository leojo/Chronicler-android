package com.android.chronicler.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;

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

        inviteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPopup(view);
            }
        });
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.menu_campaign_details);
        popup.show();
    }
}
