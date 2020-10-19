package com.willpower.widget.entity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.text.SpannableString
import android.text.Spanned
import android.text.style.*
import android.widget.TextView


class IContent(source: String) {
    private var placeholder = '*'//占位符
    private var mSpannableArrays = arrayListOf<ISpannable>()
    private var content: StringBuilder = StringBuilder(source)
    private val length: Int = source.length
    private val defaultCompressTo = 40f

    companion object {
        /**
         * 建议图片尺寸  TextView.textSize
         */
        fun imageSnugSize(tv: TextView): Float {
            return tv.textSize
        }
    }

    /**
     * 删除线
     */
    fun strikeThrough(): IContent {
        mSpannableArrays.add(ISpannable(StrikethroughSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE))
        return this
    }

    /**
     * 下划线
     */
    fun underline(): IContent {
        mSpannableArrays.add(ISpannable(UnderlineSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE))
        return this
    }

    /**
     * 字体颜色
     */
    fun textColor(color: Int): IContent {
        mSpannableArrays.add(ISpannable(ForegroundColorSpan(color), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE))
        return this
    }

    /**
     * 背景颜色
     */
    fun backgroundColor(color: Int): IContent {
        mSpannableArrays.add(ISpannable(BackgroundColorSpan(color), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE))
        return this
    }

    /**
     * 点击事件
     */
    fun clickListener(span: ClickableSpan): IContent {
        mSpannableArrays.add(ISpannable(span, 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE))
        return this
    }

    /**
     * 字体尺寸【绝对值】
     */
    fun textSize(size: Int): IContent {
        mSpannableArrays.add(ISpannable(AbsoluteSizeSpan(size), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE))
        return this
    }

    /**
     * 字体尺寸【相对值】
     */
    fun textScale(scale: Float): IContent {
        mSpannableArrays.add(ISpannable(RelativeSizeSpan(scale), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE))
        return this
    }

    /**
     * 字体尺寸【相对值】
     * @param style -> Typeface.NORMAL,Typeface.BOLD,Typeface.ITALIC,Typeface.BOLD_ITALIC
     */
    fun style(style: Int): IContent {
        mSpannableArrays.add(ISpannable(StyleSpan(style), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE))
        return this
    }

    /**
     * 上标
     */
    fun upper(): IContent {
        mSpannableArrays.add(ISpannable(SuperscriptSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE))
        return this
    }

    /**
     * 下标
     */
    fun lower(): IContent {
        mSpannableArrays.add(ISpannable(SubscriptSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE))
        return this
    }

    /**
     * 添加 Span
     */
    fun addSpan(what: Object, start: Int, end: Int, flags: Int): IContent {
        mSpannableArrays.add(ISpannable(what, start, end, flags))
        return this
    }


    fun value(): SpannableString {
        var mSpannable = SpannableString.valueOf(content)
        val increment = mSpannable.length - length
        mSpannableArrays.forEach {
            val start = if (it.start == 0) {
                0
            } else {
                increment + it.start
            }
            val end = if (it.end == 1) {
                1
            } else {
                increment + it.end
            }
            mSpannable.setSpan(it.what, start, end, it.flags)
        }
        return mSpannable
    }
}