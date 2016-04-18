package com.android.chronicler.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.chronicler.R;
import com.android.chronicler.util.DataLoader;

import java.util.ArrayList;

/**
 * My account activity for information about your account, pending invites to campaigns, etc.
 */
public class MyAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        getSupportActionBar().hide();
    }

    // Opens the list of invites. For now it is a hard-coded test vector,
    // this will however use the dataloader to get the list of invites.
    public void openInvites(View view) {
        Intent intent = new Intent(this, InvitesActivity.class);
        DataLoader.readyInvitesThenStart(this, intent);
    }

    // Empty for now; the app has no settings atm.
    public void openSettings(View view) {
        return;
    }
}
