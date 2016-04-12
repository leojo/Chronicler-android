package com.android.chronicler.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.android.chronicler.R;
import com.android.chronicler.character.CharacterSheet;
import com.android.chronicler.ui.WaitingActivity;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by leo on 12.2.2016.
 *
 * A class that handles all data requests to the server.
 */
public class DataLoader {

    // Function to ready an existing character-sheet and then start the characterActivity
    // INCOMPLETE: needs to be reworked (Doesn't load a character sheet from the database)
    public static void readySheetThenStart(final Context context, final Intent intent, int id) {

        final ChroniclerRestClient cli = new ChroniclerRestClient(context);
        cli.getUserData("/getCharacterJSON?id="+id, null, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String characterJSON = new String(responseBody);
                CharacterSheet character = null;
                try {
                    character = CharacterSheet.fromJSON(characterJSON);
                } catch (IOException e) {
                    Log.e("LOAD_CHARACTER", "Failure reading charactersheet from JSON");
                    Log.e("LOAD_CHARACTER", new String(responseBody));
                    e.printStackTrace();
                    return;
                }
                intent.putExtra("CharacterSheet", character);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = (responseBody==null?"Empty response":new String(responseBody));
                Log.i("LOAD_CHARACTER", "Failure fetching character data: " + response);
            }
        });
        goToWaitScreen(context);
    }

    // Function to ready a fresh character-sheet and then start the characterActivity.
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
        goToWaitScreen(context);
    }

    // Function to fetch data for dropdown menus and then start the NewCharActivity
    // FIXME: 10.3.2016 This class could take more advantage of the asynch nature of the requests.
    public static void readyCreateCharThenStart(final Context context, final Intent intent){
        final ChroniclerRestClient cli = new ChroniclerRestClient(context);
        final ObjectMapper mapper = new ObjectMapper();
        // First fetch the raceList
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
                // Attach the list to the intent
                intent.putStringArrayListExtra("raceList", raceList);
                // Next fetch the classList
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
                        // Attach to the intent and then start the activity.
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
        goToWaitScreen(context);
    }

    // These functions are used to get a list of character the user has to populate
    // a list before starting the activity with the list.
    public static void readyCharlistThenStart(final Context context, final Intent intent) {
        readyCharlistThenStart(context, intent, false, 0);
    }

    public static void readyCharlistThenStartForResult(final Context context, final Intent intent, int code) {
        readyCharlistThenStart(context, intent, true, code);
    }

    public static void readyCharlistThenStart(final Context context, final Intent intent, final boolean getResult, final int code) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        cli.getUserData("/characters", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONresponse = new String(responseBody);
                ArrayList<String> content = new ArrayList<String>();
                ArrayList<Integer> ids = new ArrayList<Integer>();
                try {

                    JSONObject jObject = new JSONObject(JSONresponse);
                    Iterator<?> keys = jObject.keys();

                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        content.add(jObject.get(key).toString());
                        ids.add(Integer.parseInt(key));
                    }
                } catch (JSONException e) {
                    Log.i("CHARLIST", "JSON EXCEPTION");
                }
                // Finally start the activity with 'content' as extra:
                intent.putExtra("CharacterList", content);
                intent.putExtra("CharacterIds", ids);
                if (getResult) {
                    ((Activity) context).startActivityForResult(intent, code);
                } else {
                    context.startActivity(intent);
                }
                Log.i("USERGET", new String(responseBody));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("CHARLIST", "failed to send request???");
            }
        });
        goToWaitScreen(context);
    }
/*
    public static void readyResultsThenStart(final Context context, final Intent intent, final boolean getResult, final int code) {
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
        goToWaitScreen(context);
    }

*/

    // Fetches the list of campaigns for the current user and then starts the Campaigns activity.
    public static void readyCampaignlistThenStart(final Context context, final Intent intent) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        UserLocalStore store = new UserLocalStore(context.getApplicationContext());
        cli.getUserData("/campaignData", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                ArrayList<String> DMCampaigns = new ArrayList<>();
                ArrayList<String> PCCampaigns = new ArrayList<>();
                Log.i("CAMPAIGNS", responseBody.toString());

                try {
                    JSONArray DMResponse = responseBody.getJSONObject(0).names();

                    if (DMResponse != null) {
                        for (int i = 0; i < DMResponse.length(); i++) {
                            DMCampaigns.add(responseBody.getJSONObject(0).getString(DMResponse.getString(i)));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray PCResponse = responseBody.getJSONObject(1).names();
                    if (PCResponse != null) {
                        for (int i = 0; i < PCResponse.length(); i++) {
                            PCCampaigns.add(responseBody.getJSONObject(1).getString(PCResponse.getString(i)));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                intent.putExtra("DMCampaignList", DMCampaigns);
                intent.putExtra("PCCampaignList", PCCampaigns);
                context.startActivity(intent);
            }
        });
        goToWaitScreen(context);
    }

    public static void inviteToCampaign(final Context context, String campaignName, String user) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        RequestParams params = new RequestParams();
        params.put("Campaign", campaignName);
        params.put("User", user);
        cli.postUserData("/inviteToCampaign", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("Campaign", "Successfully invited player to campaign: " + responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Campaign", "Failed to invite player to campaign");
            }
        });
    }

    // Stores the specified campaign in the database and then opens the Campaign activity for it.
    public static void postCampaignThenOpen(final Context context, final Intent intent, String campaignName) throws IOException {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        RequestParams params = new RequestParams();
        params.put("campaign_name", campaignName);
        cli.postUserData("/campaignData", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("DataLoader", "Failed to post campaign");
            }
        });
        goToWaitScreen(context);
    }

    public static void getCampaignDetailsThenOpen(final Context context, final Intent intent, String campaignName) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        UserLocalStore store = new UserLocalStore(context.getApplicationContext());

        RequestParams params = new RequestParams();
        params.put("campaign_name", campaignName);

        cli.getUserData("/campaignDetails", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                //ArrayList<String> characters = new ArrayList<>();
                HashMap<Integer, String> characters = new HashMap<Integer, String>();

                Log.i("CAMPAIGN_DATA", responseBody.toString());
                try {
                    characters = new ObjectMapper().readValue(responseBody.getString(0), HashMap.class);
                    Log.i("CAMPAIGN_DATA", characters.toString());
                    JSONArray campaignCharacters = responseBody.getJSONObject(0).names();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                intent.putExtra("campaign_characters", characters);
                context.startActivity(intent);
            }
        });
        goToWaitScreen(context);
    }

    // Stores a given character sheet in the server-side database.
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

    // Readies a list of all of active users' invites and then start the activity
    public static void readyInvitesThenStart(final Context context, final Intent intent) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        UserLocalStore store = new UserLocalStore(context.getApplicationContext());
        cli.getUserData("/invites", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                ArrayList<String> invites = new ArrayList<>();

                try {
                    JSONArray inviteJSON = new JSONArray(responseBody.getString(0));

                    for (int i = 0; i < inviteJSON.length(); i++) {
                        invites.add(inviteJSON.getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                intent.putExtra("INVITES", invites);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject object) {
                Log.i("DataLoader", object.toString());
            }
        });
        goToWaitScreen(context);
    }


    public static void respondToInvite(final Context context, final Intent intent, int index, String characterName) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        UserLocalStore store = new UserLocalStore(context.getApplicationContext());

        RequestParams params = new RequestParams();
        params.put("Character", characterName);
        params.put("CampaignIndex", index);

        cli.postUserData("/respondToInvite", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                ArrayList<String> invites = new ArrayList<>();

                try {
                    JSONArray inviteJSON = new JSONArray(responseBody.getString(0));

                    for (int i = 0; i < inviteJSON.length(); i++) {
                        invites.add(inviteJSON.getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                intent.putExtra("INVITES", invites);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject object) {
                Log.i("DataLoader", object.toString());
            }
        });
    }

    // Displays a spinning wheel waiting screen while the user waits for an async http request.
    private static void goToWaitScreen(Context context){
        Intent loadingScreenIntent = new Intent(context, WaitingActivity.class);
        context.startActivity(loadingScreenIntent);
    }

}
