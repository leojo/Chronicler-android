package com.android.chronicler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.android.chronicler.R;

public class CampaignNoteActivity extends AppCompatActivity {
    String text;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_note);

        Intent intent = getIntent();
        text = intent.getStringExtra("TEXT");

        editText = (EditText) findViewById(R.id.campaign_note);

        editText.setText(text);
    }

    @Override
    public void onBackPressed() {
        // TODO: Implement controller that saves value to server
        Intent intent = new Intent();
        intent.putExtra("TEXT", editText.getText().toString());
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

}
