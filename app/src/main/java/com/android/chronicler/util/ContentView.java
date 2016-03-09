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

    private void setEditable() {
        Log.i("EDITABLE", "Setting editable");
        this.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("EDITABLE", "CLICK! Should be making this editable");

                final TextView valView = (TextView)v.findViewById(R.id.valueView);
                final String oldVal = valView.getText().toString();
                Log.i("EDITABLE", "Current val is "+oldVal);
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

/*
    @Override
    public boolean onTouchEvent(final MotionEvent e) {
        //touchCounter++;
        //float touched_x = e.getX();
        //float touched_y = e.getY();

        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                touched = true;
                break;
            case MotionEvent.ACTION_MOVE:
                touched = true;
                break;
            case MotionEvent.ACTION_UP:
                touched = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                touched = false;
                break;
            case MotionEvent.ACTION_OUTSIDE:
                touched = false;
                break;
            default:
        }
        return true; // processed
    }*/
}
