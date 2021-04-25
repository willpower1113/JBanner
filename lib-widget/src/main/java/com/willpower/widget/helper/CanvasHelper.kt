package com.willpower.widget.helper

import android.graphics.*
import android.view.View


class CanvasHelper {
    private var mBackgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private var mBorderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mForegroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var normalRectF: RectF? = null
    private var pressedColor = 0
    var isPressed = false
    var pressedMode = 0
    var borderWidth = 0f
    var borderColor = 0
    var mBackground = 0
    var cornerRadios = 0f
    var mStatus: Int = STATE_NORMAL

    companion object {
        const val PRESSED_LIGHT_MODE = 1
        const val PRESSED_DARK_MODE = 2
        const val PRESSED_NONE = 3
        private val shadowNormal = Color.parseColor("#999999")
        private val shadowPressed = Color.parseColor("#777777")
        const val STATE_NORMAL = 0
        const val STATE_DISABLE = 1
    }


    /**
     * 创建 Paint
     */
    fun createPaints(v: View) {
        pressedColor = if (pressedMode == PRESSED_LIGHT_MODE) {
            Color.argb(90, 255, 255, 255)
        } else {
            Color.argb(30, 0, 0, 0)
        }
        mForegroundPaint.color = pressedColor
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.strokeWidth = borderWidth
    }


    /*
    构建边界
     */
    fun mathBound(width: Int, height: Int) {
        if (normalRectF == null) {
            normalRectF = RectF(borderWidth, borderWidth, width - borderWidth, height - borderWidth)
        }
    }

    /*
    绘制背景
     */
    fun drawBackground(canvas: Canvas) {
        /*if (mStatus == STATE_NORMAL)
            mBackgroundPaint.color = mBackground
        else
            mBackgroundPaint.color = Color.DKGRAY*/
        /*  canvas.drawRoundRect(normalRectF!!, cornerRadios, cornerRadios, mBackgroundPaint)
          val path = Path()
          path.addRoundRect(normalRectF!!, floatArrayOf(cornerRadios, cornerRadios,
                  cornerRadios, cornerRadios, cornerRadios, cornerRadios, cornerRadios, cornerRadios), Path.Direction.CW)
          canvas.clipPath(path)*/
        canvas.save()
        mBackgroundPaint.color = Color.WHITE
        mBackgroundPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        val path = Path()
        canvas.drawPath(path, mBackgroundPaint)
        canvas.restore()
    }

    /*
    绘制边框
     */
    fun drawBorderIfNeed(canvas: Canvas) {
        if (borderWidth > 0f) {
            if (isPressed)
                mBorderPaint.color = shadowNormal
            else
                mBorderPaint.color = borderColor
            canvas.drawRoundRect(normalRectF!!, cornerRadios, cornerRadios, mBorderPaint)
        }
    }

    /**
     * 绘制蒙版
     */
    fun drawMask(canvas: Canvas) {
        if (isPressed) {
            canvas.drawRoundRect(normalRectF!!, cornerRadios, cornerRadios, mForegroundPaint)
        }
    }
}