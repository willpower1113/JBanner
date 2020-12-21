package com.willpower.banner.item

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.willpower.banner.IBanner
import com.willpower.banner.IFragment
import com.willpower.banner.R

class ImageFragment : IFragment() {
    private var mPicturePlayer: ImageView? = null
    private var root: View? = null
    private var mBanner: IBanner? = null
    private var interval = 10000L
    private val mHandler: Handler = Handler(Looper.getMainLooper())
    private val mRunnable: Runnable = Runnable {
        mBanner!!.next()
        Log.e(IBanner.TAG, "image to next: $data")
    }
    private var data: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_banner_image, container, false)
        mPicturePlayer = root?.findViewById(R.id.mPicturePlayer)
        interval = arguments!!.getLong("interval")
        data = arguments!!.getString("stringData")
        if (data == null) {
            var resId = arguments!!.getInt("intData")
            mPicturePlayer?.setImageResource(resId)
        } else {
            mPicturePlayer?.setImageBitmap(BitmapFactory.decodeFile(data))
        }
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