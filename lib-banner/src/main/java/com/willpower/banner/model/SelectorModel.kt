package com.willpower.banner.model

/**
 * Selector 数据模型
 */
interface SelectorModel<E> {
    fun content(): String?
    fun value(): E?
}