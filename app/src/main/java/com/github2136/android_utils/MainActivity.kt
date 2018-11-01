package com.github2136.android_utils

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github2136.android_utils.load_more.ListActivity
import com.github2136.android_utils.load_more.LoadMoreActivity
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Created by yb on 2018/10/30.
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_bitmap.setOnClickListener(this)
        btn_date.setOnClickListener(this)
        btn_load_more.setOnClickListener(this)
        btn_list_adapter.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        var intent: Intent? = null
        when (v?.id) {
            R.id.btn_bitmap -> intent = Intent(this, BitmapActivity::class.java)
            R.id.btn_date -> intent = Intent(this, DateActivity::class.java)
            R.id.btn_list_adapter -> intent = Intent(this, ListActivity::class.java)
            R.id.btn_load_more -> intent = Intent(this, LoadMoreActivity::class.java)
        }
        if (intent != null) {
            startActivity(intent)
        }
    }
}