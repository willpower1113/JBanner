package com.willpower.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatButton

class JButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatButton(context, attrs, defStyleAttr) {
    private var isButtonPressed = false
    private var mPaint: Paint? = null
    private var mBorderPaint: Paint? = null
    private var mForegroundPaint: Paint? = null
    private var mShadowPaint: Paint? = null
    private var mShadowRectF: RectF? = null
    private var pressedRectF: RectF? = null
    private var pressedMode = 0
    private var pressedColor = 0
    private var mBackground: Int = 0
    private var borderWidth = 0f
    private var cornerRadios = 0f
    private var borderColor = 0
    private var hasShadow = true

    private fun init(context: Context, attrs: AttributeSet?) {
        background = ColorDrawable(Color.TRANSPARENT)
        gravity = Gravity.CENTER
        if (attrs != null) initAttrs(context.obtainStyledAttributes(attrs, R.styleable.JButton))
        initPaint()
    }

    private fun initAttrs(ta: TypedArray) {
        pressedMode = ta.getInt(R.styleable.JButton_pressedMode, PRESSED_LIGHT_MODE)
        mBackground = ta.getColor(R.styleable.JButton_backgroundColor, Color.TRANSPARENT)
        borderColor = ta.getColor(R.styleable.JButton_borderColor, Color.TRANSPARENT)
        borderWidth = ta.getDimension(R.styleable.JButton_borderWidth, 0f)
        cornerRadios = ta.getDimension(R.styleable.JButton_cornerRadios, 8f)
        hasShadow = ta.getBoolean(R.styleable.JButton_hasShadow, true)
        ta.recycle()
        pressedColor = if (pressedMode == PRESSED_LIGHT_MODE) {
            Color.argb(80, 255, 255, 255)
        } else {
            Color.argb(30, 0, 0, 0)
        }
    }

    private fun initPaint() {
        mForegroundPaint = Paint()
        mForegroundPaint!!.color = pressedColor
        mShadowPaint = Paint()
        mShadowPaint!!.color = Color.WHITE
        mShadowPaint!!.isAntiAlias = true
        mShadowPaint!!.setShadowLayer(SHADOW_RADIO.toFloat(), 0f, 0f, shadowNormal)
        mShadowPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.color = mBackground
        if (borderWidth > 0f) {
            mBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mBorderPaint!!.style = Paint.Style.STROKE
            mBorderPaint!!.strokeWidth = borderWidth
            mBorderPaint!!.color = borderColor
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        changePressed(event)
        return event.action == MotionEvent.ACTION_DOWN || super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        createRect()
        drawShadow(canvas = canvas)
        drawBackground(canvas = canvas)
        drawBorder(canvas = canvas)
        super.onDraw(canvas)
    }

    /*
    构建边界
     */
    private fun createRect() {
        if (mShadowRectF == null) {
            mShadowRectF = RectF(SHADOW_RADIO.toFloat(), SHADOW_RADIO.toFloat(), (width - SHADOW_RADIO).toFloat(), (height - SHADOW_RADIO).toFloat())
        }
        if (pressedRectF == null) {
            pressedRectF = RectF((SHADOW_RADIO - 2).toFloat(), (SHADOW_RADIO - 2).toFloat(), (width - SHADOW_RADIO - 2).toFloat(), (height - SHADOW_RADIO - 2).toFloat())
        }
    }

    /*
    绘制阴影
     */
    private fun drawShadow(canvas: Canvas) {
        if (!hasShadow) return
        if (isButtonPressed) {
            mShadowPaint!!.setShadowLayer(SHADOW_RADIO.toFloat(), 0f, 0f, shadowPressed)
        } else {
            mShadowPaint!!.setShadowLayer(SHADOW_RADIO.toFloat(), 0f, 0f, shadowNormal)
        }
        canvas.drawRoundRect(mShadowRectF!!, cornerRadios, cornerRadios, mShadowPaint!!)
    }

    /*
    绘制背景
     */
    private fun drawBackground(canvas: Canvas) {
        if (isButtonPressed) {
            canvas.drawRoundRect(pressedRectF!!, cornerRadios, cornerRadios, mPaint!!)
        } else {
            canvas.drawRoundRect(mShadowRectF!!, cornerRadios, cornerRadios, mPaint!!)
        }
    }

    /*
    绘制边框
     */
    private fun drawBorder(canvas: Canvas) {
        if (borderWidth > 0f) {
            if (isButtonPressed) {
                canvas.drawRoundRect(pressedRectF!!, cornerRadios, cornerRadios, mBorderPaint!!)
            } else {
                canvas.drawRoundRect(mShadowRectF!!, cornerRadios, cornerRadios, mBorderPaint!!)
            }
        }
    }

    private fun changePressed(e: MotionEvent) {
        if (e.action == MotionEvent.ACTION_DOWN) {
            isButtonPressed = true
            postInvalidate()
        } else if (e.action == MotionEvent.ACTION_UP || e.action == MotionEvent.ACTION_CANCEL || e.action == MotionEvent.ACTION_OUTSIDE) {
            isButtonPressed = false
            postInvalidate()
        }
    }

    /*
    绘制点击蒙层
     */
    override fun onDrawForeground(canvas: Canvas) {
        super.onDrawForeground(canvas)
        if (isButtonPressed) {
            canvas.drawRoundRect(pressedRectF!!, cornerRadios, cornerRadios, mForegroundPaint!!)
        }
    }

    companion object {
        private const val SHADOW_RADIO = 10
        private const val PRESSED_LIGHT_MODE = 1
        private val shadowNormal = Color.parseColor("#CCCCCC")
        private val shadowPressed = Color.parseColor("#BBBBBB")
    }

    init {
        init(context, attrs)
    }
}