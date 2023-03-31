package com.github2136.util

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.view.View
import android.widget.TextView

/**
 * Created by yb on 2018/8/25.
 * 更多效果参考CharacterStyle子类
 */
class SpanUtil {
    private var mSSB: SpannableStringBuilder = SpannableStringBuilder()
    private var mString: String = ""

    /**
     * 其他设置
     */
    fun setSpan(what: Any): SpanUtil {
        mSSB.setSpan(what, mSSB.length - mString.length, mSSB.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 添加内容
     */
    fun append(str: String): SpanUtil {
        mString = str
        mSSB.append(mString)
        return this
    }

    /**
     * 设置颜色
     */
    fun setTextColor(color: Int): SpanUtil {
        val span = ForegroundColorSpan(color)
        return setSpan(span)
    }

    /**
     * 设置背景颜色
     */
    fun setBackground(color: Int): SpanUtil {
        val span = BackgroundColorSpan(color)
        return setSpan(span)
    }

    /**
     * 设置字体大小SP
     */
    fun setTextSize(textSize: Int, dip: Boolean = true): SpanUtil {
        val span = AbsoluteSizeSpan(textSize, dip)
        return setSpan(span)
    }

    /**
     * 设置
     * 正常Typeface.NORMAL、
     * 加粗Typeface.BOLD、
     * 斜体Typeface.ITALIC、
     * 粗斜Typeface.BOLD_ITALIC
     */
    fun setStyle(style: Int): SpanUtil {
        if (style == Typeface.NORMAL || style == Typeface.BOLD || style == Typeface.ITALIC || style == Typeface.BOLD_ITALIC) {
            val span = StyleSpan(style)
            setSpan(span)
        }
        return this
    }

    /**
     * 设置删除线LINE_STRIKETHROUGH、下划线LINE_UNDERLINE
     */
    fun setLine(line: Int): SpanUtil {
        when (line) {
            LINE_STRIKETHROUGH -> {
                val span = StrikethroughSpan()
                setSpan(span)
            }
            LINE_UNDERLINE -> {
                val span = UnderlineSpan()
                setSpan(span)
            }
        }
        return this
    }

    /**
     * 设置链接
     */
    fun setLink(link: String): SpanUtil {
        val span = URLSpan(link)
        setBackground(Color.TRANSPARENT)
        return setSpan(span)
    }

    /**
     * 点击事件
     * @param textColor 字体颜色
     * @param underLine 下划线
     */
    fun setClick(tv: TextView, textColor: Int = 0, underLine: Boolean = true, onClick: (view: View) -> Unit): SpanUtil {
        setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClick.invoke(widget)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                if (textColor == 0) {
                    ds.color = ds.linkColor //文本颜色
                } else {
                    ds.color = textColor //文本颜色
                }
                ds.isUnderlineText = underLine //下划线
            }
        })
        tv.movementMethod = LinkMovementMethod.getInstance()
        tv.highlightColor = Color.TRANSPARENT //不设置透明会出现点击后颜色不重置问
        return this
    }

    fun build(): CharSequence {
        return mSSB
    }

    fun clear() {
        mSSB = SpannableStringBuilder()
        mString = ""
    }

    companion object {
        //删除线
        val LINE_STRIKETHROUGH = 1
        //下划线
        val LINE_UNDERLINE = 2
    }
}