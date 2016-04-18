package com.android.chronicler.util;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.chronicler.R;

/**
 * Created by leo on 17.4.2016.
 */
public class SkillListItemView extends LinearLayout {
    private final String name;
    private TextView modView, rankView, miscView, totalView;

    public SkillListItemView(Context context, String skillName) {
        super(context);
        name = skillName;
        init();
    }


    // Do all the other stuff needed to initialize the view
    private void init() {
        Log.i("SkillListItemView", "init");
        inflate(getContext(), R.layout.skill_list_item, this);
        TextView nameView = (TextView)findViewById(R.id.skillName);
        modView = (TextView)findViewById(R.id.skillMod);
        rankView = (TextView)findViewById(R.id.skillRank);
        miscView = (TextView)findViewById(R.id.skillMisc);
        totalView = (TextView)findViewById(R.id.skillTotal);
        modView.setText("0");
        rankView.setText("0");
        miscView.setText("0");
        totalView.setText("0");
        nameView.setText(name);
        setEditable(rankView);
        setEditable(miscView);
        Log.i("ContentView", "Ready, the name is " + name);
    }

    // This function detects when the view is clicked and emulates the EditField
    // i.e. makes the value part of the view editable, gives it focus and brings up the soft keyboard
    // Also it adds a listener to the now editable view to store the new value to the
    // underlying character sheet..
    // TODO: 10.3.2016 This is still buggy, and has not yet been fully implemented.
    private void setEditable(final TextView view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setClickable(true);
        view.setInputType(InputType.TYPE_CLASS_TEXT);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getRootView().findFocus().clearFocus();
                view.setCursorVisible(true);
                view.requestFocus();
                view.invalidate();
            }
        });
    }

    public String getName() {
        return name;
    }

    public void setMod(int mod){
        modView.setText(mod+"");
        recalculate();
    }

    public int getMod(){
        return Integer.parseInt(modView.getText().toString());
    }

    public void setRank(int rank){
        rankView.setText(rank+"");
        recalculate();
    }

    public int getRank(){
        return Integer.parseInt(rankView.getText().toString());
    }

    public void setMisc(int misc){
        miscView.setText(misc+"");
        recalculate();
    }

    public int getMisc(){
        return Integer.parseInt(miscView.getText().toString());
    }

    public void setTotal(int total){
        totalView.setText(total + "");
        recalculate();
    }

    public void recalculate(){
        int mod = getMod();
        int rank = getRank();
        int misc = getMisc();
        totalView.setText((mod+rank+misc)+"");
    }

    public void setRankViewOnFocusChangeListener(OnFocusChangeListener listener){
        rankView.setOnFocusChangeListener(listener);
    }

    public void setMiscViewOnFocusChangeListener(OnFocusChangeListener listener){
        miscView.setOnFocusChangeListener(listener);
    }
}
