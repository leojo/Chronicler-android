package com.android.chronicler.character.feat;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by leo on 28.11.2015.
 */
public class FeatList  implements Serializable {
    private ArrayList<FeatSlot> feats;

    public FeatList(){
        feats = new ArrayList<>();
    }

    public void add(FeatSlot feat){
        feats.add(feat);
    }

    public boolean retrain(Feat oldFeat, Feat newFeat){
        for(FeatSlot fs : feats){
            Feat f = fs.getFeat();
            if(f==null) continue;
            if(f.equals(oldFeat)){
                fs.setFeat(newFeat);
                return true;
            }

        }
        return false;
    }

    //<editor-fold desc="Getters and Setters">
    public ArrayList<FeatSlot> getFeats() {
        return feats;
    }

    public void setFeats(ArrayList<FeatSlot> feats) {
        this.feats = feats;
    }
    //</editor-fold>
}
