package com.github2136.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Json工具类
 */
public class JsonUtil {
    private Gson mGson;

    public static JsonUtil getInstance() {
        return new JsonUtil();
    }

    private JsonUtil() {
        mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    public Gson getGson() {
        return mGson;
    }

    public <T> T getByStr(String json, Class<T> cls) {
        try {
            return mGson.fromJson(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T getByStr(String json, Type typeOfT) {
        try {
            return mGson.fromJson(json, typeOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}