package com.willpower.banner.item

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.willpower.banner.BuildConfig
import com.willpower.banner.IBanner
import com.willpower.banner.IFragment
import com.willpower.banner.R

class TextFragment : IFragment() {
    private var mDisplayView: TextView? = null
    private var root: View? = null
    private var mBanner: IBanner? = null
    private var interval = 10000L
    private val mHandler: Handler = Handler(Looper.getMainLooper())
    private val mRunnable: Runnable = Runnable {
        mBanner!!.next()
        Log.e(BuildConfig.TAG, "text to next: $data")
    }
    private var data: String? = null
    private var mTextSize: Float = 18f
    private var mTextColor: Int = Color.BLACK

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_banner_text, container, false)
        mDisplayView = root?.findViewById(R.id.mDisplayView)
        interval = arguments!!.getLong("interval")
        data = arguments!!.getString("stringData")
        mTextSize = arguments!!.getFloat("textSize")
        mTextColor = arguments!!.getInt("textColor")
        mDisplayView?.text = data
        mDisplayView?.textSize = mTextSize
        mDisplayView?.setTextColor(mTextColor)
        return root
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser)
            mHandler.postDelayed(mRunnable, interval)
        else
            mHandler.removeCallbacks(mRunnable)
    }

    override fun bindBannerController(iBanner: IBanner) {
        this.mBanner = iBanner
    }

    override fun onDestroyView() {
        mHandler.removeCallbacks(mRunnable)
        super.onDestroyView()
    }
}