package com.android.chronicler.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.chronicler.R;

/**
 *  Created by leo on 9.3.2016.
 *
 *  This is a simple spinning-wheel activity to show while we wait for a data request to finish
 */
public class WaitingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        ProgressBar spinner = (ProgressBar) findViewById(R.id.spinningWheel);
        spinner.setVisibility(View.VISIBLE);
        spinner.setIndeterminate(true);
    }

    // Intended to cancel the request we are waiting for if back button is pressed
    // does nothing as of now except log a message.
    @Override
    public void onBackPressed() {
        Log.i("LOADING_SCREEN", "Pressed the back key");
        super.onBackPressed();
    }

}
