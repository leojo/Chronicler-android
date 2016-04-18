package com.android.chronicler.character;

import java.io.Serializable;

/**
 * Created by andrea on 10.4.2016.
 */
public class SheetObject implements Serializable {
    public String getName() {
        return "No Name";
    }

    public String shortDescr(){
        return "";
    }

    public String longDescr(){
        return "No Description";
    }
}
