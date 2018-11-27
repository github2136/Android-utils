package com.github2136.android_utils.load_more

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github2136.android_utils.R
import kotlinx.android.synthetic.main.activity_list_view.*
import java.util.*

class ListViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)
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
        val adapter = ListViewAdapter( s)

        rv_content.adapter = adapter
    }
}
