package com.github2136.android_utils

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github2136.util.JsonUtil
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by YB on 2022/5/13
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val data = JsonUtil.instance.fromJson<JsonData>("{\"par1\":\"\",\"par2\":\"2\",\"par3\":\"\"}")
        Assert.assertNotNull(data)
    }
}