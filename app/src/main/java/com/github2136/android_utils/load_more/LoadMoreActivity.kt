package com.github2136.android_utils.load_more

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.github2136.android_utils.R
import com.github2136.android_utils.load_more.BaseListActivity
import com.github2136.android_utils.load_more.LoadMoreAdapter
import com.github2136.base.BaseLoadMoreRecyclerAdapter
import java.util.ArrayList

/**
 * Created by yb on 2018/11/1.
 */
class LoadMoreActivity  : BaseListActivity<String>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_load_more
    }

    override fun initListData(savedInstanceState: Bundle?) {
        val textView = TextView(this)
        textView.text = "asdfsadf"
        val layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)
        textView.layoutParams = layoutParams
        mAdapter.setHeadView(textView)

    }

    override fun getAdapter(): BaseLoadMoreRecyclerAdapter<String> {
        return LoadMoreAdapter(this, null)
    }

    override fun getListData() = Thread(Runnable {
        try {
            Thread.sleep(100)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        Handler(Looper.getMainLooper()).post {
            val s = ArrayList<String>()
            s.add("asdf1")
            s.add("asdf2")
            s.add("asdf3")
            s.add("asdf4")
            s.add("asdf5")
            //                        s.add("asdf6");
            //                        s.add("asdf7");
            //                        s.add("asdf8");
            //                        s.add("asdf9");
            //                        s.add("asdf10");

            //                        if (new Random().nextBoolean()) {
            getDataSuccessful(s)
            //                        } else {
            //                            getDataSuccessful(null);
            //                        }
        }
    }).start()

    override fun itemClick(t: String, position: Int) {
        super.itemClick(t, position)
        Toast.makeText(this, t, Toast.LENGTH_SHORT).show()

    }

    override fun itemLongClick(t: String, position: Int) {
        super.itemLongClick(t, position)
        Toast.makeText(this, "long$t", Toast.LENGTH_SHORT).show()
    }
}