package com.github2136.util;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

/**
 * http://my.oschina.net/u/2406628/blog/473450
 */
public class SpanUtil {

    SpannableStringBuilder mSSB;
    String mString;

    public SpanUtil() {
        this("");
    }

    public SpanUtil(String str) {
        mSSB = new SpannableStringBuilder(str);
        mString = null;
    }

    private void setSpan(Object what) {
        mSSB.setSpan(what, mSSB.length() - mString.length(), mSSB.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 添加内容
     */
    public SpanUtil append(String str) {
        mString = str == null ? "" : str;
        mSSB.append(mString);
        return this;
    }

    /**
     * 设置颜色
     */
    public SpanUtil setTextColor(int color) {
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        setSpan(span);
        return this;
    }

    /**
     * 设置背景颜色
     */
    public SpanUtil setBackground(int color) {
        BackgroundColorSpan span = new BackgroundColorSpan(color);
        setSpan(span);
        return this;
    }

    /**
     * 设置字体大小SP
     */
    public SpanUtil setTextSizeSp(int textSizeSp) {
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(textSizeSp, true);
        setSpan(span);
        return this;
    }

    /**
     * 设置字体大小PX
     */
    public SpanUtil setTextSizePx(int textSizePx) {
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(textSizePx);
        setSpan(span);
        return this;
    }

    /**
     * 设置
     * 正常Typeface.NORMAL、
     * 加粗Typeface.BOLD、
     * 斜体Typeface.ITALIC、
     * 粗斜Typeface.BOLD_ITALIC
     */
    public SpanUtil setStyle(int style) {
        if (style == Typeface.NORMAL || style == Typeface.BOLD || style == Typeface.ITALIC || style == Typeface
                .BOLD_ITALIC) {
            StyleSpan span = new StyleSpan(style);
            setSpan(span);
        }
        return this;
    }

    //删除线
    public static final int LINE_STRIKETHROUGH = 1;
    //下划线
    public static final int LINE_UNDERLINE = 2;

    /**
     * 设置删除线LINE_STRIKETHROUGH、下划线LINE_UNDERLINE
     */
    public SpanUtil setLine(int line) {
        switch (line) {
            case LINE_STRIKETHROUGH: {
                StrikethroughSpan span = new StrikethroughSpan();
                setSpan(span);
            }
            break;
            case LINE_UNDERLINE: {
                UnderlineSpan span = new UnderlineSpan();
                setSpan(span);
            }
            break;
        }
        return this;
    }

    /**
     * 设置链接
     */
    public SpanUtil setLink(String link) {
        URLSpan span = new URLSpan(link);
        setSpan(span);
        return this;
    }

    /**
     * 点击事件
     */
    public SpanUtil setClick(ClickableSpan span, TextView tv) {
        setSpan(span);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        return this;
    }

    public CharSequence build() {
        return mSSB;
    }
}
