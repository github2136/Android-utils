package com.github2136.android_utils.load_more;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.github2136.android_utils.R;
import com.github2136.base.BaseLoadMoreRecyclerAdapter;
import com.github2136.base.BaseRecyclerAdapter;

import java.util.List;


/**
 * Created by yb on 2017/7/18.
 */
public abstract class BaseListActivity<T> extends AppCompatActivity {
    protected RecyclerView rvContent;
    protected SwipeRefreshLayout srContent;
    protected int mPageNumber = 0;//页码
    protected boolean mHasItemClick = true;
    protected BaseLoadMoreRecyclerAdapter<T> mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initData(savedInstanceState);
    }

    protected abstract int getLayoutId();

    protected void initData(Bundle savedInstanceState) {
        srContent = (SwipeRefreshLayout) findViewById(R.id.sr_content);
        rvContent = (RecyclerView) findViewById(R.id.rv_content);

        mAdapter = getAdapter();
        //加载监听
        mAdapter.setOnLoadMoreListener(mOnLoadMoreListener);
        //启用加载更多
        mAdapter.setEnableLoadMore(true);
        //加载失败点击
        mAdapter.setFailedRefresh(true);
        //刷新监听
        srContent.setOnRefreshListener(mOnRefreshListener);
        getFirstPage();
        initListData(savedInstanceState);
        if (mHasItemClick) {
            mAdapter.setOnItemClickListener( mOnItemClickListener);
            mAdapter.setOnItemLongClickListener( mOnItemLongClickListener);
        }
    }

    /**
     * 获取第一页数据
     */
    protected void getFirstPage() {
        mPageNumber = 0;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                srContent.setRefreshing(true);
                mAdapter.setIsRefresh(true);
                getListData();

            }
        });
    }

    private BaseLoadMoreRecyclerAdapter.OnLoadMoreListener mOnLoadMoreListener = new BaseLoadMoreRecyclerAdapter.OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            //获取更多时禁止刷新
            srContent.setEnabled(false);
            getListData();
        }

        @Override
        public void onRefresh() {
            getFirstPage();
        }
    };
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //刷新时禁止获取更多
            mPageNumber = 0;
            mAdapter.setIsRefresh(true);
            getListData();
        }
    };

    private BaseRecyclerAdapter.OnItemClickListener mOnItemClickListener = new BaseRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseRecyclerAdapter baseRecyclerAdapter, int i) {
            //点击事件
            if (mAdapter.getHeadView() != null) {
                i--;
            }
            itemClick(mAdapter.getItem(i), i);
        }
    };
    private BaseRecyclerAdapter.OnItemLongClickListener mOnItemLongClickListener = new BaseRecyclerAdapter.OnItemLongClickListener() {
        @Override
        public void onItemClick(BaseRecyclerAdapter baseRecyclerAdapter, int i) {
            //长按事件
            if (mAdapter.getHeadView() != null) {
                i--;
            }
            itemLongClick(mAdapter.getItem(i), i);
        }
    };

    protected void getDataSuccessful(List<T> list) {
        //获取成功
        srContent.setRefreshing(false);
        srContent.setEnabled(true);
        mAdapter.setIsRefresh(false);
        if (mPageNumber == 0) {
            mAdapter.setFailed(false);
            rvContent.setAdapter(mAdapter);
            mAdapter.setData(null);
        }
        mAdapter.loadMoreSucceed(list);
        mPageNumber++;
    }

    protected void getDataFailure() {
        //获取失败
        srContent.setRefreshing(false);
        srContent.setEnabled(true);
        mAdapter.setIsRefresh(false);
        if (mPageNumber == 0) {
            mAdapter.setData(null);
            mAdapter.setFailed(true);
            rvContent.setAdapter(mAdapter);
        } else {
            mAdapter.loadMoreFailed();
        }
    }

    protected abstract void initListData(Bundle savedInstanceState);

    protected abstract BaseLoadMoreRecyclerAdapter<T> getAdapter();

    protected abstract void getListData();

    protected void itemClick(T t, int position) { }

    protected void itemLongClick(T t, int position) { }
}