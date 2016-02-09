package com.android.chronicler.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

import com.android.chronicler.R;
import com.android.chronicler.util.accDbLookup;

import java.util.Map;

/**
 * Created by andrea on 28.1.2016.
 */
public class CampaignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);
        Intent intent = getIntent();
    }


}
