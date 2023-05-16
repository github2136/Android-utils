package com.github2136.android_utils.util

import android.util.Log
import org.json.JSONObject
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Created by YB on 2022/5/12
 */
object GeoJsonUtil {
    private const val TYPE_POINT = "Point"
    private const val TYPE_MULTI_POINT = "MultiPoint"
    private const val TYPE_LINE_STRING = "LineString"
    private const val TYPE_MULTI_LINE_STRING = "MultiLineString"
    private const val TYPE_POLYGON = "Polygon"
    private const val TYPE_MULTI_POLYGON = "MultiPolygon"
    private const val TYPE_GEOMETRY_COLLECTION = "GeometryCollection"
    private const val TYPE_FEATURE = "Feature"
    private const val TYPE_FEATURE_COLLECTION = "FeatureCollection"

    private const val KEY_TYPE = "type"
    private const val KEY_GEOMETRY = "geometry"
    private const val KEY_GEOMETRIES = "geometries"
    private const val KEY_PROPERTIES = "properties"
    private const val KEY_COORDINATES = "coordinates"
    private const val KEY_FEATURES = "features"
    private const val KEY_ID = "id"

    fun getData(geoJson: String): GeoData? {
        try {
            val jsonObject = JSONObject(geoJson)
            val geoType = jsonObject.getString(KEY_TYPE)
            return when (geoType) {
                TYPE_POINT, TYPE_MULTI_POINT, TYPE_LINE_STRING, TYPE_MULTI_LINE_STRING, TYPE_POLYGON, TYPE_MULTI_POLYGON -> {
                    getGeometry(jsonObject)
                }
                TYPE_GEOMETRY_COLLECTION -> {
                    val geometrysArray = jsonObject.getJSONArray(KEY_GEOMETRIES)
                    val geometrys = mutableListOf<GeoData.GeoDataGeometry>()
                    for (i in 0 until geometrysArray.length()) {
                        getGeometry(geometrysArray.getJSONObject(i))?.apply {
                            geometrys.add(this)
                        }
                    }
                    GeoData.GeoDataGeometryCollection(geoType, geometrys)
                }
                TYPE_FEATURE -> {
                    getFeature(jsonObject)
                }
                TYPE_FEATURE_COLLECTION -> {
                    val featuresArray = jsonObject.getJSONArray(KEY_FEATURES)
                    val features = mutableListOf<GeoData.GeoDataFeature>()
                    for (i in 0 until featuresArray.length()) {
                        getFeature(featuresArray.getJSONObject(i))?.apply {
                            features.add(this)
                        }
                    }
                    GeoData.GeoDataFeatureCollection(geoType, features)
                }
                else -> null
            }
        } catch (e: Exception) {
            val sw = StringWriter()
            e.printStackTrace(PrintWriter(sw))
            Log.e("GeoJsonUtil", sw.toString())
            return null
        }
    }

    private fun getGeometry(jsonObject: JSONObject): GeoData.GeoDataGeometry? {
        val type = jsonObject.getString(KEY_TYPE)
        val coordinates = jsonObject.getJSONArray(KEY_COORDINATES)
        when (type) {
            TYPE_POINT -> {
                return GeoData.GeoDataGeometry(type, Coordinate.Point(coordinates.getDouble(0), coordinates.getDouble(1)))
            }
            TYPE_LINE_STRING -> {
                val line = mutableListOf<Coordinate.Point>()
                for (i in 0 until coordinates.length()) {
                    val p = coordinates.getJSONArray(i)
                    line.add(Coordinate.Point(p.getDouble(0), p.getDouble(1)))
                }
                return GeoData.GeoDataGeometry(type, Coordinate.LineString(line))
            }
            TYPE_POLYGON -> {
                val polygons = mutableListOf<Coordinate.LineString>()
                for (i in 0 until coordinates.length()) {
                    val polygon = mutableListOf<Coordinate.Point>()
                    val polygonObj = coordinates.getJSONArray(i)
                    for (j in 0 until polygonObj.length()) {
                        val point = polygonObj.getJSONArray(j)
                        polygon.add(Coordinate.Point(point.getDouble(0), point.getDouble(1)))
                    }
                    polygons.add(Coordinate.LineString(polygon))
                }
                return GeoData.GeoDataGeometry(type, Coordinate.Polygon(polygons))
            }
            TYPE_MULTI_POINT -> {
                val points = mutableListOf<Coordinate.Point>()
                for (i in 0 until coordinates.length()) {
                    val p = coordinates.getJSONArray(i)
                    points.add(Coordinate.Point(p.getDouble(0), p.getDouble(1)))
                }
                return GeoData.GeoDataGeometry(type, Coordinate.MultiPoint(points))
            }
            TYPE_MULTI_LINE_STRING -> {
                val lines = mutableListOf<Coordinate.LineString>()
                for (i in 0 until coordinates.length()) {
                    val lineObj = coordinates.getJSONArray(i)
                    val line = mutableListOf<Coordinate.Point>()
                    for (j in 0 until lineObj.length()) {
                        val p = lineObj.getJSONArray(j)
                        line.add(Coordinate.Point(p.getDouble(0), p.getDouble(1)))
                    }
                    lines.add(Coordinate.LineString(line))
                }
                return GeoData.GeoDataGeometry(type, Coordinate.MultiLineString(lines))
            }
            TYPE_MULTI_POLYGON -> {
                val polygons = mutableListOf<Coordinate.Polygon>()
                for (i in 0 until coordinates.length()) {
                    val polygonObj = coordinates.getJSONArray(i)
                    val polygon = mutableListOf<Coordinate.LineString>()
                    for (j in 0 until polygonObj.length()) {
                        val lineString = polygonObj.getJSONArray(j)
                        val line = mutableListOf<Coordinate.Point>()
                        for (k in 0 until lineString.length()) {
                            val point = lineString.getJSONArray(k)
                            line.add(Coordinate.Point(point.getDouble(0), point.getDouble(1)))
                        }
                        polygon.add(Coordinate.LineString(line))
                    }
                    polygons.add(Coordinate.Polygon(polygon))
                }
                return GeoData.GeoDataGeometry(type, Coordinate.MultiPolygon(polygons))
            }
            else -> return null
        }
    }

    private fun getFeature(jsonObject: JSONObject): GeoData.GeoDataFeature? {
        val properties = if (jsonObject.has(KEY_PROPERTIES)) jsonObject.getJSONObject(KEY_PROPERTIES) else null
        val id = if (jsonObject.has(KEY_ID)) jsonObject.getString(KEY_ID) else null
        val type = jsonObject.getString(KEY_TYPE)
        return getGeometry(jsonObject.getJSONObject(KEY_GEOMETRY))?.run {
            GeoData.GeoDataFeature(type, this, id, properties)
        }
    }
}

sealed class GeoData {
    data class GeoDataFeature(
        var type: String,
        var geometry: GeoDataGeometry,
        var id: String?,
        var properties: JSONObject?
    ) : GeoData()

    data class GeoDataFeatureCollection(
        var type: String,
        var features: MutableList<GeoDataFeature>
    ) : GeoData()

    data class GeoDataGeometry(
        var type: String,
        var coordinates: Coordinate

    ) : GeoData()

    data class GeoDataGeometryCollection(
        var type: String,
        var geometrys: MutableList<GeoDataGeometry>
    ) : GeoData()
}

sealed class Coordinate {

    /**
     * 单点
     * @param lon 经度
     * @param lat 纬度
     */
    data class Point(var lon: Double, var lat: Double) : Coordinate()

    /**
     * 单线
     */
    data class LineString(var line: List<Point>) : Coordinate()

    /**
     * 多边形
     */
    data class Polygon(var polygon: List<LineString>) : Coordinate()

    /**
     * 多点
     */
    data class MultiPoint(var points: List<Point>) : Coordinate()

    /**
     * 多线
     */
    data class MultiLineString(var lines: List<LineString>) : Coordinate()

    /**
     * 多多边形
     */
    data class MultiPolygon(var polygons: List<Polygon>) : Coordinate()
}