package com.willpower.jbanner;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.willpower.jutils.JColorUtil;

public class PageJUtil extends AppCompatActivity {

    LinearLayout mTarget;
    float count = 0.1f;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jutil);
        mTarget = findViewById(R.id.mTarget);
        mTarget.setBackgroundColor(getColor(R.color.colorPrimary));
        mTarget.setOnClickListener(v ->
        {
            count+=0.2f;
            mTarget.setBackgroundColor(JColorUtil.changeColorToLight(getColor(R.color.colorPrimary), count));
        });
    }

}
