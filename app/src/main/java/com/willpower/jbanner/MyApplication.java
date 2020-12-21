package com.willpower.jbanner;

import android.app.Application;

import com.willpower.jutils.JActivityManager;
import com.willpower.jutils.JDisplayUtil;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JActivityManager.INSTANCE.init(this);
    }
}
