package com.willpower.jbanner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    fun toPageJBanner(v: View) {
        startActivity(Intent(this, PageJBanner::class.java))
    }

    fun toPageJLamp(v: View) {
        //显示导航栏
        ShellUtil.exec("LD_LIBRARY_PATH=/vendor/lib:/system/lib am startservice -n com.android.systemui/.SystemUIService")
    }

    fun toPageJButton(v: View) {
        //隐藏导航栏
        ShellUtil.exec("LD_LIBRARY_PATH=/vendor/lib:/system/lib service call activity 42 s16 com.android.systemui")
    }

    fun toPageJTextView(v: View) {
        ShellUtil.exec("screencap -p /sdcard/screen.png")
    }

    fun toPageJEditText(v: View) {
        startActivity(Intent(this, PageJStyle::class.java))
    }

    fun toPageJImageView(v: View) {
    }

    fun toPageJPopup(v: View) {
        startActivity(Intent(this, PopupActivity::class.java))
    }
}