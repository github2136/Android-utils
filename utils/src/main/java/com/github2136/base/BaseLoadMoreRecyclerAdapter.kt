package com.github2136.base

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.github2136.util.R

/**
 * Created by yb on 2018/10/31.
 */
abstract class BaseLoadMoreRecyclerAdapter<T>(context: Context, list: MutableList<T>?) :
        BaseRecyclerAdapter<T>(context, list) {
    val SPACE_FULL = -1//表示占满
    private val TYPE_EMPTY = 100//空view
    private val TYPE_LOAD_MORE = 101//加载更多view
    private val TYPE_HEAD = 102//头部view

    protected var mViewEmptyResId = -1//空view
    protected var mViewFailedResId = -1//首次加载失败
    protected var mViewLoadMoreResId = -1//加载更多
    protected var mViewEmpty: View? = null//无数据数据
    protected var mViewFailed: View? = null//获取首页数据失败
    protected var mViewLoadMore: View? = null//加载更多

    var isFailed: Boolean = false//首页加载失败
    var isEnableLoadMore: Boolean = false//启用更多
        set(value) {
            field = value
            if (value) {
                isLoadMore = false
                isHasMore = mList.size % getPageSize() == 0
            }
        }
    protected var isLoadMore: Boolean = false//正在加载更多
    protected var isHasMore: Boolean = false//是否还有更多
    protected var isRefresh: Boolean = false//是否正在刷新
    var isFailedRefresh: Boolean = false//失败时是否可以点击刷新

    protected var mHeadView: View? = null//头部view
    private var mLoadMoreListener: OnLoadMoreListener? = null

    init {
        mViewLoadMoreResId = getLoadMoreResId()
        mViewEmptyResId = getEmptyResId()
        mViewFailedResId = getFailedResId()
    }

    /**
     * 空数据或加载失败页
     */
    protected fun hasEmptyView(): Boolean {
        return mViewEmptyResId != -1 || mViewEmpty != null || mViewFailedResId != -1 || mViewFailed != null
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val lm = recyclerView.layoutManager
        if (lm is GridLayoutManager) {
            lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (hasEmptyView() && mList.isEmpty()) {
                        //空数据
                        return lm.spanCount
                    } else if (isEnableLoadMore && position == itemCount - 1) {
                        //加载更多
                        return lm.spanCount
                    } else if (mHeadView != null && position == 0) {
                        return lm.spanCount
                    } else {
                        //其他
                        val space = getSpanCount(position - if (mHeadView != null) 1 else 0)
                        return if (space == SPACE_FULL) {
                            lm.spanCount
                        } else {
                            space
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRecyclerView {
        when (viewType) {
            TYPE_EMPTY -> {
                //空有两种状态空数据或加载失败
                var v: View? = null
                if (isFailed) {
                    if (mViewFailed == null) {
                        mViewFailed = mLayoutInflater.inflate(mViewFailedResId, parent, false)
                    }
                    mViewFailed?.also {
                        v = it
                        failViewInit(it)
                    }
                } else {
                    if (mViewEmpty == null) {
                        mViewEmpty = mLayoutInflater.inflate(mViewEmptyResId, parent, false)
                    }
                    mViewEmpty?.also {
                        v = it
                        emptyViewInit(it)
                    }
                }
                return ViewHolderRecyclerView(mContext, this, v!!, if (isFailed && isFailedRefresh) mFailClick else null, null)
            }
            TYPE_HEAD -> return ViewHolderRecyclerView(mContext, this, mHeadView!!, null, null)
            TYPE_LOAD_MORE -> {
                mViewLoadMore = mLayoutInflater.inflate(mViewLoadMoreResId, parent, false)
                loadMoreInit(mViewLoadMore!!)
                return ViewHolderRecyclerView(mContext, this, mViewLoadMore!!, mLoadMoreClick, null)
            }
            else -> {
                val v = mLayoutInflater.inflate(getLayoutId(viewType), parent, false)
                return ViewHolderRecyclerView(mContext, this, v, itemClickListener, itemLongClickListener)
            }
        }
    }

    private val mLoadMoreClick = object : BaseRecyclerAdapter.OnItemClickListener {
        override fun onItemClick(adapter: BaseRecyclerAdapter<*>, position: Int) {
            if (isRefresh) {
                loadMoreRefresh(mViewLoadMore!!)
                return
            }
            if (isHasMore && !isLoadMore && mLoadMoreListener != null) {
                loadMoreLoading(mViewLoadMore!!)
                mLoadMoreListener?.onLoadMore()
            }
        }
    }

    private val mFailClick = object : BaseRecyclerAdapter.OnItemClickListener {
        override fun onItemClick(adapter: BaseRecyclerAdapter<*>, position: Int) {
            mLoadMoreListener?.onRefresh()
        }
    }

    override fun getItemViewType(position: Int): Int {
        var position = position
        //没有头部没有数据
        if (mHeadView == null && mList.isEmpty() && hasEmptyView()) {
            return TYPE_EMPTY
        } else if (position == 0 && mHeadView != null) {
            return TYPE_HEAD
        } else if (isEnableLoadMore && position + 1 == itemCount) {
            return TYPE_LOAD_MORE
        } else {
            if (mHeadView != null) {
                position--
            }
            return getViewType(position)
        }
    }

    override fun onBindViewHolder(holder: ViewHolderRecyclerView, position: Int) {
        var position = position
        when (getItemViewType(position)) {
            TYPE_EMPTY -> {
            }
            TYPE_HEAD -> {
            }
            TYPE_LOAD_MORE -> if (isLoadMore) {
                loadMoreLoading(mViewLoadMore!!)
            } else {
                if (mList.size != 0 && mList.size < getPageSize() || mLoadMoreListener == null) {
                    //没有数据、首页不足一页、没有回调直接显示条数
                    loadMoreNoMore(mViewLoadMore!!)
                } else if (isRefresh) {
                    loadMoreRefresh(mViewLoadMore!!)
                } else if (isHasMore) {
                    isLoadMore = true
                    loadMoreLoading(mViewLoadMore!!)
                    mLoadMoreListener?.onLoadMore()
                }
            }
            else -> {
                if (mHeadView != null) {
                    position--
                }
                onBindView(getItem(position), holder, position)
            }
        }
    }

    override fun getItemCount(): Int {
        var count: Int
        if (mList.isEmpty()) {
            if (hasEmptyView() || mHeadView != null) {
                count = 1
            } else {
                count = 0
            }
        } else {
            if (mHeadView == null) {
                count = mList.size
            } else {
                count = mList.size + 1
            }
            count += if (isEnableLoadMore) 1 else 0
        }
        return count
    }

    ///////////////////////////////////////////////////////////////////////////
    // 可调用方法
    ///////////////////////////////////////////////////////////////////////////


    /**
     * 数据加载成功
     *
     * @param list
     */
    fun loadMoreSucceed(list: List<T>) {
        isLoadMore = false
        //传入的集合为空或不为一整页则不会有更多
        if (list.isEmpty() || list.size % getPageSize() != 0) {
            isHasMore = false
            if (!list.isEmpty()) {
                mList.addAll(list)
                notifyItemInserted(mList.size)
            }
            if (mLoadMoreListener != null && mViewLoadMore != null) {
                loadMoreNoMore(mViewLoadMore!!)
            }
        } else {
            isHasMore = true
            mList.addAll(list)
            notifyItemInserted(itemCount)
            if (mLoadMoreListener != null && mViewLoadMore != null) {
                loadMoreLoading(mViewLoadMore!!)
            }
        }
    }


    /**
     * 加载失败
     */
    fun loadMoreFailed() {
        isLoadMore = false
        if (mLoadMoreListener != null && mViewLoadMore != null) {
            loadMoreFailure(mViewLoadMore!!)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // get/set
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 设置正在刷新，不再在更多
     */
    fun setIsRefresh(isRefresh: Boolean) {
        this.isRefresh = isRefresh
    }

    /**
     * 加载更多失败刷新
     *
     * @param loadMoreListener
     */
    fun setOnLoadMoreListener(loadMoreListener: OnLoadMoreListener) {
        this.mLoadMoreListener = loadMoreListener
    }


    fun getHeadView(): View {
        return mHeadView!!
    }

    fun setHeadView(headView: View) {
        this.mHeadView = headView
    }
    ///////////////////////////////////////////////////////////////////////////
    // 可覆写方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 在GridLayoutManager中改项所占空间
     */
    protected fun getSpanCount(position: Int): Int {
        return 1
    }

    /**
     * 根据下标返回view类型
     *
     * @param position
     * @return
     */
    fun getViewType(position: Int): Int {
        return 0
    }

    private var tvText: TextView? = null
    private var pbMore: ProgressBar? = null

    /**
     * 没有更多
     *
     * @param loadMore
     */
    fun loadMoreNoMore(loadMore: View) {
        if (tvText != null && pbMore != null) {
            tvText!!.text = String.format("共有 %d 条数据", mList.size)
            pbMore!!.visibility = View.GONE
        }
    }

    /**
     * 正在加载更多
     *
     * @param loadMore
     */
    fun loadMoreLoading(loadMore: View) {
        if (tvText != null && pbMore != null) {
            tvText!!.text = "正在加载数据"
            pbMore!!.visibility = View.VISIBLE
        }
    }

    /**
     * 更多获取失败
     *
     * @param loadMore
     */
    fun loadMoreFailure(loadMore: View) {
        if (tvText != null && pbMore != null) {
            tvText!!.text = "加载失败，点击重试"
            pbMore!!.visibility = View.GONE
        }
    }

    /**
     * 提示正在刷新
     *
     * @param loadMore
     */
    fun loadMoreRefresh(loadMore: View) {
        if (tvText != null && pbMore != null) {
            tvText!!.text = "正在刷新，请稍候"
            pbMore!!.visibility = View.GONE
        }
    }

    /**
     * 初始化控件
     *
     * @param loadMore
     */
    fun loadMoreInit(loadMore: View) {
        tvText = loadMore.findViewById<View>(R.id.tv_load_more) as TextView
        pbMore = loadMore.findViewById<View>(R.id.pb_load_more) as ProgressBar
    }

    /**
     * 空view初始化
     *
     * @param emptyView
     */
    fun emptyViewInit(emptyView: View) {}

    /**
     * 失败view初始化
     *
     * @param failView
     */
    fun failViewInit(failView: View) {}

    /**
     * 加载更多
     *
     * @return
     */
    protected fun getLoadMoreResId(): Int {
        return R.layout.item_util_load_more
    }

    /**
     * 空view
     *
     * @return
     */
    protected fun getEmptyResId(): Int {
        return R.layout.item_uti_empty
    }

    /**
     * 失败view
     *
     * @return
     */
    fun getFailedResId(): Int {
        return R.layout.item_util_failed
    }

    ///////////////////////////////////////////////////////////////////////////
    // 实现方法
    ///////////////////////////////////////////////////////////////////////////
    protected abstract fun getPageSize(): Int

    ///////////////////////////////////////////////////////////////////////////
    // 接口
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 更多
     */
    interface OnLoadMoreListener {
        //加载更多
        fun onLoadMore()

        //失败点击刷新
        fun onRefresh()
    }
}