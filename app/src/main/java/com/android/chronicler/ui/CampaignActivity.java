package com.android.chronicler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.chronicler.R;
import com.android.chronicler.util.ChroniclerRestClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bjorn.
 *
 * An overview activity for the campaign: Should include the players and
 * their characters with an option to view character sheet if the user is the DM.
 * Should include an option to invite users to campaign as well.
 */

public class CampaignActivity extends AppCompatActivity {
    private String campaignName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);

        Intent intent = getIntent();
        campaignName = intent.getStringExtra("CAMPAIGN_NAME");

        final EditText textField = (EditText) findViewById(R.id.invite_field);
        final Button button = (Button) findViewById(R.id.send_invite_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inviteToCampaign(textField.getText().toString());
            }
        });
    }

    // Uses the RestClient to post an invite to a specific user.
    private void inviteToCampaign(String user) {
        ChroniclerRestClient cli = new ChroniclerRestClient(this);
        RequestParams params = new RequestParams();
        params.put("Campaign", "Awesome campaign!");
        params.put("User", user);
        cli.postUserData("/inviteToCampaign", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("Campaign", "Successfully invited player to campaign: "+responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Campaign", "Failed to invite player to campaign");
            }
        });
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
