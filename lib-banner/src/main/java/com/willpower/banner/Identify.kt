package com.willpower.banner

import java.io.File

object Identify {
    fun identify(path: String): Int {
        return when (path.substring(path.lastIndexOf('.')).toLowerCase()) {
            ".jpg", ".jpeg", ".webp", ".png" -> IBanner.IMAGE
            ".mp4", ".wav", ".avi", ".flv" -> IBanner.VIDEO
            else -> -1
        }
    }

    fun isFile(path: String) : Boolean{
        return File(path).exists()
    }
}