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
        s.add("aaaaaa")
        s.add("aaaaaa")
        s.add("aaaaaa")
        s.add("aaaaaa")
        s.add("aaaaaa")
        s.add("aaaaaa")
        s.add("aaaaaa")
        s.add("aaaaaa")
        s.add("bbbbbb")
        s.add("bbbbbb")
        s.add("bbbbbb")
        s.add("bbbbbb")
        s.add("bbbbbb")
        s.add("bbbbbb")
        s.add("bbbbbb")
        s.add("bbbbbb")
        s.add("bbbbbb")
        s.add("bbbbbb")
        s.add("bbbbbb")
        s.add("bbbbbb")
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
        d.lineColor = Color.parseColor("#ff0000")
        rv_content.addItemDecoration(d)
        adapter.setOnItemClickListener { position -> Toast.makeText(this@ListActivity, "" + position, Toast.LENGTH_SHORT).show() }
        adapter.setOnItemLongClickListener { position -> Toast.makeText(this@ListActivity, "" + position, Toast.LENGTH_SHORT).show() }
    }
}