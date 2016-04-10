package com.android.chronicler.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.chronicler.R;
import com.android.chronicler.character.CharacterSheet;

/**
 * Created by andrea on 3.3.2016.
 *
 * A custom view to display a named field which may be editable.
 */
public class ContentView extends LinearLayout {
    final boolean intField;
    final String name;
    public String value;
    public boolean editable;
    int border;
    public final String id;
    public TextView valueView;

    public ContentView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Get attributes as defined in values/attrs.xml:
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ContentView,
                0, 0);
        try {
            // Get all the tags and put them into variables:
            intField = a.getBoolean(R.styleable.ContentView_integerField, false);
            name = a.getString(R.styleable.ContentView_name);
            id = a.getString(R.styleable.ContentView_id);
            value = a.getString(R.styleable.ContentView_value);
            editable = a.getBoolean(R.styleable.ContentView_editable, false);
            border = a.getInt(R.styleable.ContentView_border, 0);

            Log.i("ContentView", "Getting all your style attributes!");
        } finally {
            a.recycle();
        }

        init();
    }


    // Do all the other stuff needed to initialize the view
    private void init() {
        Log.i("ContentView", "init");
        inflate(getContext(), R.layout.content_view, this);
        TextView nameView = (TextView)findViewById(R.id.nameView);
        valueView = (TextView)findViewById(R.id.valueView);
        nameView.setText(name);
        valueView.setText(value);
        if(border != 0) this.setBackgroundResource(border);
        if(editable) setEditable();
        Log.i("ContentView", "Ready, the name is " + name);
    }

    public void updateText(String newText){
        value = newText;
        valueView.setText(value);
    }

    // This function detects when the view is clicked and emulates the EditField 
    // i.e. makes the value part of the view editable, gives it focus and brings up the soft keyboard
    // Also it adds a listener to the now editable view to store the new value to the
    // underlying character sheet..
    // TODO: 10.3.2016 This is still buggy, and has not yet been fully implemented.
    private void setEditable() {
        Log.i("EDITABLE", "Setting editable");

        final TextView valView = (TextView) this.findViewById(R.id.valueView);

        // Do editable magics!!!!
        valView.setFocusable(true);
        valView.setFocusableInTouchMode(true);
        valView.setClickable(true);
        valView.setInputType(InputType.TYPE_CLASS_TEXT);

        Log.i("EDITABLE", "This is the value of our field " + valView.getText());
        Log.i("EDITABLE", "CLICK! Should now be editable");

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                valView.setCursorVisible(true);
                valView.requestFocus();
                valView.invalidate();
            }
        });
    }
}
