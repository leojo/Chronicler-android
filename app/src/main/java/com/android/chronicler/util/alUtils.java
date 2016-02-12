package com.android.chronicler.util;

import java.util.Collection;

/**
 * Created by leo on 2.2.2016.
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
