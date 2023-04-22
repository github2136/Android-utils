package com.github2136.base.divider

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by 44569 on 2023/4/20
 * 网格分割线
 */
class GridItemDecoration : RecyclerView.ItemDecoration() {
    var space = 1 //间距px
    var spaceColor = Color.parseColor("#dddddd")
        //边框颜色
        set(value) {
            field = value
            spacePaint.color = value
        }

    private val spacePaint = Paint()
    private var spanCount = 0
    private val mBounds = Rect()

    init {
        spacePaint.color = spaceColor
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        var position: Int
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            position = parent.getChildAdapterPosition(child)

            val column = position % spanCount
            val left = space - column * space / spanCount
            val right = (column + 1) * space / spanCount
            val top = if (position < spanCount) space else 0
            val bottom = space
            //绘制左侧
            c.drawRect(mBounds.left.toFloat(), mBounds.top.toFloat(), (mBounds.left + left).toFloat(), mBounds.bottom.toFloat(), spacePaint)
            //绘制右侧
            if (i + 1 == childCount) {
                //最后一个
                c.drawRect((mBounds.right - right).toFloat(), mBounds.top.toFloat(), (mBounds.left + left + child.width + space).toFloat(), mBounds.bottom.toFloat(), spacePaint)
            } else {
                c.drawRect((mBounds.right - right).toFloat(), mBounds.top.toFloat(), mBounds.right.toFloat(), mBounds.bottom.toFloat(), spacePaint)
            }
            if (top != 0) {
                //绘制顶部
                c.drawRect(mBounds.left + left.toFloat(), mBounds.top.toFloat(), (mBounds.right - right).toFloat(), (mBounds.top + top).toFloat(), spacePaint)
            }
            //绘制底部
            c.drawRect(mBounds.left + left.toFloat(), (mBounds.bottom - bottom).toFloat(), (mBounds.right - right).toFloat(), mBounds.bottom.toFloat(), spacePaint)
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (spanCount == 0) {
            spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        }
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount
        outRect.left = space - column * space / spanCount
        outRect.right = (column + 1) * space / spanCount
        if (position < spanCount) {
            outRect.top = space
        }
        outRect.bottom = space
    }
}