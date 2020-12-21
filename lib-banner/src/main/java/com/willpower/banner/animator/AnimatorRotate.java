package com.willpower.banner.animator;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class AnimatorRotate implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position >= -1.0f && position <= 0.0f) {
            // 旋转
            page.setPivotX(0);
            page.setPivotY(0);
            page.setRotation(-90f * position);
        } else if (position > 0.0f) {
            page.setPivotX(0);
            page.setPivotY(0);
            page.setRotation(0f);
        } else {
            // 控制左侧其它缓存View旋转状态固定
            page.setPivotX(0);
            page.setPivotY(0);
            page.setRotation(90f);
        }
    }
}
