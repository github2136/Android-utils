package com.github2136.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import java.lang.reflect.Type

/**
 * Created by yb on 2018/8/24.
 */
class JsonUtil private constructor() {
    private val mGson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
    fun getGson(): Gson {
        return mGson
    }

    @Synchronized
    fun <T> getObjectByStr(json: String, cls: Class<T>): T? {
        return try {
            mGson.fromJson<T>(json, cls)
        } catch (e: JsonSyntaxException) {
            null
        }
    }
    @Synchronized
    fun <T> getObjectByStr(json: String, typeOf: Type): T? {
        return try {
            mGson.fromJson<T>(json, typeOf)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    companion object {
        val instance: JsonUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            JsonUtil()
        }
    }
}