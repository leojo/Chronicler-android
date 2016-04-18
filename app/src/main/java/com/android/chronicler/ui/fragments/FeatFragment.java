package com.android.chronicler.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.android.chronicler.R;
import com.android.chronicler.character.SheetObject;
import com.android.chronicler.character.feat.FeatList;
import com.android.chronicler.character.feat.FeatSlot;
import com.android.chronicler.ui.SearchActivity;
import com.android.chronicler.ui.SheetObjectOverviewActivity;
import com.android.chronicler.util.SheetAdapter;

/**
 * Fragment for the CharacterActivity: This is the character's list of feats.
 *
 * Created by andrea on 26.2.2016.
 */
public class FeatFragment extends SheetFragment {
    private SheetAdapter adapter;
    private FeatList feats;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.feat_fragment_layout, container, false);

        ListView featsView = (ListView)(rootView.findViewById(R.id.featListView));


        final FeatFragment thisFragment = this;

        // Set add button to footer
        ImageView addButtonView = new ImageView(getContext());
        addButtonView.setPadding(20, 20, 20, 20);
        addButtonView.setImageResource(R.drawable.ic_add_circle_24dp);
        featsView.addFooterView(addButtonView);

        feats = (FeatList)getArguments().getSerializable("FEATS");

        adapter = new SheetAdapter(getContext(), feats);
        featsView.setAdapter(adapter);

        featsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == adapter.getCount()) {
                    Intent intent = new Intent(thisFragment.getContext(), SheetObjectOverviewActivity.class);
                    intent.putExtra("TYPE","feat");
                    thisFragment.startActivityForResult(intent, 1);

                } else {
                    return;
                }
            };
            // --------------------------------------
        });

        featsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Activate popup when an invite is clicked
                showPopup(view,feats.getFeats().get(position),position);

                return false;
            }
        });

        return rootView;
    }



    // Pop-up for accepting or declining invites: Will later be replaced with buttons
    // nested inside the list elements for accepting and declining.
    public void showPopup(View v, final SheetObject sheetObject, final int position) {
        final FeatFragment thisFragment = this;
        PopupMenu popup = new PopupMenu(thisFragment.getContext(), v);
        popup.inflate(R.menu.menu_feat_options);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch ((String) item.getTitle()) {
                    case "Overview":
                        Intent intent = new Intent(thisFragment.getContext(), SheetObjectOverviewActivity.class);
                        intent.putExtra("TYPE","feat");
                        intent.putExtra(SearchActivity.SHEET_OBJECT, sheetObject);
                        intent.putExtra("StartedForResult", false);
                        startActivity(intent);
                        break;
                    case "Delete":
                        adapter.remove(position);
                        adapter.notifyDataSetChanged();
                        feats.getFeats().remove(position);
                        break;
                    default:
                        Log.d("PopupMenu", "This should not happen.");
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0) return;

        String featName = data.getStringExtra("toBeAdded");

        FeatSlot newFeatSlot = (FeatSlot)data.getSerializableExtra(SearchActivity.SHEET_OBJECT);
        SheetAdapter.searching = false;

        feats.add(newFeatSlot);
        adapter.clearAndAddAll(feats);
        adapter.notifyDataSetChanged();
    }


    // newInstance is called when the CharacterActivity is started and the fragments get
    // created. Here is where we would put our arguments specific to that fragment (say, a list of spells)
    // as arguments for this function.
    public static FeatFragment newInstance(String type, FeatList feats) {
        Bundle args = new Bundle();
        args.putString("ID", type);
        args.putSerializable("FEATS", feats);
        FeatFragment featFrag = new FeatFragment();
        featFrag.setArguments(args);

        return featFrag;
    }
}
