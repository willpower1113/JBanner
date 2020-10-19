package com.willpower.banner

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView

abstract class OnModelClickListener : RecyclerView.OnItemTouchListener {
    private var mGestureDetector: GestureDetectorCompat? = null
    private var recyclerView: RecyclerView? = null

    abstract fun onBannerClick(position: Int)

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        mGestureDetector?.onTouchEvent(e)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        if (recyclerView == null) {
            this.recyclerView = rv
            mGestureDetector = GestureDetectorCompat(recyclerView?.context, onGestureListener)
        } else if (recyclerView !== rv) {
            this.recyclerView = rv
            mGestureDetector = GestureDetectorCompat(recyclerView?.context, onGestureListener)
        }
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }

    private val onGestureListener = object : GestureDetector.OnGestureListener {
        override fun onShowPress(p0: MotionEvent?) {
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            if (recyclerView!!.scrollState !== RecyclerView.SCROLL_STATE_IDLE) {
                return false
            }
            val v: View = recyclerView!!.findChildViewUnder(e.x, e.y)!!
            val vh: RecyclerView.ViewHolder = recyclerView!!.getChildViewHolder(v)
            onBannerClick(vh.layoutPosition)
            return true
        }

        override fun onDown(p0: MotionEvent?): Boolean {
            return false
        }

        override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            return false
        }

        override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            return false
        }

        override fun onLongPress(p0: MotionEvent?) {
        }
    }
}