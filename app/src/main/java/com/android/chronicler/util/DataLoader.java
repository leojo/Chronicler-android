package com.android.chronicler.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.chronicler.character.CharacterSheet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


import cz.msebera.android.httpclient.Header;

/**
 * Created by andrea on 12.2.2016.
 *
 * DataLoader handles all preparation steps for opening up a new activity.
 * If an activity needs some data from the server before it starts, DataLoader takes
 * care of requesting said data and then starting the activity.
 *
 * Example: Instead of starting the CharactersActivity directly with the list of characters,
 * you would use the DataLoader to start the activity by doing:
 * DataLoader.readyCharlistThenStart....
 */
public class DataLoader {

    // Readies a character sheet by requesting relevant JSON and then starts the Character activity.
    public void readySheetThenStart(final Context context, final Intent intent) {

        ChroniclerRestClient cli = new ChroniclerRestClient();
        cli.get("/skillData", null, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                CharacterSheet character = new CharacterSheet("Bob", "Elf", "Barbarian", new String(responseBody));
                intent.putExtra("CharacterSheet", character);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = (responseBody==null?"Empty response":new String(responseBody));
                Log.i("SKILLS", "Failure fetching skill data: " + response);
            }
        });

    }

    // Readies the user's list of characters by requesting relevant JSON and then starts the Characters activity.
    // Returns a boolean value false if the user's cookie was expired, in which case it doesn't make the request.
    public boolean readyCharlistThenStart(final Context context, final Intent intent) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        boolean inSession = cli.getUserData("/characters", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONresponse = new String(responseBody);
                ArrayList<String> content = new ArrayList<String>();

                try {

                JSONObject jObject = new JSONObject(JSONresponse);
                Iterator<?> keys = jObject.keys();

                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        content.add(jObject.get(key).toString());
                    }
                }catch(JSONException e) {Log.i("CHARLIST", "JSON EXCEPTION");}


                // Finally start the activity with 'content' as extra:
                intent.putExtra("CharacterList", content);
                context.startActivity(intent);
                Log.i("USERGET", new String(responseBody));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("CHARLIST", "failed to send requesT???");
            }
        });

        if(!inSession) return false;
        else return true;
    }

    // Readies the user's list of campaigns by requesting relevant JSON and then starts the campaign activity
    // Returns a boolean value false if the user's cookie was expired, in which case it doesn't make the request.
    public boolean readyCampListThenStart(final Context context, final Intent intent) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        UserLocalStore store = new UserLocalStore(context);

        // FIXME: @Bjorn, use cookies instead of username. I changed this to getUserData instead of get so that it sends cookie automatically as header
        RequestParams user_data = new RequestParams("username", store.getUserData()[0]);
        boolean inSession = cli.getUserData("/campaignData", user_data, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                try {
                    System.out.println(responseBody.getJSONObject(1).getString("7"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayList<String> DMCampaigns = new ArrayList<>();
                ArrayList<String> PCCampaigns = new ArrayList<>();

                try {
                    JSONArray DMResponse = responseBody.getJSONObject(0).names();

                    for (int i=0; i<DMResponse.length(); i++) {
                        DMCampaigns.add(responseBody.getJSONObject(0).getString(DMResponse.getString(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray PCResponse = responseBody.getJSONObject(1).names();
                    for (int i=0; i<PCResponse.length(); i++) {
                        PCCampaigns.add(responseBody.getJSONObject(1).getString(PCResponse.getString(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                intent.putExtra("DMCampaignList", DMCampaigns);
                intent.putExtra("PCCampaignList", PCCampaigns);
                context.startActivity(intent);
            }
        });
        if(!inSession) return false;
        else return true;
    }
}
