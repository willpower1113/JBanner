package com.willpower.banner

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream


class CompressHelper {

    companion object {

        @JvmStatic
        fun compress(context: Context?, target: File, iLogger: ILogger?): File? {
            if (context == null){
                iLogger?.i(BuildConfig.TAG,"[compress] context can not be null")
                return null
            }
            iLogger?.i(BuildConfig.TAG, "request width：${JBanner.displayWidth} , request height：${JBanner.displayHeight}")
            // 配置压缩的参数
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true //获取当前图片的边界大小，而不是将整张图片载入在内存中，避免内存溢出
            BitmapFactory.decodeFile(target.absolutePath, options)
            options.inJustDecodeBounds = false
            ////inSampleSize的作用就是可以把图片的长短缩小inSampleSize倍，所占内存缩小inSampleSize的平方
            options.inSampleSize = compressSize(options, JBanner.displayWidth, JBanner.displayHeight)
            val compressDir = File(target.parentFile, "compress")
            if (!compressDir.exists()) compressDir.mkdirs()
            val compress = File(compressDir, "compress_${options.inSampleSize}_${target.name}")
            if (!compress.exists()) {
                Log.i(BuildConfig.TAG, "原图：${options.outWidth} * ${options.outHeight} , 压缩比例：${options.inSampleSize}")
                val bm = BitmapFactory.decodeFile(target.absolutePath, options) // 解码文件
                val bos = BufferedOutputStream(FileOutputStream(compress))
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                bos.flush()
                bos.close()
            }
            return compress
        }

        /**
         * 计算出所需要压缩的大小
         *
         * @param options
         * @param reqWidth  我们期望的图片的宽，单位px
         * @param reqHeight 我们期望的图片的高，单位px
         * @return
         */
        private fun compressSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            var sampleSize = 1
            val picWidth = options.outWidth
            val picHeight = options.outHeight
            if (picWidth > reqWidth || picHeight > reqHeight) {
                val halfPicWidth = picWidth / 2
                val halfPicHeight = picHeight / 2
                while (halfPicWidth / sampleSize > reqWidth || halfPicHeight / sampleSize > reqHeight) {
                    sampleSize *= 2
                }
            }
            return sampleSize
        }
    }
}