package com.android.chronicler.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.chronicler.R;
import com.android.chronicler.util.DataLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by andrea on 28.1.2016.
 * Campaigns activity includes a list of the user's campaigns (both as player and as dungeon master)
 * and the option to create a new campaign. The user that creates a campaign immediately becomes
 * the dungeon master of the campaign (at least for now)
 */
public class CampaignsActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter, adapter2;
    ListView campaignListView, playerCampaignsView;
    public List<String> DMCampaigns;
    public List<String> PCCampaigns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaigns);

        Intent intent = getIntent();
        DMCampaigns = intent.getStringArrayListExtra("DMCampaignList");
        PCCampaigns = intent.getStringArrayListExtra("PCCampaignList");

        campaignListView = (ListView)findViewById(R.id.DMCampaignListView);
        playerCampaignsView = (ListView)findViewById(R.id.PlayerCampaignListView);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, DMCampaigns);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, PCCampaigns);

        // Set add button to footer
        ImageView addButtonView = new ImageView(this);
        addButtonView.setPadding(20, 20, 20, 20);
        addButtonView.setImageResource(R.drawable.ic_add_circle_24dp);

        campaignListView.addFooterView(addButtonView);

        campaignListView.setAdapter(adapter);
        playerCampaignsView.setAdapter(adapter2);

        campaignListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.i("Campaigns", "Position "+position+" of "+adapter.getCount());
                if (position == adapter.getCount()) {
                    newCampaign();
                } else {
                    openCampaign(DMCampaigns.get(position));
                }
            };
        // --------------------------------------
        });
    }

    public void openCampaign(String name) {
        Intent intent = new Intent(this, CampaignActivity.class);
        intent.putExtra("CAMPAIGN_NAME", name);
        DataLoader.getCampaignDetailsThenOpen(this, intent, name);
    }


    public void newCampaign() {
        Intent intent = new Intent(this, NewCampaignActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_campaigns, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

