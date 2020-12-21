package com.willpower.banner.animator;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class AnimatorAlpha implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        float alpha = 0.0f;
        if (0.0f <= position && position <= 1.0f) {
            alpha = 1.0f - position;
        } else if (-1.0f <= position && position < 0.0f) {
            alpha = position + 1.0f;
        }
        page.setAlpha(alpha);
    }
}
