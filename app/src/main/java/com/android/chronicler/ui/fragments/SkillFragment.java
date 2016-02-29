package com.android.chronicler.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.chronicler.R;

/**
 * Created by andrea on 26.2.2016.
 */
public class SkillFragment extends SheetFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.skill_fragment_layout, container, false);

        // This is where we should input stuff, by using getArguments().get....
        ((TextView)rootView.findViewById(R.id.tabID)).setText(getArguments().getString("ID"));

        return rootView;
    }
}
