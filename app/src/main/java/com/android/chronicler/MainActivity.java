package com.android.chronicler;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.chronicler.ui.CampaignsActivity;
import com.android.chronicler.ui.CharactersActivity;
import com.android.chronicler.ui.InvitesActivity;
import com.android.chronicler.ui.LoginActivity;
import com.android.chronicler.util.DataLoader;
import com.loopj.android.http.PersistentCookieStore;

import java.util.List;

import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;


public class MainActivity extends AppCompatActivity {
<<<<<<< HEAD

=======
>>>>>>> 62649bcb6512a8c31cdc33b93c152132bba32a27
    private static PersistentCookieStore cookieStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cookieStore = new PersistentCookieStore(this);
        boolean inSession = userInSession(cookieStore.getCookies());
        getSupportActionBar().hide();
        if(!inSession) {
            redirectToLogin();
        }

        TextView heading = (TextView) findViewById(R.id.Chronicler_heading);
        Typeface font = Typeface.createFromAsset(getAssets(), "DroidSerif-Regular.ttf");
        heading.setTypeface(font);
    }

    // Checks whether there is an unexpired cookie in the cookie store
    public boolean userInSession(List<Cookie> cookies) {
        Cookie userCookie = new BasicClientCookie("user", null);
        for(Cookie c : cookies) {
            if(c.getName().equals("user")) userCookie = c;
        }
        return (userCookie.getValue() != null);
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

    // Redirect to login screen whenever the cookie expires
    public void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    // Open a list of the user's characters; uses dataloader to fire the async request
    public void openCharacters(View view) {
        Intent intent = new Intent(this, CharactersActivity.class);
        DataLoader.readyCharlistThenStart(this, intent);
    }
    // Opens a list of the user's campaigns; uses dataloader to fire async request
    public void openCampaigns(View view) {
        final Intent intent = new Intent(this, CampaignsActivity.class);
        DataLoader.readyCampaignlistThenStart(this, intent);
    }


    // Opens the list of invites. For now it is a hard-coded test vector,
    // this will however use the dataloader to get the list of invites.
    public void openInvites(View view) {
        Intent intent = new Intent(this, InvitesActivity.class);
        DataLoader.readyInvitesThenStart(this, intent);
    }


    // logout user: clear his session
    public void logout(View view) {
        cookieStore.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }


}
