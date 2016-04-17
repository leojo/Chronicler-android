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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_overview);
        final String item = getIntent().getStringExtra("itemName");
        TextView itemName = (TextView)findViewById(R.id.itemName);
        itemName.setText(item);


        Button addItemBtn = (Button)findViewById(R.id.addItemBtn);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("toBeAdded",item);
                setResult(1,intent);
                finish();
            }
        });
    }
}
