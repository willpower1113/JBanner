package com.willpower.banner.model

/**
 * Banner 数据模型
 */
interface BannerModel<E> {

    companion object {
        const val video: Int = 1
        const val image: Int = 2
    }

    fun resource(): E?;

    fun content(): String?

    fun link(): String?

    fun type(): Int?
}