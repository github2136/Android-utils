package com.github2136.base;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yubin on 2017/7/7.
 */

public class ViewHolderRecyclerView extends RecyclerView.ViewHolder /*implements View.OnClickListener, View.OnLongClickListener */{
    private SparseArray<View> views = new SparseArray<>();
    private View itemView;
//    private BaseRecyclerAdapter.OnItemClickListener itemClickListener;
//    private BaseRecyclerAdapter.OnItemLongClickListener itemLongClickListener;
    private BaseRecyclerAdapter mAdapter;
    private Context mContext;

    public ViewHolderRecyclerView(Context context, BaseRecyclerAdapter mAdapter, View itemView/*, BaseRecyclerAdapter.OnItemClickListener
            itemClickListener, BaseRecyclerAdapter.OnItemLongClickListener itemLongClickListener*/) {
        super(itemView);
        this.mContext = context;
        this.itemView = itemView;
        this.mAdapter = mAdapter;
//        this.itemClickListener = itemClickListener;
//        this.itemLongClickListener = itemLongClickListener;

//        itemView.setOnClickListener(this);
//        itemView.setOnLongClickListener(this);
    }

    public View getRootView() {
        return itemView;
    }

    public <T extends View> T getView(@IdRes int id) {
        if (views == null) {
            views = new SparseArray<>();
        }
        View v = views.get(id);
        if (v != null) {
            return (T) v;
        }
        v = itemView.findViewById(id);
        if (v == null) {
            return null;
        }
        views.put(id, v);
        return (T) v;
    }

//    @Override
//    public void onClick(View v) {
//        if (itemClickListener != null) {
//            //如果adpater正在调用notifyDataSetChanged那么getAdapterPosition会返回 RecyclerView.NO_POSITION
//            int position = getAdapterPosition();
//            if (position != RecyclerView.NO_POSITION) {
//                itemClickListener.onItemClick(mAdapter, position);
//            }
//        }
//    }
//
//    @Override
//    public boolean onLongClick(View v) {
//        if (itemLongClickListener != null) {
//            int position = getAdapterPosition();
//            if (position != RecyclerView.NO_POSITION) {
//                itemLongClickListener.onItemClick(mAdapter, position);
//            }
//            return true;
//        } else {
//            return false;
//        }
//    }

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