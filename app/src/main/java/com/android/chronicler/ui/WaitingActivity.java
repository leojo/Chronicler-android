package com.android.chronicler.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.chronicler.R;
import com.loopj.android.http.AsyncHttpClient;

public class WaitingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        ProgressBar spinner = (ProgressBar) findViewById(R.id.spinningWheel);
        spinner.setVisibility(View.VISIBLE);
        spinner.setIndeterminate(true);
    }

    @Override
    public void onBackPressed() {
        Log.i("LOADING_SCREEN","Pressed the back key");
        super.onBackPressed();
        return;
    }

}
