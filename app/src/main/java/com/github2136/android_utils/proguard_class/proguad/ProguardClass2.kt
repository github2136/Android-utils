package com.github2136.android_utils.proguard_class.proguad

import android.util.Log

/**
 * Created by YB on 2019/12/9
 * 混淆演示类
 */
class ProguardClass2() {
    init {
        Log.e("init", "init1")
    }

    constructor(str: String) : this() {
        Log.e("init", "init2")
    }


    val testParams1: String = "params"
    public val testParams2: String = "params"
    val testParams3: String = "params"
    val testParams4: String = "params"

    fun test1() {
        Log.e("test", "test")
    }

    fun test1(str: String) {
        Log.e("test", "test")
    }

    fun test2(str: String, str2: String) {
        Log.e("test", "test")
    }
}