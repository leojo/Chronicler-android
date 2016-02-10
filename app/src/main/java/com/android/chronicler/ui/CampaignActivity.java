package com.android.chronicler.ui;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.android.chronicler.R;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by andrea on 28.1.2016.
 */
public class CampaignActivity extends AppCompatActivity {
    ListView campaignListView;
    public List<String> CONTENT;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);
        // ---------------------------------------
        // ADD SOMETHING TO CONTENT
        // ---------------------------------------
        CONTENT = new ArrayList<>();
        CONTENT.add("andrea");
        CONTENT.add("leo");
        CONTENT.add("bjorn");
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
                adapter.add("You just clicked item number "+position);
            }
        });
        // --------------------------------------
        Intent intent = getIntent();
    }
}