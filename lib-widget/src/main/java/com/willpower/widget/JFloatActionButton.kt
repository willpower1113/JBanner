package com.willpower.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.AppCompatImageButton
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.willpower.widget.helper.ClickProxy

class JFloatActionButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatImageButton(context, attrs, defStyleAttr) {

    private var mBackgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mShadowPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var isTouchView: Boolean = false
    private var mColor: Int = Color.WHITE /* 背景颜色 */
    private var mShadowRadios: Float = 0f /* 阴影 */
    private var movable: Boolean = true /* 是否支持移动*/
    private var pressedMode: Int = PRESSED_LIGHT_MODE /* 触摸反馈 */
    private var moved: Boolean = false

    private var location: Rect? = null

    companion object {
        private const val PRESSED_LIGHT_MODE = 1
        private const val threshold = 20f
        private val shadowNormal = Color.parseColor("#999999")
        private val shadowPressed = Color.parseColor("#777777")
    }

    init {
        background = ColorDrawable(Color.TRANSPARENT)
        scaleType = ScaleType.CENTER_INSIDE

        initAttrs(attrs)

        mBackgroundPaint.color = mColor

        mShadowPaint.color = Color.WHITE
        mShadowPaint.isAntiAlias = true
        mShadowPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)//关闭硬件加速

        setPadding(paddingLeft + mShadowRadios.toInt() * 2,
                paddingTop + mShadowRadios.toInt() * 2,
                paddingRight + mShadowRadios.toInt() * 2,
                paddingBottom + mShadowRadios.toInt() * 2)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        if (attrs == null) return
        val ta = context.obtainStyledAttributes(attrs, R.styleable.JFloatActionButton)
        movable = ta.getBoolean(R.styleable.JFloatActionButton_movable, true)
        mColor = ta.getColor(R.styleable.JFloatActionButton_backgroundColor, resources.getColor(R.color.colorAccent))
        mShadowRadios = ta.getFloat(R.styleable.JFloatActionButton_android_shadowRadius,10f)
        pressedMode = ta.getInt(R.styleable.JFloatActionButton_pressedMode, PRESSED_LIGHT_MODE)
        ta.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        if (location == null && right != 0) {
            location = Rect(left, top, right, bottom)
        }
        drawShadowIfNeed(canvas)
        drawBackground(canvas)
        super.onDraw(canvas)
    }

    /*
  绘制阴影
   */
    private fun drawShadowIfNeed(canvas: Canvas) {
        if (isTouchView) {
            mShadowPaint.setShadowLayer(mShadowRadios, 0f, 0f, shadowPressed)
        } else {
            mShadowPaint.setShadowLayer(mShadowRadios, 0f, 0f, shadowNormal)
        }
        canvas.drawCircle(width / 2f, height / 2f, width / 2f - mShadowRadios, mShadowPaint)
    }


    private fun drawBackground(canvas: Canvas) {
        canvas.drawCircle(width / 2f, height / 2f, width / 2f - mShadowRadios * 2, mBackgroundPaint)
    }

    var pointX: Float = 0f
    var pointY: Float = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                moved = false
                pointX = event.rawX
                pointY = event.rawY
                isTouchView = true
                postInvalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                move((event.rawX - pointX).toInt(), (event.rawY - pointY).toInt())
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_OUTSIDE -> {
                isTouchView = false
                postInvalidate()
            }
            MotionEvent.ACTION_UP -> {
                isTouchView = false
                postInvalidate()
                if (movable)
                    location = Rect(left, top, right, bottom)
            }
        }
        if (moved) {
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun move(x: Int, y: Int) {
        moved = movable && x > threshold || y > threshold
        if (!movable) return
        location?.let {
            layout(location!!.left + x, location!!.top + y, location!!.right + x, location!!.bottom + y)
            postInvalidate()
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(ClickProxy(l, 1000L))
    }
}