package com.willpower.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.MotionEvent
import com.willpower.widget.entity.IContent
import com.willpower.widget.entity.IImage
import com.willpower.widget.helper.CanvasHelper
import com.willpower.widget.helper.ClickProxy

class JTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatTextView(context, attrs, defStyleAttr), JWidget {
    private var helper: CanvasHelper

    init {
        background = ColorDrawable(Color.TRANSPARENT)
        helper = CanvasHelper()
        initAttrs(attrs)
        helper.createPaints(this)
    }


    private fun initAttrs(attrs: AttributeSet?) {
        if (attrs == null) return
        val ta = context.obtainStyledAttributes(attrs, R.styleable.JTextView)
        helper.pressedMode = ta.getInt(R.styleable.JTextView_pressedMode, CanvasHelper.PRESSED_LIGHT_MODE)
        helper.borderColor = ta.getColor(R.styleable.JTextView_borderColor, Color.TRANSPARENT)
        helper.borderWidth = ta.getDimension(R.styleable.JTextView_borderWidth, 0f)
        helper.mBackground = ta.getColor(R.styleable.JTextView_backgroundColor, Color.TRANSPARENT)
        helper.cornerRadios = ta.getDimension(R.styleable.JTextView_cornerRadios, 0f)
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
        if (hasOnClickListeners()) listenPressed(event)
        return super.onTouchEvent(event)
    }

    /**
     * 处理点击效果
     */
    private fun listenPressed(e: MotionEvent) {
        if (e.action == MotionEvent.ACTION_DOWN) {
            helper.isPressed = true
            postInvalidate()
        } else if (e.action == MotionEvent.ACTION_UP
                || e.action == MotionEvent.ACTION_CANCEL
                || e.action == MotionEvent.ACTION_OUTSIDE) {
            helper.isPressed = false
            postInvalidate()
        }
    }


    /**
     * 代理 点击事件
     */
    override fun setOnClickListener(l: OnClickListener) {
        super.setOnClickListener(ClickProxy(l))
    }

    fun clearText(): JTextView {
        text = ""
        return this
    }

    /**
     * 添加带样式的文字
     */
    fun appendContent(content: IContent): JTextView {
        append(content.value())
        return this
    }

    /**
     * 插入图片
     */
    fun appendImage(image: IImage): JTextView {
        append(image.value())
        return this
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