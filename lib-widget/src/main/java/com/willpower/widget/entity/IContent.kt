package com.willpower.widget.entity

import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.widget.TextView


class IContent(source: String) {
    private var mSpannable = SpannableString.valueOf(source)

    /**
     * 删除线
     */
    fun strikeThrough(): IContent {
        mSpannable.setSpan(StrikethroughSpan(), 0, mSpannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 下划线
     */
    fun underline(): IContent {
        mSpannable.setSpan(UnderlineSpan(), 0, mSpannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 字体颜色
     */
    fun textColor(color: Int): IContent {
        mSpannable.setSpan(ForegroundColorSpan(color), 0, mSpannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 背景颜色
     */
    fun backgroundColor(color: Int): IContent {
        mSpannable.setSpan(BackgroundColorSpan(color), 0, mSpannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 点击事件
     */
    fun clickListener(tv: TextView, span: ClickableSpan): IContent {
        mSpannable.setSpan(span, 0, mSpannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv.movementMethod = LinkMovementMethod.getInstance()
        return this
    }

    /**
     * 字体尺寸【绝对值】
     */
    fun textSize(size: Int): IContent {
        mSpannable.setSpan(AbsoluteSizeSpan(size), 0, mSpannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 字体尺寸【相对值】
     */
    fun textScale(scale: Float): IContent {
        mSpannable.setSpan(RelativeSizeSpan(scale), 0, mSpannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 字体尺寸【相对值】
     * @param style -> Typeface.NORMAL,Typeface.BOLD,Typeface.ITALIC,Typeface.BOLD_ITALIC
     */
    fun style(style: Int): IContent {
        mSpannable.setSpan(StyleSpan(style), 0, mSpannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 上标
     */
    fun upper(): IContent {
        mSpannable.setSpan(SuperscriptSpan(), 0, mSpannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 下标
     */
    fun lower(): IContent {
        mSpannable.setSpan(SubscriptSpan(), 0, mSpannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 添加 Span
     */
    fun addSpan(what: Object, start: Int, end: Int, flags: Int): IContent {
        mSpannable.setSpan(what, start, end, flags)
        return this
    }


    fun value(): SpannableString {
        return mSpannable
    }
}