package com.android.chronicler.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.chronicler.R;
import com.android.chronicler.character.CharacterSheet;
import com.android.chronicler.character.enums.SavingThrowID;
import com.android.chronicler.util.CompactContentView;
import com.android.chronicler.util.ContentView;

import java.util.HashMap;

/**
 * Fragment for the CharacterActivity: This is the character's combat information.
 *
 * Created by andrea on 26.2.2016.
 */
public class CombatFragment extends SheetFragment {

    private static CharacterSheet cs;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.combat_fragment_layout, container, false);
        HashMap<String,String> combatVals = getVals(cs);
        populate(rootView, combatVals);
        return rootView;
    }

    private void populate(ViewGroup rootView, HashMap<String,String> aboutVals){
        for(int i=0; i<rootView.getChildCount(); i++){
            final View v = rootView.getChildAt(i);

            if(v instanceof ContentView){
                ((ContentView) v).updateText(aboutVals.get(((ContentView) v).getCustomId().toLowerCase()));

                if(((ContentView) v).isEditable()){
                    ((ContentView) v).setValueViewOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean hasFocus) {
                            if (!hasFocus) {
                                String newVal = ((ContentView) v).getText();
                                cs.updateField(((ContentView) v).getCustomId(), newVal);
                                ((ContentView) v).updateText(newVal);
                            } else
                                Log.d("UPDATE_FIELD", ((ContentView) v).getCustomId() + " gained focus! value = " + ((ContentView) v).getValue() + " and text = " + ((ContentView) v).getText());
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
                            } else
                                Log.d("UPDATE_FIELD", ((CompactContentView) v).getCustomId() + " gained focus! value = " + ((CompactContentView) v).getValue() + " and text = " + ((CompactContentView) v).getText());
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

    private static HashMap<String,String> getVals(CharacterSheet cs){
        HashMap<String,String> vals = new HashMap<>();

        vals.put("ac",cs.getAc()+"");
        vals.put("touch",cs.getTouch()+"");
        vals.put("ff",cs.getFf()+"");
        vals.put("hp",cs.getHp()+"");
        vals.put("init",cs.getInitiative()+"");
        vals.put("bab",cs.getBaB()+"");
        vals.put("fort",cs.getSaves().getSaves().get(SavingThrowID.FORT).getTotal()+"");
        vals.put("ref",cs.getSaves().getSaves().get(SavingThrowID.REF).getTotal()+"");
        vals.put("will",cs.getSaves().getSaves().get(SavingThrowID.WILL).getTotal()+"");
        vals.put("speed",cs.getSpeed()+"");

        return vals;
    }


    // newInstance is called when the CharacterActivity is started and the fragments get
    // created. Here is where we would put our arguments specific to that fragment (say, a list of spells)
    // as arguments for this function.
    public static CombatFragment newInstance(String type,  CharacterSheet characterSheet) {
        cs = characterSheet;
        Bundle args = new Bundle();
        args.putString("ID", type);
        CombatFragment combatFrag = new CombatFragment();
        combatFrag.setArguments(args);
        return combatFrag;
    }
}
