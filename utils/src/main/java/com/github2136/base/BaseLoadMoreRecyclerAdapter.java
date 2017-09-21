package com.github2136.base;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github2136.util.CollectionsUtil;
import com.github2136.util.R;

import java.util.List;

/**
 * Created by yubin on 2017/8/18.
 */

public abstract class BaseLoadMoreRecyclerAdapter<T> extends BaseRecyclerAdapter<T> {
    public static final int SPACE_FULL = -1;//表示占满
    private static final int TYPE_EMPTY = 100;//空view
    private static final int TYPE_LOAD_MORE = 101;//加载更多view
    private static final int TYPE_HEAD = 102;//头部view

    protected int mViewEmptyResId = -1;//空view
    protected int mViewFailedResId = -1;//首次加载失败
    protected int mViewLoadMoreResId = -1;//加载更多
    protected View mViewEmpty;//无数据数据
    protected View mViewFailed;//获取首页数据失败
    protected View mViewLoadMore;//加载更多

    protected boolean isFailed;//首页加载失败
    protected boolean isEnableLoadMore;//启用更多
    protected boolean isLoadMore;//正在加载更多
    protected boolean isHasMore;//是否还有更多
    protected boolean isRefresh;//是否正在刷新
    protected boolean isFailedRefresh;//失败时是否可以点击刷新

    protected View mHeadView;//头部view

    private OnLoadMoreListener mLoadMoreListener;
    private OnItemClickListener mLoadClickListener;
    private OnItemLongClickListener mLoadLongClickListener;

    public BaseLoadMoreRecyclerAdapter(Context context, List<T> list) {
        super(context, list);
        mViewLoadMoreResId = getLoadMoreResId();
        mViewEmptyResId = getEmptyResId();
        mViewFailedResId = getFailedResId();
    }

    /**
     * 空数据或加载失败页
     */
    protected boolean hasEmptyView() {
        return mViewEmptyResId != -1 || mViewEmpty != null || mViewFailedResId != -1 || mViewFailed != null;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        if (lm instanceof GridLayoutManager) {
            final GridLayoutManager glm = (GridLayoutManager) lm;
            glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (hasEmptyView() && mList.isEmpty()) {
                        //空数据
                        return glm.getSpanCount();
                    } else if (isEnableLoadMore && position == getItemCount() - 1) {
                        //加载更多
                        return glm.getSpanCount();
                    } else if (mHeadView != null && position == 0) {
                        return glm.getSpanCount();
                    } else {
                        //其他
                        int space = getSpanCount(position - (mHeadView != null ? 1 : 0));
                        if (space == SPACE_FULL) {
                            return glm.getSpanCount();
                        } else {
                            return space;
                        }
                    }
                }
            });
        }
    }

    @Override
    public ViewHolderRecyclerView onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_EMPTY: {
                //空有两种状态空数据或加载失败
                View v;
                if (isFailed) {
                    if (mViewFailed == null) {
                        mViewFailed = mLayoutInflater.inflate(mViewFailedResId, parent, false);
                    }
                    v = mViewFailed;
                    failViewInit(mViewFailed);
                } else {
                    if (mViewEmpty == null) {
                        mViewEmpty = mLayoutInflater.inflate(mViewEmptyResId, parent, false);
                    }
                    v = mViewEmpty;
                    emptyViewInit(mViewEmpty);
                }
                return new ViewHolderRecyclerView(mContext, this, v);
            }
            case TYPE_HEAD:
                return new ViewHolderRecyclerView(mContext, this, mHeadView);
            case TYPE_LOAD_MORE:
                mViewLoadMore = mLayoutInflater.inflate(mViewLoadMoreResId, parent, false);
                loadMoreInit(mViewLoadMore);
                return new ViewHolderRecyclerView(mContext, this, mViewLoadMore);
            default:
                View v = mLayoutInflater.inflate(getLayoutId(viewType), parent, false);
                return new ViewHolderRecyclerView(mContext, this, v);
        }
    }

    public void setOnItemClickListener(final RecyclerView recyclerView, OnItemClickListener itemClickListener) {
        mLoadClickListener = itemClickListener;
        this.itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, int position) {
                if (isEnableLoadMore && position == getItemCount()) {
                    if (isRefresh) {
                        loadMoreRefresh(mViewLoadMore);
                        return;
                    }
                    if (isHasMore && !isLoadMore && mLoadMoreListener != null) {
                        loadMoreLoading(mViewLoadMore);
                        mLoadMoreListener.onLoadMore();
                    }
                } else {
                    mLoadClickListener.onItemClick(adapter, position);
                }
            }
        };
        super.setOnItemClickListener(recyclerView, BaseLoadMoreRecyclerAdapter.this.itemClickListener);
    }

    public void setOnItemLongClickListener(RecyclerView recyclerView, OnItemLongClickListener itemLongClickListener) {
        mLoadLongClickListener = itemLongClickListener;
        this.itemLongClickListener = new OnItemLongClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, int position) {
                mLoadLongClickListener.onItemClick(adapter, position);
            }
        };
        super.setOnItemLongClickListener(recyclerView, BaseLoadMoreRecyclerAdapter.this.itemLongClickListener);
    }

//    private OnItemClickListener mFailClick = new OnItemClickListener() {
//        @Override
//        public void onItemClick(BaseRecyclerAdapter adapter, int position) {
//            mLoadMoreListener.onRefresh();
//        }
//    };

    @Override
    public final int getItemViewType(int position) {
        //没有头部没有数据
        if (mHeadView == null && mList.isEmpty() && hasEmptyView()) {
            return TYPE_EMPTY;
        } else if (position == 0 && mHeadView != null) {
            return TYPE_HEAD;
        } else if (isEnableLoadMore && position + 1 == getItemCount()) {
            return TYPE_LOAD_MORE;
        } else {
            if (mHeadView != null) {
                position--;
            }
            return getViewType(position);
        }
    }

    @Override
    public final void onBindViewHolder(ViewHolderRecyclerView holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_EMPTY:
                break;
            case TYPE_HEAD:
                break;
            case TYPE_LOAD_MORE:
                if (isLoadMore) {
                    loadMoreLoading(mViewLoadMore);
                } else {
                    if (mList.size() != 0 && mList.size() < getPageSize() || mLoadMoreListener == null) {
                        //没有数据、首页不足一页、没有回调直接显示条数
                        loadMoreNoMore(mViewLoadMore);
                    } else if (isRefresh) {
                        loadMoreRefresh(mViewLoadMore);
                    } else if (isHasMore) {
                        isLoadMore = true;
                        loadMoreLoading(mViewLoadMore);
                        mLoadMoreListener.onLoadMore();
                    }
                }
                break;
            default:
                if (mHeadView != null) {
                    position--;
                }
                onBindView(getItem(position), holder, position);
                break;
        }
    }

    @Override
    public final int getItemCount() {
        int count;
        if (mList.isEmpty()) {
            if (hasEmptyView() || mHeadView != null) {
                count = 1;
            } else {
                count = 0;
            }
        } else {
            if (mHeadView == null) {
                count = mList.size();
            } else {
                count = mList.size() + 1;
            }
            count += (isEnableLoadMore ? 1 : 0);
        }
        return count;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 可调用方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 启用加载更多
     *
     * @param enableLoadMore
     */
    public void setEnableLoadMore(boolean enableLoadMore) {
        if (enableLoadMore) {
            isEnableLoadMore = true;
            isLoadMore = false;
            if (mList.size() % getPageSize() == 0) {
                isHasMore = true;
            } else {
                isHasMore = false;
            }
        } else {
            isEnableLoadMore = false;
        }
    }

    /**
     * 数据加载成功
     *
     * @param list
     */
    public void loadMoreSucceed(List<T> list) {
        isLoadMore = false;
        //传入的集合为空或不为一整页则不会有更多
        if (CollectionsUtil.isEmpty(list) || list.size() % getPageSize() != 0) {
            isHasMore = false;
            if (CollectionsUtil.isNotEmpty(list)) {
                mList.addAll(list);
                notifyItemInserted(mList.size());
            }
            if (mLoadMoreListener != null && mViewLoadMore != null) {
                loadMoreNoMore(mViewLoadMore);
            }
        } else {
            isHasMore = true;
            mList.addAll(list);
            notifyItemInserted(getItemCount());
            if (mLoadMoreListener != null && mViewLoadMore != null) {
                loadMoreLoading(mViewLoadMore);
            }
        }
    }


    /**
     * 加载失败
     */
    public void loadMoreFailed() {
        isLoadMore = false;
        if (mLoadMoreListener != null && mViewLoadMore != null) {
            loadMoreFailure(mViewLoadMore);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // get/set
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 设置正在刷新，不再在更多
     */
    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    /**
     * 正在加载更多
     */
    public boolean isLoadMore() {
        return isLoadMore;
    }

    /**
     * 加载更多失败刷新
     *
     * @param loadMoreListener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;
    }

    /**
     * 加载失败
     *
     * @return
     */
    public boolean isFailed() {
        return isFailed;
    }

    public void setFailed(boolean failed) {
        isFailed = failed;
    }

    public View getHeadView() {
        return mHeadView;
    }

    public void setHeadView(View headView) {
        this.mHeadView = headView;
    }

    /**
     * 开启失败点击刷新
     *
     * @return
     */
    public boolean isFailedRefresh() {
        return isFailedRefresh;
    }

    public void setFailedRefresh(boolean failedRefresh) {
        isFailedRefresh = failedRefresh;
    }
    ///////////////////////////////////////////////////////////////////////////
    // 可覆写方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 在GridLayoutManager中改项所占空间
     */
    protected int getSpanCount(int position) {
        return 1;
    }

    /**
     * 根据下标返回view类型
     *
     * @param position
     * @return
     */
    public int getViewType(int position) {
        return 0;
    }

    private TextView tvText;
    private ProgressBar pbMore;

    /**
     * 没有更多
     *
     * @param loadMore
     */
    public void loadMoreNoMore(View loadMore) {
        if (tvText != null && pbMore != null) {
            tvText.setText(String.format("共有 %d 条数据", mList.size()));
            pbMore.setVisibility(View.GONE);
        }
    }

    /**
     * 正在加载更多
     *
     * @param loadMore
     */
    public void loadMoreLoading(View loadMore) {
        if (tvText != null && pbMore != null) {
            tvText.setText("正在加载数据");
            pbMore.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 更多获取失败
     *
     * @param loadMore
     */
    public void loadMoreFailure(View loadMore) {
        if (tvText != null && pbMore != null) {
            tvText.setText("加载失败，点击重试");
            pbMore.setVisibility(View.GONE);
        }
    }

    /**
     * 提示正在刷新
     *
     * @param loadMore
     */
    public void loadMoreRefresh(View loadMore) {
        if (tvText != null && pbMore != null) {
            tvText.setText("正在刷新，请稍候");
            pbMore.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化控件
     *
     * @param loadMore
     */
    public void loadMoreInit(View loadMore) {
        tvText = (TextView) loadMore.findViewById(R.id.tv_load_more);
        pbMore = (ProgressBar) loadMore.findViewById(R.id.pb_load_more);
    }

    /**
     * 空view初始化
     *
     * @param emptyView
     */
    public void emptyViewInit(View emptyView) { }

    /**
     * 失败view初始化
     *
     * @param failView
     */
    public void failViewInit(View failView) { }

    /**
     * 加载更多
     *
     * @return
     */
    protected int getLoadMoreResId() {
        return R.layout.item_util_load_more;
    }

    /**
     * 空view
     *
     * @return
     */
    protected int getEmptyResId() {
        return R.layout.item_uti_empty;
    }

    /**
     * 失败view
     *
     * @return
     */
    public int getFailedResId() {
        return R.layout.item_util_failed;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 实现方法
    ///////////////////////////////////////////////////////////////////////////
    protected abstract int getPageSize();

    ///////////////////////////////////////////////////////////////////////////
    // 接口
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 更多
     */
    public interface OnLoadMoreListener {
        //加载更多
        void onLoadMore();

        //失败点击刷新
        void onRefresh();
    }
}
