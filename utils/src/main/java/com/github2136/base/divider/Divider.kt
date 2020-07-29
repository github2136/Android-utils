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
    private val bgPaint = Paint()//背景图
    private val txtPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)//文字
    private val linePaintTop = Paint()//上方线
    private val linePaintBottom = Paint()//底部线
    private var groupHeight: Int//分组项总高度，填充+线高度+字体高度
    private val txtBottom: Int//文字绘制底部距离
    private var lineHeightTop2: Int//上方线高度的一半
    private var lineHeightBottom2: Int//下方线高度的一半

    var leftPadding = CommonUtil.dp2px(context, 8f)
    var rightPadding = CommonUtil.dp2px(context, 8f)
    var topPadding = CommonUtil.dp2px(context, 12f)
    var bottomPadding = CommonUtil.dp2px(context, 12f)

    var textSize = CommonUtil.sp2px(context, 20f)
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

    var lineHeightBottom = CommonUtil.dp2px(context, 1f)
        set(value) {
            field = value
            lineHeightBottom2 = value / 2
            linePaintBottom.strokeWidth = value.toFloat()
            groupHeight = (lineHeightTop + topPadding + txtPaint.fontSpacing + bottomPadding + value).toInt()
        }

    var lineColorBottom = Color.parseColor("#1F000000")
        set(value) {
            field = value
            linePaintBottom.color = value
        }

    var lineHeightTop = CommonUtil.dp2px(context, 1f)
        set(value) {
            field = value
            lineHeightTop2 = value / 2
            linePaintTop.strokeWidth = value.toFloat()
            groupHeight = (value + topPadding + txtPaint.fontSpacing + bottomPadding + lineHeightBottom).toInt()
        }

    var lineColorTop = Color.parseColor("#1F000000")
        set(value) {
            field = value
            linePaintTop.color = value
        }

    init {
        txtPaint.textSize = textSize
        txtPaint.color = textColor
        txtPaint.textAlign = align

        bgPaint.color = backgroundColor

        linePaintTop.strokeWidth = lineHeightTop.toFloat()
        linePaintTop.color = lineColorTop
        lineHeightTop2 = lineHeightTop / 2

        linePaintBottom.strokeWidth = lineHeightBottom.toFloat()
        linePaintBottom.color = lineColorBottom
        lineHeightBottom2 = lineHeightBottom / 2

        val font = txtPaint.fontMetricsInt
        txtBottom = font.descent
        groupHeight = (lineHeightTop + topPadding + txtPaint.fontSpacing + bottomPadding + lineHeightBottom).toInt()

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

        val adapter = parent.adapter
        if (adapter is IDivider) {
            var preTxt: String?
            var nextTxt: String?
            var currentTxt: String?
            var position: Int
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                parent.getDecoratedBoundsWithMargins(child, mBounds)
                position = parent.getChildAdapterPosition(child)

                currentTxt = adapter.getShowTxt(position)
                nextTxt = if (position + 2 < adapter.itemCount) adapter.getShowTxt(position + 1) else null

                if (nextTxt == currentTxt) {
                    parent.getDecoratedBoundsWithMargins(child, mBounds)
                    val dividerBottom = mBounds.bottom + child.translationY.roundToInt()
                    val dividerTop = dividerBottom - mDivider.intrinsicHeight
                    mDivider.setBounds(left, dividerTop, right, dividerBottom)
                    mDivider.draw(c)
                }
            }


            var txtWidth: Int
            var txt: String
            var txtX: Float

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
                            //第一个或固定的头部的项
                            var groupTop = top.toFloat()
                            if (currentTxt != nextTxt) {
                                if (mBounds.bottom < groupHeight + top) {
                                    groupTop = -groupHeight + mBounds.bottom.toFloat()
                                }
                            }
                            //背景绘制
                            c.drawRect(left.toFloat(), groupTop, right.toFloat(), groupTop + groupHeight.toFloat(), bgPaint)

                            //上方线绘制
                            c.drawLine(left.toFloat(), groupTop + lineHeightTop2, right.toFloat(), groupTop + lineHeightTop2, linePaintTop)

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
                            //文字绘制
                            c.drawText(txt, txtX, groupTop + groupHeight.toFloat() - bottomPadding - txtBottom - lineHeightBottom, txtPaint)
                            //下方线绘制
                            c.drawLine(left.toFloat(), groupTop + groupHeight.toFloat() - lineHeightBottom2, right.toFloat(), groupTop + groupHeight.toFloat() - lineHeightBottom2, linePaintBottom)
                        } else {
                            if (this != preTxt) {
                                //绘制跟随屏幕滚动的项
                                val groupTop = mBounds.top.toFloat()
                                //背景绘制
                                c.drawRect(left.toFloat(), groupTop, right.toFloat(), groupTop + groupHeight.toFloat(), bgPaint)

                                //上方线绘制
                                c.drawLine(left.toFloat(), groupTop + lineHeightTop2, right.toFloat(), groupTop + lineHeightTop2, linePaintTop)

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
                                //文字绘制
                                c.drawText(txt, txtX, groupTop + groupHeight.toFloat() - bottomPadding - txtBottom - lineHeightBottom, txtPaint)
                                //下方线绘制
                                c.drawLine(left.toFloat(), groupTop + groupHeight.toFloat() - lineHeightBottom2, right.toFloat(), groupTop + groupHeight.toFloat() - lineHeightBottom2, linePaintBottom)
                            }
                        }
                    }
                } else {
                    // 非粘性项
                    preTxt = if (position > 0) adapter.getShowTxt(position - 1) else null
                    currentTxt = adapter.getShowTxt(position)
                    currentTxt?.apply {
                        if (this != preTxt) {
                            val groupTop = mBounds.top.toFloat()
                            c.drawRect(left.toFloat(), groupTop, right.toFloat(), groupTop + groupHeight.toFloat(), bgPaint)
                            //上方线绘制
                            c.drawLine(left.toFloat(), groupTop + lineHeightTop2, right.toFloat(), groupTop + lineHeightTop2, linePaintTop)

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
                            //文字绘制
                            c.drawText(txt, txtX, groupTop + groupHeight.toFloat() - bottomPadding - txtBottom - lineHeightBottom, txtPaint)
                            //下方线绘制
                            c.drawLine(left.toFloat(), groupTop + groupHeight.toFloat() - lineHeightBottom2, right.toFloat(), groupTop + groupHeight.toFloat() - lineHeightBottom2, linePaintBottom)
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