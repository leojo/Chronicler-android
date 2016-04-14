package com.android.chronicler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.chronicler.R;

public class EditJournalActivity extends AppCompatActivity {
    private String body, title;
    private EditText titleField, bodyField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journal);

        Intent intent = getIntent();
        title = intent.getStringExtra("TITLE");
        body  = intent.getStringExtra("TEXT");

        titleField = (EditText) findViewById(R.id.entry_title);
        titleField.setText(title);

        bodyField = (EditText) findViewById(R.id.entry_body);
        bodyField.setText(body);
    }

    @Override
    public void onBackPressed() {
        // TODO: Implement controller that saves value to server
        Intent intent = new Intent();
        intent.putExtra("TITLE", titleField.getText().toString());
        intent.putExtra("TEXT", bodyField.getText().toString());
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

}
