package com.android.chronicler.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.chronicler.R;
import com.android.chronicler.util.DataLoader;

import java.util.ArrayList;

/**
 * Created by andrea on 10.4.2016.
 */
public class SearchActivity extends AppCompatActivity {
    // TODO: http://developer.android.com/training/search/setup.html
    public static SearchActivity searchActivity;
    public static ArrayAdapter<String> adapter;
    private ArrayList<String> searchResults;
    private SearchView searchView;
    private String searchType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchActivity = this;
        Log.i("RESULT", "Search activity has been started");
        // Define this final variable to be able to call start
        // activity for result on this within inner class (in click listener)
        final SearchActivity thisActivity = this;

        searchType = getIntent().getStringExtra("TYPE");
        searchResults = new ArrayList<>();
        searchResults.add("Dummy result 1");
        searchResults.add("Dummy result 2");
        searchResults.add("Dummy result 3");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, searchResults);
        ListView resultsView = (ListView)findViewById(R.id.resultsView);
        resultsView.setAdapter(adapter);

        final Intent spellIntent = new Intent(this, SpellOverviewActivity.class);
        final Intent featIntent = new Intent(this, FeatOverviewActivity.class);
        final Intent itemIntent = new Intent(this, ItemOverviewActivity.class);


        resultsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // The SpellFragment, FeatFragment or InventoryFragment start this
                // activity for result and the result should be a specific spell, feat or inventory.
                switch (searchType) {
                    case "spell":
                        Log.i("RESULT", "Search activity, reordering the spelloverview activity to front");
                        spellIntent.putExtra("spellName", searchResults.get(position));
                        spellIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(spellIntent);
                        break;
                    case "feat":
                        featIntent.putExtra("featName", searchResults.get(position));
                        featIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(featIntent);
                        break;
                    case "item":
                        itemIntent.putExtra("itemName", searchResults.get(position));
                        itemIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(itemIntent);
                }
            };
        });
    }


    // This will never trigger while Search Activity is a single top activity, we have
    // to figure something out to work around this problem.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Grab the result from the OverviewActivity and define it as the result
        // for this activity to send it back to the fragments:
        Log.i("RESULT", "Search Activity class, is the data null even here? "+(data==null)+"if not, can we get the toBeAdded extra? "+data.getStringExtra("toBeAdded"));
        Intent intent=new Intent();
        intent.putExtra("toBeAdded",data.getStringExtra("toBeAdded"));
        setResult(1,intent);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.i("SEARCH", "Were handling the intent");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Log.i("SEARCH", "The intent was an ACTION_SEARCH intent!");
            String query = intent.getStringExtra(SearchManager.QUERY);
            DataLoader.handleSearchQuery(getApplication(), intent, searchType, query);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        //MenuItemCompat searchItem = new Menu
        MenuItem searchItem = menu.findItem(R.id.search);

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                SearchActivity.searchActivity.onBackPressed();
                return true;
            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        Log.i("RESULT", "PRESSED THE BACK BUTTON!!!!");
        if(SpellOverviewActivity.overviewActivity != null) {
            SpellOverviewActivity.overviewActivity.setResult(0);
            SpellOverviewActivity.overviewActivity.finish();
        }
        if(FeatOverviewActivity.overviewActivity != null) {
            FeatOverviewActivity.overviewActivity.setResult(0);
            FeatOverviewActivity.overviewActivity.finish();
        }
        if(ItemOverviewActivity.overviewActivity != null) {
            ItemOverviewActivity.overviewActivity.setResult(0);
            ItemOverviewActivity.overviewActivity.finish();
        }
        this.finish();
    }
}

