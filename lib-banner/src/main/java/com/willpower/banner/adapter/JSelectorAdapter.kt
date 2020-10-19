package com.willpower.banner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.willpower.banner.R
import com.willpower.banner.model.SelectorModel

class JSelectorAdapter(private val mContext: Context, private val mData: ArrayList<SelectorModel<Any>>) : RecyclerView.Adapter<JSelectorAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvSelector: TextView = v.findViewById(R.id.tvSelector)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_selector, parent, false))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selector: SelectorModel<Any> = mData[position]
        holder.tvSelector.text = selector.content()
    }
}