package com.github2136.base;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * adapter基础类
 * Created by yubin on 2016/3/10.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    protected List<T> mList;
    protected Context mContext;
    protected LayoutInflater inflater;
    /**
     * 需要返回item布局的resource id
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 使用该getItemView方法替换原来的getView方法
     *
     * @param position
     * @param convertView
     * @param holder
     */
    public abstract void getItemView(T t, ViewHolderListView holder, int position, View convertView);

    public BaseListAdapter(Context context, List<T> list) {
        inflater = LayoutInflater.from(context);
        this.mList = list == null ? new ArrayList<T>() : list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderListView holder;
        if (convertView == null) {
            convertView = inflater.inflate(getLayoutId(), parent, false);
            holder = new ViewHolderListView(mContext, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderListView) convertView.getTag();
        }
        getItemView(getItem(position), holder, position, convertView);
        return convertView;
    }
    public void setListData(List<T> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void appendListData(List<T> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }
}