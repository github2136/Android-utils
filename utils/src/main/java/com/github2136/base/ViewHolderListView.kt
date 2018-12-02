package com.github2136.base

import android.util.SparseArray
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes

/**
 * Created by yb on 2018/10/30.
 */
open class ViewHolderListView(private val convertView: View) {
    private val viewArray = SparseArray<View>()

    private fun <T : View> getView(@IdRes resId: Int): T? {
        var v: View? = viewArray.get(resId)
        if (v != null) {
            return v as T
        }
        v = convertView.findViewById(resId)
        if (v == null) {
            return null
        }
        viewArray.put(resId, v)
        return v as T
    }
    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 设置文字内容
     *
     * @param resId
     * @param text
     */
    fun setText(@IdRes resId: Int, text: CharSequence) {
        getView<TextView>(resId)?.text = text
    }

    fun setText(@IdRes resId: Int, @StringRes resStr: Int) {
        getView<TextView>(resId)?.setText(resStr)
    }

    fun setGone(@IdRes resId: Int) {
        getView<View>(resId)?.run {
            visibility = View.GONE
        }
    }

    fun setVisible(@IdRes resId: Int) {
        getView<View>(resId)?.run {
            visibility = View.VISIBLE
        }
    }
}