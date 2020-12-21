package com.willpower.jutils

/**
 * 效验
 */
class JValidityUtil {
    companion object {
        @JvmStatic
        fun isEmpty(target: Collection<*>?): Boolean {
            return target == null || target.isEmpty()
        }

        @JvmStatic
        fun isEmpty(target: Map<*, *>?): Boolean {
            return target == null || target.isEmpty()
        }

        @JvmStatic
        fun isEmpty(target: Array<String?>?): Boolean {
            return target == null || target.isEmpty()
        }

        @JvmStatic
        fun isEmpty(target: LongArray?): Boolean {
            return target == null || target.isEmpty()
        }

        @JvmStatic
        fun isEmpty(target: IntArray?): Boolean {
            return target == null || target.isEmpty()
        }

        @JvmStatic
        fun isEmpty(target: FloatArray?): Boolean {
            return target == null || target.isEmpty()
        }

        @JvmStatic
        fun isEmpty(target: ShortArray?): Boolean {
            return target == null || target.isEmpty()
        }

        @JvmStatic
        fun isEmpty(target: CharArray?): Boolean {
            return target == null || target.isEmpty()
        }

        @JvmStatic
        fun isEmpty(target: ByteArray?): Boolean {
            return target == null || target.isEmpty()
        }
    }
}