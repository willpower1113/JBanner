package com.willpower.banner

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

internal class WheelPickerLayoutManager(mContext: Context) : LinearLayoutManager(mContext) {

    override fun setOrientation(orientation: Int) {
        super.setOrientation(VERTICAL)
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        for (i in 0..childCount) {
            val child = getChildAt(i)
            if (child != null) {
                (child as TextView).setTextColor(Color.RED)
            }
        }
        val focused = focusedChild
        if (focused != null) {
            Log.e("测试","focused != null")
            (focused as TextView).setTextColor(Color.BLACK)
        }else{
            Log.e("测试","focused == null")
        }
    }

    init {
        orientation = VERTICAL
    }
}