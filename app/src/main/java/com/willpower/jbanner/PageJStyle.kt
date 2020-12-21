package com.willpower.jbanner

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import com.willpower.style.HorizontalSelectedLayoutManager

class PageJStyle : AppCompatActivity() {
    private var testList: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_style)
        testList = findViewById(R.id.testList)
        testList?.layoutManager = HorizontalSelectedLayoutManager()
        testList?.adapter = PageAdapter(9)
//        PagerSnapHelper().attachToRecyclerView(testList)
    }
}