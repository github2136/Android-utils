package com.github2136.android_utils

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github2136.android_utils.load_more.ListActivity
import com.github2136.android_utils.load_more.ListViewActivity
import kotlinx.android.synthetic.main.activity_main.*


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