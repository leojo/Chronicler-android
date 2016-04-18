package com.android.chronicler.ui;

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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.chronicler.R;
import com.android.chronicler.character.SheetObject;
import com.android.chronicler.character.spell.SpellSlot;
import com.android.chronicler.util.DataLoader;
import com.android.chronicler.util.SheetAdapter;

import java.util.ArrayList;

/**
 * Created by andrea on 10.4.2016.
 */
public class SearchActivity extends AppCompatActivity {
    // TODO: http://developer.android.com/training/search/setup.html
    public static SearchActivity searchActivity;
    public static SheetAdapter adapter;
    public static ArrayList<SheetObject> searchResults;
    private SearchView searchView;
    private String searchType;
    private static ListView resultsView;
    private static TextView searchMessage;
    public static final String SHEET_OBJECT = "SHEET_OBJECT";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.d("SEARCH", "OnCreate fired");
        searchActivity = this;
        // Define this final variable to be able to call start
        // activity for result on this within inner class (in click listener)
        final SearchActivity thisActivity = this;

        // Hide results view for now and just show the message
        resultsView = (ListView)findViewById(R.id.resultsView);
        searchMessage = (TextView)findViewById(R.id.searchMessage);

        searchType = getIntent().getStringExtra("TYPE");
        initListAndDialog();

        if(searchResults==null) searchResults = new ArrayList<>();
        adapter = new SheetAdapter(this,searchResults);
        adapter.searching = true;
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
                        spellIntent.putExtra(SHEET_OBJECT, searchResults.get(position));
                        spellIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(spellIntent);
                        break;
                    case "feat":
                        featIntent.putExtra("featName", searchResults.get(position).getName());
                        featIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(featIntent);
                        break;
                    case "item":
                        itemIntent.putExtra("itemName", searchResults.get(position).getName());
                        itemIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(itemIntent);
                }
            }
        });
    }


    // This will never trigger while Search Activity is a single top activity, we have
    // to figure something out to work around this problem.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Grab the result from the OverviewActivity and define it as the result
        // for this activity to send it back to the fragments:
        Intent intent=new Intent();
        intent.putExtra("toBeAdded",data.getStringExtra("toBeAdded"));
        setResult(1,intent);
        finish();
    }

    @Override
    public void finish() {
        Log.d("SEARCH", "Oh no! It dies!");
        super.finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("SEARCH", "onNewIntent fired!");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
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

        switch (searchType) {
            case "spell":
                searchView.setQueryHint("Fireball");
                break;
            case "feat":
                searchView.setQueryHint("Initiative");
                break;
            case "item":
                searchView.setQueryHint("Pouch");
        }



        //MenuItemCompat searchItem = new Menu
        final MenuItem searchItem = menu.findItem(R.id.search);

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                searchView.setIconified(false);
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

    private void initListAndDialog()  {
        showView(searchMessage);
        hideView(resultsView);
        searchMessage.setText("Find a particular "+searchType+" by using the search above.");
    }

    public static void showResults() {
        hideView(searchMessage);
        showView(resultsView);
    }

    public static void noResults() {
        hideView(resultsView);
        searchMessage.setText("No results found for your query.");
        showView(searchMessage);
    }

    private static void hideView(View v) {
        v.setVisibility(View.GONE);
    }

    private static void showView(View v) {
        v.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
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

