package com.willpower.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.willpower.widget.helper.CanvasHelper


class JImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatImageView(context, attrs, defStyleAttr) , JWidget {
    private var helper: CanvasHelper = CanvasHelper()

    init {
        background = ColorDrawable(Color.TRANSPARENT)
        helper = CanvasHelper()
        initAttrs(attrs)
        helper.createPaints(this)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        if (attrs == null) return
        val ta = context.obtainStyledAttributes(attrs, R.styleable.JImageView)
        helper.pressedMode = ta.getInt(R.styleable.JImageView_pressedMode, CanvasHelper.PRESSED_LIGHT_MODE)
        helper.borderColor = ta.getColor(R.styleable.JImageView_borderColor, Color.TRANSPARENT)
        helper.borderWidth = ta.getDimension(R.styleable.JImageView_borderWidth, 0f)
        helper.mBackground = ta.getColor(R.styleable.JImageView_backgroundColor, Color.TRANSPARENT)
        helper.cornerRadios = ta.getDimension(R.styleable.JImageView_cornerRadios, 0f)
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