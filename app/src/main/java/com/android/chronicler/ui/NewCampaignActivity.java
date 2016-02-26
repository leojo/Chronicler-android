package com.android.chronicler.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.chronicler.R;

/**
 * Created by bjorn on 24.2.2016.
 * An activity for creating a new campaign
 * PS. I'm not sure this activity is necessarily needed, we could use a new campaign fragment instead
 */
public class NewCampaignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_campaign);
    }
}
