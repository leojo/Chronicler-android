package com.android.chronicler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.chronicler.R;
import com.android.chronicler.util.DataLoader;

import java.util.ArrayList;

/**
 * Created by leo on 4.3.2016.
 *
 * An activity to create a new character.
 */
public class NewCharacterActivity extends AppCompatActivity {
    Spinner raceSp, classSp;
    EditText nameField;
    TextView previewTextView;
    Button createButton;
    String previewText,buttonText,race, className,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_character);
        Intent intent = getIntent();
        // Get array list extras, that is the list of races and classes available for a new character
        final ArrayList<String> raceList = intent.getStringArrayListExtra("raceList");
        final ArrayList<String> classList = intent.getStringArrayListExtra("classList");

        raceSp = (Spinner)findViewById(R.id.raceSpinner);
        classSp = (Spinner)findViewById(R.id.classSpinner);
        nameField = (EditText)findViewById(R.id.charNameField);
        previewTextView = (TextView)findViewById(R.id.previewText);
        createButton = (Button)findViewById(R.id.confirmButton);
        previewText = getString(R.string.new_character_preview_text);
        buttonText = getString(R.string.new_character_button_text);

        // Set adapters so the list gets populated
        ArrayAdapter<String> raceSpAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, raceList);
        raceSp.setAdapter(raceSpAdapter);
        
        ArrayAdapter<String> classSpAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classList);
        classSp.setAdapter(classSpAdapter);

        race = (String)raceSp.getSelectedItem();
        className = (String)classSp.getSelectedItem();
        name = nameField.getText().toString();

        previewTextView.setText(String.format(previewText,race,className,name));
        createButton.setText(String.format(buttonText, name));

        // LISTENERS ====================================
        // Listeners for the lists so we can update the descriptive string
        //          " You will roll a gnome cleric..."
        raceSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                race = (String)raceSp.getSelectedItem();
                previewTextView.setText(String.format(previewText,race,className,name));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        classSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                className = (String) classSp.getSelectedItem();
                previewTextView.setText(String.format(previewText,race,className,name));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //  Listener to update the name field of our descriptive text.
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name = s.toString();
                previewTextView.setText(String.format(previewText, race, className, name));
                createButton.setText(String.format(buttonText, name));
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeChar();
            }
        });
    }

    // Data loader is used to request character creation when name, race and class
    // have been picked out.
    public void makeChar(){
        if (getCallingActivity() == null) {
            // If called with startActivity
            Intent intent = new Intent(this,CharacterActivity.class);
            DataLoader.readyNewSheetThenStart(this, intent, name, race, className);
        } else {
            // If called with startActivityForResult
            Intent intent = new Intent();
            intent.putExtra("CHARACTER_NAME", name);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
