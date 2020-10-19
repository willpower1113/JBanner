package com.willpower.banner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.willpower.banner.R
import com.willpower.banner.model.LampModel

class JLampAdapter(private val mContext: Context, private val textSize: Float,
                   private val textColor: Int, private val mData: ArrayList<LampModel>)
    : RecyclerView.Adapter<JLampAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_lamp, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val banner: LampModel = mData[position]
        holder.tvLamp.text = banner.text()
        holder.tvLamp.textSize = textSize
        holder.tvLamp.setTextColor(textColor)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvLamp: TextView = v.findViewById(R.id.tvLamp)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

}