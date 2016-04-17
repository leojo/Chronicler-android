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
public class FeatOverviewActivity extends AppCompatActivity{

    public static FeatOverviewActivity overviewActivity;
    private Button addFeatBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feat_overview);
        overviewActivity = this;
        addFeatBtn = (Button)findViewById(R.id.addFeatBtn);

        if(!getIntent().getBooleanExtra("StartedForResult", true)) {
            addFeatBtn.setVisibility(View.GONE);
        } else {
            Intent intent2 = new Intent(this, SearchActivity.class);
            intent2.putExtra("TYPE", "spell");
            intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            this.startActivity(intent2);
        }


        Intent intent2 = new Intent(this, SearchActivity.class);
        intent2.putExtra("TYPE", "feat");
        intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        this.startActivity(intent2);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final String feat = intent.getStringExtra("featName");
        TextView featName = (TextView)findViewById(R.id.featName);
        featName.setText(feat);

        addFeatBtn.setVisibility(View.VISIBLE);

        addFeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("toBeAdded",feat);
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
