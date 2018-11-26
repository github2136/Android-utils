package com.github2136.android_utils.load_more

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github2136.android_utils.R
import com.github2136.base.BaseRecyclerAdapter
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
        s.add("asdf1")
        s.add("asdf2")
        s.add("asdf3")
        s.add("asdf4")
        s.add("asdf5")
        s.add("asdf6")
        s.add("asdf7")
        s.add("asdf8")
        s.add("asdf9")
        s.add("asdf10")
        val adapter = ListAdapter( s)
        adapter.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseRecyclerAdapter<*>, position: Int) {
                Toast.makeText(this@ListActivity, "" + position, Toast.LENGTH_SHORT).show()
            }
        })
        adapter.setOnItemLongClickListener(object : BaseRecyclerAdapter.OnItemLongClickListener {
            override fun onItemClick(adapter: BaseRecyclerAdapter<*>, position: Int) {
                Toast.makeText(this@ListActivity, "" + position, Toast.LENGTH_SHORT).show()
            }
        })
        rv_content.adapter = adapter
    }
}