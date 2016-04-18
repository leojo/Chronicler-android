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
        searchActivity = this;
        Log.i("RESULT", "Search activity has been started");
        // Define this final variable to be able to call start
        // activity for result on this within inner class (in click listener)
        final SearchActivity thisActivity = this;

        // Hide results view for now and just show the message
        resultsView = (ListView)findViewById(R.id.resultsView);
        searchMessage = (TextView)findViewById(R.id.searchMessage);

        searchType = getIntent().getStringExtra("TYPE");
        Log.d("ITEMSEARCH", "Search type is " + searchType);
        initListAndDialog();

        if(searchResults==null) searchResults = new ArrayList<>();
        adapter = new SheetAdapter(this,searchResults);
        adapter.searching = true;
        resultsView.setAdapter(adapter);

        final Intent overviewIntent = new Intent(this, SheetObjectOverviewActivity.class);


        resultsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // The SpellFragment, FeatFragment or InventoryFragment start this
                // activity for result and the result should be a specific spell, feat or inventory.
                switch (searchType) {
                    case "spell":
                        Log.i("RESULT", "Search activity, reordering the spelloverview activity to front");
                        overviewIntent.putExtra("TYPE", "spell");
                        break;
                    case "feat":
                        overviewIntent.putExtra("TYPE", "spell");
                        break;
                    case "item":
                        overviewIntent.putExtra("TYPE", "spell");
                }
                overviewIntent.putExtra(SHEET_OBJECT, searchResults.get(position));
                overviewIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(overviewIntent);
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
            Log.d("ITEMSEARCH","Inside handleIntent, searchType is "+searchType);
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
        Log.i("RESULT", "PRESSED THE BACK BUTTON!!!!");
        if(SheetObjectOverviewActivity.overviewActivity != null) {
            SheetObjectOverviewActivity.overviewActivity.setResult(0);
            SheetObjectOverviewActivity.overviewActivity.finish();
        }
        this.finish();
    }
}

