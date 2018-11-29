package com.github2136.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by yb on 2018/10/29.
 */
abstract class BaseRecyclerAdapter<T>(private var list: MutableList<T>? = null) : RecyclerView.Adapter<ViewHolderRecyclerView>() {
    protected lateinit var mLayoutInflater: LayoutInflater
    /**
     * 通过类型获得布局ID
     *
     * @param viewType
     * @return
     */
    abstract fun getLayoutId(viewType: Int): Int

    protected abstract fun onBindView(t: T, holder: ViewHolderRecyclerView, position: Int)

    /**
     * 获得对象
     */
    fun getItem(position: Int): T? {
        return list?.get(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRecyclerView {
        if (!::mLayoutInflater.isInitialized) {
            mLayoutInflater = LayoutInflater.from(parent.context)
        }
        val v = mLayoutInflater.inflate(getLayoutId(viewType), parent, false)
        return ViewHolderRecyclerView(this, v, itemClickListener, itemLongClickListener)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolderRecyclerView, position: Int) {
        getItem(position)?.let {
            onBindView(it, holder, position)
        }
    }

    protected var itemClickListener: OnItemClickListener? = null
    protected var itemLongClickListener: OnItemLongClickListener? = null

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun setOnItemLongClickListener(itemLongClickListener: OnItemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener
    }

    /**
     * 单项点击事件
     */
    interface OnItemClickListener {
        fun onItemClick(adapter: RecyclerView.Adapter<*>, position: Int)
    }

    /**
     * 单项长按
     */
    interface OnItemLongClickListener {
        fun onItemClick(adapter: RecyclerView.Adapter<*>, position: Int)
    }

    fun setData(list: MutableList<T>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun appendData(list: List<T>) {
        this.list?.let {
            it.addAll(list)
            notifyDataSetChanged()
        }
    }
}