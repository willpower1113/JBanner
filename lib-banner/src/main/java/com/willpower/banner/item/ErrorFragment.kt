package com.willpower.banner.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.willpower.banner.IBanner
import com.willpower.banner.IFragment
import com.willpower.banner.R

class ErrorFragment : IFragment() {
    private var root: View? = null
    private var mBanner: IBanner? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_banner_error, container, false)
        return root
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){
            mBanner?.next()
        }
    }

    override fun bindBannerController(iBanner: IBanner) {
        this.mBanner = iBanner
    }
}