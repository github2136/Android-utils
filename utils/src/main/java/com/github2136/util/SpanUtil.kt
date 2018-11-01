package com.github2136.util

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.widget.TextView

/**
 * Created by yb on 2018/8/25.
 */
class SpanUtil constructor(mString: String) {
    private var mSSB: SpannableStringBuilder = SpannableStringBuilder()
    private var mString: String = mString

    init {
        append(mString)
    }

    private fun setSpan(what: Any) {
        mSSB.setSpan(what, mSSB.length - mString.length, mSSB.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
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
        setSpan(span)
        return this
    }

    /**
     * 设置背景颜色
     */
    fun setBackground(color: Int): SpanUtil {
        val span = BackgroundColorSpan(color)
        setSpan(span)
        return this
    }

    /**
     * 设置字体大小SP
     */
    fun setTextSizeSp(textSizeSp: Int): SpanUtil {
        val span = AbsoluteSizeSpan(textSizeSp, true)
        setSpan(span)
        return this
    }

    /**
     * 设置字体大小PX
     */
    fun setTextSizePx(textSizePx: Int): SpanUtil {
        val span = AbsoluteSizeSpan(textSizePx)
        setSpan(span)
        return this
    }

    /**
     * 设置
     * 正常Typeface.NORMAL、
     * 加粗Typeface.BOLD、
     * 斜体Typeface.ITALIC、
     * 粗斜Typeface.BOLD_ITALIC
     */
    fun setStyle(style: Int): SpanUtil {
        if (style == Typeface.NORMAL || style == Typeface.BOLD || style == Typeface.ITALIC || style == Typeface
                        .BOLD_ITALIC) {
            val span = StyleSpan(style)
            setSpan(span)
        }
        return this
    }

    //删除线
    val LINE_STRIKETHROUGH = 1
    //下划线
    val LINE_UNDERLINE = 2

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
        setSpan(span)
        return this
    }

    /**
     * 点击事件
     */
    fun setClick(span: ClickableSpan, tv: TextView): SpanUtil {
        setSpan(span)
        tv.movementMethod = LinkMovementMethod.getInstance()
        return this
    }

    fun build(): CharSequence {
        return mSSB
    }

    fun clear() {
        mSSB = SpannableStringBuilder();
        mString = ""
    }
}