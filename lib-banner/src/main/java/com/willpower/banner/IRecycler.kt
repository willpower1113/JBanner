package com.willpower.banner

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*

open class IRecycler<T> : RecyclerView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var interval: Long = 10000L
    private var mode: Int = 1
    protected var mData: ArrayList<T> = arrayListOf()
    private var direction: Int = HORIZONTAL
    private var timer: Timer? = null

    /** 目标项是否在最后一个可见项之后 */
    private var mShouldScroll = false

    /** 记录目标项位置 */
    private var mToPosition = 0

    /**
     * 翻页方向
     */
   open fun direction(direction: Int): IRecycler<T> {
        this.direction = direction
        return this
    }

    /**
     * 翻页间隔时间
     */
   open fun interval(interval: Long): IRecycler<T> {
        this.interval = interval
        return this
    }


    /**
     * 翻页动画
     */
    open fun setAnimatorMode(mode: Int): IRecycler<T> {
        this.mode = mode
        return this
    }

    /**
     * 设置数据源
     */
   open fun setNewData(newData: ArrayList<T>): IRecycler<T> {
        mData = newData
        return this
    }

   open fun addData(data: T): IRecycler<T> {
        mData.add(data)
        return this
    }

   open fun addData(index: Int, data: T): IRecycler<T> {
        mData.add(index, data)
        return this
    }

    open fun removeData(data: T): IRecycler<T> {
        mData.remove(data)
        return this
    }

    open fun removeIndex(index: Int): IRecycler<T> {
        mData.removeAt(index)
        return this
    }

    /**
     * 添加点击事件
     */
    open fun setOnBannerClickListener(listener: OnModelClickListener) {
        addOnItemTouchListener(listener)
    }

    open fun create() {
        layoutManager = when (direction) {
            VERTICAL -> {
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
            else -> {
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
        PagerSnapHelper().attachToRecyclerView(this)
    }

    open fun start() {
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                smoothMoveToPosition()
            }
        }, interval, interval)
    }

    open fun stop() {
        timer?.cancel()
    }


    private fun smoothMoveToPosition() {
        mToPosition++
        if (mToPosition >= mData!!.size) {
            mToPosition = 0
            post { scrollToPosition(mToPosition) }
            return
        }
        // 第一个可见位置
        val firstItem = getChildLayoutPosition(getChildAt(0))
        // 最后一个可见位置
        val lastItem = getChildLayoutPosition(getChildAt(mData!!.size - 1))
        if (mToPosition < firstItem) {
            // 如果跳转位置在第一个可见位置之前，就smoothScrollToPosition可以直接跳转
            smoothScrollToPosition(mToPosition)
        } else if (mToPosition <= lastItem) {
            // 跳转位置在第一个可见项之后，最后一个可见项之前
            // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
            val movePosition = mToPosition - firstItem
            if (movePosition in 0 until childCount) {
                val top = getChildAt(movePosition).top
                smoothScrollBy(0, top)
            }
        } else {
            // 如果要跳转的位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            smoothScrollToPosition(mToPosition)
            mShouldScroll = true
        }
    }
}