package com.willpower.widget.entity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.widget.TextView

/**
 * 添加Image
 */
class IImage(context: Context, res: Int, compressTo: Float = 50f) {
    private var mSpannable = SpannableString.valueOf("*")

    companion object {
        /**
         * 建议图片尺寸  TextView.textSize
         */
        fun imageSnugSize(tv: TextView): Float {
            return tv.textSize
        }
    }

    init {
        mSpannable.setSpan(ImageSpan(context, compress(context, res, compressTo)), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    fun value(): SpannableString {
        return mSpannable
    }

    /*
    压缩图片
     */
    private fun compress(context: Context, res: Int, compressTo: Float): Bitmap {
        val bm = BitmapFactory.decodeResource(context.resources, res)
        // 获得图片的宽高
        val width = bm.width
        val height = bm.height
        // 计算缩放比例
        val scaleWidth = compressTo / width
        val scaleHeight = compressTo / height
        // 取得想要缩放的matrix参数
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true)
    }
}