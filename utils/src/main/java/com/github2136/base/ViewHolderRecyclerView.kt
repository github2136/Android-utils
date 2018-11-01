package com.github2136.base

import android.content.Context
import android.support.annotation.*
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by yb on 2018/10/29.
 */
class ViewHolderRecyclerView(val mContext: Context,
                             val mAdapter: BaseRecyclerAdapter<*>,
                             itemView: View,
                             val itemClickListener: BaseRecyclerAdapter.OnItemClickListener?,
                             val itemLongClickListener: BaseRecyclerAdapter.OnItemLongClickListener?) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener,
        View.OnLongClickListener {
    private var views: SparseArray<View> = SparseArray()
    fun getRootView(): View {
        return itemView
    }

    fun <T : View> getView(@IdRes id: Int): T? {
        var v: View? = views.get(id)
        if (v != null) {
            return v as T
        }
        v = itemView.findViewById(id)
        if (v == null) {
            return null
        }
        views.put(id, v)
        return v as T
    }

    override fun onClick(v: View) {
        if (itemClickListener != null) {
            //如果adpater正在调用notifyDataSetChanged那么getAdapterPosition会返回 RecyclerView.NO_POSITION
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClick(mAdapter, position)
            }
        }
    }

    override fun onLongClick(v: View): Boolean {
        if (itemLongClickListener != null) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemLongClickListener.onItemClick(mAdapter, position)
            }
            return true
        } else {
            return false
        }
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