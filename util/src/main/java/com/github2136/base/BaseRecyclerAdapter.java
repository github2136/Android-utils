package com.github2136.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView所需的Adpater基类
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder> {
    protected Context mContext;
    protected List<T> mList;//使用的集合
    protected LayoutInflater mLayoutInflater;

    public BaseRecyclerAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.mList = list == null ? new ArrayList<T>() : list;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * 通过类型获得布局ID
     *
     * @param viewType
     * @return
     */
    public abstract int getLayoutId(int viewType);

    protected abstract void onBindView(T t, ViewHolder holder, int position);

    /**
     * 获得对象
     */
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(getLayoutId(viewType), parent, false);
        return new ViewHolder(mContext, this, v, itemClickListener, itemLongClickListener);
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        onBindView(getItem(position), holder, position);
    }

    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    /**
     * 单项点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(BaseRecyclerAdapter adapter, int position);
    }

    /**
     * 单项长按
     */
    public interface OnItemLongClickListener {
        void onItemClick(BaseRecyclerAdapter adapter, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private SparseArray<View> views = new SparseArray<>();
        private View itemView;
        private OnItemClickListener itemClickListener;
        private OnItemLongClickListener itemLongClickListener;
        private BaseRecyclerAdapter mAdapter;

        public ViewHolder(Context context, BaseRecyclerAdapter mAdapter, View itemView, OnItemClickListener itemClickListener,
                          OnItemLongClickListener itemLongClickListener) {
            super(itemView);
            this.itemView = itemView;
            this.mAdapter = mAdapter;
            this.itemClickListener = itemClickListener;
            this.itemLongClickListener = itemLongClickListener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        public View getRootView() {
            return itemView;
        }

        public <T extends View> T getView(int id) {
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

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                //如果adpater正在调用notifyDataSetChanged那么getAdapterPosition会返回 RecyclerView.NO_POSITION
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(mAdapter, position);
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (itemLongClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    itemLongClickListener.onItemClick(mAdapter, position);
                }
                return true;
            } else {
                return false;
            }
        }
    }
}