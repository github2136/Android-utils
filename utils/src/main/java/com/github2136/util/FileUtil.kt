package com.github2136.util

import android.annotation.TargetApi
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import java.io.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 *      文件工具 返回的所有根目录都不带斜杠
 *      在application中添加名为util_project_path的<meta-data/>使用getExternalStorageProjectPath获取
 *      getSuffix(urlStr);//获取文件后缀
 *      MimeTypeMap.getFileExtensionFromUrl(urlStr);//获取文件后缀
 *      MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
 *      mimeTypeMap.getMimeTypeFromExtension(suffix);//通过后缀获取MIME
 *      所需权限<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 */
object FileUtil {
    private val PATH_LOG = "Log"
    private val PATH_DOC = "Documents"

    /**
     * 外部存储可写
     */
    @JvmStatic
    fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    /**
     * 外部存储可读
     */
    @JvmStatic
    fun isExternalStorageReadable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
    }

    /**
     * 外部存储根目录
     */
    @JvmStatic
    fun getExternalStorageRootPath(): String {
        return Environment.getExternalStorageDirectory().absoluteFile.toString()
    }

    /**
     * 外部存储项目目录，默认为AndroidUtil
     *
     * @return
     */
    @JvmStatic
    fun getExternalStorageProjectPath(context: Context): String {
        var projectPath = "AndroidUtil"
        try {
            val appInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            appInfo.metaData?.getString("util_project_path")?.apply {
                projectPath = this
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return Environment.getExternalStorageDirectory().absoluteFile.toString() + File.separator + projectPath
    }


    /**
     * 外部存储私有根目录
     */
    @JvmStatic
    fun getExternalStoragePrivateRootPath(context: Context, path: String? = null): String {
        return context.getExternalFilesDir(path)!!.toString()
    }

    /**
     * 外部存储私有缓存目录
     */
    @JvmStatic
    fun getExternalStoragePrivateCachePath(context: Context): String {
        return context.externalCacheDir!!.toString()
    }

    /**
     * 外部存储私有文档目录
     */
    @JvmStatic
    fun getExternalStoragePrivateDocPath(context: Context): String {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            val path = context.getExternalFilesDir(PATH_DOC)!!.absolutePath
            val doc = File(path)
            if (!doc.exists()) {
                doc.mkdirs()
            }
            path
        } else {
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.absolutePath
        }
    }

    /**
     * 外部存储私有图片目录
     */
    @JvmStatic
    fun getExternalStoragePrivatePicPath(context: Context): String {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath
    }

    /**
     * 外部存储私有日志目录
     */
    @JvmStatic
    fun getExternalStoragePrivateLogPath(context: Context): String {
        val path = context.getExternalFilesDir(PATH_LOG)!!.absolutePath
        val log = File(path)
        if (!log.exists()) {
            log.mkdirs()
        }
        return path
    }
    ///////////////////////////////////////////////////////////////////////////
    // 创建文件名称、创建文件
    ///////////////////////////////////////////////////////////////////////////
    /**
     * 创建文件名称
     *
     * @param suffix 后缀 .jpg .png
     */
    @JvmStatic
    fun createFileName(suffix: String): String {
        return createFileName(null, suffix)
    }

    /**
     * 创建文件名称
     *
     * @param prefix 前缀
     * @param suffix 后缀 .jpg .png
     */
    @JvmStatic
    fun createFileName(prefix: String?, suffix: String): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.CHINA).format(Date())
        return (if (prefix == null) "" else prefix + "_") + timeStamp + suffix
    }

    ///////////////////////////////////////////////////////////////////////////
    // 获得文件绝对路径
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 根据Uri获取文件绝对路径，解决Android4.4以上版本Uri转换
     */
    @TargetApi(19)
    @JvmStatic
    fun getFileAbsolutePath(context: Context, imageUri: Uri): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + File.separator + split[1]
                }
            } else if (isDownloadsDocument(imageUri)) {
                val id = DocumentsContract.getDocumentId(imageUri)
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(imageUri.scheme, ignoreCase = true)) {
            return if (isGooglePhotosUri(imageUri)) imageUri.lastPathSegment else getDataColumn(context, imageUri, null, null)
        } else if ("file".equals(imageUri.scheme, ignoreCase = true)) {
            return imageUri.path
        }
        return null
    }

    private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = MediaStore.Images.Media.DATA
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    ///////////////////////////////////////////////////////////////////////////
    // 文件大小
    ///////////////////////////////////////////////////////////////////////////
    val SIZETYPE_B = 1/*获取文件大小单位为B的double值*/
    val SIZETYPE_KB = 1024/*获取文件大小单位为KB的double值*/
    val SIZETYPE_MB = 1048576/*获取文件大小单位为MB的double值*/
    val SIZETYPE_GB = 1073741824/*获取文件大小单位为GB的double值*/

    /**
     * 获取指定文件夹大小
     */
    @Throws(Exception::class)
    private fun getFileSize(f: File): Long {
        var size: Long = 0
        if (f.isDirectory) {
            val files = f.listFiles()
            files.forEach {
                size += getFileSize(it)
            }
        } else {
            val fis = FileInputStream(f)
            size = fis.available().toLong()
        }
        return size
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param f 文件
     * @return 计算好的带1B、1KB、1MB、1GB的字符串
     */
    @JvmStatic
    fun getAutoFilesSize(f: File): String {
        var blockSize: Long = 0
        try {
            blockSize = getFileSize(f)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("获取文件大小", "获取失败!")
        }
        val df = DecimalFormat("#.00")
        return df.format(getAutoFileSize(blockSize)) + getAutoFileSizeUnit(blockSize)
    }

    /**
     * 自动换算为合适的文件大小
     */
    @JvmStatic
    fun getAutoFileSize(fileS: Long): Double {
        return when {
            fileS < SIZETYPE_KB -> fileS.toDouble()
            fileS < SIZETYPE_MB -> fileS.toDouble() / SIZETYPE_KB
            fileS < SIZETYPE_GB -> fileS.toDouble() / SIZETYPE_MB
            else                -> fileS.toDouble() / SIZETYPE_GB
        }
    }

    /**
     * 根据文件长度获取大小 1 2 5等,指定转换的类型
     */
    @JvmStatic
    fun getFilesSize(fileS: Long, sizeType: Int): Double {
        val df = DecimalFormat("#.00")
        return df.format(fileS.toDouble() / sizeType).toDouble()
    }

    /**
     * 根据文件大小自动获取文件单位
     */
    @JvmStatic
    fun getAutoFileSizeUnit(fileS: Long): String {
        return when {
            fileS < SIZETYPE_KB -> "B"
            fileS < SIZETYPE_MB -> "KB"
            fileS < SIZETYPE_GB -> "MB"
            else                -> "GB"
        }
    }
    ///////////////////////////////////////////////////////////////////////////
    // 文件操作
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 从本地或网络地址获取文件后缀如 jpg  txt<br></br>
     * MimeTypeMap.getFileExtensionFromUrl(urlStr);
     *
     * @param str
     * @return
     */
    @JvmStatic
    fun getSuffix(str: String): String {
        var str = str
        val fragment = str.lastIndexOf('#')
        if (fragment > 0) {
            str = str.substring(0, fragment)
        }
        val query = str.lastIndexOf('?')
        if (query > 0) {
            str = str.substring(0, query)
        }
        val filenamePos = str.lastIndexOf(File.separatorChar)
        val filename = if (0 <= filenamePos) str.substring(filenamePos + 1) else str
        if (!filename.isEmpty()) {
            val dotPos = filename.lastIndexOf('.')
            if (0 <= dotPos) {
                return filename.substring(dotPos + 1)
            }
        }
        return ""
    }

    /**
     * 保存文字至指定文件
     *
     * @param path
     * @param text
     */
    @JvmStatic
    fun saveFile(path: String, text: String) {
        saveFile(path, text.toByteArray())
    }

    /**
     * 内容追加
     *
     * @param path
     * @param text
     */
    @JvmStatic
    fun appendFile(path: String, text: String) {
        appendFile(path, text.toByteArray())
    }

    /**
     * 存文字至指定文件
     *
     * @param path
     * @param bytes
     */
    @JvmStatic
    fun saveFile(path: String, bytes: ByteArray) {
        try {
            val file = File(path)
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            if (!file.exists()) {
                file.createNewFile()
            }
            val outputStream = FileOutputStream(file)
            outputStream.write(bytes)
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    /**
     * 内容追加
     *
     * @param path
     * @param bytes
     */
    @JvmStatic
    fun appendFile(path: String, bytes: ByteArray) {
        try {
            val file = File(path)
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            if (!file.exists()) {
                file.createNewFile()
            }
            val outputStream = FileOutputStream(file, true)
            outputStream.write(bytes)
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 文件读取
     *
     * @param path
     * @return
     */
    @JvmStatic
    fun readFile(path: String): String? {
        try {
            val file = File(path)
            if (file.exists()) {
                val inputStream = FileInputStream(file)
                val bytes = ByteArray(file.length().toInt())
                inputStream.read(bytes)
                inputStream.close()
                return String(bytes, charset("UTF-8"))
            } else {
                return null
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 清理文件件
     *
     * @param path
     * @return
     */
    @JvmStatic
    fun cleanFolder(path: String): Boolean {
        val folder = File(path)
        return if (folder.isDirectory) {
            val files = folder.listFiles()
            files.forEach { it.delete() }
            true
        } else {
            false
        }
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    @JvmStatic
    fun deleteFile(path: String): Boolean {
        val file = File(path)
        if (file.exists()) {
            file.delete()
            return true
        } else {
            return false
        }
    }
}