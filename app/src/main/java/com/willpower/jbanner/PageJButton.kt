package com.willpower.jbanner

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_jbutton.*
import java.text.SimpleDateFormat
import java.util.*

class PageJButton : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jbutton)
        test.setOnClickListener {
            Toast.makeText(this@PageJButton, "点击！", Toast.LENGTH_SHORT).show()
        }
    }

    fun onTest(v: View) {
        Toast.makeText(this, SimpleDateFormat("YYYY年MM月dd日 hh:mm:ss").format(Date()), Toast.LENGTH_SHORT).show()
    }
}