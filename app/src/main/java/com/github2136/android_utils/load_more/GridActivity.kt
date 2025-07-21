package com.github2136.android_utils.load_more

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.github2136.android_utils.R
import com.github2136.base.divider.GridItemDecoration
import kotlinx.android.synthetic.main.activity_list.*

/**
 * Created by 44569 on 2023/4/21
 */
class GridActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)
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
        s.add("cccccc")
        s.add("cccccc")
        s.add("cccccc")
        s.add("cccccc")
        s.add("ddddd")
        s.add("ddddd")
        s.add("ddddd")
        s.add("ddddd")
        s.add("ddddd")
        s.add("ddddd")
        s.add("ddddd")
        s.add("eeeeee")
        s.add("eeeeee")
        val adapter = GridAdapter(s)
        rv_content.adapter = adapter
        val gid = GridItemDecoration()
        gid.includeEdge=false
        gid.spaceColor = Color.parseColor("#33ff00ff")
        gid.space = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, Resources.getSystem().displayMetrics).toInt()
        rv_content.addItemDecoration(gid)
    }
}