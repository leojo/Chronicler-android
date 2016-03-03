package com.android.chronicler;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.chronicler.character.CharacterSheet;
import com.android.chronicler.ui.CampaignsActivity;
import com.android.chronicler.ui.CharacterActivity;
import com.android.chronicler.ui.CharactersActivity;
import com.android.chronicler.ui.LoginActivity;
import com.android.chronicler.util.ChroniclerRestClient;
import com.android.chronicler.util.DataLoader;
import com.android.chronicler.util.UserLocalStore;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    private UserLocalStore store;
    private DataLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        store = new UserLocalStore(getApplicationContext());
        loader = new DataLoader();
        if(!store.userInSession()) {
            redirectToLogin();
            Log.i("LOGIN", "Seem to be logged in, this is the cookie: " + store.getUserCookie());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void openCharacters(View view) {
        Intent intent = new Intent(this, CharactersActivity.class);
        loader.readyCharlistThenStart(this, intent);
    }


    //  SHOULD PUT THIS CODE IN DATALOADER class .... thats exactly what it was created for, see
    // loader.readySheetThenStart(intent) above ^
    public void openCampaigns(View view) {
        final Intent intent = new Intent(this, CampaignsActivity.class);
        loader.readyCampaignlistThenStart(this, intent);
    }

    public void logout(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        store.clearSession();
    }


}
