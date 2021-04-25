package com.willpower.banner.item

import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.willpower.banner.*
import java.io.File


class ImageFragment : IFragment() {
    private lateinit var mPicturePlayer: ImageView
    private lateinit var root: View
    private var mBanner: IBanner? = null
    private var interval = 10000L
    private val mHandler: Handler = Handler(Looper.getMainLooper())

    private val mRunnable: Runnable = Runnable {
        mBanner?.next()
        if (BuildConfig.DEBUG)
            mBanner?.getLogger()?.i(BuildConfig.TAG, "image to next: $data")
    }

    private var data: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_banner_image, container, false)
        mPicturePlayer = root.findViewById(R.id.mPicturePlayer)
        interval = arguments!!.getLong("interval")
        data = arguments!!.getString("stringData")
        if (data == null) {
            var resId = arguments!!.getInt("intData")
            mPicturePlayer.setImageResource(resId)
        } else {
            setImageBitmap(data!!)
        }
        return root
    }


    /**
     *将文件压缩后覆盖源文件
     */
    private fun setImageBitmap(path: String) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute {
            val compress = CompressHelper.compress(context, File(path), mBanner?.getLogger())
            mBanner?.getLogger()?.i(BuildConfig.TAG, "压缩结果：${compress?.absolutePath}")
            if (compress != null) {
                val bmp = BitmapFactory.decodeFile(compress.absolutePath)
                mBanner?.getLogger()?.i(BuildConfig.TAG, "bitmap：${bmp != null}")
                mHandler.post { mPicturePlayer.setImageBitmap(bmp) }
            }
        }
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