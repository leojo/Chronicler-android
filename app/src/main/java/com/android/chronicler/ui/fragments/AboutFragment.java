package com.android.chronicler.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.chronicler.R;
import com.android.chronicler.character.CharacterSheet;
import com.android.chronicler.character.ability.AbilityScores;
import com.android.chronicler.util.CompactContentView;
import com.android.chronicler.util.ContentView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Fragment for the CharacterActivity: This is the screen that includes all information about
 * the character that doesn't fall into any of the other categories.
 *
 * Created by andrea on 26.2.2016.
 */
public class AboutFragment extends SheetFragment {
    private static CharacterSheet cs;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.about_fragment_layout, container, false);
        HashMap<String,String> aboutVals = getVals(cs);
        populate(rootView,aboutVals);

        return rootView;
    }

    private void populate(ViewGroup rootView, HashMap<String,String> aboutVals){
        for(int i=0; i<rootView.getChildCount(); i++){
            final View v = rootView.getChildAt(i);

            if(v instanceof ContentView){
                Log.d("POPULATE", "Setting " + ((ContentView) v).id);
                ((ContentView) v).updateText(aboutVals.get(((ContentView) v).id.toLowerCase()));
                Log.d("POPULATE", "set to " + ((ContentView) v).value);

                if(((ContentView) v).editable){
                    ((ContentView) v).valueView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean hasFocus) {
                            if (!hasFocus) {
                                String newVal = ((ContentView) v).valueView.getText().toString();
                                Log.d("UPDATE_FIELD", ((ContentView) v).id + " lost focus! Should store now! Value: " + newVal);
                                cs.updateField(((ContentView) v).id, newVal);
                                ((ContentView) v).value=newVal;
                                Log.d("UPDATE_FIELD", "Value in characterSheet alignment is now: " + cs.getAlignment());
                            }
                            else
                            Log.d("UPDATE_FIELD", ((ContentView) v).id + " gained focus!");
                        }
                    });
                }
            }
            else if(v instanceof CompactContentView){
                Log.d("POPULATE","Setting "+((CompactContentView) v).id);
                ((CompactContentView) v).updateText(aboutVals.get(((CompactContentView) v).id.toLowerCase()));
                Log.d("POPULATE","set to "+((CompactContentView) v).value);

                if(((CompactContentView) v).editable){
                    ((CompactContentView) v).valueView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean hasFocus) {
                            if (!hasFocus) {
                                String newVal = ((CompactContentView) v).valueView.getText().toString();
                                Log.d("UPDATE_FIELD", ((CompactContentView) v).id + " lost focus! Should store now! Value: " + newVal);
                                Log.d("UPDATE_FIELD", "The id we are updating is actually "+((CompactContentView) v).id);
                                cs.updateField(((CompactContentView) v).id, newVal);
                                ((CompactContentView) v).value=newVal;
                                Log.d("UPDATE_FIELD", "Value in characterSheet alignment is now: " + cs.getAlignment());
                            }
                            else
                                Log.d("UPDATE_FIELD", ((CompactContentView) v).id + " gained focus!");
                        }
                    });
                }
            }
            else if(v instanceof ViewGroup){
                //Log.d("POPULATE", "Hold my beer, I'm going in!");
                populate((ViewGroup) v, aboutVals);
            }
        }
    }

    // newInstance is called when the CharacterActivity is started and the fragments get
    // created. Here is where we would put our arguments specific to that fragment (say, a list of spells)
    // as arguments for this function.
    public static AboutFragment newInstance(String type, CharacterSheet characterSheet) {
        cs = characterSheet;
        Bundle args = new Bundle();
        args.putString("ID", type);
        AboutFragment aboutFrag = new AboutFragment();
        aboutFrag.setArguments(args);

        return aboutFrag;
    }

    private static HashMap<String,String> getVals(CharacterSheet cs){
        HashMap<String,String> vals = new HashMap<>();
        Log.d("POPULATE","getting values");

        // The fluff stuff:
        vals.put("name",cs.getName());
        vals.put("race",cs.getRace());
        vals.put("align",cs.getAlignment());
        vals.put("gender",cs.getGender());
        vals.put("deity",cs.getDeity());
        vals.put("eyes",cs.getEyes());
        vals.put("hair",cs.getHair());
        vals.put("height",cs.getHeight());
        vals.put("weight",cs.getWeight());
        vals.put("size",cs.getSize());
        vals.put("skin",cs.getSkin());

        // The ability score stuff:
        AbilityScores ass = cs.getAbilityScores();
        String[] scoreNames = new String[]{"str","con","dex","int","wis","cha"};
        String[] modNames = new String[]{"strmod","dexmod","conmod","intmod","wismod","chamod"};
        int[] scoreVals = ass.getAbilityScoreTotals();
        int[] modVals = ass.getAbilityScoreMods();
        for(int i=0; i<6; i++){
            vals.put(scoreNames[i],""+scoreVals[i]);
            vals.put(modNames[i],""+modVals[i]);
        }
        return vals;
    }
}
