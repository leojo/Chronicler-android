package com.android.chronicler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.chronicler.R;
import com.android.chronicler.character.SheetObject;

/**
 * Created by andrea on 16.4.2016.
 */
public class SpellOverviewActivity extends AppCompatActivity {

    private Button addSpellBtn;
    public static SpellOverviewActivity overviewActivity;
    private String type;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_overview);
        overviewActivity = this;
        addSpellBtn = (Button)findViewById(R.id.addSpellBtn);
        type = getIntent().getStringExtra("TYPE");

        if(!getIntent().getBooleanExtra("StartedForResult", true)) {
            addSpellBtn.setVisibility(View.GONE);
            populate((SheetObject)getIntent().getSerializableExtra(SearchActivity.SHEET_OBJECT));
        } else {
            Intent intent2 = new Intent(this, SearchActivity.class);
            intent2.putExtra("TYPE", type);
            intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            this.startActivity(intent2);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final SheetObject spell = (SheetObject)intent.getSerializableExtra(SearchActivity.SHEET_OBJECT);
        populate(spell);

        addSpellBtn.setVisibility(View.VISIBLE);
        addSpellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("RESULT", "SpellOverViewActivity: About to set the result");
                Intent intent=new Intent();
                intent.putExtra("toBeAdded",spell.getName());
                intent.putExtra(SearchActivity.SHEET_OBJECT,spell);
                overviewActivity.setResult(1, intent);
                Log.i("RESULT", "SpellOverviewActivity, result has extra " + spell.getName());
                SearchActivity.searchActivity.finish();
                overviewActivity.finish();

            }
        });
    }

    private void populate(SheetObject spell){
        String html = spell.longDescr();
        Log.i("SPELL",html);
        TextView spellDescr = (TextView)findViewById(R.id.spellDescr);
        if(html.indexOf("</div>") == -1) html = "<p><h3>"+spell.getName()+"</h3></p>"+html;
        spellDescr.setText(Html.fromHtml(html));
    }


    @Override
    public void onBackPressed() {
        if(!getIntent().getBooleanExtra("StartedForResult", true)) {
            super.onBackPressed();
        } else {
            Intent intent2 = new Intent(this, SearchActivity.class);
            intent2.putExtra("TYPE", type);
            intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            this.startActivity(intent2);
        }
    }
}
