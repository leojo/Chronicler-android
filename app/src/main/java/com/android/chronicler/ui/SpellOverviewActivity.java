package com.android.chronicler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.chronicler.R;

/**
 * Created by andrea on 16.4.2016.
 */
public class SpellOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_overview);
        final String spell = getIntent().getStringExtra("spellName");
        TextView spellName = (TextView)findViewById(R.id.spellName);
        spellName.setText(spell);

        Button addSpellBtn = (Button)findViewById(R.id.addSpellBtn);
        addSpellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("RESULT", "SpellOverViewActivity: About to set the result");
                Intent intent=new Intent();
                intent.putExtra("toBeAdded",spell);
                setResult(1,intent);
                Log.i("RESULT", "SpellOverviewActivity, result has extra "+spell);
                finish();
            }
        });
    }
}
