package com.willpower.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import com.willpower.widget.helper.CanvasHelper
import com.willpower.widget.helper.ClickProxy

class JButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatButton(context, attrs, defStyleAttr), JWidget {

    private var helper: CanvasHelper

    init {
        background = ColorDrawable(Color.TRANSPARENT)
        gravity = Gravity.CENTER
        helper = CanvasHelper()
        initAttrs(attrs)
        helper.createPaints(this)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        if (attrs == null) return
        val ta = context.obtainStyledAttributes(attrs, R.styleable.JButton)
        helper.pressedMode = ta.getInt(R.styleable.JButton_pressedMode, CanvasHelper.PRESSED_LIGHT_MODE)
        helper.mBackground = ta.getColor(R.styleable.JButton_backgroundColor, Color.TRANSPARENT)
        helper.borderColor = ta.getColor(R.styleable.JButton_borderColor, Color.TRANSPARENT)
        helper.borderWidth = ta.getDimension(R.styleable.JButton_borderWidth, 0f)
        helper.cornerRadios = ta.getDimension(R.styleable.JButton_cornerRadios, 0f)
        ta.recycle()
    }


    override fun onDraw(canvas: Canvas) {
        helper.mathBound(width = width, height = height)
        helper.drawBackground(canvas = canvas)
        helper.drawBorderIfNeed(canvas = canvas)
        super.onDraw(canvas)
        helper.drawMask(canvas = canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (helper.mStatus == CanvasHelper.STATE_DISABLE) return true
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                helper.isPressed = true
                postInvalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE
            -> {
                helper.isPressed = false
                postInvalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 代理 点击事件
     */
    override fun setOnClickListener(l: OnClickListener) {
        super.setOnClickListener(ClickProxy(l))
    }

    fun disable() {
        helper.mStatus = CanvasHelper.STATE_DISABLE
        postInvalidate()
    }

    fun enable() {
        helper.mStatus = CanvasHelper.STATE_NORMAL
        postInvalidate()
    }

    override fun setCornerRadios(radios: Float) {
        helper.cornerRadios = radios
        postInvalidate()
    }

    override fun setBorderColor(color: Int) {
        helper.borderColor = color
        postInvalidate()
    }

    override fun setBorderWidth(width: Float) {
        helper.borderWidth = width
        postInvalidate()
    }

    override fun setWidgetBackgroundColor(color: Int) {
        helper.mBackground = color
        postInvalidate()
    }
}