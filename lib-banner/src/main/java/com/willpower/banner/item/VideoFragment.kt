package com.willpower.banner.item

import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import com.willpower.banner.IBanner
import com.willpower.banner.IFragment
import com.willpower.banner.Identify
import com.willpower.banner.R
import java.io.File
import java.io.FileInputStream

class VideoFragment : IFragment(), TextureView.SurfaceTextureListener {
    private var mBanner: IBanner? = null
    private var mTextureView: TextureView? = null
    private var mMediaPlayer: MediaPlayer? = null
    private var data: String? = null;
    private var interval = 10000L
    private val mHandler: Handler = Handler(Looper.getMainLooper())
    private val mRunnable: Runnable = Runnable {
        mBanner!!.next()
        Log.e(IBanner.TAG, "video to next: $data")
    }
    private var isPrepared = false;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_banner_video, container, false)
        mTextureView = root?.findViewById(R.id.mTextureView)
        mTextureView?.surfaceTextureListener = this
        interval = arguments!!.getLong("interval")
        data = arguments!!.getString("stringData")
        return root
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isPrepared)
            mMediaPlayer?.let { if (!it.isPlaying) it.start() }
        else
            mHandler.removeCallbacks(mRunnable)
    }

    private fun initMedia(surface: SurfaceTexture) {
        try {
            mMediaPlayer = MediaPlayer()
            mMediaPlayer?.setSurface(Surface(surface))
            when {
                Identify.isFile(data!!) -> {
                    val fis = FileInputStream(File(data!!))
                    mMediaPlayer?.setDataSource(fis.fd, 0, 0x7ffffffffffffffL)
                }
                else -> {
                    val afd = activity!!.assets.openFd(data!!)
                    mMediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.declaredLength)
                }
            }
            mMediaPlayer?.setOnPreparedListener {
                Log.d(IBanner.TAG, "video prepared：[$data]")
                isPrepared = true
                mMediaPlayer?.seekTo(0)
                if (userVisibleHint)
                    mMediaPlayer?.start()
            }
            mMediaPlayer?.setOnCompletionListener {
                Log.d(IBanner.TAG, "video play complete[$data]")
                mHandler.postDelayed(mRunnable, 1000L)
            }
            mMediaPlayer?.prepareAsync()
        } catch (e: Exception) {
            Log.e(IBanner.TAG, "video exception[$data]", e)
            mHandler.postDelayed(mRunnable, interval)
        }
    }

    override fun onDestroyView() {
        mHandler.removeCallbacks(mRunnable)
        mMediaPlayer?.stop()
        mMediaPlayer?.release()
        mMediaPlayer = null
        super.onDestroyView()
    }

    override fun bindBannerController(iBanner: IBanner) {
        mBanner = iBanner
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        surface?.release()
        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        initMedia(surface)
    }
}