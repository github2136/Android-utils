package com.github2136.android_utils.load_more

import com.github2136.android_utils.R
import com.github2136.base.BaseRecyclerAdapter
import com.github2136.base.ViewHolderRecyclerView
import com.github2136.base.divider.IDivider

/**
 * Created by yb on 2018/11/1.
 */
class ListAdapter(list: MutableList<String>) : BaseRecyclerAdapter<String>(list), IDivider {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_item
    }

    override fun onBindView(t: String, holder: ViewHolderRecyclerView, position: Int) {
        holder.setText(R.id.tv_text, t)
    }

    override fun getShowTxt(position: Int): String? {
        return getItem(position)
    }
}