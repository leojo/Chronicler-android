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

    private Button addSpellBtn;
    public static SpellOverviewActivity overviewActivity;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_overview);
        overviewActivity = this;
        addSpellBtn = (Button)findViewById(R.id.addSpellBtn);

        if(!getIntent().getBooleanExtra("StartedForResult", true)) {
            addSpellBtn.setVisibility(View.GONE);
        } else {
            Intent intent2 = new Intent(this, SearchActivity.class);
            intent2.putExtra("TYPE", "spell");
            intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            this.startActivity(intent2);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final String spell = intent.getStringExtra("spellName");
        TextView spellName = (TextView)findViewById(R.id.spellName);
        spellName.setText(spell);

        addSpellBtn.setVisibility(View.VISIBLE);


        addSpellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("RESULT", "SpellOverViewActivity: About to set the result");
                Intent intent=new Intent();
                intent.putExtra("toBeAdded",spell);
                overviewActivity.setResult(1,intent);
                Log.i("RESULT", "SpellOverviewActivity, result has extra "+spell);
                SearchActivity.searchActivity.finish();
                overviewActivity.finish();

            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent2 = new Intent(this, SearchActivity.class);
        intent2.putExtra("TYPE", "spell");
        intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        this.startActivity(intent2);
    }
}
