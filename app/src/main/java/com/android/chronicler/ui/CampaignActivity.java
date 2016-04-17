package com.android.chronicler.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.android.chronicler.R;
import com.android.chronicler.character.CharacterSheet;
import com.android.chronicler.character.enums.AbilityID;
import com.android.chronicler.ui.fragments.AboutFragment;
import com.android.chronicler.ui.fragments.CampaignPlayersFragment;
import com.android.chronicler.ui.fragments.CombatFragment;
import com.android.chronicler.ui.fragments.FeatFragment;
import com.android.chronicler.ui.fragments.InventoryFragment;
import com.android.chronicler.ui.fragments.JournalFragment;
import com.android.chronicler.ui.fragments.PrivateNotesFragment;
import com.android.chronicler.ui.fragments.PublicNotesFragment;
import com.android.chronicler.ui.fragments.SheetFragment;
import com.android.chronicler.ui.fragments.SkillFragment;
import com.android.chronicler.ui.fragments.SpellFragment;
import com.android.chronicler.util.ChroniclerRestClient;
import com.android.chronicler.util.SkillsAdapter;
import com.android.chronicler.util.UserLocalStore;
import com.android.chronicler.util.ViewPagerTabs;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bjorn.
 *
 * An overview activity for the campaign: Should include the players and
 * their characters with an option to view character sheet if the user is the DM.
 * Should include an option to invite users to campaign as well.
 */
public class CampaignActivity extends FragmentActivity {
    // We have to decide whether we want to extend AppCompatActivity, thereby keeping the action bar
    // and perhaps using it somehow, e.g. putting the name and class and level of our character there
    // or instead extend FragmentActivity, in which case the fragment fills the whole screen (if we
    // like) or perhaps we could add a banner up top with name, class, level....

    private CharacterSheet character;

    // For fragment view stuff
    private ViewPager mPager;
    private SheetPagerAdapter mPagerAdapter;
    private static final int INITIAL_PAGE = 0;

    private String campaignName;
    private ArrayList<String> campaignCharacters;
    private ArrayList<String> campaignCharacterIDs;
    private ArrayList<String> privateNotes;
    private ArrayList<String> publicNotes;
    //This should probably be a ArrayList<ArrayList<String>>
    private ArrayList<ArrayList<String>> journalNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        Intent intent = getIntent();
        campaignName = intent.getStringExtra("CAMPAIGN_NAME");
        campaignCharacters = intent.getStringArrayListExtra("campaign_characters");
        if (intent.hasExtra("NEW_PLAYER") && !campaignCharacters.contains(intent.getStringExtra("NEW_PLAYER"))) {
            campaignCharacters.add(intent.getStringExtra("NEW_PLAYER"));
        }
        campaignCharacterIDs = intent.getStringArrayListExtra("campaign_character_ids");
        privateNotes = intent.getStringArrayListExtra("campaign_private_notes");
        publicNotes = intent.getStringArrayListExtra("campaign_public_notes");
        journalNotes = (ArrayList<ArrayList<String>>) intent.getSerializableExtra("campaign_journal_entries");
        // -------------------------------------------------------- FRAGMENT RELATED

        // Create the tab bar with - COMBAT SPELLS ABOUT FEATS
        final ViewPagerTabs pagerTabs = (ViewPagerTabs) findViewById(R.id.transactions_pager_tabs);
        pagerTabs.addTabLabels(
                R.string.campaign_player_list,
                R.string.campaign_private_notes,
                R.string.campaign_public_notes,
                R.string.campaign_journal);

        // Fragments are added to a list of fragments that are later put into mPagerAdapter.
        final List<SheetFragment> fragments = new Vector<SheetFragment>();
        // Call new instance and include a string 'type' to identify each fragment

        fragments.add(CampaignPlayersFragment.newInstance(campaignName, campaignCharacters, campaignCharacterIDs));
        fragments.add(PrivateNotesFragment.newInstance(campaignName, privateNotes));
        fragments.add(PublicNotesFragment.newInstance(campaignName, publicNotes));

        fragments.add(JournalFragment.newInstance(campaignName, journalNotes));


        // The view pager is an element that can shift through views by swiping right and left
        mPager = (ViewPager) findViewById(R.id.product_pager);

        // the view pager needs an adapter to handle the fragments, inflate the views, etc.
        mPagerAdapter = new SheetPagerAdapter(getSupportFragmentManager(), fragments);
        mPager.setAdapter(mPagerAdapter);

        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(pagerTabs);
        mPager.setCurrentItem(INITIAL_PAGE);
        mPager.setPageMargin(2);
        mPager.setPageMarginDrawable(R.color.tabs_color);

        // Pager tabs are the small tabs that scroll the fragments and have the names
        // of each fragment on top
        pagerTabs.onPageScrolled(INITIAL_PAGE, 0, 0); // should not be needed

    }

    public void setNotePublic(String note) {
        ((PublicNotesFragment) this.mPagerAdapter.getItem(2)).addNote(note);
    }

    public void setNotePrivate(String note) {
        ((PrivateNotesFragment) this.mPagerAdapter.getItem(1)).addNote(note);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        int currIndex = mPager.getCurrentItem();
        savedInstanceState.putInt("INDEX", currIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_character, menu);
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

    /**
     * A simple pager adapter for our fragments
     */
    private class SheetPagerAdapter extends FragmentStatePagerAdapter {
        private List<SheetFragment> fragments;

        public SheetPagerAdapter(FragmentManager fm, List<SheetFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public SheetFragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

}