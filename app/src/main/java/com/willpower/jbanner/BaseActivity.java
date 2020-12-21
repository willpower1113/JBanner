package com.willpower.jbanner;

import android.support.v7.app.AppCompatActivity;

import me.jessyan.autosize.internal.CustomAdapt;

public class BaseActivity extends AppCompatActivity implements CustomAdapt {

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 1500;
    }
}
