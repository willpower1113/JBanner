package com.willpower.banner

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.willpower.banner.IBanner.DEFAULT_BANNER_SIZE
import com.willpower.banner.animator.*
import com.willpower.banner.item.ErrorFragment
import com.willpower.banner.item.ImageFragment
import com.willpower.banner.item.VideoFragment
import java.io.File
import java.lang.reflect.Field
import java.util.*


class JBanner @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs), IBanner {
    private var interval = 10000L
    private var turnDuration = 500
    private var animator: IBanner.Animator = IBanner.Animator.NONE
    private var mData: ArrayList<IModel<*>> = arrayListOf()
    private var mActivity: FragmentActivity? = null
    private var bannerDirectory: String = ""
    private var defaultBanners: IntArray? = null
    private var childClickable = false
    private val fragments = arrayListOf<Fragment>()
    private var mListener: OnClickListener? = null
    private var clickTime = 0L

    @Volatile
    private var isPlay: Boolean = false

    override fun bind(activity: FragmentActivity): IBanner {
        mActivity = activity
        return this
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (mListener != null) true else {
            if (childClickable) return super.onTouchEvent(ev)
            else true
        }
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        mListener?.let {
            when (ev?.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    clickTime = System.currentTimeMillis()
                }
                MotionEvent.ACTION_UP -> {
                    if (System.currentTimeMillis() - clickTime >= 50L) {
                        Log.i(IBanner.TAG, "click banner")
                        it.onClick(this)
                    }
                }
            }
        }

        return if (mListener != null) true else {
            if (childClickable) return super.onTouchEvent(ev)
            else true
        }
    }

    override fun clickListener(listener: OnClickListener): IBanner {
        mListener = listener
        return this
    }

    /**
     * 默认Banner图
     */
    override fun defaultBanners(vararg defaultBanners: Int): IBanner {
        this.defaultBanners = defaultBanners
        return this
    }

    /**
     * 循环周期
     */
    override fun interval(interval: Long): IBanner {
        this.interval = interval
        return this
    }

    /**
     * 播放下一条
     * @param type IBanner.DELAY 延迟跳转 IBanner.FAST 立刻跳转
     */
    override fun next() {
        if (!isPlay) return
        currentItem++
        Log.e(IBanner.TAG, "play now: ${currentItem % mData.size}")
    }

    /**
     * 设置动画
     */
    override fun animator(animator: IBanner.Animator): IBanner {
        this.animator = animator;
        return this
    }

    /**
     * 创建
     */
    private fun create(): IBanner {
        if (mActivity == null) throw NullPointerException("please bind FragmentActivity first!")
        val directory = File(bannerDirectory)
        if (!directory.exists()) {
            Log.d(IBanner.TAG, "default banners")
            defaultBanners?.forEach { addImage(it) }
        } else {
            val listFiles = directory.listFiles()
            if (listFiles == null || listFiles.isEmpty()) {
                Log.d(IBanner.TAG, "default banners")
                defaultBanners?.forEach { addImage(it) }
            } else {
                Log.d(IBanner.TAG, "user banners")
                listFiles.forEach {
                    if (Identify.identify(it.absolutePath) == IBanner.IMAGE)
                        addImage(it.absolutePath)
                    else if (Identify.identify(it.absolutePath) == IBanner.VIDEO)
                        addVideo(it.absolutePath)
                }
            }
        }
        Log.d(IBanner.TAG, "banner size [before make up]: ${mData.size}")
        makeUp()
        Log.d(IBanner.TAG, "banner size [after make up]: ${mData.size}")
        mData.forEach { data ->
            val bundle = Bundle()
            bundle.putLong("interval", interval)
            when (data.data) {
                is Int -> bundle.putInt("intData", data.data as Int)
                is String -> bundle.putString("stringData", data.data as String)
            }
            val fragment: IFragment = when (data.type) {
                IBanner.IMAGE -> ImageFragment()
                IBanner.VIDEO -> VideoFragment()
                else -> ErrorFragment()
            }
            fragment.arguments = bundle
            fragment.bindBannerController(this)
            fragments.add(fragment)
        }
        try {
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
        if (mData.isEmpty() || mData.size >= DEFAULT_BANNER_SIZE) {
            return
        } else {
            val temp = mData
            mData.addAll(temp)
            makeUp()
        }
    }

    /**
     * 添加图片
     */
    override fun addImage(res: Int): IBanner {
        mData.add(IModel(res, IBanner.IMAGE))
        return this
    }

    /**
     * 添加图片
     */
    override fun addImage(path: String): IBanner {
        mData.add(IModel(path, IBanner.IMAGE))
        return this
    }

    /**
     * 添加视频
     */
    override fun addVideo(path: String): IBanner {
        mData.add(IModel(path, IBanner.VIDEO))
        return this
    }

    /**
     * 设置Banner 文件路径
     */
    override fun directory(directory: String): IBanner {
        this.bannerDirectory = directory
        return this
    }

    override fun start() {
        isPlay = true
        visibility = View.VISIBLE
        create()
    }

    /**
     * 暂停动画
     */
    override fun stop() {
        isPlay = false
        visibility = View.GONE
        fragments.clear()
        mData.clear()
        adapter?.notifyDataSetChanged()
    }

    /**
     * 释放资源
     */
    override fun release() {
        fragments.clear()
        mData.clear()
        mActivity = null
    }

    /**
     * 刷新
     */
    override fun refreshData() {
        fragments.clear()
        mData.clear()
        create()
        adapter?.notifyDataSetChanged()
        next()
    }

    override fun turnDuration(duration: Int): IBanner {
        turnDuration = duration
        return this
    }
}