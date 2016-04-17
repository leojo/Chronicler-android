package com.android.chronicler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.chronicler.R;
import com.android.chronicler.util.DataLoader;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bjorn on 24.2.2016.
 * An activity for creating a new campaign
 * PS. I'm not sure this activity is necessarily needed, we could use a new campaign fragment instead
 */
public class NewCampaignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_campaign);

        final EditText textField = (EditText) findViewById(R.id.campaign_name_field);
        final Button button = (Button) findViewById(R.id.confirm_campaign_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addCampaign(textField.getText().toString());
            }
        });
    }

    // Add campaign by using the data loader to post the request to
    // the server with the name of the campaign.
    public void addCampaign(String campaignName) {
        DataLoader loader = new DataLoader();
        Intent intent = new Intent(this, CampaignActivity.class);
        intent.putExtra("campaign_characters", new ArrayList<String>());
        intent.putExtra("campaign_character_ids", new ArrayList<String>());
        intent.putExtra("campaign_private_notes", new ArrayList<String>());
        intent.putExtra("campaign_public_notes", new ArrayList<String>());
        intent.putExtra("campaign_journal_entries", new ArrayList<ArrayList<String>>());
        try {
            loader.postCampaignThenOpen(this, intent, campaignName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
