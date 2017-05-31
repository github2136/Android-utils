package com.github2136.android_utils;

import android.content.Context;

import com.github2136.util.SPUtil;

/**
 * Created by yubin on 2017/4/19.
 */

public class SPUtilCompat extends SPUtil {
    public static SPUtil getInstance(Context context) {
        return getInstance(context, "ssss");
    }

    protected SPUtilCompat(Context context) {
        super(context);
    }
}
