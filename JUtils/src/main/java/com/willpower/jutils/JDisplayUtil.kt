package com.willpower.jutils

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * 显示 相关
 */
class JDisplayUtil {
    companion object {
        const val KEYBOARD_VISIBLE_THRESHOLD_DP = 100

        /**
         * 获取屏幕参数
         *
         * @return 屏幕右下角坐标，相当于屏幕尺寸，包含导航栏，状态栏，坐标值会根据屏幕方向改变
         */
        @JvmStatic
        fun getDisplayPoint(activity: Activity): Point {
            val p = Point()
            activity.windowManager.defaultDisplay.getRealSize(p)
            return p
        }

        /**
         * 获取屏幕尺寸【不包含导航栏，状态栏高度】
         *
         * @param context
         * @return 宽
         */
        @JvmStatic
        fun getScreenWidth(context: Context): Int {
            return context.resources.displayMetrics.widthPixels
        }

        /**
         * 获取屏幕尺寸 【不包含导航栏，状态栏高度】
         *
         * @param context
         * @return 高
         */
        @JvmStatic
        fun getScreenHeight(context: Context): Int {
            return context.resources.displayMetrics.heightPixels
        }

        /**
         * 屏幕密度
         *
         * @param context
         * @return
         */
        @JvmStatic
        fun getDensity(context: Context): Float {
            return context.resources.displayMetrics.density
        }

        /**
         * 屏幕密度【文字】
         *
         * @param context
         * @return
         */
        @JvmStatic
        fun getFontDensity(context: Context): Float {
            return context.resources.displayMetrics.scaledDensity
        }

        /**
         * 单位转换：dp -> px
         *
         * @param context
         * @param dp
         * @return px value
         */
        @JvmStatic
        fun dp2px(context: Context, dp: Int): Int {
            return (getDensity(context) * dp + 0.5).toInt()
        }

        /**
         * 单位转换：sp -> px
         *
         * @param context
         * @param sp
         * @return px value
         */
        @JvmStatic
        fun sp2px(context: Context, sp: Int): Int {
            return (getFontDensity(context) * sp + 0.5).toInt()
        }

        /**
         * 单位转换：px -> dp
         *
         * @param context
         * @param px
         * @return dp value
         */
        @JvmStatic
        fun px2dp(context: Context, px: Int): Int {
            return (px / getDensity(context) + 0.5).toInt()
        }

        /**
         * 单位转换：px -> sp
         *
         * @param context
         * @param px
         * @return sp value
         */
        @JvmStatic
        fun px2sp(context: Context, px: Int): Int {
            return (px / getFontDensity(context) + 0.5).toInt()
        }

        /**
         * 沉浸式状态栏
         *
         * @param activity
         */
        @JvmStatic
        fun translucentStatusBar(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                val flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                val window = activity.window
                val attributes = window.attributes
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    attributes.flags = attributes.flags or flagTranslucentNavigation
                    window.attributes = attributes
                    window.statusBarColor = Color.TRANSPARENT
                } else {
                    attributes.flags = attributes.flags or (flagTranslucentStatus or flagTranslucentNavigation)
                    window.attributes = attributes
                }
            }
        }

        /**
         * 获取状态栏高度
         *
         * @param context
         * @return
         */
        @JvmStatic
        fun getStatusHeight(context: Context): Int {
            return try {
                context.resources
                        .getDimensionPixelSize(context.resources.getIdentifier("status_bar_height", "dimen", "android"))
            } catch (e: Exception) {
                0
            }
        }

        /**
         * 针对给定的editText显示软键盘（editText会先获得焦点）
         *
         * @param editText
         */
        @JvmStatic
        @JvmOverloads
        fun showKeyboard(editText: EditText?, delay: Int = 0) {
            if (null == editText) return
            if (!editText.requestFocus()) {
                //EditText 无法获取焦点
                return
            }
            if (delay > 0) {
                editText.postDelayed(Runnable {
                    val imm = editText.context.applicationContext
                            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
                }, delay.toLong())
            } else {
                val imm = editText.context.applicationContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        /**
         * 隐藏软键盘
         *
         * @param view
         * @return
         */
        @JvmStatic
        fun hideKeyboard(view: View?): Boolean {
            if (null == view) return false
            val inputManager = view.context.applicationContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            // 即使当前焦点不在editText，也是可以隐藏的。
            return inputManager.hideSoftInputFromWindow(view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
        }

        /**
         * 注册键盘监听
         *
         * @param activity
         * @param listener
         */
        @JvmStatic
        fun setVisibilityEventListener(activity: Activity?, listener: KeyboardVisibilityEventListener?) {
            if (activity == null) return
            if (listener == null) return
            val activityRoot = activity.findViewById<View>(R.id.content)
            val layoutListener: OnGlobalLayoutListener = object : OnGlobalLayoutListener {
                private val r = Rect()
                private val visibleThreshold = Math.round(dp2px(activity, KEYBOARD_VISIBLE_THRESHOLD_DP).toFloat())
                private var wasOpened = false
                override fun onGlobalLayout() {
                    activityRoot.getWindowVisibleDisplayFrame(r)
                    val heightDiff = activityRoot.rootView.height - r.height()
                    val isOpen = heightDiff > visibleThreshold
                    if (isOpen == wasOpened) return  //keyboard state has not changed
                    wasOpened = isOpen
                    val removeListener = listener.onVisibilityChanged(isOpen, heightDiff)
                    if (removeListener) {
                        activityRoot.viewTreeObserver
                                .removeOnGlobalLayoutListener(this)
                    }
                }
            }
            activityRoot.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
            activity.application.registerActivityLifecycleCallbacks(object : JActivityLifecycleCallbacks() {
                override fun onActivityDestroyed(a: Activity) {
                    if (a === activity) {
                        activityRoot.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
                        a.application.unregisterActivityLifecycleCallbacks(this)
                    }
                }
            })
        }
    }

}