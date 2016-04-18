package com.android.chronicler.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.chronicler.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassTableActivity extends AppCompatActivity {
    private TableLayout.LayoutParams layoutParamsForRow;
    private TableRow.LayoutParams layoutParansForCell;
    private TableLayout classTable;
    private ArrayList<HashMap<String,String>> tableData;

    public static final String TABLE_DATA = "TABLE_DATA";
    public static final String[] columnOrder = {"level","base_attack_bonus","fort_save","ref_save","will_save","caster_level","points_per_day","ac_bonus","flurry_of_blows","bonus_spells","powers_known","unarmored_speed_bonus","unarmed_damage","power_level","special","slots_0","slots_1","slots_2","slots_3","slots_4","slots_5","slots_6","slots_7","slots_8","slots_9","spells_known_0","spells_known_1","spells_known_2","spells_known_3","spells_known_4","spells_known_5","spells_known_6","spells_known_7","spells_known_8","spells_known_9"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_table);

        tableData = (ArrayList<HashMap<String,String>>) getIntent().getSerializableExtra(TABLE_DATA);
        classTable = (TableLayout) findViewById(R.id.classTable);

        layoutParamsForRow = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        layoutParansForCell = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        addHeaderRow(getHeaders());
        for(int i=1; i<tableData.size(); i++){
            addContentRow(getEntries(i));
        }
    }

    private void addHeaderRow(ArrayList<String> headers){
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(layoutParamsForRow);
        boolean firstColumn = true;
        for(String header : headers){
            TextView textView = new TextView(this);
            textView.setLayoutParams(layoutParansForCell);
            textView.setText("  " + header + "  ");
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            if(firstColumn){
                firstColumn = false;
                textView.setBackgroundResource(R.drawable.border_bottom);
            } else {
                textView.setBackgroundResource(R.drawable.border_left_bottom);
            }

            tableRow.addView(textView);
        }
        classTable.addView(tableRow);
    }

    private void addContentRow(ArrayList<String> entries){
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(layoutParamsForRow);
        boolean firstColumn = true;
        for(String entry : entries){
            TextView textView = new TextView(this);
            textView.setLayoutParams(layoutParansForCell);
            textView.setText("  " + entry + "  ");
            textView.setGravity(Gravity.CENTER);
            if(firstColumn){
                firstColumn = false;
                textView.setBackgroundResource(R.drawable.border_bottom);
            } else {
                textView.setBackgroundResource(R.drawable.border_left_bottom);
            }

            tableRow.addView(textView);
        }
        classTable.addView(tableRow);
    }

    private ArrayList<String> getHeaders(){
        HashMap<String,String> headerRow = tableData.get(0);
        ArrayList<String> headers = new ArrayList<>();
        for(String key : columnOrder){
            if(headerRow.containsKey(key)) headers.add(headerRow.get(key));
        }
        return headers;
    }

    private ArrayList<String> getEntries(int i){
        HashMap<String,String> tableRow = tableData.get(i);
        ArrayList<String> entries = new ArrayList<>();
        for(String key : columnOrder){
            if(tableRow.containsKey(key)) entries.add(tableRow.get(key));
        }
        return entries;
    }
}
