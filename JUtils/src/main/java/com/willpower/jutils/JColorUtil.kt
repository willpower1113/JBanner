package com.willpower.jutils

import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.annotation.FloatRange

class JColorUtil {
    companion object {
        /**
         * 设置颜色透明度
         *
         * @param color
         * @param alpha 透明度
         * @return
         */
        @JvmStatic
        fun setColorAlpha(color: Int, alpha: Float): Int {
            return color and 0x00ffffff or (alpha * 0xff).toInt() shl 24
        }

        /**
         * 根据比例，在两个color值之间计算出一个color值
         * **注意该方法是ARGB通道分开计算比例的**
         *
         * @param fromColor 开始的color值
         * @param toColor   最终的color值
         * @param fraction  比例，取值为[0,1]，为0时返回 fromColor， 为1时返回 toColor
         * @return 计算出的color值
         */
        @JvmStatic
        fun computeColor(@ColorInt fromColor: Int, @ColorInt toColor: Int, @FloatRange(from = 0.0, to = 1.0) fraction: Float): Int {
            if (fraction >= 1f) return toColor
            if (fraction <= 0f) return fromColor
            val minColorA = Color.alpha(fromColor)
            val maxColorA = Color.alpha(toColor)
            val resultA = ((maxColorA - minColorA) * fraction).toInt() + minColorA
            val minColorR = Color.red(fromColor)
            val maxColorR = Color.red(toColor)
            val resultR = ((maxColorR - minColorR) * fraction).toInt() + minColorR
            val minColorG = Color.green(fromColor)
            val maxColorG = Color.green(toColor)
            val resultG = ((maxColorG - minColorG) * fraction).toInt() + minColorG
            val minColorB = Color.blue(fromColor)
            val maxColorB = Color.blue(toColor)
            val resultB = ((maxColorB - minColorB) * fraction).toInt() + minColorB
            return Color.argb(resultA, resultR, resultG, resultB)
        }

        /**
         * 给定颜色变暗
         *
         * @param color
         * @param fraction
         * @return
         */
        @JvmStatic
        fun changeColorToDark(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) fraction: Float): Int {
            return computeColor(color, Color.BLACK, fraction)
        }

        /**
         * 给定颜色变亮
         *
         * @param color
         * @param fraction
         * @return
         */
        @JvmStatic
        fun changeColorToLight(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) fraction: Float): Int {
            return computeColor(color, Color.WHITE, fraction)
        }

        /**
         * 将 color 颜色值转换为十六进制字符串
         *
         * @param color 颜色值
         * @return 转换后的字符串
         */
        @JvmStatic
        fun colorToString(@ColorInt color: Int): String {
            return String.format("#%08X", color)
        }
    }
}