package com.android.chronicler.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.chronicler.character.CharacterSheet;
import com.loopj.android.http.AsyncHttpResponseHandler;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


import cz.msebera.android.httpclient.Header;

/**
 * Created by leo on 12.2.2016.
 */
public class DataLoader {

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

    public void readyCharlistThenStart(final Context context, final Intent intent) {
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
                }catch(JSONException e) {Log.i("CHARLIST", "JSON EXCEPTION");}
/*
                JSONParser parser = new JSONParser();

                try {
                    Object obj = parser.parse(JSONresponse);
                    Log.i("CHARLIST", obj.toString());
                    JSONArray jArray = (JSONArray)obj;
                    Log.i("CHARLIST", jArray.toString());
                    for(int i = 0; i< jArray.size(); i++) {
                        content.add((String)jArray.get(i));
                    }

                }catch(ParseException e) {
                    Log.i("CHARLIST", "Parsing exception: Not valid character list JSON");
                }
*/
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

}
