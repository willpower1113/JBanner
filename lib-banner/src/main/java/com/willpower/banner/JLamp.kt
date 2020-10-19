package com.willpower.banner

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import com.willpower.banner.adapter.JLampAdapter
import com.willpower.banner.model.LampModel

/**
 * 跑马灯
 */
class JLamp : IRecycler<LampModel> {
    var textSize: Float = 20f
    var textColor: Int = Color.BLACK

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs == null) return
        var ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.JLamp)
        textSize = ta.getDimension(R.styleable.JLamp_lampSize, 20f)
        textColor = ta.getColor(R.styleable.JLamp_lampColor, Color.BLACK)
        ta.recycle()
    }

    override fun create() {
        super.create()
        adapter = JLampAdapter(context, textSize, textColor, mData)
    }
}