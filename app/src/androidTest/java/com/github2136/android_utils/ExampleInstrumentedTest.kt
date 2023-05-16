package com.github2136.android_utils

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github2136.android_utils.util.GeoData
import com.github2136.android_utils.util.GeoJsonUtil
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
    @Test
    fun useAppContext0() {
        GeoJsonUtil.getData("{\"type\": \"Point\", \"coordinates\": [100.0, 0.0 ] }")?.apply {
            Log.e("GeoData", this.toString())
        }
        GeoJsonUtil.getData("{\"type\": \"LineString\", \"coordinates\": [[100.0, 0.0 ], [101.0, 1.0 ] ] }")?.apply {
            Log.e("GeoData", this.toString())
        }
        GeoJsonUtil.getData("{\"type\": \"Polygon\", \"coordinates\": [[[100.0, 0.0 ], [101.0, 0.0 ], [101.0, 1.0 ], [100.0, 1.0 ], [100.0, 0.0 ] ], [[100.8, 0.8 ], [100.8, 0.2 ], [100.2, 0.2 ], [100.2, 0.8 ], [100.8, 0.8 ] ] ] }")
            ?.apply {
                Log.e("GeoData", this.toString())
            }
        GeoJsonUtil.getData("{\"type\": \"GeometryCollection\", \"geometries\": [{\"type\": \"Point\", \"coordinates\": [100.0, 0.0 ] }, {\"type\": \"LineString\", \"coordinates\": [[101.0, 0.0 ], [102.0, 1.0 ] ] } ] }")
            ?.apply {
                Log.e("GeoData", this.toString())
            }
        GeoJsonUtil.getData("{\"type\": \"Feature\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5 ] }, \"properties\": {\"prop0\": \"value0\"} }")
            ?.apply {
                Log.e("GeoData", this.toString())
            }
        GeoJsonUtil.getData("{\"type\": \"FeatureCollection\", \"features\": [{\"type\": \"Feature\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5 ] }, \"properties\": {\"prop0\": \"value0\"} }, {\"type\": \"Feature\", \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[102.0, 0.0 ], [103.0, 1.0 ], [104.0, 0.0 ], [105.0, 1.0 ] ] }, \"properties\": {\"prop0\": \"value0\", \"prop1\": 0.0 } }, {\"type\": \"Feature\", \"geometry\": {\"type\": \"Polygon\", \"coordinates\": [[[100.0, 0.0 ], [101.0, 0.0 ], [101.0, 1.0 ], [100.0, 1.0 ], [100.0, 0.0 ] ] ] }, \"properties\": {\"prop0\": \"value0\", \"prop1\": {\"this\": \"that\"} } } ] }")
            ?.apply {
                Log.e("GeoData", this.toString())
            }
    }
}