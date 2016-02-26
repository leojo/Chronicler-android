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
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;


public class MainActivity extends AppCompatActivity {

    private UserLocalStore store;
    private DataLoader loader;
    private static PersistentCookieStore cookieStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loader = new DataLoader();
        cookieStore = new PersistentCookieStore(this);
        boolean inSession = userInSession(cookieStore.getCookies());
        if(!inSession) {
            redirectToLogin();
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

    public boolean userInSession(List<Cookie> cookies) {
        Cookie userCookie = new BasicClientCookie("user", null);
        for(Cookie c : cookies) {
            if(c.getName().equals("user")) userCookie = c;
        }

        return (userCookie.getValue() != null);
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
        UserLocalStore store = new UserLocalStore(getApplicationContext());
        RequestParams user_data = new RequestParams("username", store.getUserData()[0]);
        ChroniclerRestClient.get("/campaignData", user_data, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                //CharacterSheet character = new CharacterSheet("Bob", "Elf", "Barbarian", new String(responseBody));
                ArrayList<String> responseContent = new ArrayList<>();
                for (int i = 0; i < responseBody.length(); i++) {
                    try {
                        responseContent.add(responseBody.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                intent.putExtra("CampaignList", responseContent);
                startActivity(intent);
            }

            /*@Override
            public void onFailure(int statusCode, Header[] headers, JSONArray responseBody, Throwable error) {
                //String response = (responseBody == null ? "Empty response" : new String(responseBody));
                Log.i("SKILLS", "Failure fetching campaign data");
            }*/
        });
    }

    public void logout(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        store.clearSession();
    }


}
