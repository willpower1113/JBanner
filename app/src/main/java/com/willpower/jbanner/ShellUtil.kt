package com.willpower.jbanner

import android.util.Log
import java.util.concurrent.TimeUnit

class ShellUtil {
    companion object {
        private const val TAG = "ShellUtil"

        @JvmStatic
        fun exec(command: String): Boolean {
            return exec(command, 3L)
        }

        /**
         * @param command 命令行
         * @param timeout 超时时间，单位：秒
         */
        @JvmStatic
        fun exec(command: String, timeout: Long): Boolean {
            var process: Process? = null
            return try {
                process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
                val result = waitFor(process, timeout)
                Log.d(TAG, "Process: " + if (result) "success" else "fail")
                result
            } catch (e: Exception) {
                Log.d(TAG, "Process", e)
                false
            } finally {
                process?.destroy()
                Log.d(TAG, "Process destroy")
            }
        }

        @Throws(InterruptedException::class)
        private fun waitFor(process: Process, timeout: Long): Boolean {
            val startTime = System.nanoTime()
            var rem = TimeUnit.SECONDS.toNanos(timeout)
            do {
                try {
                    process.exitValue()
                    return true
                } catch (ex: Exception) {
                    if (rem > 0) Thread.sleep((TimeUnit.NANOSECONDS.toMillis(rem) + 1).coerceAtMost(100))
                }
                rem = TimeUnit.SECONDS.toNanos(timeout) - (System.nanoTime() - startTime)
            } while (rem > 0)
            return false
        }
    }

}