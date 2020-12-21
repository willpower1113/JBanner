package com.willpower.widget.helper

import android.graphics.*
import android.view.View

class CanvasHelper {
    private var mBackgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mBorderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mForegroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mShadowPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mShadowRectF: RectF? = null
    private var normalRectF: RectF? = null
    private var pressedColor = 0
    var isPressed = false
    var pressedMode = 0
    var borderWidth = 0f
    var borderColor = 0
    var mBackground = 0
    var cornerRadios = 0f
    var hasShadow = false
    var shadowRadios = 0f

    companion object {
        const val PRESSED_LIGHT_MODE = 1
        private val shadowNormal = Color.parseColor("#999999")
        private val shadowPressed = Color.parseColor("#777777")
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
        mShadowPaint.color = Color.WHITE
        mShadowPaint.isAntiAlias = true
        mShadowPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
        v.setLayerType(View.LAYER_TYPE_SOFTWARE, null)//关闭硬件加速
        mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBackgroundPaint.color = mBackground

        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.strokeWidth = borderWidth
    }


    /*
    构建边界
     */
    fun mathBound(width: Int, height: Int) {
        if (mShadowRectF == null) {
            mShadowRectF = RectF(shadowRadios, shadowRadios, width - shadowRadios, height - shadowRadios)
        }
        if (normalRectF == null) {
            normalRectF = RectF(shadowRadios * 2f + borderWidth,
                    shadowRadios * 2f + borderWidth,
                    width - shadowRadios * 2f - borderWidth,
                    height - shadowRadios * 2f - borderWidth)
        }
    }

    /*
    绘制阴影
     */
    fun drawShadowIfNeed(canvas: Canvas) {
        if (!hasShadow) return
        if (isPressed) {
            mShadowPaint.setShadowLayer(shadowRadios, 0f, 0f, shadowPressed)
        } else {
            mShadowPaint.setShadowLayer(shadowRadios, 0f, 0f, shadowNormal)
        }
        canvas.drawRoundRect(mShadowRectF!!, cornerRadios, cornerRadios, mShadowPaint)
    }

    /*
    绘制背景
     */
    fun drawBackground(canvas: Canvas) {
        canvas.drawRoundRect(normalRectF!!, cornerRadios, cornerRadios, mBackgroundPaint)
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