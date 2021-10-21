package com.github2136.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Created by yb on 2018/8/24.
 */
class JsonUtil private constructor(val gson: Gson) {

    inline fun <reified T> fromJson(json: String?): T? {
        return try {
            gson.fromJson<T>(json, object : TypeToken<T>() {}.type)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    fun toJson(obj: Any): String {
        return gson.toJson(obj)
    }

    companion object {
        var dateFormat = DateUtil.DATE_PATTERN_YMDHMS
        var mGson = GsonBuilder().setDateFormat(dateFormat).create()

        val instance: JsonUtil by lazy {
            JsonUtil(mGson)
        }

        fun newInstance(gson: Gson = GsonBuilder().setDateFormat(DateUtil.DATE_PATTERN_YMDHMS).create()): JsonUtil {
            return JsonUtil(gson)
        }
    }
}