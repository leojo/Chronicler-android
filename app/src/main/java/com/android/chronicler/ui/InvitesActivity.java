package com.android.chronicler.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.chronicler.R;
import com.android.chronicler.util.DataLoader;

import java.util.List;

/**
 * Activity with list of invites that the user can either accept (And then choose the character
 * he/she wants to add to the campaign) or decline the invite.
 */
public class InvitesActivity extends AppCompatActivity {
    final int SELECT_CHARACTER = 1;
    List<String> invites;
    ListView inviteListView;
    ArrayAdapter<String> adapter;
    String selectedItem;
    int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invites);

        Intent intent = getIntent();
        invites = intent.getStringArrayListExtra("INVITES");

        if (invites.isEmpty()) {
            TextView txt = (TextView)findViewById(R.id.no_invites_text);
            txt.setVisibility(View.VISIBLE);
        }

        inviteListView = (ListView)findViewById(R.id.inviteListView);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, invites);

        inviteListView.setAdapter(adapter);

        // Activate popup when an invite is clicked
        inviteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = invites.get(position);
                selectedPosition = position;
                showPopup(view, position);
            }
        });
    }

    // Pop-up for accepting or declining invites: Will later be replaced with buttons
    // nested inside the list elements for accepting and declining.
    public void showPopup(View v, final int position) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.menu_invite_options);
        final Activity thisActivity = this;

        final Intent intent = new Intent(this, CharactersActivity.class);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch ((String) item.getTitle()) {
                    case "Accept":
                        DataLoader.readyCharlistThenStartForResult(thisActivity, intent, SELECT_CHARACTER);
                        break;
                    case "Decline":
                        adapter.remove(selectedItem);
                        DataLoader.respondToInvite(thisActivity, intent, position, null);
                        break;
                    default:
                        Log.d("PopupMenu", "This is weird");
                }
                return false;
            }
        });
        popup.show();
    }

    // Get a result back from the create a character activity
    // so that we can add the new character to the campaign
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String selectedCharacter = data.getStringExtra("CHARACTER_NAME");
        adapter.remove(selectedItem);

        if (adapter.isEmpty()) {
            TextView txt = (TextView)findViewById(R.id.no_invites_text);
            txt.setVisibility(View.VISIBLE);
        }
        DataLoader.respondToInvite(this, new Intent(this, CharactersActivity.class), selectedPosition, selectedCharacter);

        Intent intent = new Intent(this, CampaignActivity.class);
        intent.putExtra("CAMPAIGN_NAME", selectedItem);
        DataLoader.getCampaignDetailsThenOpen(this, intent, selectedItem);
    }
}
