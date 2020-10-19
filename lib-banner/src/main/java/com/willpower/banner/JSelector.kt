package com.willpower.banner

import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.text.StaticLayout
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.PagerSnapHelper
import com.willpower.banner.adapter.JSelectorAdapter
import com.willpower.banner.model.SelectorModel


/**
 * 单项选择器
 */
class JSelector : IRecycler<SelectorModel<Any>> {
    private var mTextSize: Float = 20f
    private val mCamera: Camera = Camera()
    private val mMatrix: Matrix = Matrix()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun textSize(textSize: Float): JSelector {
        mTextSize = textSize
        return this
    }

    override fun create() {
        layoutManager = WheelPickerLayoutManager(context)
        adapter = JSelectorAdapter(context, mData)
        PagerSnapHelper().attachToRecyclerView(this)
    }

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        if (child is TextView) {
            val data = child.text.toString()
            if (child.textSize == mTextSize) {
                var finalTextSize: Float = mTextSize
                val dataStringW = StaticLayout.getDesiredWidth(data, 0, data.length, child.paint)
                if (getHorizontalSpace() > 0 && dataStringW * 1.1f > getHorizontalSpace()) {
                    finalTextSize = getHorizontalSpace() / dataStringW / 1.1f * mTextSize
                }
                child.setTextSize(TypedValue.COMPLEX_UNIT_PX, finalTextSize)
            }
        }
        return super.drawChild(canvas, child, drawingTime)
    }

    private fun getVerticalSpace(): Int {
        return height - paddingBottom - paddingTop
    }

    private fun getHorizontalSpace(): Int {
        return width - paddingLeft - paddingRight
    }

    /** @hide */
    override fun start() {
    }

    /** @hide */
    override fun stop() {
    }

    /** @hide */
    override fun interval(interval: Long): IRecycler<SelectorModel<Any>> {
        return this
    }

    /** @hide */
    override fun direction(direction: Int): IRecycler<SelectorModel<Any>> {
        return this
    }
}