package com.github2136.android_utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.collection.ArrayMap
import androidx.core.content.edit
import com.github2136.android_utils.load_more.ListActivity
import com.github2136.android_utils.load_more.ListViewActivity
import com.github2136.util.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * Created by yb on 2018/10/30.
 */
class MainActivity : BaseActivity(), View.OnClickListener {

    private val permissionArrayMap = ArrayMap<String, String>()
    val permissionUtil by lazy { PermissionUtil(this) }

    override fun getViewResId(): Int {
        return R.layout.activity_main
    }

    override fun initData(savedInstanceState: Bundle?) {
        btn_bitmap.setOnClickListener(this)
        btn_date.setOnClickListener(this)
        btn_list_adapter.setOnClickListener(this)
        btn_list_view_adapter.setOnClickListener(this)


        val UTC = DateUtil.date2str(Date(), timeZone = TimeZone.getTimeZone("UTC").id)
        val UTCDate = DateUtil.str2date(UTC, timeZone = TimeZone.getTimeZone("UTC").id)
        val t = DateUtil.getDateNow(timeZone = TimeZone.getTimeZone("UTC").id)
        Log.e("t", t)
        val t2 = DateUtil.UTC2GMT(t, TimeZone.getDefault().id)
        Log.e("t", t2)
        MessageDigestUtil.getMessageDigest("ttt".toByteArray(), "MD5")

        val calendar1 = Calendar.getInstance()
        val calendar2 = Calendar.getInstance()
//        calendar2.add(Calendar.DAY_OF_MONTH, 2)
        calendar2.add(Calendar.HOUR, 23)
        calendar2.add(Calendar.MINUTE, 10)
        calendar2.add(Calendar.SECOND, 15)

        val interval = DateUtil.getRelativeTimeString(calendar1.time, calendar2.time, DateUtil.MINUTE)
        Log.e("interval", interval)

        val data = "12355484s5d8f1w5w"

        val keyData = SymmetricEncryptionUtil.getKey(SymmetricEncryptionUtil.AES, 128)!!.encoded
        val keyStr = Base64.encodeToString(keyData, Base64.NO_WRAP)
        Log.e("asexxx", "key $keyStr")

        val encryptData = SymmetricEncryptionUtil.encrypt(Base64.decode(keyStr, Base64.NO_WRAP),
                                                          SymmetricEncryptionUtil.AES,
                                                          data.toByteArray(),
                                                          SymmetricEncryptionUtil.MODE_ECB,
                                                          SymmetricEncryptionUtil.PADDING_PKCS5)
        val encryptStr = Base64.encodeToString(encryptData, Base64.NO_WRAP)
        Log.e("asexxx", "encrypt $encryptStr")

        val decryptData = SymmetricEncryptionUtil.decrypt(Base64.decode(keyStr, Base64.NO_WRAP),
                                                          SymmetricEncryptionUtil.AES,
                                                          Base64.decode(encryptStr, Base64.NO_WRAP),
                                                          SymmetricEncryptionUtil.MODE_ECB,
                                                          SymmetricEncryptionUtil.PADDING_PKCS5)

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
            AsymmetricEncryptionUtil.PADDING_PKCS1)


        val encryptStr2 = Base64.encodeToString(encryptData2, Base64.NO_WRAP)
        Log.e("rsaxxx", "encrypt2 $encryptStr2")

        val decryptData2 = AsymmetricEncryptionUtil.decryptByPrivateKey(
            keyData2.private.encoded,
            encryptData2!!,
            AsymmetricEncryptionUtil.MODE_ECB,
            AsymmetricEncryptionUtil.PADDING_PKCS1)
        Log.e("rsaxxx", "decrypt ${String(decryptData2!!)}")


        val encryptData3 = AsymmetricEncryptionUtil.encryptByPrivateKey(
            keyData2.private.encoded,
            data2.toByteArray(),
            AsymmetricEncryptionUtil.MODE_ECB,
            AsymmetricEncryptionUtil.PADDING_PKCS1)
        val encryptStr3 = Base64.encodeToString(encryptData3, Base64.NO_WRAP)
        Log.e("rsaxxx", "encrypt3 $encryptStr3")

        val decryptData3 = AsymmetricEncryptionUtil.decryptByPublicKey(
            keyData2.public.encoded,
            encryptData3!!,
            AsymmetricEncryptionUtil.MODE_ECB,
            AsymmetricEncryptionUtil.PADDING_PKCS1)
        Log.e("rsaxxx", "decrypt ${String(decryptData3!!)}")
        JsonUtil.dateFormat = "yyyy-MM-dd"
        val json = JsonUtil.instance
        val dStr = json.getGson().toJson(Date())
        val d = json.getObjectByStr(dStr, Date::class.java)


        permissionArrayMap[Manifest.permission.WRITE_EXTERNAL_STORAGE] = "读写手机存储"
        permissionArrayMap[Manifest.permission.READ_PHONE_STATE] = "获取手机信息"

        permissionUtil.getPermission(permissionArrayMap) {
            showToast("ok")
        }
        val sp = SPUtil.getSharedPreferences(this)
        getPreferences(Context.MODE_PRIVATE)
        sp.edit {
            putString("abc", "def")
            putInt("aaa", 111)
        }
        sp.getString("abc","")

        FileUtil.getExternalStoragePrivateRootPath(this,"abc")
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
        var intent: Intent? = null
        when (v?.id) {
            R.id.btn_bitmap -> intent = Intent(this, BitmapActivity::class.java)
            R.id.btn_date -> intent = Intent(this, DateActivity::class.java)
            R.id.btn_list_adapter -> intent = Intent(this, ListActivity::class.java)
            R.id.btn_list_view_adapter -> intent = Intent(this, ListViewActivity::class.java)
        }
        intent?.let {
            startActivity(it)
        }
    }
}