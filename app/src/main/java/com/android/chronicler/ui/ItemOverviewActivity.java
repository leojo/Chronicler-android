package com.android.chronicler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.chronicler.R;

/**
 * Created by andrea on 16.4.2016.
 */
public class ItemOverviewActivity extends AppCompatActivity {


    public static ItemOverviewActivity overviewActivity;
    private Button addItemBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_overview);
        overviewActivity = this;
        addItemBtn = (Button)findViewById(R.id.addItemBtn);

        if(!getIntent().getBooleanExtra("StartedForResult", true)) {
            addItemBtn.setVisibility(View.GONE);
        } else {
            Intent intent2 = new Intent(this, SearchActivity.class);
            intent2.putExtra("TYPE", "item");
            intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            this.startActivity(intent2);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final String item = intent.getStringExtra("itemName");
        TextView itemName = (TextView)findViewById(R.id.itemName);
        itemName.setText(item);

        addItemBtn.setVisibility(View.VISIBLE);

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("toBeAdded",item);
                overviewActivity.setResult(1,intent);
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
