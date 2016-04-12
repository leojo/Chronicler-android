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
import android.widget.ListView;
import com.android.chronicler.R;
import com.android.chronicler.character.CharacterSheet;
import com.android.chronicler.character.enums.AbilityID;
import com.android.chronicler.ui.fragments.AboutFragment;
import com.android.chronicler.ui.fragments.CampaignPlayersFragment;
import com.android.chronicler.ui.fragments.CombatFragment;
import com.android.chronicler.ui.fragments.FeatFragment;
import com.android.chronicler.ui.fragments.InventoryFragment;
import com.android.chronicler.ui.fragments.SheetFragment;
import com.android.chronicler.ui.fragments.SkillFragment;
import com.android.chronicler.ui.fragments.SpellFragment;
import com.android.chronicler.util.ChroniclerRestClient;
import com.android.chronicler.util.SkillsAdapter;
import com.android.chronicler.util.ViewPagerTabs;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
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
    private static final int INITIAL_PAGE = 1;

    private String campaignName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        Intent intent = getIntent();
        campaignName = intent.getStringExtra("CAMPAIGN_NAME");
        // -------------------------------------------------------- FRAGMENT RELATED

        // Create the tab bar with - COMBAT SPELLS ABOUT FEATS
        final ViewPagerTabs pagerTabs = (ViewPagerTabs) findViewById(R.id.transactions_pager_tabs);
        pagerTabs.addTabLabels(R.string.campaign_player_list);

        // Fragments are added to a list of fragments that are later put into mPagerAdapter.
        final List<SheetFragment> fragments = new Vector<SheetFragment>();
        // Call new instance and include a string 'type' to identify each fragment
        ArrayList<String> demoList = new ArrayList<>();
        demoList.add("player1");
        demoList.add("player2");
        demoList.add("player3");
        demoList.add("player4");
        fragments.add(CampaignPlayersFragment.newInstance(demoList));
        //fragments.add(AboutFragment.newInstance("ABOUT",character));


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

    // Uses the RestClient to post an invite to a specific user.
    private void inviteToCampaign(String user) {
        ChroniclerRestClient cli = new ChroniclerRestClient(this);
        RequestParams params = new RequestParams();
        params.put("Campaign", campaignName);
        params.put("User", user);
        cli.postUserData("/inviteToCampaign", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("Campaign", "Successfully invited player to campaign: "+responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Campaign", "Failed to invite player to campaign");
            }
        });
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