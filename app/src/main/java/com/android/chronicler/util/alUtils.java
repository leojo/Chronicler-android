package com.android.chronicler.util;

import java.util.Collection;

/**
 * Created by leo on 2.2.2016.
 */
public class alUtils {
    public static int sum(Collection<Integer> c){
        Object[] nums = c.toArray();
        int sum = 0;
        for(Object n : nums){
            sum += (int) n;
        }
        return sum;
    }
}
