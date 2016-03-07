package com.android.chronicler.ui.fragments;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.chronicler.R;
import com.android.chronicler.util.ContentView;

/**
 * Created by andrea on 26.2.2016.
 */
public class CombatFragment extends SheetFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.combat_fragment_layout, container, false);

        int n = rootView.getChildCount();
        for(int i=0; i<n; i++) {
            View v = rootView.getChildAt(i);
            if(v instanceof ContentView && ((ContentView)v).isEditable()) {
                v.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Log.i("EDITABLE", "CLICK! Should be making this editable");
                        TextView valView = (TextView)v.findViewById(R.id.valueView);
                        valView.setFocusable(true);
                        valView.setFocusableInTouchMode(true);
                        valView.setCursorVisible(true);
                        valView.setInputType(InputType.TYPE_CLASS_TEXT);
                        valView.requestFocus();
                        valView.invalidate();
                        Log.i("EDITABLE", "This is the value of valView "+valView.getText());
                        Log.i("EDITABLE", "CLICK! Should now be editable");
                    }
                });
            }
        }


        return rootView;
    }

    // newInstance is called when the CharacterActivity is started and the fragments get
    // created. Here is where we would put our arguments specific to that fragment (say, a list of spells)
    // as arguments for this function.
    public static CombatFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("ID", type);
        CombatFragment combatFrag = new CombatFragment();
        combatFrag.setArguments(args);

        return combatFrag;
    }
}
