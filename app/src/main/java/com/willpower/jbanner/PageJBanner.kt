package com.willpower.jbanner

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.willpower.banner.IBanner
import kotlinx.android.synthetic.main.activity_jbanner.*

class PageJBanner : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBar()
        setContentView(R.layout.activity_jbanner)
        mBanner.bind(this)
                .defaultBanners(R.drawable.b2a, R.drawable.b2b, R.drawable.b2c, R.drawable.b2d, R.drawable.b2e)
                .directory("${Environment.getExternalStorageDirectory()}/Xingrui/banner")
                .interval(3000L)
                .turnDuration(700)
                .animator(IBanner.Animator.TRANSITION3D)
                .clickListener(View.OnClickListener { Toast.makeText(this@PageJBanner, "点击", Toast.LENGTH_SHORT).show() })
    }

    private fun statusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val flag = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window?.decorView?.systemUiVisibility = flag
            window?.statusBarColor = Color.TRANSPARENT
            window?.navigationBarColor = Color.TRANSPARENT
        } else {
            window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    override fun onResume() {
        super.onResume()
        mBanner.start()
    }

    override fun onPause() {
        super.onPause()
        mBanner.stop()
    }

    override fun onDestroy() {
        mBanner.release()
        super.onDestroy()
    }
}