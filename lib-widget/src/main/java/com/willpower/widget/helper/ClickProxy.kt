package com.willpower.widget.helper

import android.view.View

/**
 * 点击事件代理类
 */
class ClickProxy(private var l: View.OnClickListener?, private val threshold: Long) : View.OnClickListener {

    private var t: Long = 0L

    override fun onClick(v: View?) {
        if (threshold > 0L) {
            if (System.currentTimeMillis() - t < threshold) return
            t = System.currentTimeMillis()
        }
        l?.onClick(v)
    }
}