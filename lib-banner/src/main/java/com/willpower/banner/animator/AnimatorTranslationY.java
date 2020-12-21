package com.willpower.banner.animator;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class AnimatorTranslationY implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        /**
         *  0 当前界面
         *  -1 前一页
         *  1 后一页
         */
        if (position >= -1 && position <= 1) {
            page.setAlpha(page.getWidth() * -position);
            float yPosition = position * page.getHeight();
            page.setTranslationY(yPosition);
        }
    }
}
