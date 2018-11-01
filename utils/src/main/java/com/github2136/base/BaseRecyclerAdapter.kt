package com.github2136.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.ArrayList

/**
 * Created by yb on 2018/10/29.
 */
abstract class BaseRecyclerAdapter<T>(context: Context, list: MutableList<T>?) : RecyclerView.Adapter<ViewHolderRecyclerView>() {
    protected val mContext: Context by lazy { context }
    protected var mList: MutableList<T>
    protected val mLayoutInflater: LayoutInflater by lazy { LayoutInflater.from(context) }

    init {
        mList = list ?: mutableListOf()
    }

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
    fun getItem(position: Int): T {
        return mList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRecyclerView {
        val v = mLayoutInflater.inflate(getLayoutId(viewType), parent, false)
        return ViewHolderRecyclerView(mContext, this, v, null, null)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolderRecyclerView, position: Int) {
        onBindView(getItem(position), holder, position)
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
        fun onItemClick(adapter: BaseRecyclerAdapter<*>, position: Int)
    }

    /**
     * 单项长按
     */
    interface OnItemLongClickListener {
        fun onItemClick(adapter: BaseRecyclerAdapter<*>, position: Int)
    }

    fun setData(list: MutableList<T>) {
        this.mList = list
        notifyDataSetChanged()
    }

    fun appendData(list: List<T>) {
        mList.addAll(list)
        notifyDataSetChanged()
    }
}