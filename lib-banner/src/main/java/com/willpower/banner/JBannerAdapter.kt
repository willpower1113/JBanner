package com.willpower.banner

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

internal class JBannerAdapter(manager: FragmentManager, private var mData: List<Fragment>) : FragmentPagerAdapter(manager) {

    override fun getCount(): Int {
        return if (mData.isEmpty()) 0 else if (mData.size == 1) 1 else Int.MAX_VALUE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return try {
            super.instantiateItem(container, position % mData.size)
        } catch (e: Exception) {
            super.instantiateItem(container, 0)
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        try {
            super.destroyItem(container, position % mData.size, any)
        } catch (e: Exception) {
            super.destroyItem(container, 0, any)
        }
    }

    override fun getItem(position: Int): Fragment {
        return try {
            mData[position % mData.size]
        } catch (e: Exception) {
            mData[0]
        }
    }

    override fun getItemPosition(any: Any): Int {
        return POSITION_NONE
    }

    override fun getItemId(position: Int): Long {
        return try {
            mData[position % mData.size].hashCode().toLong()
        } catch (e: Exception) {
            mData[0].hashCode().toLong()
        }
    }

}