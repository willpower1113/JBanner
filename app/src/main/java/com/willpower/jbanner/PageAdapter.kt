package com.willpower.jbanner

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.willpower.widget.JButton

class PageAdapter(private var size: Int) : RecyclerView.Adapter<PageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("JStyle", "onCreateViewHolder")
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_base, parent, false))
    }

    override fun onBindViewHolder(holder: PageAdapter.ViewHolder, position: Int) {
        Log.i("JStyle", "onBindViewHolder")
        holder.mTv.text = "$position"
    }

    override fun getItemCount(): Int {
        return size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTv: JButton = itemView.findViewById(R.id.mTv)
    }

}