package com.github2136.base;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView所需的adapter基类
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolderRecyclerView> {
    protected Context mContext;
    protected List<T> mList;//使用的集合
    protected LayoutInflater mLayoutInflater;
    protected RecyclerItemClickListener mRecyclerItemClickListener;

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

    protected abstract void onBindView(T t, ViewHolderRecyclerView holder, int position);

    /**
     * 获得对象
     */
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public ViewHolderRecyclerView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(getLayoutId(viewType), parent, false);
        return new ViewHolderRecyclerView(mContext, this, v);
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolderRecyclerView holder, int position) {
        onBindView(getItem(position), holder, position);
    }

    protected OnItemClickListener itemClickListener;
    protected OnItemLongClickListener itemLongClickListener;

    public void setOnItemClickListener(RecyclerView recyclerView, OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        addClickListener(recyclerView);
        mRecyclerItemClickListener.setItemClickListener(itemClickListener);
    }

    public void setOnItemLongClickListener(RecyclerView recyclerView, OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
        addClickListener(recyclerView);
        mRecyclerItemClickListener.setItemLongClickListener(itemLongClickListener);
    }

    protected void addClickListener(RecyclerView recyclerView) {
        if (mRecyclerItemClickListener == null) {
            mRecyclerItemClickListener = new RecyclerItemClickListener(recyclerView);
            recyclerView.addOnItemTouchListener(mRecyclerItemClickListener);
        }
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

    public void setData(List<T> list) {
        this.mList = list == null ? new ArrayList<T>() : list;
        notifyDataSetChanged();
    }

    public void appendData(List<T> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        protected OnItemClickListener itemClickListener;
        protected OnItemLongClickListener itemLongClickListener;
        private RecyclerView mRecyclerView;
        private GestureDetectorCompat mGestureDetectorCompat;

        public RecyclerItemClickListener(RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
            mGestureDetectorCompat = new GestureDetectorCompat(recyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if (itemClickListener != null) {
                        View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                        int position = mRecyclerView.getChildAdapterPosition(child); // item position
                        itemClickListener.onItemClick((BaseRecyclerAdapter) mRecyclerView.getAdapter(), position);
                        return true;
                    } else {
                        return false;
                    }
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    if (itemLongClickListener != null) {
                        View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                        int position = mRecyclerView.getChildAdapterPosition(child); // item position
                        itemLongClickListener.onItemClick((BaseRecyclerAdapter) mRecyclerView.getAdapter(), position);
                    }
                }
            });
        }

        public void setItemClickListener(OnItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
            this.itemLongClickListener = itemLongClickListener;
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            if (mGestureDetectorCompat.onTouchEvent(e)) { // 把事件交给GestureDetector处理
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            mGestureDetectorCompat.onTouchEvent(e);
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

}