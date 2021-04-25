package com.willpower.banner

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.willpower.banner.IBanner.Companion.DEFAULT_BANNER_SIZE
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
    private var mLogger: ILogger? = null
    private var bannerDirectory: String = ""
    private var defaultBanners: IntArray? = null
    private var childClickable = false
    private val fragments = arrayListOf<Fragment>()
    private var mListener: OnClickListener? = null
    private var clickTime = 0L

    companion object {
        var displayWidth = 500
        var displayHeight = 500
    }

    init {
        post {
            if (width != 0)
                displayWidth = width
            if (height != 0)
                displayHeight = height
        }
    }

    @Volatile
    private var isPlay: Boolean = false

    override fun bind(activity: FragmentActivity): JBanner {
        mActivity = activity
        return this
    }

    override fun logger(iLogger: ILogger): IBanner {
        mLogger = iLogger
        return this
    }

    override fun getLogger(): ILogger? {
        return mLogger
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
                    if (System.currentTimeMillis() - clickTime >= 10L) {
                        if (BuildConfig.DEBUG)
                            mLogger?.i(BuildConfig.TAG, "click banner")
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

    override fun clickListener(listener: OnClickListener): JBanner {
        mListener = listener
        return this
    }

    /**
     * 默认Banner图
     */
    fun defaultBanners(vararg defaultBanners: Int): JBanner {
        this.defaultBanners = defaultBanners
        return this
    }


    /**
     * 循环周期
     */
    override fun interval(interval: Long): JBanner {
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
        if (BuildConfig.DEBUG)
            mLogger?.i(BuildConfig.TAG, "play now: ${currentItem % mData.size}")
    }

    /**
     * 设置动画
     */
    override fun animator(animator: IBanner.Animator): JBanner {
        this.animator = animator;
        return this
    }

    /**
     * 创建
     */
    private fun create(): JBanner {
        if (mActivity == null) throw NullPointerException("please bind FragmentActivity first!")
        val directory = File(bannerDirectory)
        if (!directory.exists()) {
            if (BuildConfig.DEBUG)
                mLogger?.i(BuildConfig.TAG, "default banners")
            defaultBanners?.forEach { addImage(it) }
        } else {
            var count = 0
            val listFiles = directory.listFiles()
            listFiles?.forEach { if (it.isFile) count++ }
            if (count == 0) {
                if (BuildConfig.DEBUG)
                    mLogger?.i(BuildConfig.TAG, "default banners")
                defaultBanners?.forEach { addImage(it) }
            } else {
                if (BuildConfig.DEBUG)
                    mLogger?.i(BuildConfig.TAG, "user banners")
                listFiles.forEach {
                    if (Identify.identify(it.absolutePath) == IBanner.IMAGE)
                        addImage(it.absolutePath)
                    else if (Identify.identify(it.absolutePath) == IBanner.VIDEO)
                        addVideo(it.absolutePath)
                }
            }
        }
        mLogger?.i(BuildConfig.TAG, "banner size [before make up]: ${mData.size}")
        makeUp()
        mLogger?.i(BuildConfig.TAG, "banner size [after make up]: ${mData.size}")
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
        if (mData.isEmpty() || mData.size >= DEFAULT_BANNER_SIZE) {
            return
        } else if (mData.size == 1) {
            return //如果只有一张Banner图，不进行补偿
        } else {
            val temp = mData
            mData.addAll(temp)
            makeUp()
        }
    }

    /**
     * 添加图片
     */
    fun addImage(res: Int): JBanner {
        mData.add(IModel(res, IBanner.IMAGE))
        return this
    }

    /**
     * 添加图片
     */
    fun addImage(path: String): JBanner {
        mData.add(IModel(path, IBanner.IMAGE))
        return this
    }

    /**
     * 添加视频
     */
    fun addVideo(path: String): JBanner {
        mData.add(IModel(path, IBanner.VIDEO))
        return this
    }

    /**
     * 设置Banner 文件路径
     */
    fun directory(directory: String): JBanner {
        this.bannerDirectory = directory
        return this
    }

    /**
     * 开启轮播
     */
    override fun start() {
        if (mData.size == 1) return
        if (isPlay) return
        isPlay = true
        create()
    }

    /**
     * 判断显示状态
     */
    override fun isShowOnScreen(): Boolean {
        return visibility == View.VISIBLE
    }

    /**
     * 判断 轮播 状态
     */
    override fun isPlaying(): Boolean {
        return isPlay
    }

    /**
     * 暂停动画
     */
    override fun stop() {
        if (mData.size == 1) return
        if (!isPlay) return
        isPlay = false
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