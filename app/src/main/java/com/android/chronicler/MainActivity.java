package com.android.chronicler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.chronicler.ui.CampaignsActivity;
import com.android.chronicler.ui.CharactersActivity;
import com.android.chronicler.ui.LoginActivity;
import com.android.chronicler.util.DataLoader;
import com.loopj.android.http.PersistentCookieStore;

import java.util.List;

import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

/**
 * Created by andrea on 28.1.2016.
 * Main activity is a controller for the main screen. The main screen has the following options
 *      Characters - Generating a list of ones' characters.
 *      Campaigns - Generating a list of ones' campaigns.
 *      About - Maybe remove this? Maybe include a search option
 *      Logout - An option to logout, this doesn't need to be one of the "main buttons" necessarily
 */
public class MainActivity extends AppCompatActivity {

    private DataLoader loader;
    private static PersistentCookieStore cookieStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create a loader for loading character lists, campaign lists, etc from the main screen
        loader = new DataLoader();

        // Create the cookie store: We need this to be able to redirect instantly to
        // the login screen if the user's cookie has expired.
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

    // ----
    // UTIL
    // ----

    // Check if user is in session, i.e. has a valid cookie:
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


    // ------------
    // MENU OPTIONS
    // ------------

    // Menu option #1: Opens up a list of the user's characters and allows for the option
    // to create a new character
    public void openCharacters(View view) {
        Intent intent = new Intent(this, CharactersActivity.class);
        boolean inSession = loader.readyCharlistThenStart(this, intent);
        if(!inSession) redirectToLogin();
    }


    // Menu option #2: Opens up a list of the user's campaigns and allows for the
    // option to create a new campaign.
    // TODO: @Bjorn: I took the liberty to move your code to the Data Loader as I'd previously asked you, but it still needs to be fixed so that it uses the cookies and not the user name.
    public void openCampaigns(View view) {
        final Intent intent = new Intent(this, CampaignsActivity.class);
        boolean inSession = loader.readyCampListThenStart(this, intent);
        if(!inSession) redirectToLogin();
    }

    // Menu option #4 (Should probably but this elsewhere than in the main menu)
    // Allows the user to logout, thereby clearing the cookie store.
    public void logout(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        cookieStore.clear();
    }


}
