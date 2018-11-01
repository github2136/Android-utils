package com.github2136.base

import android.content.Context
import android.support.annotation.*
import android.support.v4.content.ContextCompat
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by yb on 2018/10/30.
 */
class ViewHolderListView(val mContext: Context, val convertView: View) {
    private val viewArray = SparseArray<View>()

    fun <T : View> getView(@IdRes resId: Int): T? {
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
        val textView = getView<TextView>(resId)
        if (textView != null) {
            textView.text = text
        }
    }

    fun setText(@IdRes resId: Int, @StringRes resStr: Int) {
        val textView = getView<TextView>(resId)
        textView?.setText(resStr)
    }

    /**
     * 设置图片
     *
     * @param resId
     * @param resDraw
     */
    fun setImage(@IdRes resId: Int, @DrawableRes resDraw: Int) {
        val imageView = getView<ImageView>(resId)
        imageView?.setImageResource(resDraw)
    }

    /**
     * 背景图
     *
     * @param resId
     * @param resDraw
     */
    fun setBackgroundRes(@IdRes resId: Int, @DrawableRes resDraw: Int) {
        val view = getView<View>(resId)
        view?.setBackgroundResource(resDraw)
    }

    fun setBackgroundColRes(@IdRes resId: Int, @ColorRes resColor: Int) {
        val view = getView<View>(resId)
        view?.setBackgroundColor(ContextCompat.getColor(mContext, resColor))
    }

    fun setBackgroundColInt(@IdRes resId: Int, @ColorInt color: Int) {
        val view = getView<View>(resId)
        view?.setBackgroundColor(color)
    }

    fun setGone(@IdRes resId: Int) {
        val view = getView<View>(resId)
        if (view != null) {
            view.visibility = View.GONE
        }
    }

    fun setVisible(@IdRes resId: Int) {
        val view = getView<View>(resId)
        if (view != null) {
            view.visibility = View.VISIBLE
        }
    }
}