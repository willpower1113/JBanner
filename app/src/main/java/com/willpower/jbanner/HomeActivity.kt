package com.willpower.jbanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    fun toPageJBanner(v: View) {}
    fun toPageJLamp(v: View) {}
    fun toPageJButton(v: View) {
        startActivity(Intent(this, PageJButton::class.java))
    }

    fun toPageJEditText(v: View) {}
    fun toPageJImageView(v: View) {}

}