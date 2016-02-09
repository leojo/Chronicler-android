package com.android.chronicler.ui;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.chronicler.R;
import com.android.chronicler.util.accDbLookup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andrea on 28.1.2016.
 */
public class CampaignActivity extends AppCompatActivity {
    private ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);
        Intent intent = getIntent();

        //ArrayList data = new ArrayList<Map<String, Object>>();
        //Map item = new HashMap<String, Object>();


        this.listView = (ListView) findViewById(R.id.CampaignListView);
        //SimpleAdapter adapter = new SimpleAdapter(this, )
        String[] from = new String[] {"campaign"};
        int[] to = new int[] {R.id.text};
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> m = new HashMap<>();
            m.put("campaign", "asdf" + i);
            fillMaps.add(m);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.activity_campaign, from, to);
        //ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_campaign, new String[]{"asdf1", "asdf2", "asdf3"});
        this.listView.setAdapter(adapter);
    }


}
