package com.android.chronicler.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.android.chronicler.character.CharacterSheet;
import com.android.chronicler.character.SheetObject;
import com.android.chronicler.character.feat.Feat;
import com.android.chronicler.character.feat.FeatSlot;
import com.android.chronicler.character.item.Inventory;
import com.android.chronicler.character.item.Item;
import com.android.chronicler.character.spell.Spell;
import com.android.chronicler.character.spell.SpellSlot;
import com.android.chronicler.ui.CharactersActivity;
import com.android.chronicler.ui.SearchActivity;
import com.android.chronicler.ui.WaitingActivity;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
        cli.getUserData("/getCharacterJSON?id=" + id, null, new AsyncHttpResponseHandler() {

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
                String response = (responseBody == null ? "Empty response" : new String(responseBody));
                Log.i("LOAD_CHARACTER", "Failure fetching character data: " + response);
            }
        });
        goToWaitScreen(context);
    }



    //Wrapper functions for readyCreateCharThenStart
    public static void readyNewSheetThenStart(final Context context, final Intent intent, final String name, final String race, final String charClass){
        readyNewSheetThenStart(context, intent, name, race, charClass, false, 0);
    }

    public static void readyNewSheetThenStartForResult(final Context context, final Intent intent, final String name, final String race, final String charClass, int code){
        readyNewSheetThenStart(context, intent, name, race, charClass, true, code);
    }

    // Function to ready a fresh character-sheet and then start the characterActivity.
    public static void readyNewSheetThenStart(final Context context, final Intent intent, final String name, final String race, final String charClass, final boolean getResult, final int code){
        final ChroniclerRestClient cli = new ChroniclerRestClient(context);
        cli.get("/skillData", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Create a new character sheet object
                final String skillData = new String(responseBody);
                cli.get("/classData?s=" + charClass, null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        CharacterSheet character = new CharacterSheet(name, race, charClass, skillData, new String(responseBody));
                        storeCharSheet(context, character);
                        intent.putExtra("CharacterSheet", character);
                        if(getResult){
                            ((Activity) context).startActivityForResult(intent, code);
                        } else {
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        String response = (responseBody == null ? "Empty response" : new String(responseBody));
                        Log.i("CHARACTER_CREATE", "Failure fetching class data: " + response);
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = (responseBody == null ? "Empty response" : new String(responseBody));
                Log.i("CHARACTER_CREATE", "Failure fetching skill data: " + response);
            }
        });
        goToWaitScreen(context);
    }

    // Function to ready a fresh character-sheet in the background.
    public static void readyNewSheet(final Context context,final String name, final String race, final String charClass, final boolean finishAfter){
        final ChroniclerRestClient cli = new ChroniclerRestClient(context);
        cli.get("/skillData", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Create a new character sheet object
                final String skillData = new String(responseBody);
                cli.get("/classData?s=" + charClass, null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        CharacterSheet character = new CharacterSheet(name, race, charClass, skillData, new String(responseBody));
                        if (finishAfter) {
                            storeCharSheetThenFinish(context, character);
                        } else {
                            storeCharSheet(context, character);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        String response = (responseBody == null ? "Empty response" : new String(responseBody));
                        Log.i("CHARACTER_CREATE", "Failure fetching class data: " + response);
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = (responseBody == null ? "Empty response" : new String(responseBody));
                Log.i("CHARACTER_CREATE", "Failure fetching skill data: " + response);
            }
        });
    }

    //Wrapper functions for readyCreateCharThenStart
    public static void readyCreateCharThenStart(final Context context, final Intent intent){
        readyCreateCharThenStart(context, intent, false, 0);
    }

    public static void readyCreateCharThenStartForResult(final Context context, final Intent intent, int code){
        readyCreateCharThenStart(context, intent, true, code);
    }

    // Function to fetch data for dropdown menus and then start the NewCharActivity
    // FIXME: 10.3.2016 This class could take more advantage of the asynch nature of the requests.
    public static void readyCreateCharThenStart(final Context context, final Intent intent, final boolean getResult, final int code){
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
                        if(getResult){
                            ((Activity) context).startActivityForResult(intent, code);
                        } else {
                            context.startActivity(intent);
                        }
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

    public static void deleteCharacter(final Context context,final int charID, final int charPosition) {
        final ChroniclerRestClient cli = new ChroniclerRestClient(context);

        RequestParams params = new RequestParams();
        params.put("charID", charID);
        // First fetch the raceList
        cli.getUserData("/deleteChar", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.i("LOGIN", "This is the response: " + new String(response));
                // called when response HTTP status is "200 OK"
                try {
                    JSONObject res = new JSONObject(new String(response));
                    String code = res.getString("code");
                    if (code.equals("success")) {
                        // Needs to be this way because CONTENT and IDS don't talk to each other
                        // so if we simply did adapter.remove, we would remove the first character
                        // with a specific name but maybe not the one we meant to remove.
                        // For this reason we will remove by index from both CONTENT and IDS and then
                        // tell the adapter that something changed.
                        CharactersActivity.CONTENT.remove(charPosition);
                        CharactersActivity.IDS.remove(charPosition);
                        CharactersActivity.adapter.notifyDataSetChanged();
                    } else if (code.equals("failure")) {
                        Log.d("CHARACTER DELETE", res.getString("message"));
                    }
                } catch (JSONException e) {
                    Log.d("CHARACTER DELETE", "Invalid JSON response from server");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
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

    public static void handleSearchQuery(final Context context, final Intent intent, final String searchtype, final String searchtext) {
        Log.d("ITEMSEARCH","Inside handleSearchQuery, searchType is "+searchtype);
        final ChroniclerRestClient cli = new ChroniclerRestClient(context);
        String queryURL = "/"+searchtype+"?s="+searchtext; // For example: .../feat?s=healing etc
        cli.get(queryURL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSONresponse = new String(responseBody);
                // This response will be:
                //  { 0 : [id: , name: , fullText: ,...], 1: [id: , name: , fullText: ,...]

                ArrayList<SheetObject> content = new ArrayList<>();
                ObjectMapper mapper = new ObjectMapper();
                ArrayList<Integer> ids = new ArrayList<Integer>();
                try {
                    switch (searchtype){
                        case "spell":
                            // The JSONrespnse represents an Arraylist<Spell>
                            ArrayList<Spell> spells = mapper.readValue(JSONresponse, new TypeReference<ArrayList<Spell>>() { });
                            ArrayList<SpellSlot> spellSlots = new ArrayList<>();
                            for(Spell s : spells){
                                SpellSlot ss = new SpellSlot();
                                ss.setSpell(s);
                                spellSlots.add(ss);
                            }
                            content.addAll(spellSlots);
                            break;
                        case "feat":
                            // The JSONrespnse represents an Arraylist<Feat>
                            ArrayList<Feat> feats = mapper.readValue(JSONresponse, new TypeReference<ArrayList<Feat>>() { });
                            ArrayList<FeatSlot> featSlots = new ArrayList<>();
                            for(Feat f : feats){
                                FeatSlot fs = new FeatSlot();
                                fs.setFeat(f);
                                featSlots.add(fs);
                            }
                            content.addAll(featSlots);
                            break;
                        case "item":
                            // The JSONrespnse represents an Inventory containing an ArrayList<Item>
                            Inventory inv = mapper.readValue(JSONresponse, Inventory.class);
                            content.addAll(inv.getItems());
                            break;
                        default:
                            Log.d("ITEMSEARCH",JSONresponse);
                            SearchActivity.noResults();
                            break;
                    }
                    /*JSONArray allResults = new JSONArray(JSONresponse);
                    //Iterator<?> keys = allResults.keys(); // keys will be 0, 1, 2, 3...
                    ObjectMapper mapper = new ObjectMapper();
                    for (int i = 0; i < allResults.length(); i++) {
                        //JSONObject result = allResults.getJSONObject(i);
                        //content.add(result.get("name").toString());
                        //ids.add(Integer.parseInt(key));
                        String resultString = allResults.getString(i);
                        SheetObject result;
                        switch (searchtype){
                            case "spell":
                                result = new SpellSlot();
                                ((SpellSlot)result).setSpell(mapper.readValue(resultString, Spell.class));
                                content.add(result);
                                break;
                            case "feat":
                                result = new FeatSlot();
                                ((FeatSlot)result).setFeat(mapper.readValue(resultString, Feat.class));
                                content.add(result);
                                break;
                            case "item":
                                content.addAll(mapper.readValue(JSONresponse, Inventory.class).getItems());
                                break;
                            default:
                                SearchActivity.noResults();
                                return;
                        }
                    }*/
                    if (content.size() != 0) {
                        SearchActivity.adapter.clearAndAddAll(content);
                        SearchActivity.showResults();
                    } else {
                        SearchActivity.noResults();
                    }
                //} catch (JSONException e) {
                //    SearchActivity.noResults();
                } catch (JsonMappingException e) {
                    Log.d("ITEMSEARCH","JsonMappingException:",e);
                    SearchActivity.noResults();
                } catch (JsonParseException e) {
                    Log.d("ITEMSEARCH","JsonParseException:",e);
                    SearchActivity.noResults();
                } catch (IOException e) {
                    Log.d("ITEMSEARCH","IOException:",e);
                    SearchActivity.noResults();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = (responseBody == null ? "Empty response" : new String(responseBody));
                Log.i("SEARCH", "Failure fetching search results for search type: " + searchtype + " and search string " + searchtext);
            }
        });
    }

    // Fetches the list of campaigns for the current user and then starts the Campaigns activity.
    public static void readyCampaignlistThenStart(final Context context, final Intent intent) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        UserLocalStore store = new UserLocalStore(context.getApplicationContext());
        cli.getUserData("/campaignData", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                ArrayList<String> DMCampaigns = new ArrayList<>();
                ArrayList<String> PCCampaigns = new ArrayList<>();
                Log.i("Campaigns", responseBody.toString());

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

    public static void leaveCampaign(final Context context, String campaignName) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        RequestParams params = new RequestParams();
        params.put("campaign_name", campaignName);
        cli.postUserData("/leaveCampaign", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("Campaign", new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Campaign", new String(responseBody));
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
                Log.i("Campaign", new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("DataLoader", "Failed to post campaign");
            }
        });
        goToWaitScreen(context);
    }

    public static void deleteCampaign(final Context context, String campaignName) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        UserLocalStore store = new UserLocalStore(context.getApplicationContext());

        RequestParams params = new RequestParams();
        params.put("campaign_name", campaignName);

        Log.i("Campaign", "Deleting campaign " + campaignName + "...");
        cli.postUserData("/deleteCampaign", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("Campaign", "Database response: " + new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Campaign", "Failed to delete campaign with responseBody: " + new String(responseBody));
            }
        });
    }

    public static void getCampaignDetailsThenOpen(final Context context, final Intent intent, String campaignName) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        UserLocalStore store = new UserLocalStore(context.getApplicationContext());

        RequestParams params = new RequestParams();
        params.put("campaign_name", campaignName);

        cli.getUserData("/campaignDetails", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                //ArrayList<String> characters = new ArrayList<>();
                ArrayList<String> characterNames = new ArrayList<>();
                ArrayList<String> characterIDs = new ArrayList<>();
                ArrayList<ArrayList<String>> journalEntries = new ArrayList<>();
                ArrayList<String> privateNotes = new ArrayList<>();
                ArrayList<String> publicNotes = new ArrayList<>();
                Log.d("Campaign", responseBody.toString());
                try {
                    JSONObject players = new JSONObject(responseBody.getString("Players"));
                    JSONArray JCharacterIDs = players.names();
                    if (JCharacterIDs != null) {
                        for (int i = 0; i < JCharacterIDs.length(); i++) {
                            characterIDs.add(JCharacterIDs.getString(i));
                            characterNames.add(players.getString(JCharacterIDs.getString(i)));
                        }
                    }

                    JSONObject journal = new JSONObject(responseBody.getString("Journal Entries"));
                    JSONArray journalKeys = journal.names();
                    if (journalKeys != null) {
                        for (int i = 0; i < journalKeys.length(); i++) {
                            ArrayList<String> entry = new ArrayList<>();
                            entry.add(journalKeys.getString(i));
                            entry.add(journal.getString(journalKeys.getString(i)));
                            journalEntries.add(entry);
                        }
                    }

                    JSONArray pubNotes = new JSONArray(responseBody.getString("Public Notes"));
                    for (int i = 0; i < pubNotes.length(); i++) {
                        publicNotes.add(pubNotes.getString(i));
                    }

                    JSONArray privNotes = new JSONArray(responseBody.getString("Private Notes"));
                    for (int i = 0; i < privNotes.length(); i++) {
                        privateNotes.add(privNotes.getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                intent.putExtra("campaign_characters", characterNames);
                intent.putExtra("campaign_character_ids", characterIDs);
                intent.putExtra("campaign_private_notes", privateNotes);
                intent.putExtra("campaign_public_notes", publicNotes);
                intent.putExtra("campaign_journal_entries", journalEntries);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("DataLoader", "Got response string: " + responseString);
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
        goToWaitScreen(context);
    }

    public static void storePublicNote(Context context, final int index, final String noteText, final String campaign) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        UserLocalStore store = new UserLocalStore(context.getApplicationContext());

        RequestParams params = new RequestParams();
        params.add("note", noteText);
        params.add("campaign_name", campaign);
        params.add("index", String.valueOf(index));

        cli.postUserData("/savePublicNotes", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("Campaign", "Successfully stored public note with responseBody: " + new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Campaign", "Failed to store public note with responseBody: " + new String(responseBody));
            }
        });
    }

    public static void storePrivateNote(Context context, final int index, final String noteText, final String campaign) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        UserLocalStore store = new UserLocalStore(context.getApplicationContext());

        RequestParams params = new RequestParams();
        params.add("note", noteText);
        params.add("campaign_name", campaign);
        params.add("index", String.valueOf(index));

        cli.postUserData("/savePrivateNotes", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("Campaign", "Successfully stored private note with responseBody: " + new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Campaign", "Failed to store private note with responseBody: " + new String(responseBody));
            }
        });
    }
    public static void storeJournalEntry(Context context, final String title, final String entry, final String campaign) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        UserLocalStore store = new UserLocalStore(context.getApplicationContext());

        RequestParams params = new RequestParams();
        params.add("title", title);
        params.add("entry", entry);
        params.add("campaign_name", campaign);

        cli.postUserData("/saveJournalEntry", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("Campaign", "Successfully stored journal entry with responseBody: "+new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Campaign", "Failed to store journal entry with responseBody: "+new String(responseBody));
            }
        });
    }

    public static void deletePublicNote(Context context, final int index, final String campaign) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        UserLocalStore store = new UserLocalStore(context.getApplicationContext());

        RequestParams params = new RequestParams();
        params.add("index", String.valueOf(index));
        params.add("campaign_name", campaign);

        cli.postUserData("/deletePublicNote", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("Campaign", "Successfully deleted public note with responseBody: "+new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Campaign", "Failed to delete public note with responseBody: "+new String(responseBody));
            }
        });
    }

    public static void deletePrivateNote(Context context, final int index, final String campaign) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        UserLocalStore store = new UserLocalStore(context.getApplicationContext());

        RequestParams params = new RequestParams();
        params.add("index", String.valueOf(index));
        params.add("campaign_name", campaign);

        cli.postUserData("/deletePrivateNote", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("Campaign", "Successfully deleted private note with responseBody: "+new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Campaign", "Failed to delete private note with responseBody: "+new String(responseBody));
            }
        });
    }

    public static void deleteJournalEntry(Context context, final int index, final String campaign) {
        ChroniclerRestClient cli = new ChroniclerRestClient(context);
        UserLocalStore store = new UserLocalStore(context.getApplicationContext());

        RequestParams params = new RequestParams();
        params.add("index", String.valueOf(index));
        params.add("campaign_name", campaign);

        cli.postUserData("/deleteJournalEntry", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("Campaign", "Successfully deleted journal entry with responseBody: " + new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Campaign", "Failed to delete journal entry with responseBody: " + new String(responseBody));
            }
        });
    }

    public static void storeCharSheet(Context context, final CharacterSheet c){
        storeCharSheet(context, c, true, false, 0);
    }

    public static void storeCharSheetThenFinish(Context context, final CharacterSheet c) {
        storeCharSheet(context, c, true, true, 0);
    }

    public static void storeCharSheet(Context context, final CharacterSheet c, final boolean newSheet, final int charID){
        storeCharSheet(context, c, newSheet, false, charID);
    }


    public static void updateCharSheet(Context context, final CharacterSheet c){
        int charID = CharactersActivity.openCharID;
        storeCharSheet(context,c,false,charID);
    }

    // Stores a given character sheet in the server-side database.
    public static void storeCharSheet(final Context context, final CharacterSheet c, final boolean newSheet, final boolean finishActivity, final int charID){
        final ChroniclerRestClient cli = new ChroniclerRestClient(context);
        StringEntity charEntity = null;
        try {
            charEntity = new StringEntity(c.toJSON(), ContentType.APPLICATION_JSON);
        } catch (JsonProcessingException e) {
            Log.e("STORECHAR","Error converting character sheet to JSON");
            e.printStackTrace();
        }
        if(charEntity!= null) {
            String url = "/storeChar?"+(newSheet?"new=true":"new=false")+"&id="+charID;
            cli.postUserData(url, charEntity, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    Log.i("STORECHAR", "Success: "+new String(response));
                    try {
                        JSONObject res = new JSONObject(new String(response));
                        String code = res.getString("code");
                        if(code.equals("success") && newSheet) {
                            // Could add to CONTENT and notify adapter as with remove but this is fine too
                            int newCharID = Integer.parseInt(res.getString("message"));
                            CharactersActivity.IDS.add(newCharID);
                            CharactersActivity.adapter.add(c.getName());
                            CharactersActivity.openCharID = newCharID;
                        } else if(code.equals("failure")) {
                            Log.d("STORECHAR", res.getString("message"));
                        } else {
                            Log.d("STORECHAR", "This should only happen when storing an existing sheet! Response = { code="+res.getString("code")+", message="+res.getString("message")+" }");
                        }
                    }catch(JSONException e) {
                        Log.d("STORECHAR", "This should never happen. ");
                    }

                    if (context instanceof Activity && newSheet) {
                        ((Activity) context).finish();
                    }
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
