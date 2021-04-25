package com.willpower.widget.helper

import android.view.View

/**
 * 点击事件代理类
 */
class ClickProxy(private var l: View.OnClickListener) : View.OnClickListener {

    private var t: Long = 0L

    private val threshold = 200L

    override fun onClick(v: View?) {
        if (System.currentTimeMillis() - t < threshold) return
        t = System.currentTimeMillis()
        l.onClick(v)
    }
}