package com.github2136.base

import android.util.SparseArray
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by yb on 2018/10/29.
 */
open class ViewHolderRecyclerView(itemView: View,
                                  val itemClickListener: BaseRecyclerAdapter.OnItemClickListener? = null,
                                  val itemLongClickListener: BaseRecyclerAdapter.OnItemLongClickListener? = null) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
    private var views: SparseArray<View> = SparseArray()

    init {
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
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
                itemClickListener.onItemClick(position)
            }
        }
    }

    override fun onLongClick(v: View): Boolean {
        return if (itemLongClickListener != null) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemLongClickListener.onItemClick(position)
            }
            true
        } else {
            false
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