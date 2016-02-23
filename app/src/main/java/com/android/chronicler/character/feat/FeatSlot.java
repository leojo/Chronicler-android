package com.android.chronicler.character.feat;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by leo on 28.11.2015.
 */
public class FeatSlot  implements Serializable {
    private String type;
    private Feat feat;

    public FeatSlot(){ /* Empty constructor for JSON */ }

    public FeatSlot(String slotDescriptor){
        this.type = slotDescriptor;
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
