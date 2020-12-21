package com.willpower.jbanner

import android.content.Intent
import android.os.Bundle
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
    }

    fun toPageJButton(v: View) {
        startActivity(Intent(this, PageJButton::class.java))
    }

    fun toPageJTextView(v: View) {
        startActivity(Intent(this, PageJTextView::class.java))
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