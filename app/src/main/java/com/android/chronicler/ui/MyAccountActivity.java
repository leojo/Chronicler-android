package com.android.chronicler.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.chronicler.R;
import com.android.chronicler.util.DataLoader;

import java.util.ArrayList;

public class MyAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
    }

    public void openInvites(View view) {
        Intent intent = new Intent(this, InvitesActivity.class);
        DataLoader.readyInvitesThenStart(this, intent);
    }

    public void openSettings(View view) {
        return;
    }
}
