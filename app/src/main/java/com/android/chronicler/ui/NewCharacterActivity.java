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

import java.util.ArrayList;

/**
 * Created by leo on 4.3.2016.
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
        final ArrayList<String> raceList = intent.getStringArrayListExtra("raceList");
        final ArrayList<String> classList = intent.getStringArrayListExtra("classList");

        raceSp = (Spinner)findViewById(R.id.raceSpinner);
        classSp = (Spinner)findViewById(R.id.classSpinner);
        nameField = (EditText)findViewById(R.id.charNameField);
        previewTextView = (TextView)findViewById(R.id.previewText);
        createButton = (Button)findViewById(R.id.confirmButton);
        previewText = getString(R.string.new_character_preview_text);
        buttonText = getString(R.string.new_character_button_text);

        ArrayAdapter<String> raceSpAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, raceList);
        raceSp.setAdapter(raceSpAdapter);
        
        ArrayAdapter<String> classSpAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classList);
        classSp.setAdapter(classSpAdapter);

        race = (String)raceSp.getSelectedItem();
        className = (String)classSp.getSelectedItem();
        name = nameField.getText().toString();

        previewTextView.setText(String.format(previewText,race,className,name));
        createButton.setText(String.format(buttonText,name));

        // LISTENERS ====================================
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
                previewTextView.setText(String.format(previewText,race,className,name));
                createButton.setText(String.format(buttonText,name));
            }
        });
    }
}
