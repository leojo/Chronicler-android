package com.android.chronicler.character.feat;

import com.android.chronicler.character.SheetObject;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by leo on 28.11.2015.
 */
public class FeatSlot extends SheetObject implements Serializable {
    private String type;
    private Feat feat;

    public FeatSlot(){ /* Empty constructor for JSON */ }

    public FeatSlot(String slotDescriptor){
        this.type = slotDescriptor;
    }

    @Override
    public String longDescr() {
        if(feat == null) return "Please Select a Feat for this Feat Slot";
        return feat.getFullText();
    }

    @Override
    public String shortDescr() {
        if(feat == null) return "- Empty -";
        return feat.shortDescription();
    }

    // given a list of feats, returns a list of feats that can occupy this slot.
    public ArrayList<Feat> getPossibleFeats(ArrayList<Feat> featList){
        ArrayList<Feat> feats = new ArrayList<>();
        for(Feat f : featList){
            if(f.getType().equalsIgnoreCase(type)) feats.add(f);
        }
        Collections.sort(feats);
        return feats;
    }

    @JsonIgnore
    public String getName() {
        if(feat == null) return "Empty";
        return feat.getName();
    }

    //<editor-fold desc="Getters and Setters">
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Feat getFeat() {
        return feat;
    }

    public void setFeat(Feat feat) {
        this.feat = feat;
    }
    //</editor-fold>
}
