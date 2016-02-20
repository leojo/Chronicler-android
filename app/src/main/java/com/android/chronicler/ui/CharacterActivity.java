package com.android.chronicler.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.chronicler.character.*;
import com.android.chronicler.R;
import com.android.chronicler.character.enums.AbilityID;
import com.android.chronicler.character.enums.SavingThrowID;
import com.android.chronicler.util.DataLoader;
import com.android.chronicler.util.SkillsAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class CharacterActivity extends ActionBarActivity {

    private CharacterSheet character;
    ListView skillsView;
    private SkillsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);
        skillsView = (ListView)findViewById(R.id.skillsView);
        character = (CharacterSheet)getIntent().getSerializableExtra("CharacterSheet");
        skillsView.setAdapter(new SkillsAdapter(this, character.getSkills()));

        ((TextView)this.findViewById(R.id.charName)).setText(character.getName());
        ((TextView)this.findViewById(R.id.classLevels)).setText(character.getCharacterClass());
        ((TextView)this.findViewById(R.id.strInfo)).setText("STR: "+character.getAbilityScores().get(AbilityID.STR).getTotalValue()+"  -  Mod: "+character.getAbilityScores().get(AbilityID.STR).getModifier());
        ((TextView)this.findViewById(R.id.dexInfo)).setText("DEX: "+character.getAbilityScores().get(AbilityID.DEX).getTotalValue()+"  -  Mod: "+character.getAbilityScores().get(AbilityID.DEX).getModifier());
        ((TextView)this.findViewById(R.id.conInfo)).setText("CON: "+character.getAbilityScores().get(AbilityID.CON).getTotalValue()+"  -  Mod: "+character.getAbilityScores().get(AbilityID.CON).getModifier());
        ((TextView)this.findViewById(R.id.intInfo)).setText("INT: "+character.getAbilityScores().get(AbilityID.INT).getTotalValue()+"  -  Mod: "+character.getAbilityScores().get(AbilityID.INT).getModifier());
        ((TextView)this.findViewById(R.id.wisInfo)).setText("WIS: "+character.getAbilityScores().get(AbilityID.WIS).getTotalValue()+"  -  Mod: "+character.getAbilityScores().get(AbilityID.WIS).getModifier());
        ((TextView)this.findViewById(R.id.chaInfo)).setText("CHA: "+character.getAbilityScores().get(AbilityID.CHA).getTotalValue()+"  -  Mod: "+character.getAbilityScores().get(AbilityID.CHA).getModifier());
        ((TextView)this.findViewById(R.id.hpInfo)).setText("HP: To be added");
        ((TextView)this.findViewById(R.id.fortInfo)).setText(character.getSaves().getSaves().get(SavingThrowID.FORT).getTotal());
        ((TextView)this.findViewById(R.id.refInfo)).setText(character.getSaves().getSaves().get(SavingThrowID.REF).getTotal());
        ((TextView)this.findViewById(R.id.willInfo)).setText(character.getSaves().getSaves().get(SavingThrowID.WILL).getTotal());

        /*
        String JSON;
        try {
            Log.d("CHARACTERSHEET","Writing as JSON...");
            JSON = character.toJSON();
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
*/
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
