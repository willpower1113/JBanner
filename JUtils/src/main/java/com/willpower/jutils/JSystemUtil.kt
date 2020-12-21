package com.willpower.jutils

import android.Manifest.permission.*
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.support.annotation.IntRange
import android.support.annotation.RequiresApi
import android.support.annotation.RequiresPermission
import android.view.WindowManager
import android.Manifest.permission.ACCESS_WIFI_STATE as ACCESS_WIFI_STATE1


/**
 * 屏幕相关
 */
class JSystemUtil {
    companion object {
        /*****************************************屏幕亮度*******************************************/
        /**
         * 是否是自动亮度
         *
         * @param context
         * @return
         */
        @JvmStatic
        fun isSystemAutoBrightness(context: Context): Boolean {
            return Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, 0) ==
                    Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
        }

        /**
         * 开启/关闭自动亮度
         *
         * @return
         */
        @JvmStatic
        fun setSystemAutoBrightness(context: Context, isAuto: Boolean) {
            if (isAuto) Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) else Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)
        }

        /**
         * 获取系统亮度值
         *
         * @param context
         * @return
         */
        @JvmStatic
        fun getSystemBrightness(context: Context): Int {
            return Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 255)
        }

        /**
         * 设置系统亮度值【注：正常范围0~255，但是许多手机亮度值远远超过255，例如 Mi10 可以设置4000，所以这里没有上限值，建议取值范围0~255】
         *
         * @param context
         * @param value
         */
        @JvmStatic
        @RequiresPermission(WRITE_SETTINGS)
        fun setSystemBrightness(context: Context, value: Int) {
            if (isSystemAutoBrightness(context)) {
                setSystemAutoBrightness(context, false)
            }
            Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, value)
            context.contentResolver.notifyChange(Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), null)
        }

        /**
         * 获取当前界面亮度值
         *
         * @param activity
         * @return
         */
        @JvmStatic
        fun getWindowBrightness(activity: Activity): Float {
            val window = activity.window
            val layoutParams = window.attributes
            return if (layoutParams.screenBrightness == WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE)
                getSystemBrightness(activity).toFloat()
            else layoutParams.screenBrightness
        }

        /**
         * 设置当前界面亮度值
         *
         * @param activity
         * @return
         */
        @JvmStatic
        fun setWindowBrightness(activity: Activity, @IntRange(from = 0, to = 255) value: Int) {
            val window = activity.window
            val layoutParams = window.attributes
            layoutParams.screenBrightness = value / 255f
            window.attributes = layoutParams
        }

        /*****************************************设备震动*******************************************/
        /**
         * 震动
         */
        @JvmStatic
        @RequiresPermission(VIBRATE)
        fun vibrator(context: Context, duration: Long) {
            (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(duration)
        }

        @JvmStatic
        @RequiresApi(api = Build.VERSION_CODES.O)
        @RequiresPermission(VIBRATE)
        fun vibrator(context: Context, vibe: VibrationEffect?) {
            (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(vibe)
        }

        /*****************************************系统音量*******************************************/
        /**
         * 设置音量
         *
         * @param context
         * @param streamType 通道
         * @param value      音量
         */
        @JvmStatic
        fun setVolume(context: Context, streamType: Int, value: Int) {
            (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).setStreamVolume(streamType, value, AudioManager.FLAG_PLAY_SOUND)
        }

        /**
         * 获取最大音量
         *
         * @param context
         * @return the  max volume value
         */
        @JvmStatic
        fun getMaxVolume(context: Context, streamType: Int): Int {
            return (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).getStreamMaxVolume(streamType)
        }

        /**
         * 设置音乐音量
         *
         * @param context
         * @param value
         */
        @JvmStatic
        fun setMusicVolume(context: Context, value: Int) {
            (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).setStreamVolume(AudioManager.STREAM_MUSIC, value, AudioManager.FLAG_PLAY_SOUND)
        }

        /**
         * 设置闹钟音量
         *
         * @param context
         * @param value
         */
        @JvmStatic
        fun setAlarmVolume(context: Context, value: Int) {
            (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).setStreamVolume(AudioManager.STREAM_ALARM, value, AudioManager.FLAG_PLAY_SOUND)
        }

        /**
         * 设置通知音量
         *
         * @param context
         * @param value
         */
        @JvmStatic
        fun setNotificationVolume(context: Context, value: Int) {
            (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).setStreamVolume(AudioManager.STREAM_NOTIFICATION, value, AudioManager.FLAG_PLAY_SOUND)
        }

        /*****************************************WIFI*******************************************/
        /**
         * 打开关闭WIFI
         */
        @JvmStatic
        @RequiresPermission(CHANGE_WIFI_STATE)
        fun setWifiOnOrOff(context: Context, on: Boolean) {
            if (on) openWifi(context) else closeWifi(context)
        }

        @JvmStatic
        @RequiresPermission(CHANGE_WIFI_STATE)
        fun openWifi(context: Context) {
            (context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).isWifiEnabled = true
        }

        @JvmStatic
        @RequiresPermission(CHANGE_WIFI_STATE)
        fun closeWifi(context: Context) {
            (context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).isWifiEnabled = false
        }

        /**
         *  获取WIFI状态
         *  1.WIFI_STATE_DISABLED：禁用状态
         *  2.WIFI_STATE_DISABLING：正在禁用中
         *  3.WIFI_STATE_ENABLED：启用状态
         *  4.WIFI_STATE_ENABLING：开启中
         *  5.WIFI_STATE_UNKNOWN
         */
        @JvmStatic
        @RequiresPermission(ACCESS_WIFI_STATE1)
        fun getWifiState(context: Context) {
            (context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).wifiState
        }

        /*****************************************蓝牙*******************************************/

        /**
         * 开启蓝牙
         */
        @JvmStatic
        @RequiresPermission(BLUETOOTH_ADMIN)
        fun openBluetooth() {
            BluetoothAdapter.getDefaultAdapter().enable()
        }

        /**
         * 关闭蓝牙
         */
        @JvmStatic
        @RequiresPermission(BLUETOOTH_ADMIN)
        fun closeBluetooth() {
            BluetoothAdapter.getDefaultAdapter().disable()
        }
    }
}