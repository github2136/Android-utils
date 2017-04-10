package com.github2136.android_utils;

import com.github2136.util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by yubin on 2017/4/10.
 */

public class JsonUtilCompat extends JsonUtil {
    public static JsonUtilCompat getInstance() {
        return new JsonUtilCompat();
    }

    @Override
    public Gson getGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }
}
