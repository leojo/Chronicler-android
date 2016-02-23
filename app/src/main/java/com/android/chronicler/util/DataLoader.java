package com.android.chronicler.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.chronicler.character.CharacterSheet;
import com.android.chronicler.character.skill.Skill;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
                Log.i("USERGET", new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

}
