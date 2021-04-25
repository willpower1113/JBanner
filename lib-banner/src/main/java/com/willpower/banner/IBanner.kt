package com.willpower.banner

import android.support.v4.app.FragmentActivity
import android.view.View

interface IBanner {
    fun bind(activity: FragmentActivity): IBanner
    fun logger(iLogger: ILogger): IBanner
    fun getLogger(): ILogger?
    fun interval(interval: Long): IBanner
    fun animator(animator: Animator): IBanner
    fun turnDuration(duration: Int): IBanner
    fun clickListener(listener: View.OnClickListener): IBanner
    operator fun next()
    fun start()
    fun stop()
    fun release()
    fun refreshData()
    fun isShowOnScreen(): Boolean
    fun isPlaying(): Boolean

    enum class Animator {
        ROTATE, TRANSITION3D, TRANSITION_X, TRANSITION_Y, ALPHA, SCALE, NONE
    }

    companion object {
        const val FAST = 1
        const val DELAY = 2
        const val VIDEO = 1
        const val IMAGE = 2
        const val TEXT = 3
        const val DEFAULT_BANNER_SIZE = 4
    }
}