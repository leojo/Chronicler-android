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
 *
 * A compact version of our custom ContentView
 */
public class CompactContentView extends LinearLayout {
    final boolean intField;
    final String name;
    public final String id;
    public String value;
    public boolean editable;
    int border;
    public TextView valueView;

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
            id = a.getString(R.styleable.CompactContentView_compId);
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
        Log.i("ContentView", "init, value is "+value);
        inflate(getContext(), R.layout.compact_content_view, this);
        TextView nameView = (TextView)findViewById(R.id.compNameView);
        valueView = (TextView)findViewById(R.id.compValueView);
        nameView.setText(name);
        valueView.setText(value);
        if(border != 0) this.setBackgroundResource(border);
        if(editable) setEditable();
        Log.i("ContentView", "Ready, the name is " + name);

        Log.i("ContentView", "Text of value view is certainly "+valueView.getText());
    }

    public void updateText(String newText){
        Log.i("ContentView", "Calling update text for "+name+" with value " +value);
        value = newText;
        valueView.setText(value);
    }

    private void setEditable() {
        Log.i("EDITABLE", "Setting editable");
        final TextView valView = (TextView) this.findViewById(R.id.compValueView);

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
