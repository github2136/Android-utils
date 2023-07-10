package com.github2136.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by yb on 2018/10/29.
 */
abstract class BaseRecyclerAdapter<T>(protected var list: MutableList<T>? = null) : RecyclerView.Adapter<ViewHolderRecyclerView>() {
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
        return ViewHolderRecyclerView(v, itemClickListener, itemLongClickListener)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolderRecyclerView, position: Int) {
        getItem(position)?.let {
            onBindView(it, holder, position)
        }
    }

    protected var itemClickListener: ((Int) -> Unit)? = null
    protected var itemLongClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(itemClickListener: (position: Int) -> Unit) {
        this.itemClickListener = itemClickListener
    }

    fun setOnItemLongClickListener(itemLongClickListener: (position: Int) -> Unit) {
        this.itemLongClickListener = itemLongClickListener
    }

    fun setData(list: List<T>) {
        this.list = list.toMutableList()
        notifyDataSetChanged()
    }

    fun appendData(list: List<T>) {
        if (this.list == null) {
            this.list = list.toMutableList()
        }
        this.list?.let {
            it.addAll(list)
            notifyDataSetChanged()
        }
    }
}