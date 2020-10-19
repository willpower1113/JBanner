package com.willpower.jbanner

import com.willpower.banner.model.SelectorModel

class MySelector<E>(private var content: String?, private var value: E?) : SelectorModel<E> {
    override fun content(): String? {
        return content
    }

    override fun value(): E? {
        return value
    }

}