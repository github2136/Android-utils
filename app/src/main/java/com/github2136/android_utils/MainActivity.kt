package com.github2136.android_utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.style.MaskFilterSpan
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.collection.ArrayMap
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.edit
import com.github2136.android_utils.load_more.GridActivity
import com.github2136.android_utils.load_more.ListActivity
import com.github2136.android_utils.load_more.ListViewActivity
import com.github2136.android_utils.proguard_class.ProguardClass
import com.github2136.android_utils.service.ServiceActivity
import com.github2136.android_utils.util.SSLUtil
import com.github2136.util.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection
import kotlin.concurrent.thread


/**
 * Created by yb on 2018/10/30.
 */
class MainActivity : BaseActivity(), View.OnClickListener {
    override fun getViewResId() = R.layout.activity_main

    private val permissionArrayMap = ArrayMap<String, String>()
    val permissionUtil by lazy { PermissionUtil(this) }

    override fun initData(savedInstanceState: Bundle?) {

        btn_bitmap.setOnClickListener(this)
        btn_date.setOnClickListener(this)
        btn_list_adapter.setOnClickListener(this)
        btn_grid_adapter.setOnClickListener(this)
        btn_list_view_adapter.setOnClickListener(this)
        btn_service.setOnClickListener(this)

        val UTC = Date().str(timeZone = TimeZone.getTimeZone("UTC").id) //获取UTC时间字符串
        val UTCDate = UTC.date(timeZone = TimeZone.getTimeZone("UTC").id) //UTC字符串转Date对象
        Log.e("utc time", UTC)
        val t2 = DateUtil.timeZoneConversion(UTC, DateUtil.DATE_PATTERN_YMDHMS, TimeZone.getTimeZone("UTC").id, TimeZone.getTimeZone("GMT+8").id) //UTC字符转转换为指定时区字符串
        val t3 = UTC.timeZoneConversion(DateUtil.DATE_PATTERN_YMDHMS, TimeZone.getTimeZone("UTC").id) //UTC字符转转换为指定时区字符串
        Log.e("t", t2)
        MessageDigestUtil.getMessageDigest("ttt".toByteArray(), "MD5")

        val calendar1 = Calendar.getInstance()
        val calendar2 = Calendar.getInstance()
        //        calendar2.add(Calendar.DAY_OF_MONTH, 2)
        calendar2.add(Calendar.HOUR, 23)
        calendar2.add(Calendar.MINUTE, 10)
        calendar2.add(Calendar.SECOND, 15)

        val interval = DateUtil.getRelativeTimeString(calendar1.time, calendar2.time, DateUtil.SECOND)
        Log.e("interval", interval)

        val data = "12355484s5d8f1w5w"

        val keyData = SymmetricEncryptionUtil.getKey(SymmetricEncryptionUtil.AES, 128)!!.encoded
        val keyStr = Base64.encodeToString(keyData, Base64.NO_WRAP)
        Log.e("asexxx", "key $keyStr")

        val encryptData = SymmetricEncryptionUtil.encrypt(
            Base64.decode(keyStr, Base64.NO_WRAP),
            SymmetricEncryptionUtil.AES,
            data.toByteArray(),
            SymmetricEncryptionUtil.MODE_ECB,
            SymmetricEncryptionUtil.PADDING_PKCS5
        )
        val encryptStr = Base64.encodeToString(encryptData, Base64.NO_WRAP)
        Log.e("asexxx", "encrypt $encryptStr")

        val decryptData = SymmetricEncryptionUtil.decrypt(
            Base64.decode(keyStr, Base64.NO_WRAP),
            SymmetricEncryptionUtil.AES,
            Base64.decode(encryptStr, Base64.NO_WRAP),
            SymmetricEncryptionUtil.MODE_ECB,
            SymmetricEncryptionUtil.PADDING_PKCS5
        )

        Log.e("asexxx", "decrypt ${String(decryptData!!)}")

        val data2 = "0000000000000000000000000000000000000000000000000000012345601234560112"
        val keyData2 = AsymmetricEncryptionUtil.getKey()
        val publicKey = Base64.encodeToString(keyData2!!.public.encoded, Base64.NO_WRAP)
        val privateKey = Base64.encodeToString(keyData2.private.encoded, Base64.NO_WRAP)

        Log.e("rsaxxx", "publicKey $publicKey")
        Log.e("rsaxxx", "privateKey $privateKey")

        val encryptData2 = AsymmetricEncryptionUtil.encryptByPublicKey(
            keyData2.public.encoded,
            data2.toByteArray(),
            AsymmetricEncryptionUtil.MODE_ECB,
            AsymmetricEncryptionUtil.PADDING_PKCS1
        )


        val encryptStr2 = Base64.encodeToString(encryptData2, Base64.NO_WRAP)
        Log.e("rsaxxx", "encrypt2 $encryptStr2")

        val decryptData2 = AsymmetricEncryptionUtil.decryptByPrivateKey(
            keyData2.private.encoded,
            encryptData2!!,
            AsymmetricEncryptionUtil.MODE_ECB,
            AsymmetricEncryptionUtil.PADDING_PKCS1
        )
        Log.e("rsaxxx", "decrypt ${String(decryptData2!!)}")


        val encryptData3 = AsymmetricEncryptionUtil.encryptByPrivateKey(
            keyData2.private.encoded,
            data2.toByteArray(),
            AsymmetricEncryptionUtil.MODE_ECB,
            AsymmetricEncryptionUtil.PADDING_PKCS1
        )
        val encryptStr3 = Base64.encodeToString(encryptData3, Base64.NO_WRAP)
        Log.e("rsaxxx", "encrypt3 $encryptStr3")

        val decryptData3 = AsymmetricEncryptionUtil.decryptByPublicKey(
            keyData2.public.encoded,
            encryptData3!!,
            AsymmetricEncryptionUtil.MODE_ECB,
            AsymmetricEncryptionUtil.PADDING_PKCS1
        )
        Log.e("rsaxxx", "decrypt ${String(decryptData3!!)}")

        val json = JsonUtil.instance
        //默认实例操作
        val str = JsonUtil.instance.toJson(JsonData("val1", 2, Date()))
        val jdata = json.fromJson<JsonData>(str)

        //新实例操作，每次newInstance都会调用有新的对象
        val json2 = JsonUtil.newInstance()
        val str2 = json2.toJson(JsonData("val1", 2, Date()))
        val jdata2 = json2.fromJson<JsonData>(str2)

        permissionArrayMap[Manifest.permission.WRITE_EXTERNAL_STORAGE] = "读写手机存储"
        permissionArrayMap[Manifest.permission.READ_PHONE_STATE] = "获取手机信息"

        permissionUtil.getPermission(permissionArrayMap) {
            Log.e("permissionUtil", "ok")
        }
        val sp = SPUtil.getSharedPreferences(this)
        getPreferences(Context.MODE_PRIVATE)
        sp.edit {
            putString("abc", "def")
            putInt("aaa", 111)
        }
        sp.getString("abc", "")

        FileUtil.getExternalStoragePrivateRootPath(this, "abc")

        val n1 = FileUtil.createFileName("log", ".txt")
        val n2 = FileUtil.createFileName(".txt")

        val c = ProguardClass()
        Log.e("init", "-----")
        val c1 = ProguardClass("aa")

        thread {
            val signKey = SignUtil.getKey(2048, mEncryptType = SignUtil.DSA)
            signKey?.apply {
                val signPublicKey = Base64.encodeToString(public.encoded, Base64.NO_WRAP)
                val signPrivateKey = Base64.encodeToString(private.encoded, Base64.NO_WRAP)
                Log.e("sign", "private key $signPrivateKey")
                Log.e("sign", "public key $signPublicKey")
                val content = "xxx111555999"
                val pri = Base64.decode(signPrivateKey, Base64.NO_WRAP)
                val pub = Base64.decode(signPublicKey, Base64.NO_WRAP)
                val sign = SignUtil.sign(pri, content.toByteArray(), SignUtil.SIGN_SHA256withDSA)
                val signStr = Base64.encodeToString(sign, Base64.NO_WRAP)

                Log.e("sign", "signStr $signStr")
                val v = SignUtil.verify(pub, content.toByteArray(), sign!!, SignUtil.SIGN_SHA256withDSA)
                Log.e("sign", "v $v")
            }
        }

        // Log.e("fileSize", FileUtil.getAutoFileSizeStr(FileUtil.getFileSize(File(FileUtil.getExternalStorageRootPath() + "/ForestAll"))))
        Log.e("fileSize", FileUtil.getAutoFileSizeStr(FileUtil.getExternalStorageSize()))
        Log.e("fileSize", FileUtil.getAutoFileSizeStr(FileUtil.getExternalStorageFreeSize()))

        getHttps()

        //通知权限是否打开
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            Log.e("xxx", "xxxxx")
        } else {
            Log.e("xxx", "yyyy")
            val intent = Intent()
            when {
                Build.VERSION.SDK_INT >= 26 -> {
                    // android 8.0引导
                    intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                    intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)
                }
                Build.VERSION.SDK_INT >= 21 -> {
                    // android 5.0-7.0
                    intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                    intent.putExtra("app_package", packageName)
                    intent.putExtra("app_uid", applicationInfo.uid)
                }
                else -> {
                    // 其他
                    intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                    intent.data = Uri.fromParts("package", packageName, null)
                }
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        val tv = findViewById<TextView>(R.id.tvSp)
        val spanUtil = SpanUtil()
        spanUtil.append("红色").setTextColor(Color.RED)
        spanUtil.append("20SP").setTextSize(20)
        spanUtil.append("红色背景").setBackground(Color.RED)
        spanUtil.append("加粗").setStyle(Typeface.BOLD)
        spanUtil.append("删除线").setLine(SpanUtil.LINE_STRIKETHROUGH)
        spanUtil.append("点击").setClick(tv, Color.MAGENTA) { v ->
            showToast("点击")
        }
        spanUtil.append("链接").setLink("http://www.baidu.com").setTextColor(Color.BLACK)
        spanUtil.append("其他").setSpan(MaskFilterSpan(BlurMaskFilter(5f, BlurMaskFilter.Blur.NORMAL)))

        tv.text = spanUtil.build()

    }

    fun getHttps() {
        thread {
            try {
                val sslContext = SSLUtil.verified(assets.open("a.cer"), assets.open("b.cer"))

                // Tell the URLConnection to use a SocketFactory from our SSLContext
                val url = URL("https://certs.cac.washington.edu/CAtest/")
                val urlConnection = url.openConnection() as HttpsURLConnection
                urlConnection.sslSocketFactory = sslContext.socketFactory
                val inputStream: InputStream = urlConnection.inputStream
                val result = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var length: Int = -1
                while (({ length = inputStream.read(buffer); length }()) != -1) {
                    result.write(buffer, 0, length)
                }
                val str = result.toString("UTF-8")
                Log.e("https", str)
            } catch (e: Exception) {
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        permissionUtil.onRestart()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_bitmap -> {
                intent = Intent(this, BitmapActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_date -> {
                intent = Intent(this, DateActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_list_adapter -> {
                intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_grid_adapter -> {
                intent = Intent(this, GridActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_list_view_adapter -> {
                intent = Intent(this, ListViewActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_service -> {
                intent = Intent(this, ServiceActivity::class.java)
                startActivity(intent)
            }
        }
    }
}