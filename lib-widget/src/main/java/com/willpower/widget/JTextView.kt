package com.willpower.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.willpower.widget.entity.IContent
import com.willpower.widget.entity.IImage

class JTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {
    private var hasClickListener = false
    private var isTextPressed = false
    private var mPaint: Paint? = null
    private var mBorderPaint: Paint? = null
    private var mForegroundPaint: Paint? = null
    private var mShadowPaint: Paint? = null
    private var mShadowRectF: RectF? = null
    private var pressedRectF: RectF? = null
    private var pressedMode = 0
    private var pressedColor = 0
    private var borderWidth = 0f
    private var borderColor = 0
    private var mBackground = 0
    private var cornerRadios = 0f
    private var hasShadow = false

    private fun init(context: Context, attrs: AttributeSet?) {
        background = ColorDrawable(Color.TRANSPARENT)
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.onClick))
            hasClickListener = ta.getString(ta.getIndex(0)) != null
            ta.recycle()
            initAttrs(context.obtainStyledAttributes(attrs, R.styleable.JTextView))
        }
        initPaint()
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

    private fun initAttrs(ta: TypedArray) {
        pressedMode = ta.getInt(R.styleable.JTextView_pressedMode, PRESSED_LIGHT_MODE)
        mBackground = ta.getColor(R.styleable.JTextView_backgroundColor, Color.TRANSPARENT)
        borderColor = ta.getColor(R.styleable.JTextView_borderColor, Color.TRANSPARENT)
        borderWidth = ta.getDimension(R.styleable.JTextView_borderWidth, 0f)
        cornerRadios = ta.getDimension(R.styleable.JTextView_cornerRadios, 8f)
        hasShadow = ta.getBoolean(R.styleable.JTextView_hasShadow, false)
        ta.recycle()
        pressedColor = if (pressedMode == PRESSED_LIGHT_MODE) {
            Color.argb(80, 255, 255, 255)
        } else {
            Color.argb(30, 0, 0, 0)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        hasClickListener = l != null
        super.setOnClickListener(l)
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
        if (isTextPressed) {
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
        if (isTextPressed) {
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
            if (isTextPressed) {
                canvas.drawRoundRect(pressedRectF!!, cornerRadios, cornerRadios, mBorderPaint!!)
            } else {
                canvas.drawRoundRect(mShadowRectF!!, cornerRadios, cornerRadios, mBorderPaint!!)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (hasClickListener) changePressed(event)
        return super.onTouchEvent(event)
    }

    private fun changePressed(e: MotionEvent) {
        if (e.action == MotionEvent.ACTION_DOWN) {
            isTextPressed = true
            postInvalidate()
        } else if (e.action == MotionEvent.ACTION_UP
                || e.action == MotionEvent.ACTION_CANCEL
                || e.action == MotionEvent.ACTION_OUTSIDE) {
            isTextPressed = false
            postInvalidate()
        }
    }

    override fun onDrawForeground(canvas: Canvas) {
        super.onDrawForeground(canvas)
        if (isTextPressed) {
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

    fun appendContent(content: IContent): JTextView {
        append(content.value())
        return this
    }

    fun appendImage(image:IImage): JTextView {
        append(image.value())
        return this
    }
}