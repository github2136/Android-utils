package com.github2136.android_utils.load_more

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.github2136.android_utils.R
import com.github2136.base.BaseLoadMoreRecyclerAdapter
import com.github2136.base.ViewHolderRecyclerView

/**
 * Created by yb on 2018/11/1.
 */
class LoadMoreAdapter(context: Context, list: MutableList<String>?) : BaseLoadMoreRecyclerAdapter<String>(context, list) {
    override fun getPageSize(): Int {
        return 5
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_item
    }

    override fun onBindView(t: String, holder: ViewHolderRecyclerView, position: Int) {
        holder.setText(R.id.tv_text, t)
        val tv: TextView? = holder.getView(R.id.tv)
        tv?.setOnClickListener({ Toast.makeText(mContext, "ssss", Toast.LENGTH_SHORT).show() })
    }
}