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
public class FeatOverviewActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feat_overview);
        final String feat = getIntent().getStringExtra("featName");
        TextView featName = (TextView)findViewById(R.id.featName);
        featName.setText(feat);

        Button addFeatBtn = (Button)findViewById(R.id.addFeatBtn);
        addFeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("toBeAdded",feat);
                setResult(1,intent);
                finish();
            }
        });
    }
}
