package com.github2136.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * Created by yb on 2018/10/27.
 */
abstract class BaseListViewAdapter<T>(protected var list: MutableList<T>? = null) : BaseAdapter() {
    lateinit var layoutInflater: LayoutInflater
    /**
     * 需要返回item布局的resource id
     *
     * @return
     */
    abstract fun getLayoutId(): Int

    /**
     * 使用该getItemView方法替换原来的getView方法
     *
     * @param position
     * @param convertView
     * @param holder
     */
    abstract fun getItemView(t: T, holder: ViewHolderListView, position: Int, convertView: View)


    override fun getCount(): Int {
        return list?.size ?: 0
    }

    override fun getItem(position: Int): T? {
        return list?.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (!::layoutInflater.isInitialized) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        var convertView = convertView
        val holder: ViewHolderListView
        if (convertView == null) {
            convertView = layoutInflater.inflate(getLayoutId(), parent, false)
            holder = ViewHolderListView(convertView)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolderListView
        }
        getItem(position)?.let {
            getItemView(it, holder, position, convertView!!)
        }
        return convertView!!
    }

    fun setData(list: MutableList<T>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun appendData(list: MutableList<T>) {
        if (this.list == null) {
            this.list = list
        }
        this.list?.let {
            it.addAll(list)
            notifyDataSetChanged()
        }
    }
}