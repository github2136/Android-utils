package com.github2136.android_utils.util

import android.util.Log
import org.json.JSONObject
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Created by YB on 2022/5/12
 */
object GeoJsonUtil {
    fun getData(geoJson: String): GeoData? {
        try {
            val jsonObject = JSONObject(geoJson)
            val featuresObj = jsonObject.getJSONArray("features")
            val geoType = jsonObject.getString("type")
            val featurs = mutableListOf<Feature>()
            for (i in 0 until featuresObj.length()) {
                val featureObj = featuresObj.getJSONObject(i)
                val featureType = featureObj.getString("type")
                val geometryObj = featureObj.getJSONObject("geometry")
                val geomet = getCoordinates(geometryObj)
                val geometry = when (geomet) {
                    is Coordinate.Point -> Geometry("Point", geomet)
                    is Coordinate.LineString -> Geometry("LineString", geomet)
                    is Coordinate.Polygon -> Geometry("LineString", geomet)
                    is Coordinate.MultiPoint -> Geometry("LineString", geomet)
                    is Coordinate.MultiLineString -> Geometry("MultiLineString", geomet)
                    is Coordinate.MultiPolygon -> Geometry("MultiPolygon", geomet)
                    else -> throw  NullPointerException("unknown geometry type")
                }
                featurs.add(Feature(featureType, geometry))
            }
            return GeoData(geoType, featurs)
        } catch (e: Exception) {
            val sw = StringWriter()
            e.printStackTrace(PrintWriter(sw))
            Log.e("GeoJsonUtil", sw.toString())
            return null
        }
    }

    private fun getCoordinates(jsonObject: JSONObject): Coordinate? {
        val type = jsonObject.getString("type")
        val coordinates = jsonObject.getJSONArray("coordinates")
        when (type) {
            "Point" -> {
                return Coordinate.Point(coordinates.getDouble(0), coordinates.getDouble(1))
            }
            "LineString" -> {
                val line = mutableListOf<Coordinate.Point>()
                for (i in 0 until coordinates.length()) {
                    val p = coordinates.getJSONArray(i)
                    line.add(Coordinate.Point(p.getDouble(0), p.getDouble(1)))
                }
                return Coordinate.LineString(line)
            }
            "Polygon" -> {
                val polygons = mutableListOf<Coordinate.LineString>()
                for (i in 0 until coordinates.length()) {
                    val polygon = mutableListOf<Coordinate.Point>()
                    val polygonObj = coordinates.getJSONArray(i)
                    for (j in 0 until polygonObj.length()) {
                        polygon.add(Coordinate.Point(polygonObj.getDouble(0), polygonObj.getDouble(1)))
                    }
                    polygons.add(Coordinate.LineString(polygon))
                }
                return Coordinate.Polygon(polygons)
            }
            "MultiPoint" -> {
                val points = mutableListOf<Coordinate.Point>()
                for (i in 0 until coordinates.length()) {
                    val p = coordinates.getJSONArray(i)
                    points.add(Coordinate.Point(p.getDouble(0), p.getDouble(1)))
                }
                return Coordinate.MultiPoint(points)
            }
            "MultiLineString" -> {
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
                return Coordinate.MultiLineString(lines)
            }
            "MultiPolygon" -> {
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
                return Coordinate.MultiPolygon(polygons)
            }
            else -> return null
        }
    }
}

data class GeoData(
    var type: String,
    var features: List<Feature>
)

data class Feature(
    var type: String,
    var geometry: Geometry
)

data class Geometry(
    var type: String,
    var coordinates: Coordinate
)

sealed class Coordinate {

    /**
     * 单点
     */
    data class Point(var x: Double, var y: Double) : Coordinate()

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