package com.android.chronicler.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.chronicler.R;

/**
 * Created by andrea on 3.3.2016.
 */
public class CompactContentView extends LinearLayout {
    final boolean intField;
    final String name;
    String value;
    boolean editable;
    int border;

    public CompactContentView(Context context, AttributeSet attrs) {
        super(context, attrs);


        // Get attributes as defined in values/attrs.xml:
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CompactContentView,
                0, 0);
        try {
            intField = a.getBoolean(R.styleable.CompactContentView_compIntegerField, false);
            name = a.getString(R.styleable.CompactContentView_compName);
            value = a.getString(R.styleable.CompactContentView_compValue);
            editable = a.getBoolean(R.styleable.CompactContentView_compEditable, false);
            border = a.getInt(R.styleable.CompactContentView_compBorder, 0);


            Log.i("ContentView", "Getting all your style attributes!");
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        Log.i("ContentView", "init");
        inflate(getContext(), R.layout.compact_content_view, this);
        TextView nameView = (TextView)findViewById(R.id.compNameView);
        TextView valueView = (TextView)findViewById(R.id.compValueView);
        nameView.setText(name);
        valueView.setText(value);
        if(border != 0) this.setBackgroundResource(border);
        if(editable) setEditable();
        Log.i("ContentView", "Ready, the name is "+name);
    }

    private void setEditable() {
        Log.i("EDITABLE", "Setting editable");
        this.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("EDITABLE", "CLICK! Should be making this editable");

                final TextView valView = (TextView)v.findViewById(R.id.compValueView);
                valView.setFocusable(true);
                valView.setFocusableInTouchMode(true);
                valView.setClickable(true);
                valView.setCursorVisible(true);
                valView.setInputType(InputType.TYPE_CLASS_TEXT);
                valView.requestFocus();
                valView.invalidate();
                valView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        String val = valView.getText().toString();
                        if (intField) {
                            Log.i("SHEET_EDIT", "You just clicked an integer valued field");
                            Log.i("SHEET_EDIT", "The value on click was: " + val.replaceAll("[^\\d.]", ""));
                        } else {
                            Log.i("SHEET_EDIT", "You just clicked a string valued field");
                            Log.i("SHEET_EDIT", "The value on click was: " + val);
                        }
                        return false;
                    }
                });
                Log.i("EDITABLE", "This is the value of our field "+valView.getText());
                Log.i("EDITABLE", "CLICK! Should now be editable");
            }
        });
    }

}
