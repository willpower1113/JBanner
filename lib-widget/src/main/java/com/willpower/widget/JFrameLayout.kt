package com.willpower.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.widget.FrameLayout
import com.willpower.widget.helper.CanvasHelper

class JFrameLayout @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) , JWidget {
    private var helper: CanvasHelper

    init {
        setWillNotDraw(false)
        background = ColorDrawable(Color.TRANSPARENT)
        helper = CanvasHelper()
        initAttrs(attrs)
        helper.createPaints(this)
    }


    private fun initAttrs(attrs: AttributeSet?) {
        if (attrs == null) return
        val ta = context.obtainStyledAttributes(attrs, R.styleable.JFrameLayout)
        helper.pressedMode = ta.getInt(R.styleable.JFrameLayout_pressedMode, CanvasHelper.PRESSED_LIGHT_MODE)
        helper.borderColor = ta.getColor(R.styleable.JFrameLayout_borderColor, Color.TRANSPARENT)
        helper.borderWidth = ta.getDimension(R.styleable.JFrameLayout_borderWidth, 0f)
        helper.mBackground = ta.getColor(R.styleable.JFrameLayout_backgroundColor, Color.TRANSPARENT)
        helper.cornerRadios = ta.getDimension(R.styleable.JFrameLayout_cornerRadios, 0f)
        ta.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        helper.mathBound(width = width, height = height)
        helper.drawBackground(canvas = canvas)
        super.onDraw(canvas)
        helper.drawBorderIfNeed(canvas = canvas)
        helper.drawMask(canvas = canvas)
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