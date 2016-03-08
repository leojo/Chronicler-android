package com.android.chronicler.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.chronicler.character.CharacterSheet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by leo on 12.2.2016.
 */
public class DataLoader {

    // NOTE: Wouldn't it make more sense to make these methods static?

    public static void readySheetThenStart(final Context context, final Intent intent) {

        final ChroniclerRestClient cli = new ChroniclerRestClient(context);
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

    public static void readyNewSheetThenStart(final Context context, final Intent intent, final String name, final String race, final String charClass){
        final ChroniclerRestClient cli = new ChroniclerRestClient(context);
        cli.get("/skillData", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Create a new character sheet object
                CharacterSheet character = new CharacterSheet(name, race, charClass, new String(responseBody));
                storeCharSheet(context, character);
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

    public static void readyCreateCharThenStart(final Context context, final Intent intent){
        final ChroniclerRestClient cli = new ChroniclerRestClient(context);
        final ObjectMapper mapper = new ObjectMapper();
        cli.get("/raceList", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String raceListJSON = new String(responseBody);
                ArrayList<String> raceList;
                try {
                    raceList = mapper.readValue(raceListJSON, ArrayList.class);
                } catch (IOException e) {
                    onFailure(statusCode, headers, responseBody, new Error(e.getMessage()));
                    return;
                }
                intent.putStringArrayListExtra("raceList", raceList);
                cli.get("/classList", null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String classListJSON = new String(responseBody);
                        ArrayList<String> classList;
                        try {
                            classList = mapper.readValue(classListJSON, ArrayList.class);
                        } catch (IOException e) {
                            onFailure(statusCode, headers, responseBody, new Error(e.getMessage()));
                            return;
                        }
                        intent.putStringArrayListExtra("classList", classList);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        String response = (responseBody == null ? "Empty response" : new String(responseBody));
                        Log.e("CLASSLIST", "Failure fetching class list: " + response);
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = (responseBody==null?"Empty response":new String(responseBody));
                Log.e("RACELIST", "Failure fetching race list: " + response);
            }
        });
    }

    public static void readyCharlistThenStart(final Context context, final Intent intent) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        cli.getUserData("/characters", null, new AsyncHttpResponseHandler() {
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
                } catch (JSONException e) {
                    Log.i("CHARLIST", "JSON EXCEPTION");
                }
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
    }

    public static void readyCampaignlistThenStart(final Context context, final Intent intent) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        UserLocalStore store = new UserLocalStore(context.getApplicationContext());
        cli.getUserData("/campaignData", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                Log.i("CAMPAIGNS", "Got campaigns");
                try {
                    System.out.println(responseBody.getJSONObject(1).getString("7"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayList<String> DMCampaigns = new ArrayList<>();
                ArrayList<String> PCCampaigns = new ArrayList<>();

                try {
                    JSONArray DMResponse = responseBody.getJSONObject(0).names();

                    for (int i = 0; i < DMResponse.length(); i++) {
                        DMCampaigns.add(responseBody.getJSONObject(0).getString(DMResponse.getString(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray PCResponse = responseBody.getJSONObject(1).names();
                    for (int i = 0; i < PCResponse.length(); i++) {
                        PCCampaigns.add(responseBody.getJSONObject(1).getString(PCResponse.getString(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                intent.putExtra("DMCampaignList", DMCampaigns);
                intent.putExtra("PCCampaignList", PCCampaigns);
                context.startActivity(intent);
            }

            @Override
            public void onStart() {
                Log.i("START", "Started campaign fetching");
            }
        });
    }

    public static void postCampaignThenOpen(final Context context, final Intent intent, String campaignName) throws IOException {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        RequestParams params = new RequestParams();
        params.put("campaign_name", campaignName);
        cli.postUserData("/campaignData", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("SUCCESS", "Starting campaign activity");
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("DataLoader", "Failed to post campaign");
            }
        });
    }

    public static void storeCharSheet(Context context, CharacterSheet c){
        final ChroniclerRestClient cli = new ChroniclerRestClient(context);
        StringEntity charEntity = null;
        try {
            charEntity = new StringEntity(c.toJSON(), ContentType.APPLICATION_JSON);
        } catch (JsonProcessingException e) {
            Log.e("STORECHAR","Error converting character sheet to JSON");
            e.printStackTrace();
        }
        if(charEntity!= null) {
            cli.postUserData("/storeChar", charEntity, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.i("STORECHAR", "Success: "+new String(responseBody));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.i("STORECHAR", "Failure");
                    error.printStackTrace();
                }
            });
        }
    }
}
