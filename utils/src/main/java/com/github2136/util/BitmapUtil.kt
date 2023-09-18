package com.github2136.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Handler
import android.os.Looper
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executors
import kotlin.math.min

/**
 *       图片处理
 *       首先设置图片路径BitmapUtil.getInstance(filePath)
 *       然后就可以在通过其他方法来对图片进行处理
 *       rotation//图片旋转为正
 *       limitPixel(int max)//显示图片最大宽高 0 表示不限制
 *       limitSize(int maxSize)//显示图片最大大小，单位为KB 0 表示不限制
 *       get***()//获取图片的bitmap、base64、byte[]
 *       correct()//图片是否为正的
 *       values()//获取图片宽高[0]表示为宽[1]表示为高
 *       save(String filepath,callback)//保存至指定目录
 */
class BitmapUtil private constructor(path: String) {
    //图片路径
    private var mFilePath: String = path

    //旋转角度
    private var mDegree: Int = 0

    //宽高最大值
    private var mMaxPixel: Int = 0

    //文件最大值
    private var mMaxSize: Int = 0

    //图片保存质量
    private var mQuality = 100

    private val mHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    init {
        mDegree = 0
        mMaxPixel = 0
        mMaxSize = 0
    }

    /**
     * 获取图片
     */
    private fun getBitmap(filePath: String, scale: Int): Bitmap? {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.RGB_565
        options.inSampleSize = scale
        return BitmapFactory.decodeFile(filePath, options)
    }

    /**
     * 按设置获取图片的bitmap
     */
    private fun getBitmap(): Bitmap? {
        var mBitmap: Bitmap?
        if (mMaxPixel != 0) {
            val values = getBitmapValue(mFilePath)
            val scaleFactor = Math.max(Math.ceil(values[0].toDouble() / mMaxPixel), Math.ceil(values[1].toDouble() / mMaxPixel)).toInt()
            val scaleSize: Int
            if (scaleFactor > 1) {
                var inSampleSize = 1
                while (inSampleSize shl 1 < scaleFactor) {
                    inSampleSize = inSampleSize shl 1
                }
                scaleSize = inSampleSize / 2
            } else {
                scaleSize = scaleFactor
            }
            //使用inSampleSize压缩图片
            mBitmap = getBitmap(mFilePath, scaleSize)
            if (mBitmap != null) {
                //如果图片高宽比限制大则使用Matrix再次缩小
                if (mBitmap.width > mMaxPixel || mBitmap.height > mMaxPixel) {
                    val scaleW = mMaxPixel.toFloat() / mBitmap.width
                    val scaleH = mMaxPixel.toFloat() / mBitmap.height
                    val scale = min(scaleW, scaleH)
                    mBitmap = getBitmap(mBitmap, scale)
                }
            }
        } else {
            mBitmap = getBitmap(mFilePath, 1)
        }
        if (mBitmap != null) {
            if (mMaxSize != 0) {
                val os = ByteArrayOutputStream()
                mQuality = 110
                do {
                    os.reset()
                    mQuality -= 10
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, mQuality, os)
                } while (mQuality > 0 && os.toByteArray().size / 1024 > mMaxSize)
            }
            if (mDegree > 0) {
                mBitmap = rotateBitmapByDegree(mBitmap, mDegree)
            }
        }
        return mBitmap
    }

    /**
     * 获得图片宽高信息 [0]:width [1]:height
     */
    private fun getBitmapValue(filePath: String): IntArray {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)
        val value = IntArray(2)
        value[0] = options.outWidth
        value[1] = options.outHeight
        return value
    }

    /**
     * 获得图片的旋转角度
     */
    private fun getBitmapRotateDegree(path: String): Int {
        var degree = 0
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            val exifInterface = ExifInterface(path)
            // 获取图片的旋转信息
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, ExifInterface
                    .ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return degree
    }

    /**
     * 将图片按照某个角度进行旋转
     */
    private fun rotateBitmapByDegree(sourceBitmap: Bitmap, degree: Int): Bitmap {
        val retBitmap: Bitmap
        // 根据旋转角度，生成旋转矩阵
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        retBitmap = Bitmap.createBitmap(
            sourceBitmap, 0, 0,
            sourceBitmap.width, sourceBitmap.height, matrix, true
        )
        sourceBitmap.recycle()
        return retBitmap
    }

    /**
     * 压缩至指定尺寸
     *
     * @param sourceBitmap
     * @param scale
     * @return
     */
    private fun getBitmap(sourceBitmap: Bitmap, scale: Float): Bitmap {
        val retBitmap: Bitmap
        // 根据旋转角度，生成旋转矩阵
        val matrix = Matrix()
        matrix.setScale(scale, scale)
        // 将原始图片按照比例矩阵进行压缩，并得到新的图片
        retBitmap = Bitmap.createBitmap(
            sourceBitmap, 0, 0,
            sourceBitmap.width, sourceBitmap.height, matrix, true
        )
        sourceBitmap.recycle()
        return retBitmap
    }

    /**
     * 保存图片
     */
    private fun saveBitmap(sourceBitmap: Bitmap, filePath: String): Boolean {
        val file = File(filePath)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        return try {
            val fileOut = FileOutputStream(file)
            sourceBitmap.compress(Bitmap.CompressFormat.JPEG, mQuality, fileOut)
            fileOut.flush()
            fileOut.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 旋转为正确的方向
     */
    fun rotation(): BitmapUtil {
        mDegree = getBitmapRotateDegree(mFilePath)
        return this
    }

    /**
     * 图片是否为正的
     */
    fun correct(): Boolean {
        return getBitmapRotateDegree(mFilePath) == 0
    }

    /**
     * 获取图片旋转角度
     */
    fun degree(): Int {
        return getBitmapRotateDegree(mFilePath)
    }

    /**
     * 获取图片宽高
     */
    fun values(): IntArray {
        return getBitmapValue(mFilePath)
    }

    /**
     * 宽高的最大值
     */
    fun limitPixel(maxPixel: Int): BitmapUtil {
        mMaxPixel = maxPixel
        return this
    }

    /**
     * 文件最大值
     */
    fun limitSize(maxSize: Int): BitmapUtil {
        mMaxSize = maxSize
        return this
    }

    /**
     * 获取图片
     */
    fun getByte(callBack: (ByteArray?) -> Unit) {
        executor.execute {
            var bytes: ByteArray? = null
            val mBitmap = getBitmap()
            if (mBitmap == null) {
                mHandler.post { callBack(null) }
            } else {
                try {
                    val baos = ByteArrayOutputStream()
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, mQuality, baos)
                    baos.flush()
                    baos.close()
                    bytes = baos.toByteArray()
                    mBitmap.recycle()
                    System.gc()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                mHandler.post { callBack(bytes) }
            }
        }
    }

    fun getBase64(callBack: (String?) -> Unit) {
        executor.execute {
            var base64: String? = null
            val mBitmap = getBitmap()
            if (mBitmap == null) {
                mHandler.post { callBack(null) }
            } else {
                try {
                    val baos = ByteArrayOutputStream()
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, mQuality, baos)
                    baos.flush()
                    baos.close()
                    base64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
                    mBitmap.recycle()
                    System.gc()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                mHandler.post { callBack(base64) }
            }
        }
    }

    fun getBitmap(callBack: (Bitmap?) -> Unit) {
        executor.execute {
            val mBitmap = getBitmap()
            if (mBitmap == null) {
                mHandler.post { callBack(null) }
            } else {
                mHandler.post {
                    callBack(mBitmap.copy(Bitmap.Config.RGB_565, true))
                    mBitmap.recycle()
                    System.gc()
                }
            }
        }
    }

    /**
     * 保存图片
     */
    fun save(filePath: String, callBack: (String?) -> Unit) {
        executor.execute {
            val mBitmap = getBitmap()
            if (mBitmap == null) {
                mHandler.post { callBack(null) }
            } else {
                val isSave = saveBitmap(mBitmap, filePath)
                mBitmap.recycle()
                System.gc()
                mHandler.post { callBack(if (isSave) filePath else "") }
            }
        }
    }

    companion object {
        fun getInstance(path: String): BitmapUtil = BitmapUtil(path)
    }
}

private val executor by lazy { Executors.newCachedThreadPool() }