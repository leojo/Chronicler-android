package com.android.chronicler.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.chronicler.R;

/**
 * Created by andrea on 3.3.2016.
 */
public class ContentView extends LinearLayout {
    String id;
    String name;
    String value;
    boolean editable;
    int border;

    public ContentView(Context context, AttributeSet attrs) {
        super(context, attrs);


        // Get attributes as defined in values/attrs.xml:
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ContentView,
                0, 0);
        try {
            id = a.getString(R.styleable.ContentView_id);
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
        TextView valueView = (TextView)findViewById(R.id.valueView);
        nameView.setText(name);
        valueView.setText(value);
        if(border != 0) this.setBackgroundResource(border);
        if(editable) setEditable();
        Log.i("ContentView", "Ready, the name is "+name);
    }

    private void setEditable() {
        this.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("ContentView", "The click listener is working");
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
