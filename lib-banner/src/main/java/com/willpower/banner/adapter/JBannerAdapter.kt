package com.willpower.banner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.willpower.banner.R
import com.willpower.banner.model.BannerModel

class JBannerAdapter(private val mContext: Context, private val mData: ArrayList<BannerModel<Any>>) : RecyclerView.Adapter<JBannerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_banner, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val banner: BannerModel<Any> = mData[position]
        Glide.with(mContext)
                .load(banner.resource())
                .into(holder.img)
        if (banner.content() != null) {
            holder.tv.visibility = View.VISIBLE
            holder.tv.text = banner.content()
        } else holder.tv.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tv: TextView = v.findViewById(R.id.tvDetail)
        var img: ImageView = v.findViewById(R.id.imageBanner)
    }

}