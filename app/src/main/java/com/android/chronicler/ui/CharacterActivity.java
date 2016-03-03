package com.android.chronicler.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.android.chronicler.R;
import com.android.chronicler.character.CharacterSheet;
import com.android.chronicler.ui.fragments.CombatFragment;
import com.android.chronicler.ui.fragments.FeatFragment;
import com.android.chronicler.ui.fragments.SheetFragment;
import com.android.chronicler.ui.fragments.SkillFragment;
import com.android.chronicler.ui.fragments.SpellFragment;
import com.android.chronicler.util.SkillsAdapter;
import com.android.chronicler.util.ViewPagerTabs;

import java.util.List;
import java.util.Vector;

/**
 * Created by andrea on 28.1.2016.
 * Character activity is the character sheet with all information about the character.
 * It is put together of several fragments that include information about the character's combat
 * stats, their spells, feats and skills and a generic "about" fragment containing information
 * such as name, alignment, hair color etc.
 **/
public class CharacterActivity extends FragmentActivity {
    // We have to decide whether we want to extend AppCompatActivity, thereby keeping the action bar
    // and perhaps using it somehow, e.g. putting the name and class and level of our character there
    // or instead extend FragmentActivity, in which case the fragment fills the whole screen (if we
    // like) or perhaps we could add a banner up top with name, class, level....

    private CharacterSheet character;
    ListView skillsView;
    private SkillsAdapter adapter;

    // For fragment view stuff
    private ViewPager mPager;
    private SheetPagerAdapter mPagerAdapter;
    private static final int INITIAL_PAGE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);
        // -------------------------------------------------------- FRAGMENT RELATED

        // Create the tab bar with - COMBAT SPELLS ABOUT FEATS
        final ViewPagerTabs pagerTabs = (ViewPagerTabs) findViewById(R.id.transactions_pager_tabs);
        pagerTabs.addTabLabels(R.string.charactersheet_about_tab, R.string.charactersheet_combat_tab,
                R.string.charactersheet_spells_tab, R.string.charactersheet_feats_tab, R.string.charactersheet_skills_tab);

        character = (CharacterSheet)getIntent().getSerializableExtra("CharacterSheet");

        // Fragments are added to a list of fragments that are later put into mPagerAdapter.
        final List<SheetFragment> fragments = new Vector<SheetFragment>();
        // Call new instance and include a string 'type' to identify each fragment
        fragments.add(SheetFragment.newInstance("ABOUT"));
        fragments.add(CombatFragment.newInstance("COMBAT"));
        fragments.add(SpellFragment.newInstance("SPELLS"));
        fragments.add(FeatFragment.newInstance("FEATS"));
        fragments.add(SkillFragment.newInstance("SKILLS",character.getSkills()));


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
        pagerTabs.onPageScrolled(INITIAL_PAGE, 0, 0); // should not be needed

        // ------------------------------------------------------------------------------------------

        // LEO STUFF
        /* skillsView = (ListView)findViewById(R.id.skillsView);
        character = (CharacterSheet)getIntent().getSerializableExtra("CharacterSheet");
        skillsView.setAdapter(new SkillsAdapter(this, character.getSkills()));

        ((TextView)this.findViewById(R.id.charName)).setText(character.getName());
        ((TextView)this.findViewById(R.id.classLevels)).setText(character.getCharacterClass());
        ((TextView)this.findViewById(R.id.strInfo)).setText("STR: "+character.getAbilityScores().get(AbilityID.STR).getTotalValue()+"  -  Mod: "+character.getAbilityScores().get(AbilityID.STR).getModifier());
        ((TextView)this.findViewById(R.id.dexInfo)).setText("DEX: "+character.getAbilityScores().get(AbilityID.DEX).getTotalValue()+"  -  Mod: "+character.getAbilityScores().get(AbilityID.DEX).getModifier());
        ((TextView)this.findViewById(R.id.conInfo)).setText("CON: "+character.getAbilityScores().get(AbilityID.CON).getTotalValue()+"  -  Mod: "+character.getAbilityScores().get(AbilityID.CON).getModifier());
        ((TextView)this.findViewById(R.id.intInfo)).setText("INT: "+character.getAbilityScores().get(AbilityID.INT).getTotalValue()+"  -  Mod: "+character.getAbilityScores().get(AbilityID.INT).getModifier());
        ((TextView)this.findViewById(R.id.wisInfo)).setText("WIS: "+character.getAbilityScores().get(AbilityID.WIS).getTotalValue()+"  -  Mod: "+character.getAbilityScores().get(AbilityID.WIS).getModifier());
        ((TextView)this.findViewById(R.id.chaInfo)).setText("CHA: "+character.getAbilityScores().get(AbilityID.CHA).getTotalValue()+"  -  Mod: "+character.getAbilityScores().get(AbilityID.CHA).getModifier());
        ((TextView)this.findViewById(R.id.hpInfo)).setText("HP: To be added");*/
    }

// ------------------------- FRAGMENT RELATED

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        int currIndex = mPager.getCurrentItem();
        savedInstanceState.putInt("INDEX", currIndex);
    }



    // ----------------------------------------------------------------------------------------------

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
