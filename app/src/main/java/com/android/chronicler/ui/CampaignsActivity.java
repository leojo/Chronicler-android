package com.android.chronicler.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.chronicler.NewCampaignActivity;
import com.android.chronicler.R;
import com.android.chronicler.util.ChroniclerRestClient;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by andrea on 28.1.2016.
 */
public class CampaignsActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;


    ListView campaignListView;
    public List<String> CONTENT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaigns);

        Intent intent = getIntent();
        CONTENT = intent.getStringArrayListExtra("CampaignList");
        // ---------------------------------------
        // ADD SOMETHING TO CONTENT
        // ---------------------------------------
        //CONTENT = new ArrayList<>();
        //CONTENT.add("andrea");
        //CONTENT.add("leo");
        //CONTENT.add("bjorn");

        // ---------------------------------------
        // GET THE CAMPAIGN LIST VIEW:
        // ---------------------------------------
        campaignListView = (ListView)findViewById(R.id.CampaignListView);

        // ---------------------------------------------------------------------------------------
        // CREATE AN ADAPTER FOR ARRAY LISTS
        // This adapter will keep track of the CONTENT array and call some built in functions like
        // 'notifyDataSetChanged' when we call the .add() method on CONTENT, which makes it update our list.
        // If it wouldn't call the method by default, we could just make sure to call adapter.notifyDataSetChanged() each time something changes.
        // ----------------------------------------------------------------------------------------------------------
        // Important: We can easily make use of some of the abstract adapter classes that android
        // has to offer to suit our needs if we need to do something more complicated than this.
        // ----------------------------------------------------------------------------------------

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, CONTENT);

        // Set add button to footer
        Drawable addButtonDrawable = getDrawable(R.drawable.ic_add_circle_24dp);
        ImageView addButtonView = new ImageView(this);
        addButtonView.setPadding(20, 20, 20, 20);
        addButtonView.setImageDrawable(addButtonDrawable);

        campaignListView.addFooterView(addButtonView);

        // ---------------------------------------
        // ADD THE ADAPTER TO LIST VIEW
        // -----------------------------------
        campaignListView.setAdapter(adapter);

        // -------------------------------------------------------------------------------
        // CLICK LISTENER TO LIST VIEW
        // Lets add something the CONTENT on each click, just to see that the list expands.
        // --------------------------------------------------------------------------------
        campaignListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == adapter.getCount()) {
                    newCampaign();
                } else {
                    openCampaign();
                }
                //adapter.add("You just clicked item number "+position);

            }
        });
        // --------------------------------------
    }

    public void openCampaign() {
        Intent intent = new Intent(this, CampaignActivity.class);
        startActivity(intent);
    }

    public void newCampaign() {
        Intent intent = new Intent(this, NewCampaignActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_campaigns, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

