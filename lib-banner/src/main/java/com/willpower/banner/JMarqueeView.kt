package com.willpower.banner

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.willpower.banner.animator.*
import com.willpower.banner.item.TextFragment
import java.lang.reflect.Field

class JMarqueeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : ViewPager(context, attrs), IBanner {
    private var interval = 10000L
    private var turnDuration = 500
    private var animator: IBanner.Animator = IBanner.Animator.NONE
    private var mActivity: FragmentActivity? = null
    private val fragments = arrayListOf<Fragment>()
    private var mTextSize = 18f
    private var mTextColor = Color.BLACK

    @Volatile
    private var isPlay: Boolean = false
    private var mData: ArrayList<IModel<*>> = arrayListOf()

     override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
         return true
     }
     override fun onTouchEvent(ev: MotionEvent?): Boolean {
         return true
     }

    override fun bind(activity: FragmentActivity): JMarqueeView {
        this.mActivity = activity
        return this
    }

    fun setData(data: ArrayList<String>): JMarqueeView {
        mData.clear()
        data.forEach { mData.add(IModel(it, IBanner.TEXT)) }
        return this
    }

    fun setTextSize(size: Float): JMarqueeView {
        mTextSize = size
        return this
    }

    fun setTextColor(color: Int): JMarqueeView {
        mTextColor = color
        return this
    }

    /**
     * 创建
     */
    private fun create(): IBanner {
        if (mActivity == null) throw NullPointerException("please bind FragmentActivity first!")
        Log.d(BuildConfig.TAG, "banner size [before make up]: ${mData.size}")
        makeUp()
        Log.d(BuildConfig.TAG, "banner size [after make up]: ${mData.size}")
        mData.forEach { data ->
            val bundle = Bundle()
            bundle.putLong("interval", interval)
            bundle.putString("stringData", data.data as String)
            bundle.putFloat("textSize", mTextSize)
            bundle.putInt("textColor", mTextColor)
            val fragment: IFragment = TextFragment()
            fragment.arguments = bundle
            fragment.bindBannerController(this)
            fragments.add(fragment)
        }
        try {
            //替换 ViewPager 的 Scroller
            val mField: Field = ViewPager::class.java.getDeclaredField("mScroller")
            mField.isAccessible = true
            val jScroller = JScroller(context, LinearInterpolator())
            jScroller.turnDuration = turnDuration
            mField.set(this, jScroller)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        adapter = JBannerAdapter(mActivity!!.supportFragmentManager, fragments)
        when (animator) {
            IBanner.Animator.TRANSITION3D -> setPageTransformer(true, Animator3D())
            IBanner.Animator.TRANSITION_X -> setPageTransformer(true, AnimatorTranslationX())
            IBanner.Animator.TRANSITION_Y -> setPageTransformer(true, AnimatorTranslationY())
            IBanner.Animator.ALPHA -> setPageTransformer(true, AnimatorAlpha())
            IBanner.Animator.ROTATE -> setPageTransformer(true, AnimatorRotate())
            IBanner.Animator.SCALE -> setPageTransformer(true, AnimatorScale())
            else -> {
            }
        }
        return this
    }

    /**
     * 数据长度补偿
     */
    private fun makeUp() {
        if (mData.isEmpty() || mData.size >= IBanner.DEFAULT_BANNER_SIZE) {
            return
        } else if (mData.size == 1) {
            return //如果只有一张Banner图，不进行补偿
        } else {
            val temp = mData
            mData.addAll(temp)
            makeUp()
        }
    }

    override fun interval(interval: Long): IBanner {
        this.interval = interval
        return this
    }

    override fun animator(animator: IBanner.Animator): IBanner {
        this.animator = animator;
        return this
    }

    override fun turnDuration(duration: Int): IBanner {
        turnDuration = duration
        return this
    }

    override fun clickListener(listener: OnClickListener): IBanner {
        // nothing to do
        return this
    }

    override fun next() {
        if (!isPlay) return
        currentItem++
    }

    override fun start() {
        if (mData.size == 1) return
        if (isPlay) return
        isPlay = true
        create()
    }

    override fun stop() {
        if (mData.size == 1) return
        if (!isPlay) return
        isPlay = false
        fragments.clear()
        adapter?.notifyDataSetChanged()
    }

    override fun release() {
        fragments.clear()
        mData.clear()
        mActivity = null
    }

    override fun refreshData() {
        fragments.clear()
        mData.clear()
        create()
        adapter?.notifyDataSetChanged()
        next()
    }

    override fun isShowOnScreen(): Boolean {
        return visibility == View.VISIBLE
    }

    override fun isPlaying(): Boolean {
        return isPlay
    }

}