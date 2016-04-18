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
public class SheetObjectOverviewActivity extends AppCompatActivity {

    private Button addBtn;
    public static SheetObjectOverviewActivity overviewActivity;
    private String type;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_object_overview);
        overviewActivity = this;
        addBtn = (Button)findViewById(R.id.addBtn);
        type = getIntent().getStringExtra("TYPE");

        if(!getIntent().getBooleanExtra("StartedForResult", true)) {
            addBtn.setVisibility(View.GONE);
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
        final SheetObject sheetObject = (SheetObject)intent.getSerializableExtra(SearchActivity.SHEET_OBJECT);
        populate(sheetObject);

        addBtn.setVisibility(View.VISIBLE);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("toBeAdded", sheetObject.getName());
                intent.putExtra(SearchActivity.SHEET_OBJECT, sheetObject);
                overviewActivity.setResult(1, intent);
                SearchActivity.searchActivity.finish();
                overviewActivity.finish();

            }
        });
    }

    private void populate(SheetObject sheetObject){
        String html = sheetObject.longDescr();
        TextView sheetObjectDescr = (TextView)findViewById(R.id.sheetObjectDescr);
        if(html.indexOf("</div>") == -1) html = "<p><h3>"+sheetObject.getName()+"</h3></p>"+html;
        sheetObjectDescr.setText(Html.fromHtml(html));
    }


    @Override
    public void onBackPressed() {
        if(!getIntent().getBooleanExtra("StartedForResult", true)) {
            super.onBackPressed();
        } else {
            Log.d("SEARCH", "This should be happening");
            Intent intent2 = new Intent(this, SearchActivity.class);
            intent2.putExtra("TYPE", type);
            intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            this.startActivity(intent2);
        }
    }
}
