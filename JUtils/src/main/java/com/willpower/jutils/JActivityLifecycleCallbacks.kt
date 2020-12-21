package com.willpower.jutils

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log

/**
 * Activity 生命周期监听
 */
open class JActivityLifecycleCallbacks : ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.d(TAG, "${activity.javaClass.simpleName} on Created")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d(TAG, "${activity.javaClass.simpleName} on Started")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d(TAG, "${activity.javaClass.simpleName} on Resumed")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.d(TAG, "${activity.javaClass.simpleName} on Paused")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.d(TAG, "${activity.javaClass.simpleName} on Stopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.d(TAG, "${activity.javaClass.simpleName} on SaveInstanceState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d(TAG, "${activity.javaClass.simpleName} on Destroyed")
    }

    companion object {
        private const val TAG = JBuildConfig.TAG
    }

}