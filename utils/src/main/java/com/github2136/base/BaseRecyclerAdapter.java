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
        return new ViewHolderRecyclerView(mContext, this, v/*, itemClickListener, itemLongClickListener*/);
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

//    protected OnItemClickListener itemClickListener;
//    protected OnItemLongClickListener itemLongClickListener;

//    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
//        this.itemClickListener = itemClickListener;
//    }

//    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
//        this.itemLongClickListener = itemLongClickListener;
//    }

    /**
     * 单项点击事件
     */
//    public interface OnItemClickListener {
//        void onItemClick(BaseRecyclerAdapter adapter, int position);
//    }

    /**
     * 单项长按
     */
//    public interface OnItemLongClickListener {
//        void onItemClick(BaseRecyclerAdapter adapter, int position);
//    }

    public void setData(List<T> list) {
        this.mList = list == null ? new ArrayList<T>() : list;
        notifyDataSetChanged();
    }

    public void appendData(List<T> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public static  class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        //    private OnItemClickListener mListener;
        private RecyclerView mRecyclerView;
        private GestureDetectorCompat mGestureDetectorCompat;

        public RecyclerItemClickListener(RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
            mGestureDetectorCompat = new GestureDetectorCompat(recyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                    int position = mRecyclerView.getChildAdapterPosition(child); // item position

                    return super.onSingleTapUp(e);
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            mGestureDetectorCompat.onTouchEvent(e);
            return false;
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