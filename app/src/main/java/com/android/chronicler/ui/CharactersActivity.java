package com.android.chronicler.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.chronicler.R;
import com.android.chronicler.util.DataLoader;

import java.util.List;

/**
 * Created by andrea on 28.1.2016.
 * Characters activity includes a list of the user's characters (both as player and as dungeon master)
 * and the option to create a new charater
 **/
public class CharactersActivity extends AppCompatActivity {


    private ArrayAdapter<String> adapter;
    ListView characterListView;
    public List<String> CONTENT;
    private DataLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);

        // ---------------------------------------
        // ADD TO DMCampaigns THE RESPONSE FROM SERVER
        // ---------------------------------------
        Intent intent = getIntent();
        loader = new DataLoader();
        CONTENT = intent.getStringArrayListExtra("CharacterList");
        characterListView = (ListView)findViewById(R.id.CharacterListView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, CONTENT); // Set add button to footer

        Drawable addButtonDrawable = getDrawable(R.drawable.ic_add_circle_24dp);
        ImageView addButtonView = new ImageView(this);
        addButtonView.setPadding(20, 20, 20, 20);
        addButtonView.setImageDrawable(addButtonDrawable);

        characterListView.addFooterView(addButtonView);

        characterListView.setAdapter(adapter);
        characterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ;if (position == adapter.getCount()) {
                    newSheet();
                } else {
                    openSheet();
                }
                //adapter.add("You just clicked item number "+position);

            }
        });
    }

    public void openSheet() {
        Intent intent = new Intent(this, CharacterActivity.class);
        loader.readySheetThenStart(this, intent);
    }

    public void newSheet() {
        Intent intent = new Intent(this, NewCharacterActivity.class);
        loader.readyCreateCharThenStart(this, intent);
    }

    public void newCharacter(View view, String name, String race, String charClass) {
        Intent intent = new Intent(this, NewCharacterActivity.class);
        loader.readyNewSheetThenStart(this, intent, name, race, charClass);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_characters, menu);
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
