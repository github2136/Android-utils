package com.github2136.base.divider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github2136.util.CommonUtil
import kotlin.math.roundToInt

/**
 * Created by yb on 2020/3/7
 * Padding设置边距
 * text设置字体相关信息（颜色、大小）
 * align文字对齐方式（左、中、右）
 * sticky分割标题是否粘滞顶部
 */
class Divider(context: Context) : RecyclerView.ItemDecoration() {
    private val ATTRS = intArrayOf(android.R.attr.listDivider)
    private var mDivider: Drawable
    private val mBounds = Rect()
    private val bgPaint = Paint()
    private val txtPaint = TextPaint()
    private var groupHeight: Int
    private val txtBottom: Int

    var leftPadding = CommonUtil.dp2px(context, 8f)
    var rightPadding = CommonUtil.dp2px(context, 8f)
    var topPadding = CommonUtil.dp2px(context, 8f)
    var bottomPadding = CommonUtil.dp2px(context, 8f)

    var textSize = CommonUtil.sp2px(context, 24f)
        set(value) {
            field = value
            txtPaint.textSize = value
        }
    var textColor = Color.parseColor("#DE000000")
        set(value) {
            field = value
            bgPaint.color = value
        }
    var backgroundColor = Color.parseColor("#FFFFFF")
        set(value) {
            field = value
            bgPaint.color = value
        }
    var sticky = true
    var align = Paint.Align.LEFT
        set(value) {
            field = value
            txtPaint.textAlign = value
        }

    init {
        txtPaint.textSize = textSize
        txtPaint.color = textColor
        txtPaint.textAlign = align

        bgPaint.color = backgroundColor

        val font = txtPaint.fontMetricsInt
        txtBottom = font.descent
        groupHeight = (topPadding + bottomPadding + txtPaint.fontSpacing).toInt()// font.descent - font.ascent

        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
    }

    /**
     * 绘制分割线，在item绘制前绘制
     * @param c 画布
     * @param parent RecyclerView
     * @param state 状态
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    /**
     * 绘制分割线，在item绘制后绘制
     * @param c 画布
     * @param parent RecyclerView
     * @param state 状态
     */
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        c.save()
        val left: Int
        val right: Int
        val top: Int
        if (parent.clipToPadding) {
            top = parent.paddingTop
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            c.clipRect(
                left, parent.paddingTop, right,
                parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            top = 0
            right = parent.width
        }
        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            val dividerBottom = mBounds.bottom + child.translationY.roundToInt()
            val dividerTop = dividerBottom - mDivider.intrinsicHeight
            mDivider.setBounds(left, dividerTop, right, dividerBottom)
            mDivider.draw(c)
        }

        val adapter = parent.adapter

        var preTxt: String?
        var currentTxt: String?
        var nextTxt: String?
        var position: Int
        var txtWidth: Int
        var txt: String
        var txtX: Float
        if (adapter is IDivider) {
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                parent.getDecoratedBoundsWithMargins(child, mBounds)
                position = parent.getChildAdapterPosition(child)
                if (sticky) {
                    preTxt = if (position > 0) adapter.getShowTxt(position - 1) else null
                    currentTxt = adapter.getShowTxt(position)
                    nextTxt = if (position + 2 < adapter.itemCount) adapter.getShowTxt(position + 1) else null

                    currentTxt?.apply {
                        if (position == 0 || (mBounds.top <= top && mBounds.bottom >= top)) {
                            var groupTop = top.toFloat()
                            if (currentTxt != nextTxt) {
                                if (mBounds.bottom < groupHeight + top) {
                                    groupTop = -groupHeight + mBounds.bottom.toFloat()
                                }
                            }
                            c.drawRect(left.toFloat(), groupTop, right.toFloat(), groupTop + groupHeight.toFloat(), bgPaint)

                            txtWidth = right - left - leftPadding - rightPadding
                            txt = if (txtPaint.measureText(this) > txtWidth) {
                                val subIndex = txtPaint.breakText(this, 0, this.length, true, txtWidth.toFloat(), null)
                                this.substring(0, subIndex - 1) + "..."
                            } else {
                                this
                            }
                            txtX = when (align) {
                                Paint.Align.LEFT   -> left.toFloat() + leftPadding
                                Paint.Align.CENTER -> left.toFloat() + leftPadding + txtWidth / 2
                                Paint.Align.RIGHT  -> left.toFloat() + leftPadding + txtWidth
                            }
                            c.drawText(txt, txtX, groupTop + groupHeight.toFloat() - bottomPadding - txtBottom, txtPaint)
                        } else {
                            if (this != preTxt) {
                                val groupTop = mBounds.top.toFloat()
                                c.drawRect(left.toFloat(), groupTop, right.toFloat(), groupTop + groupHeight.toFloat(), bgPaint)
                                txtWidth = right - left - leftPadding - rightPadding
                                txt = if (txtPaint.measureText(this) > txtWidth) {
                                    val subIndex = txtPaint.breakText(this, 0, this.length, true, txtWidth.toFloat(), null)
                                    this.substring(0, subIndex - 1) + "..."
                                } else {
                                    this
                                }
                                txtX = when (align) {
                                    Paint.Align.LEFT   -> left.toFloat() + leftPadding
                                    Paint.Align.CENTER -> left.toFloat() + leftPadding + txtWidth / 2
                                    Paint.Align.RIGHT  -> left.toFloat() + leftPadding + txtWidth
                                }
                                c.drawText(txt, txtX, groupTop + groupHeight.toFloat() - bottomPadding - txtBottom, txtPaint)
                            }
                        }
                    }
                } else {
                    preTxt = if (position > 0) adapter.getShowTxt(position - 1) else null
                    currentTxt = adapter.getShowTxt(position)
                    currentTxt?.apply {
                        if (this != preTxt) {
                            val groupTop = mBounds.top.toFloat()
                            c.drawRect(left.toFloat(), groupTop, right.toFloat(), groupTop + groupHeight.toFloat(), bgPaint)
                            txtWidth = right - left - leftPadding - rightPadding
                            txt = if (txtPaint.measureText(this) > txtWidth) {
                                val subIndex = txtPaint.breakText(this, 0, this.length, true, txtWidth.toFloat(), null)
                                this.substring(0, subIndex - 1) + "..."
                            } else {
                                this
                            }
                            txtX = when (align) {
                                Paint.Align.LEFT   -> left.toFloat() + leftPadding
                                Paint.Align.CENTER -> left.toFloat() + leftPadding + txtWidth / 2
                                Paint.Align.RIGHT  -> left.toFloat() + leftPadding + txtWidth
                            }
                            c.drawText(txt, txtX, groupTop + groupHeight.toFloat() - bottomPadding - txtBottom, txtPaint)
                        }
                    }
                }
            }
        }
        c.restore()
    }

    /**
     * 分割线偏移量，如果需要知道项的下标可以使用parent.getChildAdapterPosition(view)
     * @param outRect 每个item的范围，默认四边为0
     * @param view RecyclerView
     * @param parent RecyclerView
     * @param state 状态
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val adapter = parent.adapter
        if (adapter is IDivider) {
            val preTxt = if (position > 0) adapter.getShowTxt(position - 1) else null
            val currentTxt = adapter.getShowTxt(position)
            if (currentTxt != null && (position == 0 || currentTxt != preTxt)) {
                outRect.set(0, groupHeight, 0, mDivider.intrinsicHeight)
            } else {
                outRect.set(0, 0, 0, mDivider.intrinsicHeight)
            }
        }
    }

    fun sp2px(context: Context, sp: Float): Int {
        val density = context.resources.displayMetrics.scaledDensity
        return (sp * density + 0.5f).toInt()
    }
}