package com.xhh.frameworklib.common.utils;

import android.support.annotation.Nullable;

/**
 * Created by star on 2017/8/7.
 */

public class StringUtils {
    public static boolean isEmpty(@Nullable CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }
}
