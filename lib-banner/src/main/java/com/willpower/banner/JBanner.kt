package com.willpower.banner

import android.content.Context
import android.util.AttributeSet
import com.willpower.banner.adapter.JBannerAdapter
import com.willpower.banner.model.BannerModel

/**
 * 轮播图
 */
class JBanner : IRecycler<BannerModel<Any>> {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun create() {
        super.create()
        adapter = JBannerAdapter(context, mData)
    }
}