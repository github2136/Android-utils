package com.github2136.base;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yb on 2017/7/7.
 */

public class ViewHolderListView {
    private SparseArray<View> viewArray = new SparseArray<>();
    private View convertView;
    private Context mContext;

    public ViewHolderListView(Context context, View convertView) {
        this.mContext = context;
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
    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////

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

    public void setBackgroundColRes(@IdRes int resId, @ColorRes int resColor) {
        View view = getView(resId);
        if (view != null) {
            view.setBackgroundColor(ContextCompat.getColor(mContext, resColor));
        }
    }

    public void setBackgroundColInt(@IdRes int resId, @ColorInt int color) {
        View view = getView(resId);
        if (view != null) {
            view.setBackgroundColor(color);
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
