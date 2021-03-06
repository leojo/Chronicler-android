package com.android.chronicler.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.android.chronicler.R;
import com.android.chronicler.util.ChroniclerRestClient;
import com.android.chronicler.util.DataLoader;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * Created by andrea on 28.1.2016.
 * Campaigns activity includes a list of the user's campaigns (both as player and as dungeon master)
 * and the option to create a new campaign. The user that creates a campaign immediately becomes
 * the dungeon master of the campaign (at least for now)
 */
public class CampaignsActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter, adapter2;
    ListView campaignListView, playerCampaignsView;
    public ArrayList<String> DMCampaigns;
    public ArrayList<String> PCCampaigns;

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
                if (position == adapter.getCount()) {
                    newCampaign();
                } else {
                    openCampaign(DMCampaigns.get(position));
                }
            }

            ;
            // --------------------------------------
        });

        playerCampaignsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openCampaign(PCCampaigns.get(position));
            }
        });

        campaignListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == adapter.getCount()) return false;
                showDMPopup(view, DMCampaigns, position);

                return true;
            }
        });

        playerCampaignsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showPCPopup(view, PCCampaigns, position);

                return true;
            }
        });
    }

    public void showPCPopup(View v, final ArrayList<String> list, final int position) {
        final PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.menu_pc_campaign_list);
        final Activity thisActivity = this;

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch ((String) item.getTitle()) {
                    case "Leave campaign":
                        DataLoader.leaveCampaign(thisActivity, list.get(position));
                        list.remove(position);
                        adapter2.notifyDataSetChanged();
                        break;
                    default:
                        Log.d("PopupMenu", "This is weird");
                }
                return false;
            }
        });
        popup.show();
    }

    public void showDMPopup(View v, final ArrayList<String> list, final int position) {
        final PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.menu_dm_campaign_list);
        final Activity thisActivity = this;

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch ((String) item.getTitle()) {
                    case "Delete":
                        DataLoader.deleteCampaign(thisActivity, list.get(position));
                        list.remove(position);
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

    @Override
    public void onRestart() {
        super.onRestart();

        ChroniclerRestClient cli = new ChroniclerRestClient(this);
        cli.getUserData("/campaignData", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                ArrayList<String> DMCampaignResponse = new ArrayList<>();
                ArrayList<String> PCCampaignResponse = new ArrayList<>();
                Log.d("Campaigns", "Received campaigns "+responseBody.toString());

                try {
                    JSONArray DMResponse = responseBody.getJSONObject(0).names();

                    if (DMResponse != null) {
                        for (int i = 0; i < DMResponse.length(); i++) {
                            DMCampaignResponse.add(responseBody.getJSONObject(0).getString(DMResponse.getString(i)));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray PCResponse = responseBody.getJSONObject(1).names();
                    if (PCResponse != null) {
                        for (int i = 0; i < PCResponse.length(); i++) {
                            PCCampaignResponse.add(responseBody.getJSONObject(1).getString(PCResponse.getString(i)));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.clear();
                adapter.addAll(DMCampaignResponse);
                adapter2.clear();
                adapter2.addAll(PCCampaignResponse);
            }
        });
    }

    public void openCampaign(String name) {
        Intent intent = new Intent(this, CampaignActivity.class);
        intent.putExtra("CAMPAIGN_NAME", name);
        intent.putExtra("read_only", !DMCampaigns.contains(name));
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

