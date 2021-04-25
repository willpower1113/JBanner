package com.willpower.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.LinearLayout


class JLinearLayout @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    private var mRectF = RectF()
    private val mPath = Path()
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private var cornerRadios: Float = 0f

    private var borderWidth = 0f
    private var borderColor = 0

    init {
        initAttrs(attrs)
        setWillNotDraw(false)
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    }


    private fun initAttrs(attrs: AttributeSet?) {
        if (attrs == null) return
        val ta = context.obtainStyledAttributes(attrs, R.styleable.JLinearLayout)
//        helper.pressedMode = ta.getInt(R.styleable.JLinearLayout_pressedMode, CanvasHelper.PRESSED_LIGHT_MODE)
        borderColor = ta.getColor(R.styleable.JLinearLayout_borderColor, Color.TRANSPARENT)
        borderWidth = ta.getDimension(R.styleable.JLinearLayout_borderWidth, 0f)
//        helper.mBackground = ta.getColor(R.styleable.JLinearLayout_backgroundColor, Color.TRANSPARENT)
        cornerRadios = ta.getDimension(R.styleable.JLinearLayout_cornerRadios, 0f)
        ta.recycle()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) return
        mRectF.set(0f, 0f, width.toFloat(), height.toFloat())
        resetRoundPath()
    }

    override fun dispatchDraw(canvas: Canvas) {
        beforeDispatchDraw(canvas)
        super.dispatchDraw(canvas)
        afterDispatchDraw(canvas)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorderIfNeed(canvas)
    }


    private fun drawBorderIfNeed(canvas: Canvas) {
        if (borderWidth > 0f) {
            mPaint.style = Paint.Style.STROKE
            mPaint.color = borderColor
            canvas.drawRoundRect(RectF(borderWidth, borderWidth, width - borderWidth, height - borderWidth),
                    cornerRadios, cornerRadios, mPaint)
        }
    }

    private fun beforeDispatchDraw(canvas: Canvas) {
        //Android L版本以上，采用ViewOutlineProvider来裁剪view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            clipToOutline = true
        } else {
            canvas.save()
        }
    }

    private fun afterDispatchDraw(canvas: Canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(0, 0, width, height, cornerRadios)
                }
            }
        } else {
            canvas.drawPath(mPath, mPaint)
            canvas.restore()
        }
    }


    private fun resetRoundPath() {
        mPath.reset()
        mPath.addRoundRect(mRectF, cornerRadios, cornerRadios, Path.Direction.CW)
    }
}