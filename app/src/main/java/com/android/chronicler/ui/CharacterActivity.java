package com.android.chronicler.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.android.chronicler.character.*;
import com.android.chronicler.R;
import com.android.chronicler.character.enums.AbilityID;
import com.android.chronicler.util.SkillsAdapter;

import java.util.List;
import java.util.Vector;


public class CharacterActivity extends FragmentActivity {

    private CharacterSheet character;
    ListView skillsView;
    private SkillsAdapter adapter;

    // For fragment view stuff
    private ViewPager mPager;
    private SheetPagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);
        // -------------------------------------------------------- FRAGMENT RELATED


        final List<SheetFragment> fragments = new Vector<SheetFragment>();


        fragments.add(SheetFragment.newInstance("COMBAT"));
        fragments.add(SheetFragment.newInstance("SPELLS"));
        fragments.add(SheetFragment.newInstance("ABOUT"));
        fragments.add(SheetFragment.newInstance("FEATS"));


        mPager = (ViewPager) findViewById(R.id.product_pager);
        //mPager.setOffscreenPageLimit(availableProducts.size() - 1);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //if (state == ViewPager.SCROLL_STATE_DRAGGING)
                //    for (StoreFragment fragment : fragments) fragment.removeHint();
            }
        });
        //findViewById(R.id.product_pager).setBackgroundResource(R.drawable.void_panel);
        mPagerAdapter = new SheetPagerAdapter(getSupportFragmentManager(), fragments);
        mPager.setAdapter(mPagerAdapter);

        // ------------------------------------------------------------------------------------------

        // LEO STUFF
        skillsView = (ListView)findViewById(R.id.skillsView);
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
        ((TextView)this.findViewById(R.id.hpInfo)).setText("HP: To be added");
        //((TextView)this.findViewById(R.id.fortInfo)).setText(character.getSaves().getSaves().get(SavingThrowID.FORT).getTotal());
        //((TextView)this.findViewById(R.id.refInfo)).setText(character.getSaves().getSaves().get(SavingThrowID.REF).getTotal());
        //((TextView)this.findViewById(R.id.willInfo)).setText(character.getSaves().getSaves().get(SavingThrowID.WILL).getTotal());
    }

// ------------------------- FRAGMENT RELATED

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        int currIndex = mPager.getCurrentItem();
        savedInstanceState.putInt("INDEX", currIndex);
        /*for(int i=0; i<mPagerAdapter.getCount(); i++) {
            savedInstanceState.putInt(Integer.toString(i), mPagerAdapter.getItem(i).getAmount());
        }*/
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
     * A simple pager adapter that represents the products in the store.
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
