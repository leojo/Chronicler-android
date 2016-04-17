package com.android.chronicler.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
    public static ArrayAdapter<String> adapter;
    private ArrayList<String> searchResults;
    private String searchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchType = getIntent().getStringExtra("TYPE");
        searchResults = new ArrayList<>();
        searchResults.add("Dummy result 1");
        searchResults.add("Dummy result 2");
        searchResults.add("Dummy result 3");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, searchResults);
        ListView resultsView = (ListView)findViewById(R.id.resultsView);
        resultsView.setAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            DataLoader.handleSearchQuery(getApplication(), intent, searchType, query);
            //use the query to search your data somehow
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        return true;
    }
}

