package com.willpower.jutils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import java.util.*

/**
 * Activity 管理类
 */
object JActivityManager {
    private const val TAG = JBuildConfig.TAG
    private val mActivityGroup: MutableList<Activity>
    private var launchTime: Long = System.currentTimeMillis();
    private var isLaunched: Boolean = false

    fun init(application: Application) {
        Log.d(TAG, "create application")
        application.registerActivityLifecycleCallbacks(object : JActivityLifecycleCallbacks() {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                super.onActivityCreated(activity, savedInstanceState)
                addActivity(activity)
            }

            override fun onActivityResumed(activity: Activity) {
                super.onActivityResumed(activity)
                if (!isLaunched) {
                    Log.e(TAG, "application launch in: ${System.currentTimeMillis() - launchTime} ms")
                }
            }

            override fun onActivityDestroyed(activity: Activity) {
                super.onActivityDestroyed(activity)
                removeActivity(activity)
            }
        });
    }

    fun addActivity(activity: Activity) {
        mActivityGroup.add(activity)
    }

    fun removeActivity(activity: Activity) {
        mActivityGroup.remove(activity)
    }

    fun closeAll() {
        for (a in mActivityGroup) {
            a.finish()
        }
        mActivityGroup.clear()
    }

    init {
        mActivityGroup = ArrayList()
    }
}