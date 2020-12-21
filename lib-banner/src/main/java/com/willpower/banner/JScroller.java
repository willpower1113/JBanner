package com.willpower.banner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

class JScroller extends Scroller {
    private int mDuration = 500;

    public JScroller(Context context) {
        super(context);
    }

    public JScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public int getTurnDuration() {
        return mDuration;
    }

    public void setTurnDuration(int mDuration) {
        this.mDuration = mDuration;
    }
}
