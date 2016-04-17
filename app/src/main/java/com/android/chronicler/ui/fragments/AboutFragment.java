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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Fragment for the CharacterActivity: This is the screen that includes all information about
 * the character that doesn't fall into any of the other categories.
 *
 * Created by andrea on 26.2.2016.
 */
public class AboutFragment extends SheetFragment {
    private static CharacterSheet cs;
    private static final List<String> abilityFields = Arrays.asList(new String[]{"str", "dex", "con", "int", "wis", "cha"});

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.about_fragment_layout, container, false);
        HashMap<String,String> aboutVals = getVals(cs);
        populate(rootView, rootView, aboutVals);
        ((TextView) rootView.findViewById(R.id.classView)).setText(aboutVals.get("class"));
        ((TextView) rootView.findViewById(R.id.lvlView)).setText(aboutVals.get("level"));
        return rootView;
    }

    // Populates each field with values from the charactersheet and sets an onFocusChange listener
    private void populate(final ViewGroup rootView, ViewGroup viewGroup, HashMap<String,String> aboutVals){
        for(int i=0; i<viewGroup.getChildCount(); i++){
            final View v = viewGroup.getChildAt(i);

            if(v instanceof ContentView){
                ((ContentView) v).updateText(aboutVals.get(((ContentView) v).getCustomId().toLowerCase()));

                if(((ContentView) v).isEditable()){
                    final String id = ((ContentView) v).getCustomId();
                    final String modIdString = id+"Mod";
                    final int modId = getResources().getIdentifier(modIdString, "id", getActivity().getPackageName());
                    //
                    ((ContentView) v).setValueViewOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean hasFocus) {
                            if (!hasFocus) {
                                String newVal = ((ContentView) v).getText();
                                if(id.equalsIgnoreCase("dex")){
                                    Log.i("DEX_CHANGE","Dex is about to be changed to "+newVal+"! The value of AC is "+cs.getAc());
                                }
                                cs.updateField(((ContentView) v).getCustomId(), newVal);
                                String actualNewVal = cs.getField(((ContentView) v).getCustomId(), newVal);
                                ((ContentView) v).updateText(actualNewVal);

                                // If we updated an abilityscore:
                                if (abilityFields.contains(id)) {
                                    Log.i("UPDATE_MOD", "Should update " + id + "Mod now");
                                    if(id.equalsIgnoreCase("dex")){
                                        Log.i("DEX_CHANGE","Dex has just changed to "+newVal+"! The value of AC is "+cs.getAc());
                                    }
                                    ContentView modView = (ContentView) rootView.findViewById(modId);
                                    String oldMod = modView.getText();
                                    String newMod = cs.getField(modIdString,oldMod);
                                    modView.updateText(newMod);
                                }
                            }
                        }
                    });
                }
            }
            else if(v instanceof CompactContentView){
                ((CompactContentView) v).updateText(aboutVals.get(((CompactContentView) v).getCustomId().toLowerCase()));

                if(((CompactContentView) v).isEditable()){
                    ((CompactContentView) v).setValueViewOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean hasFocus) {
                            if (!hasFocus) {
                                String newVal = ((CompactContentView) v).getText();
                                cs.updateField(((CompactContentView) v).getCustomId(), newVal);
                                ((CompactContentView) v).updateText(newVal);
                            }
                        }
                    });
                }
            }
            else if(v instanceof ViewGroup){
                populate(rootView,(ViewGroup) v, aboutVals);
            }
        }
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
        String[] scoreNames = new String[]{"str","dex","con","int","wis","cha"};
        String[] modNames = new String[]{"strmod","dexmod","conmod","intmod","wismod","chamod"};
        int[] scoreVals = ass.getAbilityScoreTotals();
        int[] modVals = ass.getAbilityScoreMods();
        for(int i=0; i<6; i++){
            vals.put(scoreNames[i],""+scoreVals[i]);
            vals.put(modNames[i],""+modVals[i]);
        }

        vals.put("class",cs.getCharacterClass());
        vals.put("level","Level "+cs.getLevel());
        return vals;
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
}
