package com.github2136.android_utils.load_more

import com.github2136.android_utils.R
import com.github2136.base.BaseRecyclerAdapter
import com.github2136.base.ViewHolderRecyclerView

/**
 * Created by 44569 on 2023/4/21
 */
class GridAdapter(list: MutableList<String>) : BaseRecyclerAdapter<String>(list) {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_item
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindView(t: String, holder: ViewHolderRecyclerView, position: Int) {
        holder.setText(R.id.tv_text, t)
    }
}