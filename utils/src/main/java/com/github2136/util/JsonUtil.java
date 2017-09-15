package com.github2136.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Json工具类JsonUtilCompat</br>
 * 如果需要使用其他的GsonBuilder，继承该类重写<b>getInstance</b>/<b>getGson</b>
 */
public class JsonUtil {
    private Gson mGson;

    public static JsonUtil getInstance() {
        return new JsonUtil();
    }

    protected JsonUtil() {
        mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    public Gson getGson() {
        return mGson;
    }

    public <T> T getObjectByStr(String json, Class<T> cls) {
        try {
            return mGson.fromJson(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T getObjectByStr(String json, Type typeOfT) {
        try {
            return mGson.fromJson(json, typeOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toJsonStr(Object src) {
        return mGson.toJson(src);
    }
}