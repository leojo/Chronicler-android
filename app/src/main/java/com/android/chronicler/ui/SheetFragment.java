package com.android.chronicler.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.chronicler.R;

/**
 * Created by andrea on 23.2.2016.
 */
public class SheetFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sheet_fragment_layout, container, false);

        ((TextView)rootView.findViewById(R.id.tabID)).setText(getArguments().getString("ID"));

        return rootView;
    }

    public static SheetFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("ID", type);
        SheetFragment sheetFrag = new SheetFragment();
        sheetFrag.setArguments(args);

        return sheetFrag;
    }

    public String getName() {
        return getArguments().getString("ID");
    }
/*
    public TextView getTextField() {
        if(getView() != null && getView().findViewById(R.id.editText) != null ) return (TextView)getView().findViewById(R.id.editText);
        else return null;
    }*/
}
