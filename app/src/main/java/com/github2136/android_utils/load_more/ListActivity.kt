package com.github2136.android_utils.load_more

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github2136.android_utils.R
import com.github2136.base.divider.Divider
import kotlinx.android.synthetic.main.activity_list.*
import java.util.*

/**
 * Created by yb on 2018/11/1.
 */
class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        val s = ArrayList<String>()
        s.add("中文1")
        s.add("中文1")
        s.add("中文1")
        s.add("中文1")
        s.add("中文1")
        s.add("中文1")
        s.add("中文1")
        s.add("中文1")
        s.add("中文2")
        s.add("中文2")
        s.add("中文2")
        s.add("中文2")
        s.add("中文2")
        s.add("中文2")
        s.add("中文2")
        s.add("中文2")
        s.add("中文2")
        s.add("中文2")
        s.add("中文2")
        s.add("中文2")
        s.add("cccccc")
        s.add("cccccc")
        s.add("cccccc")
        s.add("cccccc")
        s.add("ddddd")
        s.add("ddddd")
        s.add("ddddd")
        s.add("ddddd")
        s.add("eeeeee")
        s.add("eeeeee")
        s.add("eeeeee")
        s.add("asdf6")
        s.add("asdf7")
        s.add("asdf8")
        s.add("asdf9")
        s.add("asdf10")
        val adapter = ListAdapter(s)
        rv_content.adapter = adapter
        val d = Divider(this)
        d.backgroundColor = Color.parseColor("#987654")
        d.lineColorTop = Color.parseColor("#ff0000")
        d.lineHeightTop = 0

        d.lineColorBottom = Color.parseColor("#ffff00")
        d.lineHeightBottom = 0

        d.sticky = false
        d.showItemDivider = false
        rv_content.addItemDecoration(d)
        adapter.setOnItemClickListener { position -> Toast.makeText(this@ListActivity, "" + position, Toast.LENGTH_SHORT).show() }
        adapter.setOnItemLongClickListener { position -> Toast.makeText(this@ListActivity, "" + position, Toast.LENGTH_SHORT).show() }
    }
}