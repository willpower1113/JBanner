package com.willpower.jutils

/**
 * 软键盘监听
 */
interface KeyboardVisibilityEventListener {
    fun onVisibilityChanged(isOpen: Boolean, height: Int): Boolean
}