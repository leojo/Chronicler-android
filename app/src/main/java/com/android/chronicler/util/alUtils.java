package com.android.chronicler.util;

import java.util.Collection;

/**
 * Created by leo on 2.2.2016.
 * Array List Utility functions (will perhaps move this inside another class if it turns out
 * it's not used very much)
 */
public class alUtils {
    public static int sum(Collection<Integer> c){
        int sum = 0;
        for(Integer n : c){
            sum += n;
        }
        return sum;
    }
}
