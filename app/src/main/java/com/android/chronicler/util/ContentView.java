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

/**
 * Created by andrea on 3.3.2016.
 *
 * A custom view to display a named field which may be editable.
 */
public class ContentView extends LinearLayout {
    final boolean intField;
    final String name;
    String value;
    boolean editable;
    int border;
    TextView valueView;

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
        Log.i("ContentView", "Ready, the name is "+name);
    }

    // This function detects when the view is clicked and emulates the EditField 
    // i.e. makes the value part of the view editable, gives it focus and brings up the soft keyboard
    // Also it adds a listener to the now editable view to store the new value to the
    // underlying character sheet..
    // TODO: 10.3.2016 This is still buggy, and has not yet been fully implemented.
    private void setEditable() {
        Log.i("EDITABLE", "Setting editable");
        this.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("EDITABLE", "CLICK! Should be making this editable");

                final TextView valView = (TextView)v.findViewById(R.id.valueView);
                //Grab the old value to revert if the user cancels (Maybe unnecessary??)
                final String oldVal = valView.getText().toString();

                Log.i("EDITABLE", "Current val is "+oldVal);
                // Do editable magics!!!!
                valView.setFocusable(true);
                valView.setFocusableInTouchMode(true);
                valView.setClickable(true);
                valView.setCursorVisible(true);
                valView.setInputType(InputType.TYPE_CLASS_TEXT);
                valView.requestFocus();
                valView.invalidate();

                // Try to set a "onChange" listener but this doesn't seem to do the trick... :(
                valView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    // A test of concept, nowhere near a final implementation:
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        String val = v.getText().toString();
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if (intField) {
                                Log.i("EDITABLE", "Setting field value to: " + val.replaceAll("[^\\d+-]",""));
                                valView.setText(oldVal);
                            } else {
                                Log.i("EDITABLE", "Setting field value to: " + val);
                                valView.setText(oldVal);
                            }
                        } else {
                            Log.i("EDITABLE", "Edit cancelled, setting field value to: " + oldVal);
                            valView.setText(oldVal);
                        }
                        return false;
                    }
                });
                Log.i("EDITABLE", "This is the value of our field " + valView.getText());
                Log.i("EDITABLE", "CLICK! Should now be editable");
            }
        });
    }
}
