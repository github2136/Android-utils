package com.github2136.android_utils

import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github2136.android_utils.load_more.ListActivity
import com.github2136.android_utils.load_more.ListViewActivity
import com.github2136.util.DateUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Date
import java.util.*


/**
 * Created by yb on 2018/10/30.
 */
class MainActivity : BaseActivity(), View.OnClickListener {
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