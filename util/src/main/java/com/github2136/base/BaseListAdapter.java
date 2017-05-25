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
    public abstract void getItemView(T t, ViewHolder holder, int position, View convertView);

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
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(getLayoutId(), parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        getItemView(getItem(position), holder, position, convertView);
        return convertView;
    }

    protected static class ViewHolder {
        SparseArray<View> viewArray = new SparseArray<>();
        View convertView;

        public ViewHolder(View convertView) {
            this.convertView = convertView;
        }

        public <T extends View> T getView(@IdRes int resId) {
            View v = viewArray.get(resId);
            if (v == null) {
                v = convertView.findViewById(resId);
                viewArray.append(resId, v);
            }
            return (T) v;
        }

        /**
         * 设置文字内容
         *
         * @param resId
         * @param text
         */
        public void setText(@IdRes int resId, CharSequence text) {
            TextView textView = getView(resId);
            if (textView != null) {
                textView.setText(text);
            }
        }

        public void setText(@IdRes int resId, @StringRes int resStr) {
            TextView textView = getView(resId);
            if (textView != null) {
                textView.setText(resStr);
            }
        }

        /**
         * 设置图片
         *
         * @param resId
         * @param resDraw
         */
        public void setImage(@IdRes int resId, @DrawableRes int resDraw) {
            ImageView imageView = getView(resId);
            if (imageView != null) {
                imageView.setImageResource(resDraw);
            }
        }

        /**
         * 背景图
         *
         * @param resId
         * @param resDraw
         */
        public void setBackgroundRes(@IdRes int resId, @DrawableRes int resDraw) {
            View view = getView(resId);
            if (view != null) {
                view.setBackgroundResource(resDraw);
            }
        }

        public void setBackgroundCol(@IdRes int resId, @ColorInt int resDraw) {
            View view = getView(resId);
            if (view != null) {
                view.setBackgroundColor(resDraw);
            }
        }

        public void setGone(@IdRes int resId) {
            View view = getView(resId);
            if (view != null) {
                view.setVisibility(View.GONE);
            }
        }

        public void setVisible(@IdRes int resId) {
            View view = getView(resId);
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }
}