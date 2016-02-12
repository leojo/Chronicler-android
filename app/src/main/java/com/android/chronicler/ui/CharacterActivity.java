package com.android.chronicler.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.chronicler.character.*;
import com.android.chronicler.R;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.logging.Level;

public class CharacterActivity extends ActionBarActivity {

    //private final CharacterSheet character = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        CharacterSheet cs = new CharacterSheet("Gunther", "Human", "Barista");
        String JSON;
        try {
            Log.d("CHARACTERSHEET","Writing as JSON...");
            JSON = cs.toJSON();
        } catch (JsonProcessingException e) {
            Log.e("CHARACTERSHEET",e.getMessage());
            return;
        }
        CharacterSheet loaded;
        try {
            Log.d("CHARACTERSHEET","Loading from JSON...");
            loaded = CharacterSheet.fromJSON(JSON);
        } catch (IOException e) {
            Log.e("CHARACTERSHEET",e.getMessage());
            return;
        }
        String JSON2 = "error";
        try {
            Log.d("CHARACTERSHEET","Writing as JSON...");
            JSON2 = loaded.toJSON();
        } catch (JsonProcessingException e) {
            Log.e("CHARACTERSHEET",e.getMessage());
        }
        Log.d("CHARACTERSHEET",JSON);
        Log.d("CHARACTERSHEET",JSON2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_character, menu);
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
}
