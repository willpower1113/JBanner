package com.willpower.banner.animator;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class AnimatorScale implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        //3D旋转
        int width = page.getWidth();
        int pivotX;
        if (position < 0 && position >= -1) {
            pivotX = width;
        }else {
            pivotX = 0;
        }
        //设置x轴的锚点
        page.setPivotX(pivotX);
        //设置绕Y轴旋转的角度
        page.setRotationY(90f * position);
    }
}
