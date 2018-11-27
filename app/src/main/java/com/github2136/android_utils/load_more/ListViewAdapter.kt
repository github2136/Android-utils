package com.github2136.android_utils.load_more

import android.view.View
import com.github2136.android_utils.R
import com.github2136.base.BaseListAdapter
import com.github2136.base.ViewHolderListView

/**
 * Created by yb on 2018/11/26.
 */
class ListViewAdapter(list: MutableList<String>) : BaseListAdapter<String>(list) {
    override fun getLayoutId(): Int {
        return R.layout.item_item
    }

    override fun getItemView(t: String, holder: ViewHolderListView, position: Int, convertView: View) {
        holder.setText(com.github2136.android_utils.R.id.tv_text, t)
    }
}